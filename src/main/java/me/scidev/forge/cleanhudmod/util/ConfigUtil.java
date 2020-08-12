package me.scidev.forge.cleanhudmod.util;

import java.io.File;

import me.scidev.forge.cleanhudmod.HudMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class ConfigUtil {
	
	public static Configuration config;
	
	public static Property coordPosX;
	public static Property coordNegX;
	public static Property coordPosZ;
	public static Property coordNegZ;
	public static Property cardinalNorth;
	public static Property cardinalSouth;
	public static Property cardinalEast;
	public static Property cardinalWest;
	public static Property directionFormat;
	public static Property positionFormat;
	public static Property debugFormat;
	public static Property framerateFormat;

	public static Property linePosTypes;
	public static Property linesX;
	public static Property linesY;
	public static Property lineContent;
	public static Property slotPosTypes;
	public static Property slotsX;
	public static Property slotsY;
	public static Property potionEffectsPosType;
	public static Property potionEffectsX;
	public static Property potionEffectsY;

	public static Property settingsChromaEnabled;
	public static Property settingsChromaSpeed;
	public static Property settingsShowItemEnchants;
	public static Property settingsShowItemInfo;
	public static Property settingsShowArmorItem;
	public static Property settingsShowHandItem;
	public static Property settingsShowPotionEffects;
	public static Property settingsShowDefaultItemOverlay;
	
	public static void initConfig() {
		config = new Configuration(new File(Loader.instance().getConfigDir(), HudMod.MODID+".cfg"));
		
		// Defauts
		coordPosX = config.get("text", "coordPosX", "+X");
		coordNegX = config.get("text", "coordNegX", "-X");
		coordPosZ = config.get("text", "coordPosZ", "+Z");
		coordNegZ = config.get("text", "coordNegZ", "-Z");

		cardinalNorth = config.get("text", "cardinalNorth", "N");
		cardinalSouth = config.get("text", "cardinalSouth", "S");
		cardinalEast = config.get("text", "cardinalEast", "E");
		cardinalWest = config.get("text", "cardinalWest", "W");

		directionFormat = config.get("text", "directionFormat", "%1$s (%2$s; %3$.2f / %4$.2f)");
		positionFormat = config.get("text", "positionFormat", "(%1$.2f, %2$.2f, %3$.2f)"/*"[%4$d, %5$d, %6$d]"*/);
		debugFormat = config.get("text", "debugFormat", "C: %1$d, E: %2$d+%3$d");
		framerateFormat = config.get("text", "framerateFormat", "%1$d fps");

		linePosTypes = config.get("alignment", "linePosTypes", new int[] {0,0,2});
		slotPosTypes = config.get("alignment", "slotPosTypes", new int[] {0,0,0,2,2});
		potionEffectsPosType = config.get("alignment", "potionEffectsPosType", 6);
		lineContent = config.get("alignment", "content", new String[] {"%3$s, %4$s","%1$s","%2$s"});
		linesX = config.get("alignment", "x", new double[] {4,4,4});
		linesY = config.get("alignment", "y", new double[] {4,13,4});
		slotsX = config.get("alignment", "slotsX", new double[] {4,150,250,250,150});
		slotsY = config.get("alignment", "slotsY", new double[] {26,4,4,4,4});
		potionEffectsX = config.get("alignment", "potionEffectsX", 10d);
		potionEffectsY = config.get("alignment", "potionEffectsY", 30d);
		
		settingsChromaEnabled = config.get("settings", "chromaEnabled", false);
		settingsChromaSpeed = config.get("settings", "chromaSpeed", 0.3d);
		settingsShowItemEnchants = config.get("settings", "showItemEnchants", true);
		settingsShowItemInfo = config.get("settings", "showItemInfo", true);
		settingsShowArmorItem = config.get("settings", "showArmorItem", true);
		settingsShowHandItem = config.get("settings", "showHandItem", true);
		settingsShowPotionEffects = config.get("settings", "showPotionEffects", true);
		settingsShowDefaultItemOverlay = config.get("settings", "showDefaultItemOverlay", false);
		
	}
}
