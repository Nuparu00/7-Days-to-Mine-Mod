package com.nuparu.sevendaystomine.client.renderer;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkyRenderer extends IRenderHandler {

	private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation(
			"textures/environment/moon_phases.png");
	private static final ResourceLocation BLOODMOON_PHASES_TEXTURES = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/environment/moon_phases.png");
	private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");

	private int starGLCallList;
	private int glSkyList;
	private int glSkyList2;
	private VertexBuffer starVBO;
	private VertexBuffer skyVBO;
	private VertexBuffer sky2VBO;
	private boolean vboEnabled;

	public SkyRenderer(RenderGlobal render) {
		vboEnabled = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, render, "field_175005_X");
		starGLCallList = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, render, "field_72772_v");
		glSkyList = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, render, "field_72771_w");
		glSkyList2 = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, render, "field_72781_x");

		starVBO = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, render, "field_175013_s");
		skyVBO = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, render, "field_175012_t");
		sky2VBO = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, render, "field_175011_u");
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		int pass = EntityRenderer.anaglyphEnable ? EntityRenderer.anaglyphField : 2;
		if (mc.world.provider.isSurfaceWorld()) {
			GlStateManager.disableTexture2D();
			Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
			double mult = 0d;
			if (Utils.isBloodmoon(world)) {
				mult = MathUtils.clamp(Math.abs(world.getCelestialAngle(partialTicks) - 0.54), 0, 0.30);
				mult = (0.30 - mult) / 0.30;
			}
			float f = (float) vec3d.x + (0.2f * (float) mult);
			float f1 = (float) vec3d.y;
			float f2 = (float) vec3d.z;

			if (pass != 2) {
				float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
				float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
				float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
				f = f3;
				f1 = f4;
				f2 = f5;
			}

			GlStateManager.color(f, f1, f2);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			GlStateManager.depthMask(false);
			GlStateManager.enableFog();
			GlStateManager.color(f, f1, f2);

			if (this.vboEnabled) {
				this.skyVBO.bindBuffer();
				GlStateManager.glEnableClientState(32884);
				GlStateManager.glVertexPointer(3, 5126, 12, 0);
				this.skyVBO.drawArrays(7);
				this.skyVBO.unbindBuffer();
				GlStateManager.glDisableClientState(32884);
			} else {
				GlStateManager.callList(this.glSkyList);
			}

			GlStateManager.disableFog();
			GlStateManager.disableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			RenderHelper.disableStandardItemLighting();
			float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks),
					partialTicks);

			if (afloat != null) {
				GlStateManager.disableTexture2D();
				GlStateManager.shadeModel(7425);
				GlStateManager.pushMatrix();
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(
						MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F,
						1.0F);
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
				float f6 = afloat[0];
				float f7 = afloat[1];
				float f8 = afloat[2];

				if (pass != 2) {
					float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
					float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
					float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
					f6 = f9;
					f7 = f10;
					f8 = f11;
				}

				bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
				bufferbuilder.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();
				int l1 = 16;

				for (int j2 = 0; j2 <= 16; ++j2) {
					float f21 = (float) j2 * ((float) Math.PI * 2F) / 16.0F;
					float f12 = MathHelper.sin(f21);
					float f13 = MathHelper.cos(f21);
					bufferbuilder
							.pos((double) (f12 * 120.0F), (double) (f13 * 120.0F), (double) (-f13 * 40.0F * afloat[3]))
							.color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
				}

				tessellator.draw();
				GlStateManager.popMatrix();
				GlStateManager.shadeModel(7424);
			}

			GlStateManager.enableTexture2D();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
					GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.pushMatrix();
			float f16 = 1.0F - world.getRainStrength(partialTicks);
			GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
			GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
			float f17 = 30.0F;
			mc.renderEngine.bindTexture(SUN_TEXTURES);
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos((double) (-f17), 100.0D, (double) (-f17)).tex(0.0D, 0.0D).endVertex();
			bufferbuilder.pos((double) f17, 100.0D, (double) (-f17)).tex(1.0D, 0.0D).endVertex();
			bufferbuilder.pos((double) f17, 100.0D, (double) f17).tex(1.0D, 1.0D).endVertex();
			bufferbuilder.pos((double) (-f17), 100.0D, (double) f17).tex(0.0D, 1.0D).endVertex();
			tessellator.draw();
			f17 = 20.0F;
			mc.renderEngine
					.bindTexture(Utils.isBloodmoon(world) ? BLOODMOON_PHASES_TEXTURES : MOON_PHASES_TEXTURES);
			int k1 = world.getMoonPhase();
			int i2 = k1 % 4;
			int k2 = k1 / 4 % 2;
			float f22 = (float) (i2 + 0) / 4.0F;
			float f23 = (float) (k2 + 0) / 2.0F;
			float f24 = (float) (i2 + 1) / 4.0F;
			float f14 = (float) (k2 + 1) / 2.0F;
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos((double) (-f17), -100.0D, (double) f17).tex((double) f24, (double) f14).endVertex();
			bufferbuilder.pos((double) f17, -100.0D, (double) f17).tex((double) f22, (double) f14).endVertex();
			bufferbuilder.pos((double) f17, -100.0D, (double) (-f17)).tex((double) f22, (double) f23).endVertex();
			bufferbuilder.pos((double) (-f17), -100.0D, (double) (-f17)).tex((double) f24, (double) f23).endVertex();
			tessellator.draw();
			GlStateManager.disableTexture2D();
			float f15 = world.getStarBrightness(partialTicks) * f16 * 1.5f;

			if (f15 > 0.0F) {
				GlStateManager.color(f15, f15, f15, f15);

				if (this.vboEnabled) {
					this.starVBO.bindBuffer();
					GlStateManager.glEnableClientState(32884);
					GlStateManager.glVertexPointer(3, 5126, 12, 0);
					this.starVBO.drawArrays(7);
					this.starVBO.unbindBuffer();
					GlStateManager.glDisableClientState(32884);
				} else {
					GlStateManager.callList(this.starGLCallList);
				}
			}

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableFog();
			GlStateManager.popMatrix();
			GlStateManager.disableTexture2D();
			GlStateManager.color(0.0F, 0.0F, 0.0F);
			double d3 = mc.player.getPositionEyes(partialTicks).y - world.getHorizon();

			if (d3 < 0.0D) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.0F, 12.0F, 0.0F);

				if (this.vboEnabled) {
					this.sky2VBO.bindBuffer();
					GlStateManager.glEnableClientState(32884);
					GlStateManager.glVertexPointer(3, 5126, 12, 0);
					this.sky2VBO.drawArrays(7);
					this.sky2VBO.unbindBuffer();
					GlStateManager.glDisableClientState(32884);
				} else {
					GlStateManager.callList(this.glSkyList2);
				}

				GlStateManager.popMatrix();
				float f18 = 1.0F;
				float f19 = -((float) (d3 + 65.0D));
				float f20 = -1.0F;
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
				bufferbuilder.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
			}

			if (world.provider.isSkyColored()) {
				GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
			} else {
				GlStateManager.color(f, f1, f2);
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, -((float) (d3 - 16.0D)), 0.0F);
			GlStateManager.callList(this.glSkyList2);
			GlStateManager.popMatrix();
			GlStateManager.enableTexture2D();
			GlStateManager.depthMask(true);
		}
	}

}
