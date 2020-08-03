package com.nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.model.ModelGlobe;
import com.nuparu.sevendaystomine.client.model.ModelGlobePole;
import com.nuparu.sevendaystomine.client.model.ModelSolarPanel;
import com.nuparu.sevendaystomine.client.model.ModelSolarPanelTop;
import com.nuparu.sevendaystomine.tileentity.TileEntityGlobe;
import com.nuparu.sevendaystomine.tileentity.TileEntitySolarPanel;
import com.nuparu.sevendaystomine.util.MathUtils;

import net.minecraft.block.BlockSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityGlobeRenderer extends TileEntitySpecialRenderer<TileEntityGlobe> {

	private final ResourceLocation TEX = new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/globe.png");

	private ModelGlobe modelGlobe = new ModelGlobe();
	private ModelGlobePole modelPole = new ModelGlobePole();

	public TileEntityGlobeRenderer() {

	}

	@Override
	public void render(TileEntityGlobe tileentity, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		
		Minecraft mc = Minecraft.getMinecraft();
		
		int s = 0;

		if (tileentity != null) {
			s = tileentity.getBlockMetadata();
		}

		short short1 = 0;

		if (s == 2) {
			short1 = 180;
		}

		if (s == 3) {
			short1 = 0;
		}

		if (s == 5) {
			short1 = 90;
		}

		if (s == 4) {
			short1 = -90;
		}
		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		} else {
			bindTexture(TEX);
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		/*if (tileentity.getWorld() != null && mc.player != null) {
			if (tileentity.getWorld().getBlockState(new BlockPos((int)(x+MathUtils.lerp(mc.player.prevPosX, mc.player.posX, partialTicks)), (int)(y - 1 +MathUtils.lerp(mc.player.prevPosY, mc.player.posY, partialTicks)), (int)(z + MathUtils.lerp(mc.player.prevPosZ, mc.player.posZ, partialTicks)))).getBlock() instanceof BlockSlab) {
				GL11.glTranslatef(0F, -0.5F, 0F);
			
			}
		}*/
		GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		modelPole.render();
		GL11.glTranslatef(0.0F, 0.0F, -0.625F);
		float rot = 0;
		if (tileentity != null) {
			float angle = tileentity.angle;
			float prev = tileentity.anglePrev;
			if (prev > angle) {
				prev -= 360;
			}
			rot = prev + (angle - prev) * partialTicks;
		}
		GL11.glRotatef(23.5f, 1.0f, 0f, 0f);
		GL11.glRotatef(rot, 0.0F, 1.0F, 0.0F);
		modelGlobe.render();

		GL11.glPopMatrix();
		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}

}
