package me.scidev.forge.cleanhudmod.gui;

import java.util.ArrayList;
import java.util.List;

import me.scidev.forge.cleanhudmod.HudMod;
import me.scidev.forge.cleanhudmod.util.ConfigUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModConfigGui extends GuiConfig {

	public ModConfigGui(GuiScreen parent) 
    {
        super(parent,
                ModConfigGui.allConfigCategories(),
                HudMod.MODID, 
                false, 
                false, 
                "Hud Configuration");
        titleLine2 = "root";
    }
    
	private static List<IConfigElement> allConfigCategories() {
		List<IConfigElement> categories = new ArrayList<>();
		for (String categoryName : ConfigUtil.config.getCategoryNames())
			if (!categoryName.equalsIgnoreCase("alignment"))
				categories.add(new ConfigElement(ConfigUtil.config.getCategory(categoryName)));
		return categories;
	}
	
    @Override
    public void initGui()
    {
        // You can add buttons and initialize fields here
        super.initGui();
        //int width = mc.displayWidth/mc.gameSettings.guiScale;
		//int height = mc.displayHeight/mc.gameSettings.guiScale;
        buttonList.add(new GuiButton(64932, 10, 7, 150, 20, "Edit Layout"));
    }

    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // You can do things like create animations, draw additional elements, etc. here
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
    	// You can process any additional buttons you may have added here
    	if (button.id == 64932)
    		mc.displayGuiScreen(new TextPlacementGui(this));
    	else
    		super.actionPerformed(button);
    }
    
    @Override
    public void onGuiClosed() {
    	super.onGuiClosed();
    	this.entryList.saveConfigElements();
    	ConfigUtil.config.save();
    }
}
