package me.scidev.forge.cleanhudmod;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.scidev.forge.cleanhudmod.event.EventSubscriber;
import me.scidev.forge.cleanhudmod.gui.ScreenOverlayGui;
import me.scidev.forge.cleanhudmod.util.ConfigUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = HudMod.MODID, version = HudMod.VERSION, clientSideOnly = true, guiFactory = "me.scidev.forge."+HudMod.MODID+".gui.ConfigGuiFactory")
public class HudMod
{
	public static final String MODID = "cleanhudmod";
	public static final String VERSION = "1.0.0";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	@Instance
	public static HudMod instance;
	
	public final ScreenOverlayGui screenOverlayGui;
	
	public HudMod() {
		instance = this;
		screenOverlayGui = new ScreenOverlayGui();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ConfigUtil.initConfig();
		LOGGER.log(Level.INFO, "Init cleanhudmod v1.0.0");
		MinecraftForge.EVENT_BUS.register(new EventSubscriber());
	}
}
