package com.nuparu.sevendaystomine.client.renderer.tileentity;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockCar;
import com.nuparu.sevendaystomine.client.model.ModelPoliceCar;
import com.nuparu.sevendaystomine.client.model.ModelSedan;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.tileentity.TileEntityCar;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntitySedanRenderer extends TileEntitySpecialRenderer<TileEntityCar> {

	public static final ModelSedan modelSedan = new ModelSedan();
	public static final ModelPoliceCar modelPolice = new ModelPoliceCar();
	public static final ResourceLocation TEXTURE_RED = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/tileentity/sedan_red.png");
	public static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/tileentity/sedan_blue.png");
	public static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/tileentity/sedan_yellow.png");
	public static final ResourceLocation TEXTURE_GREEN = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/tileentity/sedan_green.png");
	public static final ResourceLocation TEXTURE_BLACK = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/tileentity/sedan_black.png");
	public static final ResourceLocation TEXTURE_WHITE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/tileentity/sedan_white.png");
	public static final ResourceLocation TEXTURE_POLICE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/tileentity/police_car.png");

	public void render(TileEntityCar te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {

		if (destroyStage >= 0) {
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translate(x, y + 0.875d, z + 1);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		GlStateManager.translate(0.5F, -0.5F, 0.5F);

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		EnumFacing facing = EnumFacing.SOUTH;
		Block block = state.getBlock();
		if (block instanceof BlockCar) {
			facing = state.getValue(BlockCar.FACING);
			if (!state.getValue(BlockCar.MASTER)) {
				GlStateManager.disableRescaleNormal();
				GlStateManager.popMatrix();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				return;
			}
		}
		GlStateManager.rotate((float) facing.getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
		if (block == ModBlocks.POLICE_CAR) {
			this.bindTexture(TEXTURE_POLICE);
			modelPolice.render(null, 0f, 0f, 0f, 0f, 0f, 0.0625f);
		} else {
			this.bindTexture(TEXTURE_RED);

			if (block == ModBlocks.SEDAN_BLUE) {
				this.bindTexture(TEXTURE_BLUE);
			} else if (block == ModBlocks.SEDAN_YELLOW) {
				this.bindTexture(TEXTURE_YELLOW);
			} else if (block == ModBlocks.SEDAN_GREEN) {
				this.bindTexture(TEXTURE_GREEN);
			} else if (block == ModBlocks.SEDAN_BLACK) {
				this.bindTexture(TEXTURE_BLACK);
			} else if (block == ModBlocks.SEDAN_WHITE) {
				this.bindTexture(TEXTURE_WHITE);
			}
			modelSedan.render(null, 0f, 0f, 0f, 0f, 0f, 0f);
		}
		
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
}