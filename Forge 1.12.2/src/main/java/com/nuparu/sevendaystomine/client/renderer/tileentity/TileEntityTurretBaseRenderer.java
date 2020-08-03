package com.nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.model.ModelTurretBase;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurretBase;

import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityTurretBaseRenderer extends TileEntitySpecialRenderer<TileEntityTurretBase> {

	private static final ResourceLocation texture = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/turret.png");
	private ModelTurretBase turret = new ModelTurretBase();

	public TileEntityTurretBaseRenderer() {

	}

	@Override
	public void render(TileEntityTurretBase te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		if (te != null) {
			te.getWorld();
		}
		int s = 0;
		if (te != null) {
			s = te.getBlockMetadata();
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

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		float rotation = 0f;
		if (te != null) {
			if (partialTicks == 1.0F) {
				rotation = te.headRotation;
			} else {
				rotation = te.headRotationPrev + (te.headRotation - te.headRotationPrev) * partialTicks;
			}
		}
		GL11.glRotatef((float) short1, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		bindTexture(texture);
		turret.render(rotation);
		GL11.glPopMatrix();
	}

}
