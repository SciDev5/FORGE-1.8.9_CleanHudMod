package me.scidev.forge.cleanhudmod.event;

import me.scidev.forge.cleanhudmod.HudMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventSubscriber {

	private static Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void onRenderHud(RenderGameOverlayEvent drawEvent) {
		if (drawEvent.type != ElementType.ALL) return;
		if (mc.gameSettings.showDebugInfo) return;
		
		HudMod.instance.screenOverlayGui.draw();
	}
}
