package me.scidev.forge.cleanhudmod.util;

import java.util.Collection;
import java.util.Random;

import me.scidev.forge.cleanhudmod.gui.ScreenOverlayGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class TextUtil {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static String frameRateString() {
		try {
			return String.format(ConfigUtil.framerateFormat.getString(), Minecraft.getDebugFPS());
		} catch (Exception e) {
			return "FORMAT ERROR: "+e.getMessage();
		}
	}
	
	public static String coordinatesString() {
		try {
			return String.format(ConfigUtil.positionFormat.getString(), mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, (int)Math.floor(mc.thePlayer.posX), (int)Math.floor(mc.thePlayer.posY), (int)Math.floor(mc.thePlayer.posZ));
		} catch (Exception e) {
			return "FORMAT ERROR: "+e.getMessage();
		}
	}
	
	public static String directionString() {
		Entity entity = mc.getRenderViewEntity();
		EnumFacing enumfacing = entity.getHorizontalFacing();
		String coordDir = "--"; 
		String cardinalDir = "-";
		
		switch (enumfacing) {
			case NORTH: coordDir = ConfigUtil.coordNegZ.getString(); cardinalDir = ConfigUtil.cardinalNorth.getString(); break;
			case SOUTH: coordDir = ConfigUtil.coordPosZ.getString(); cardinalDir = ConfigUtil.cardinalSouth.getString(); break;
			case WEST: coordDir = ConfigUtil.coordNegX.getString(); cardinalDir = ConfigUtil.cardinalWest.getString(); break;
			case EAST: coordDir = ConfigUtil.coordPosX.getString(); cardinalDir = ConfigUtil.cardinalEast.getString(); break;
			default: break;
		}
		try {
			return String.format(ConfigUtil.directionFormat.getString(), coordDir, cardinalDir, mc.thePlayer.rotationPitch, (mc.thePlayer.rotationYaw+180)%360-180);
		} catch (Exception e) {
			return "FORMAT ERROR: "+e.getMessage();
		}
	}

	public static String debugString() {
		DebugInfo info = DebugInfo.getDebugInfo();
		try {
			return String.format(ConfigUtil.debugFormat.getString(), info.C, info.E, info.TE);
		} catch (Exception e) {
			return "FORMAT ERROR: "+e.getMessage();
		}
	}

	public static String randomCoordinatesString(Random random) {
		double x = random.nextDouble()*2000-1000;
		double y = random.nextDouble()*256;
		double z = random.nextDouble()*2000-1000;
		try {
			return String.format(ConfigUtil.positionFormat.getString(), x, y, z, (int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
		} catch (Exception e) {
			return "FORMAT ERROR: "+e.getMessage();
		}
	}
	public static String randomDirectionString(Random random) {
		float pitch = random.nextFloat()*180f-90f;
		float yaw = random.nextFloat()*360f-180f;
		
		EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double((double)(yaw * 4.0F / 360.0F) + 0.5D) & 3);
		String coordDir = "--"; 
		String cardinalDir = "-";
		
		switch (enumfacing) {
			case NORTH: coordDir = ConfigUtil.coordNegZ.getString(); cardinalDir = ConfigUtil.cardinalNorth.getString(); break;
			case SOUTH: coordDir = ConfigUtil.coordPosZ.getString(); cardinalDir = ConfigUtil.cardinalSouth.getString(); break;
			case WEST: coordDir = ConfigUtil.coordNegX.getString(); cardinalDir = ConfigUtil.cardinalWest.getString(); break;
			case EAST: coordDir = ConfigUtil.coordPosX.getString(); cardinalDir = ConfigUtil.cardinalEast.getString(); break;
			default: break;
		}
		try {
			return String.format(ConfigUtil.directionFormat.getString(), coordDir, cardinalDir, pitch, yaw);
		} catch (Exception e) {
			return "FORMAT ERROR: "+e.getMessage();
		}
	}
	public static String randomDebugString(Random random) {
		DebugInfo info = new DebugInfo(random.nextInt(45), random.nextInt(20), random.nextInt(5));		
		try {
			return String.format(ConfigUtil.debugFormat.getString(), info.C, info.E, info.TE);
		} catch (Exception e) {
			return "FORMAT ERROR: "+e.getMessage();
		}
	}

	public static int getLinePosX(int lineI) {
		int width = ScreenOverlayGui.scaledResolution.getScaledWidth();
		double xInDouble = ConfigUtil.linesX.getDoubleList()[lineI];
		int xIn = (int)xInDouble;
		switch (ConfigUtil.linePosTypes.getIntList()[lineI]) {
		case 0: // Top left by pixels.
		case 1: // Top left by line-heights. (X unchanged)
		case 4: // Bottom left by pixels.
		case 5: // Bottom left by line-heights. (X unchanged)
			return xIn;
		case 2: // Top right by pixels.
		case 3: // Top right by line-heights. (X like case 2)
		case 6: // Bottom right by pixels.
		case 7: // Bottom right by line-heights. (X like case 2)
			return width-xIn;
		case 8: // Centered text by fraction of screen size.
			return (int)Math.floor(width*xInDouble);
		default:
			return 0;
		}
	}
	public static int getLinePosY(int lineI, int fontHeight) {
		int height = ScreenOverlayGui.scaledResolution.getScaledHeight();
		double yInDouble = ConfigUtil.linesY.getDoubleList()[lineI];
		int yIn = (int)yInDouble;
		switch (ConfigUtil.linePosTypes.getIntList()[lineI]) {
		case 0: // Top left by pixels.
		case 2: // Top right by pixels.
			return yIn;
		case 1: // Top left by line-heights.
		case 3: // Top right by line-heights.
			return yIn*fontHeight;
		case 4: // Bottom left by pixels.
		case 6: // Bottom right by pixels.
			return height-yIn-fontHeight;
		case 5: // Bottom left by line-heights.
		case 7: // Bottom right by line-heights.
			return height-yIn*fontHeight-fontHeight;
		case 8: // Centered text by fraction screen size.
			return (int)Math.floor((height-fontHeight)*yInDouble);
		default:
			return 0;
		}
	}
	public static int getSlotPosX(int lineI, int slotWidth) {
		int width = ScreenOverlayGui.scaledResolution.getScaledWidth();
		double xInDouble = ConfigUtil.slotsX.getDoubleList()[lineI];
		int xIn = (int)xInDouble;
		switch (ConfigUtil.slotPosTypes.getIntList()[lineI]) {
		case 0: // Top left by pixels.
		case 1: // Top left by line-heights. (X unchanged)
		case 4: // Bottom left by pixels.
		case 5: // Bottom left by line-heights. (X unchanged)
			return xIn;
		case 2: // Top right by pixels.
		case 3: // Top right by line-heights. (X like case 2)
		case 6: // Bottom right by pixels.
		case 7: // Bottom right by line-heights. (X like case 2)
			return width-xIn-slotWidth;
		case 8: // Centered text by fraction of screen size.
			return (int)Math.floor((width-slotWidth)*xInDouble);
		default:
			return 0;
		}
	}
	public static int getSlotPosY(int lineI, int fontHeight, int slotHeight) {
		int height = ScreenOverlayGui.scaledResolution.getScaledHeight();
		double yInDouble = ConfigUtil.slotsY.getDoubleList()[lineI];
		int yIn = (int)yInDouble;
		switch (ConfigUtil.slotPosTypes.getIntList()[lineI]) {
		case 0: // Top left by pixels.
		case 2: // Top right by pixels.
			return yIn;
		case 1: // Top left by line-heights.
		case 3: // Top right by line-heights.
			return yIn*fontHeight;
		case 4: // Bottom left by pixels.
		case 6: // Bottom right by pixels.
			return height-yIn-slotHeight;
		case 5: // Bottom left by line-heights.
		case 7: // Bottom right by line-heights.
			return height-yIn*fontHeight-slotHeight;
		case 8: // Centered text by fraction screen size.
			return (int)Math.floor((height-slotHeight)*yInDouble);
		default:
			return 0;
		}
	}
	public static int getSlotWidth(ItemStack stack, FontRenderer fontRenderer) {
		if (stack == null) 
			return fontRenderer.getStringWidth("(Empty)");
		int width = 0;
		if (ConfigUtil.settingsShowItemInfo.getBoolean()) {
			width = Math.max(width,fontRenderer.getStringWidth((stack.stackSize > 1 ? stack.stackSize + "x " : "") + stack.getDisplayName())+17);
			if (stack.getMaxDamage() > 0)
				width = Math.max(width,fontRenderer.getStringWidth(stack.getMaxDamage()-stack.getItemDamage()+"/"+stack.getMaxDamage())+17);
		}
		NBTTagList enchlist = stack.getEnchantmentTagList();
		if (enchlist != null && ConfigUtil.settingsShowItemEnchants.getBoolean()) for (int i = 0; i < enchlist.tagCount(); i ++) {
			NBTTagCompound tag = enchlist.getCompoundTagAt(i);
			Enchantment ench = Enchantment.getEnchantmentById(tag.getInteger("id"));
			if (ench == null) continue;
			String enchText = ench.getTranslatedName(tag.getInteger("lvl"));
			width = Math.max(width,fontRenderer.getStringWidth(enchText));
		}
		return width;
	}
	public static int getSlotHeight(ItemStack stack, FontRenderer fontRenderer) {
		if (stack == null) 
			return fontRenderer.FONT_HEIGHT;
		NBTTagList enchlist = stack.getEnchantmentTagList();
		if (enchlist != null && ConfigUtil.settingsShowItemEnchants.getBoolean())
			return (enchlist.tagCount()+2)*fontRenderer.FONT_HEIGHT;
		else 
			return 2*fontRenderer.FONT_HEIGHT;
	}
	public static int getPotionListPosX(Collection<PotionEffect> effects, int effectListWidth) {
		int width = ScreenOverlayGui.scaledResolution.getScaledWidth();
		double xInDouble = ConfigUtil.potionEffectsX.getDouble();
		int xIn = (int)xInDouble;
		switch (ConfigUtil.potionEffectsPosType.getInt()) {
		case 0: // Top left by pixels.
		case 1: // Top left by line-heights. (X unchanged)
		case 4: // Bottom left by pixels.
		case 5: // Bottom left by line-heights. (X unchanged)
			return xIn;
		case 2: // Top right by pixels.
		case 3: // Top right by line-heights. (X like case 2)
		case 6: // Bottom right by pixels.
		case 7: // Bottom right by line-heights. (X like case 2)
			return width-xIn-effectListWidth;
		case 8: // Centered text by fraction of screen size.
			return (int)Math.floor((width-effectListWidth)*xInDouble);
		default:
			return 0;
		}
	}
	public static int getPotionListPosY(Collection<PotionEffect> effects, int fontHeight) {
		int height = ScreenOverlayGui.scaledResolution.getScaledHeight();
		int effectListHeight = effects.size()*(fontHeight+5);
		double yInDouble = ConfigUtil.potionEffectsY.getDouble();
		int yIn = (int)yInDouble;
		switch (ConfigUtil.potionEffectsPosType.getInt()) {
		case 0: // Top left by pixels.
		case 2: // Top right by pixels.
			return yIn;
		case 1: // Top left by line-heights.
		case 3: // Top right by line-heights.
			return yIn*fontHeight;
		case 4: // Bottom left by pixels.
		case 6: // Bottom right by pixels.
			return height-yIn-effectListHeight;
		case 5: // Bottom left by line-heights.
		case 7: // Bottom right by line-heights.
			return height-yIn*fontHeight-effectListHeight;
		case 8: // Centered text by fraction screen size.
			return (int)Math.floor((height-effectListHeight)*yInDouble);
		default:
			return 0;
		}
	}
	public static int getPotionListWidth(Collection<PotionEffect> effects, FontRenderer fontRenderer) {
		int width = 0;
		for (PotionEffect effect : effects) 
			width = Math.max(width,fontRenderer.getStringWidth(getPotionEffectText(effect)));
		return width;
	}
	public static String getPotionEffectText(PotionEffect effect) {
		Potion potion = Potion.potionTypes[effect.getPotionID()];
        String duration = Potion.getDurationString(effect);
        String potionName = I18n.format(potion.getName(), new Object[0]);

        switch (effect.getAmplifier()) {
        case 0:
        	break;
        case 1:
            potionName = potionName + " " + I18n.format("enchantment.level.2", new Object[0]);
            break;
        case 2:
            potionName = potionName + " " + I18n.format("enchantment.level.3", new Object[0]);
            break;
        case 3:
            potionName = potionName + " " + I18n.format("enchantment.level.4", new Object[0]);
            break;
        default:
            potionName = potionName + " " + Integer.toString(1+(effect.getAmplifier()+256)%256);
            break;
        }
        return potionName + " ("+duration+")";
	}
	public static String getLineContent(int lineI, String coordinates, String direction, String frameRate, String debug) {
		try {
			return String.format(ConfigUtil.lineContent.getStringList()[lineI], coordinates, direction, frameRate, debug);
		} catch (Exception e) {
			return "FORMAT ERROR: "+e.getMessage();
		}
	}
}
