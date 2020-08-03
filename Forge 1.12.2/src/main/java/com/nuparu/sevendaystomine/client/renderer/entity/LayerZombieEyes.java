package com.nuparu.sevendaystomine.client.renderer.entity;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityZombieBase;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerZombieEyes<T extends EntityZombieBase> implements LayerRenderer<T> {

	private final ResourceLocation TEXTURE;
	private final RenderCorpse<T> renderer;

	public LayerZombieEyes(RenderCorpse<T> spiderRendererIn) {
		this.renderer = spiderRendererIn;
		TEXTURE = new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/zombie_eyes.png");
	}
	
	public LayerZombieEyes(RenderCorpse<T> spiderRendererIn, ResourceLocation res) {
		this.renderer = spiderRendererIn;
		TEXTURE = res;
	}

	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!entitylivingbaseIn.isDead && entitylivingbaseIn.getHealth() > 0) {
			if (Utils.isBloodmoon(entitylivingbaseIn.world)) {

				GlStateManager.pushMatrix();
				renderer.bindTexture(TEXTURE);
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

				if (entitylivingbaseIn.isInvisible()) {
					GlStateManager.depthMask(false);
				} else {
					GlStateManager.depthMask(true);
				}

				int i = 61680;
				int j = i % 65536;
				int k = i / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
				this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks,
						netHeadYaw, headPitch, scale);
				Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
				i = entitylivingbaseIn.getBrightnessForRender();
				j = i % 65536;
				k = i / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
				this.renderer.setLightmap(entitylivingbaseIn);
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}