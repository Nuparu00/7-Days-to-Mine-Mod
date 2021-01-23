package com.nuparu.sevendaystomine.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.model.ModelRiotShield;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.tileentity.TileEntityShield;
import com.nuparu.sevendaystomine.tileentity.TileEntityWoodenLogSpike;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityShieldRenderer extends TileEntitySpecialRenderer<TileEntityShield> {

	ModelRiotShield modelShield = new ModelRiotShield();
	ResourceLocation basetexture = new ResourceLocation(SevenDaysToMine.MODID,"textures/entity/riot_shield.png");

	public TileEntityShieldRenderer() {

	}

	@Override
	public void render(TileEntityShield te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {

		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(basetexture);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		this.modelShield.render();
		GlStateManager.popMatrix();
	}

}
