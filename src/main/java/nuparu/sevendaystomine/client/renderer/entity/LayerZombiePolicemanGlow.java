package nuparu.sevendaystomine.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.EntityZombiePoliceman;

@SideOnly(Side.CLIENT)
public class LayerZombiePolicemanGlow<T extends EntityZombiePoliceman> implements LayerRenderer<T> {

	private final ResourceLocation TEXTURE;
	private final RenderCorpse<T> renderer;

	public LayerZombiePolicemanGlow(RenderCorpse<T> spiderRendererIn) {
		this.renderer = spiderRendererIn;
		TEXTURE = new ResourceLocation(SevenDaysToMine.MODID, "textures/entity/zombie_policeman_glow.png");
	}

	public LayerZombiePolicemanGlow(RenderCorpse<T> spiderRendererIn, ResourceLocation res) {
		this.renderer = spiderRendererIn;
		TEXTURE = res;
	}

	@Override
	public void doRenderLayer(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		renderer.bindTexture(TEXTURE);
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_DST_ALPHA);

		if (entitylivingbaseIn.isInvisible()) {
			GlStateManager.depthMask(false);
		} else {
			GlStateManager.depthMask(true);
		}

		int i = 61680;
		int j = i % 65536;
		int k = i / 65536;
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1-((float)entitylivingbaseIn.getVomitTimer()/EntityZombiePoliceman.RECHARGE_TIME));
		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		this.renderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw,
				headPitch, scale);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		i = entitylivingbaseIn.getBrightnessForRender();
		j = i % 65536;
		k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
		this.renderer.setLightmap(entitylivingbaseIn);

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();

	}

	public boolean shouldCombineTextures() {
		return false;
	}
}