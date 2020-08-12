package me.scidev.forge.cleanhudmod.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import me.scidev.forge.cleanhudmod.HudMod;
import me.scidev.forge.cleanhudmod.util.ConfigUtil;
import me.scidev.forge.cleanhudmod.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ScreenOverlayGui extends Gui {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static RenderItem itemRenderer = null;
	private static FontRenderer fontRenderer = mc.fontRendererObj;
	private static int color;

	public static ScaledResolution scaledResolution = null;
	
	private static Random random;
	
	public void draw() {
		if (fontRenderer == null) fontRenderer = mc.fontRendererObj;
		if (itemRenderer == null) itemRenderer = mc.getRenderItem();
		//mc.gameSettings.guiScale = new Random().nextInt(2)+1;
		
		scaledResolution = new ScaledResolution(mc);
		
		boolean inWorld = mc.theWorld != null;
		random = new Random(System.currentTimeMillis()/1000l);
		
		if (ConfigUtil.settingsChromaEnabled.getBoolean()) {
			int loopTime = (int) (1000/ConfigUtil.settingsChromaSpeed.getDouble());
			if (loopTime == 0) loopTime = 1;
			float hue = (System.currentTimeMillis()%loopTime)/(float)loopTime;
			color = Color.HSBtoRGB(hue, 0.7f, 1f) & 0x00FFFFFF;
		} else {
			color = 0x00FFFFFF;
		}
		
		
		String frameRate = TextUtil.frameRateString();
		String coordinates = inWorld?TextUtil.coordinatesString():TextUtil.randomCoordinatesString(random);
		String direction = inWorld?TextUtil.directionString():TextUtil.randomDirectionString(random);
		String debug = inWorld?TextUtil.debugString():TextUtil.randomDebugString(random);
		
		ItemStack stack = inWorld?mc.thePlayer.getHeldItem():randomItem(new Item[] {Items.diamond_sword, Items.iron_sword, Items.iron_pickaxe, Items.golden_apple, Items.bow},Enchantment.enchantmentsBookList);
		
		if (ConfigUtil.settingsShowHandItem.getBoolean())
			renderItemStack(stack,TextUtil.getSlotPosX(0, TextUtil.getSlotWidth(stack, fontRenderer)),TextUtil.getSlotPosY(0, fontRenderer.FONT_HEIGHT, TextUtil.getSlotWidth(stack, fontRenderer)));
		
		if (ConfigUtil.settingsShowArmorItem.getBoolean()) for (int i = 0; i < 4; i++) {
			stack = inWorld?mc.thePlayer.getCurrentArmor(3-i):randomItem(new Item[][] {{Items.diamond_boots,Items.iron_boots},{Items.diamond_leggings,Items.iron_leggings},{Items.diamond_chestplate,Items.iron_chestplate},{Items.diamond_helmet,Items.iron_helmet}}[3-i],new Enchantment[] {Enchantment.protection,Enchantment.aquaAffinity,Enchantment.unbreaking,Enchantment.depthStrider,Enchantment.thorns});
			renderItemStack(stack,TextUtil.getSlotPosX(i+1, TextUtil.getSlotWidth(stack, fontRenderer)),TextUtil.getSlotPosY(i+1, fontRenderer.FONT_HEIGHT, TextUtil.getSlotWidth(stack, fontRenderer)));
		}
		
		if (ConfigUtil.settingsShowPotionEffects.getBoolean()) {
			Collection<PotionEffect> potionEffects = inWorld?mc.thePlayer.getActivePotionEffects():randomPotionEffects();
			int potionEffectListWidth = TextUtil.getPotionListWidth(potionEffects, fontRenderer);
			int potionEffectListX = TextUtil.getPotionListPosX(potionEffects, potionEffectListWidth);
			int potionEffectListY = TextUtil.getPotionListPosY(potionEffects, fontRenderer.FONT_HEIGHT);
			int potionEffectListPosType = ConfigUtil.potionEffectsPosType.getInt();
			boolean potionEffectTextAlignRight = potionEffectListPosType == 2 || potionEffectListPosType == 3 || potionEffectListPosType == 6 || potionEffectListPosType == 7 || potionEffectListPosType == 8 && potionEffectListX >= 0.7*(scaledResolution.getScaledWidth()-potionEffectListWidth);
			boolean potionEffectTextAlignCenter = potionEffectListPosType == 8 && potionEffectListX > 0.3*(scaledResolution.getScaledWidth()-potionEffectListWidth) && potionEffectListX < 0.7*(scaledResolution.getScaledWidth()-potionEffectListWidth);
			int potionI = 0;
			for (PotionEffect potioneffect : potionEffects) {
	            String text = TextUtil.getPotionEffectText(potioneffect);
	            int alignmentOff = potionEffectListWidth-fontRenderer.getStringWidth(text);
				this.drawString(fontRenderer, text, potionEffectListX+(potionEffectTextAlignCenter?alignmentOff/2:(potionEffectTextAlignRight?alignmentOff:0)), potionEffectListY+potionI*(5+fontRenderer.FONT_HEIGHT), 0xFF000000|color);
				potionI++;
			}
		}
		
		int numLines = ConfigUtil.lineContent.getStringList().length;
		for (int lineI = 0; lineI < numLines; lineI++) {
			int x = TextUtil.getLinePosX(lineI);
			int y = TextUtil.getLinePosY(lineI, fontRenderer.FONT_HEIGHT);
			String content = TextUtil.getLineContent(lineI, coordinates, direction, frameRate, debug);

			switch (ConfigUtil.linePosTypes.getIntList()[lineI]) {
			case 0: // Top left by pixels.
			case 1: // Top left by line-heights.
			case 4: // Bottom left by pixels.
			case 5: // Bottom left by line-heights.
				this.drawString(fontRenderer, content, x, y, 0xFF000000|color);
				break;
			case 2: // Top right by pixels.
			case 3: // Top right by line-heights.
			case 6: // Bottom right by pixels.
			case 7: // Bottom right by line-heights.
				this.drawString(fontRenderer, content, x-fontRenderer.getStringWidth(content), y, 0xFF000000|color);
				break;
			case 8: // Centered text by fraction screen size.
				this.drawCenteredString(fontRenderer, content, x, y, 0xFF000000|color);
				break;
			}
		}
	}
	
	private ItemStack randomItem(Item[] items, Enchantment[] enchantments) {
		Item item = items[random.nextInt(items.length)];
		ItemStack stack = new ItemStack(item);
		if (stack.getMaxStackSize() > 1)
			stack.stackSize = 1+random.nextInt(stack.getMaxStackSize()-1);
		List<Enchantment> validEnchants = new ArrayList<>();
		for (Enchantment ench : enchantments)
			if (ench.canApply(stack))
				validEnchants.add(ench);
		if (stack.getMaxDamage() > 0)
			stack.setItemDamage(random.nextInt(stack.getMaxDamage()));
		if (validEnchants.size() == 0)
			return stack;
		int numEnchants = random.nextInt(validEnchants.size());
		for (int i = 0; i < numEnchants; i++) {
			int enchN = random.nextInt(validEnchants.size());
			Enchantment ench = validEnchants.get(enchN);
			validEnchants.remove(enchN);
			stack.addEnchantment(ench, random.nextInt(ench.getMaxLevel())+1);
		}
		return stack;
	}
	private Collection<PotionEffect> randomPotionEffects() {
		Collection<PotionEffect> collection = new ArrayList<>();
		int numEffects = random.nextInt(8);
		List<Potion> potions = new ArrayList<>();
		for (Potion p : Potion.potionTypes) potions.add(p);
		for (int i = 0; i < numEffects && potions.size() > 0;) {
			int potN = random.nextInt(potions.size());
			Potion potion = potions.get(potN);
			potions.remove(potN);
			if (potion == null) continue;
			collection.add(new PotionEffect(potion.id, random.nextInt(20*60*5), random.nextInt(4)));
			i++;
		}
		return collection;
	}

	private void renderItemStack(ItemStack stack, int x, int y) {
		if (stack != null) {
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();
			itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
			if (ConfigUtil.settingsShowItemInfo.getBoolean()) {
				this.drawString(fontRenderer, (stack.stackSize > 1 ? stack.stackSize + "x " : "") + stack.getDisplayName(), x+17, y, 0x66000000|color);
				if (stack.getMaxDamage() > 0)
					this.drawString(fontRenderer, stack.getMaxDamage()-stack.getItemDamage()+"/"+stack.getMaxDamage(), x+17, y+fontRenderer.FONT_HEIGHT, 0xAA000000|color);
			}
			if (ConfigUtil.settingsShowLoreNotEnchants.getBoolean()) {
				NBTTagCompound itemInfoTag = stack.serializeNBT().getCompoundTag("tag");
				NBTTagCompound displayTag = itemInfoTag.getCompoundTag("display"); NBTTagList lorelist = displayTag.getTagList("Lore", 8);
				HudMod.LOGGER.info(displayTag==null?"NULL":displayTag.toString());
				if (lorelist != null && ConfigUtil.settingsShowItemEnchants.getBoolean()) for (int i = 0; i < lorelist.tagCount(); i ++) {
					String text = lorelist.getStringTagAt(i);
					this.drawString(fontRenderer, text, x, y+fontRenderer.FONT_HEIGHT*(2+i), 0x88000000|color);
				}
			} else {
				NBTTagList enchlist = stack.getEnchantmentTagList();
				if (enchlist != null && ConfigUtil.settingsShowItemEnchants.getBoolean()) for (int i = 0; i < enchlist.tagCount(); i ++) {
					NBTTagCompound tag = enchlist.getCompoundTagAt(i);
					Enchantment ench = Enchantment.getEnchantmentById(tag.getInteger("id"));
					if (ench == null) continue;
					String enchText = ench.getTranslatedName(tag.getInteger("lvl"));
					this.drawString(fontRenderer, enchText, x, y+fontRenderer.FONT_HEIGHT*(2+i), 0x88000000|color);
				}
			}
            if (ConfigUtil.settingsShowDefaultItemOverlay.getBoolean())
            	itemRenderer.renderItemOverlays(fontRenderer, stack, x, y);
			GlStateManager.disableLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
		} else {
            GlStateManager.enableBlend();
			this.drawString(fontRenderer, "(Empty)", x, y, 0x33000000|color);
            GlStateManager.disableBlend();
		}
	}
}
