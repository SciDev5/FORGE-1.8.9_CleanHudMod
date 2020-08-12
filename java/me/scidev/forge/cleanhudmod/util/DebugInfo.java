package me.scidev.forge.cleanhudmod.util;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class DebugInfo {
	private static Minecraft mc = Minecraft.getMinecraft();
	public final int C;
	public final int E;
	public final int TE;
	public DebugInfo(int C, int E, int TE) {
		this.C = C;
		this.E = E;
		this.TE = TE;
	}
	
	public static DebugInfo getDebugInfo() {
        Entity entity = mc.getRenderViewEntity();
		
		List<?> renderInfos = null;
		try {
			renderInfos = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, "field_72755_R");
		} catch (Exception e) {
			// Only executed in dev workspace.
			renderInfos = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, "renderInfos");
		}
		
		Class<?> RG$CLRI;
		try { RG$CLRI = Class.forName("net.minecraft.client.renderer.RenderGlobal$ContainerLocalRenderInformation");
		} catch (ClassNotFoundException e1) { e1.printStackTrace();
			return null;
		}
		
		Field[] fields = RG$CLRI.getDeclaredFields();
		Field RG$CLRI$renderChunkField = null;
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equalsIgnoreCase("renderChunk") || field.getName().equalsIgnoreCase("field_178036_a")) {
				RG$CLRI$renderChunkField = field;
				break;
			}
		}
		if (RG$CLRI$renderChunkField == null) return null;
		
		int C = 0;
		int TE = 0;
		
		double fov = mc.gameSettings.fovSetting;
		try {
			fov *= (float) ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, mc.entityRenderer, "field_78507_R");
		} catch (Exception e) {
			// Only executed in dev workspace.
			fov *= (float) ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, mc.entityRenderer, "fovModifierHand");
		}
		Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(mc.theWorld, entity, 0);
		if (block.getMaterial() == Material.water)
			fov *= 60.0F / 70.0F;
    	double aspectRatio = (double)mc.displayWidth/(double)mc.displayHeight;
		fov = Math.toRadians(fov); double fovyaw = fov;
    	double tfovyaw = aspectRatio*Math.tan(fovyaw/2); double tfov = Math.tan(fov/2);
        for (Object rg$clri : renderInfos)
        {
        	RenderChunk renderChunk = null;
			try {
				renderChunk = (RenderChunk) RG$CLRI$renderChunkField.get(rg$clri);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if (renderChunk == null) return null;
            CompiledChunk compiledchunk = renderChunk.compiledChunk;
            if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty()) C++;
            List<TileEntity> tileEntities = compiledchunk.getTileEntities();
            if (!tileEntities.isEmpty())
            {
                for (TileEntity tileentity : tileEntities)
                {
                	AxisAlignedBB aabb = tileentity.getRenderBoundingBox();
                	Vec3 center = new Vec3(0.5*(aabb.minX+aabb.maxX),0.5*(aabb.minY+aabb.maxY),0.5*(aabb.minZ+aabb.maxZ));
                	Vec3 dirVec = center.subtract(entity.getPositionEyes(0)).rotateYaw((float) Math.toRadians(entity.rotationYaw)).rotatePitch((float) Math.toRadians(entity.rotationPitch));
                	if (dirVec.zCoord < 0) continue;

                	double xztan = dirVec.xCoord/dirVec.zCoord; 
                	double ytan = dirVec.yCoord/new Vec3(dirVec.xCoord,0,dirVec.zCoord).lengthVector();
                	double fac1 = Math.cos(Math.atan2(dirVec.xCoord,dirVec.zCoord));
                	ytan /= fac1;
                	
            		/*int width = mc.displayWidth/mc.gameSettings.guiScale;
            		int height = mc.displayHeight/mc.gameSettings.guiScale;
                	int x = (int) (width/2*(1-xztan/tfovyaw));
                	int y = (int) (height/2*(1-ytan/tfov));
                	
                	ScreenOverlayGui.drawRect(x, y, x+1, y+1, 0xffff8800); // Debug */
                	
                	if (Math.abs(xztan) < tfovyaw && Math.abs(ytan) < tfov)
                		TE++;
                }
            }
        }
		
		//ChunkRenderDispatcher rendisp = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, "renderDispatcher");
		//int pC = ((BlockingQueue<?>) ObfuscationReflectionHelper.getPrivateValue(ChunkRenderDispatcher.class, rendisp, "queueChunkUpdates")).size();
		//int pU = ((Queue<?>) ObfuscationReflectionHelper.getPrivateValue(ChunkRenderDispatcher.class, rendisp, "queueChunkUploads")).size();
		//int aB = ((BlockingQueue<?>) ObfuscationReflectionHelper.getPrivateValue(ChunkRenderDispatcher.class, rendisp, "queueFreeRenderBuilders")).size();
		int E;
		try {
			E = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, "field_72749_I");
		} catch (Exception e) {
			// Only executed in dev workspace.
			E = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, mc.renderGlobal, "countEntitiesRendered");
		}
		
		return new DebugInfo(C, E, TE);
	}
}