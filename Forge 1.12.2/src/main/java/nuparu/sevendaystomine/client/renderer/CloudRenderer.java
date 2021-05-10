package nuparu.sevendaystomine.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

@SideOnly(Side.CLIENT)
public class CloudRenderer extends IRenderHandler {
	private static final ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");

	public CloudRenderer(RenderGlobal render) {

	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		if (mc.world.provider.isSurfaceWorld()) {
			GlStateManager.pushMatrix();
			RenderGlobal render = mc.renderGlobal;

			Entity entity = mc.player;
			double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks;
			double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks;
			double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
			int pass = EntityRenderer.anaglyphEnable ? EntityRenderer.anaglyphField : 2;

			int cloudTickCounter = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, render,
					"field_72773_u");
			// 

			if (mc.gameSettings.shouldRenderClouds() == 2) {

				renderCloudsFancy(partialTicks, pass, x, y, z, world, cloudTickCounter, mc);
			} else {

				GlStateManager.disableCull();
				int k1 = 32;
				int l1 = 8;
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferbuilder = tessellator.getBuffer();
				mc.renderEngine.bindTexture(CLOUDS_TEXTURES);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				Vec3d vec3d = world.getCloudColour(partialTicks);
				float mult = 0;
				if (Utils.isBloodmoon(world)) {
					mult = (float) MathUtils.clamp(Math.abs(world.getCelestialAngle(partialTicks) - 0.54), 0, 0.30);
					mult = (0.30f - mult) / 0.30f;
				}

				float f = (float) vec3d.x + 0.2f * mult;
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

				float f9 = 4.8828125E-4F;
				double d5 = (double) ((float) cloudTickCounter + partialTicks);
				double d3 = x + d5 * 0.029999999329447746D;
				int i2 = MathHelper.floor(d3 / 2048.0D);
				int j2 = MathHelper.floor(z / 2048.0D);
				d3 = d3 - (double) (i2 * 2048);
				double lvt_22_1_ = z - (double) (j2 * 2048);
				float f6 = world.provider.getCloudHeight() - (float) y + 0.33F;
				float f7 = (float) (d3 * 4.8828125E-4D);
				float f8 = (float) (lvt_22_1_ * 4.8828125E-4D);
				bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

				for (int k2 = -256; k2 < 256; k2 += 32) {
					for (int l2 = -256; l2 < 256; l2 += 32) {
						bufferbuilder.pos((double) (k2 + 0), (double) f6, (double) (l2 + 32))
								.tex((double) ((float) (k2 + 0) * 4.8828125E-4F + f7),
										(double) ((float) (l2 + 32) * 4.8828125E-4F + f8))
								.color(f, f1, f2, 0.8F).endVertex();
						bufferbuilder.pos((double) (k2 + 32), (double) f6, (double) (l2 + 32))
								.tex((double) ((float) (k2 + 32) * 4.8828125E-4F + f7),
										(double) ((float) (l2 + 32) * 4.8828125E-4F + f8))
								.color(f, f1, f2, 0.8F).endVertex();
						bufferbuilder.pos((double) (k2 + 32), (double) f6, (double) (l2 + 0))
								.tex((double) ((float) (k2 + 32) * 4.8828125E-4F + f7),
										(double) ((float) (l2 + 0) * 4.8828125E-4F + f8))
								.color(f, f1, f2, 0.8F).endVertex();
						bufferbuilder.pos((double) (k2 + 0), (double) f6, (double) (l2 + 0))
								.tex((double) ((float) (k2 + 0) * 4.8828125E-4F + f7),
										(double) ((float) (l2 + 0) * 4.8828125E-4F + f8))
								.color(f, f1, f2, 0.8F).endVertex();
					}
				}

				tessellator.draw();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableBlend();
				GlStateManager.enableCull();
			}
			GlStateManager.popMatrix();
		}
	}

	private void renderCloudsFancy(float partialTicks, int pass, double x, double y, double z, World world,
			int cloudTickCounter, Minecraft mc) {
		GlStateManager.disableCull();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		float f = 12.0F;
		float f1 = 4.0F;
		double d3 = (double) ((float) cloudTickCounter + partialTicks);
		double d4 = (x + d3 * 0.029999999329447746D) / 12.0D;
		double d5 = z / 12.0D + 0.33000001311302185D;
		float f2 = world.provider.getCloudHeight() - (float) y + 0.33F;
		int k1 = MathHelper.floor(d4 / 2048.0D);
		int l1 = MathHelper.floor(d5 / 2048.0D);
		d4 = d4 - (double) (k1 * 2048);
		d5 = d5 - (double) (l1 * 2048);
		GlStateManager.pushMatrix();
		mc.renderEngine.bindTexture(CLOUDS_TEXTURES);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		Vec3d vec3d = world.getCloudColour(partialTicks);

		float mult = 0;
		if (Utils.isBloodmoon(world)) {
			mult = (float) MathUtils.clamp(Math.abs(world.getCelestialAngle(partialTicks) - 0.54), 0, 0.30);
			mult = (0.30f - mult) / 0.30f;
		}

		float f3 = (float) vec3d.x + 0.2f * mult;
		float f4 = (float) vec3d.y;
		float f5 = (float) vec3d.z;

		if (pass != 2) {
			float f6 = (f3 * 30.0F + f4 * 59.0F + f5 * 11.0F) / 100.0F;
			float f7 = (f3 * 30.0F + f4 * 70.0F) / 100.0F;
			float f8 = (f3 * 30.0F + f5 * 70.0F) / 100.0F;
			f3 = f6;
			f4 = f7;
			f5 = f8;
		}

		float f25 = f3 * 0.9F;
		float f26 = f4 * 0.9F;
		float f27 = f5 * 0.9F;
		float f9 = f3 * 0.7F;
		float f10 = f4 * 0.7F;
		float f11 = f5 * 0.7F;
		float f12 = f3 * 0.8F;
		float f13 = f4 * 0.8F;
		float f14 = f5 * 0.8F;
		float f15 = 0.00390625F;
		float f16 = (float) MathHelper.floor(d4) * 0.00390625F;
		float f17 = (float) MathHelper.floor(d5) * 0.00390625F;
		float f18 = (float) (d4 - (double) MathHelper.floor(d4));
		float f19 = (float) (d5 - (double) MathHelper.floor(d5));
		int i2 = 8;
		int j2 = 4;
		float f20 = 9.765625E-4F;
		GlStateManager.scale(12.0F, 1.0F, 12.0F);

		for (int k2 = 0; k2 < 2; ++k2) {
			if (k2 == 0) {
				GlStateManager.colorMask(false, false, false, false);
			} else {
				switch (pass) {
				case 0:
					GlStateManager.colorMask(false, true, true, true);
					break;
				case 1:
					GlStateManager.colorMask(true, false, false, true);
					break;
				case 2:
					GlStateManager.colorMask(true, true, true, true);
				}
			}

			for (int l2 = -3; l2 <= 4; ++l2) {
				for (int i3 = -3; i3 <= 4; ++i3) {
					bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
					float f21 = (float) (l2 * 8);
					float f22 = (float) (i3 * 8);
					float f23 = f21 - f18;
					float f24 = f22 - f19;

					if (f2 > -5.0F) {
						bufferbuilder.pos((double) (f23 + 0.0F), (double) (f2 + 0.0F), (double) (f24 + 8.0F))
								.tex((double) ((f21 + 0.0F) * 0.00390625F + f16),
										(double) ((f22 + 8.0F) * 0.00390625F + f17))
								.color(f9, f10, f11, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
						bufferbuilder.pos((double) (f23 + 8.0F), (double) (f2 + 0.0F), (double) (f24 + 8.0F))
								.tex((double) ((f21 + 8.0F) * 0.00390625F + f16),
										(double) ((f22 + 8.0F) * 0.00390625F + f17))
								.color(f9, f10, f11, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
						bufferbuilder.pos((double) (f23 + 8.0F), (double) (f2 + 0.0F), (double) (f24 + 0.0F))
								.tex((double) ((f21 + 8.0F) * 0.00390625F + f16),
										(double) ((f22 + 0.0F) * 0.00390625F + f17))
								.color(f9, f10, f11, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
						bufferbuilder.pos((double) (f23 + 0.0F), (double) (f2 + 0.0F), (double) (f24 + 0.0F))
								.tex((double) ((f21 + 0.0F) * 0.00390625F + f16),
										(double) ((f22 + 0.0F) * 0.00390625F + f17))
								.color(f9, f10, f11, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
					}

					if (f2 <= 5.0F) {
						bufferbuilder
								.pos((double) (f23 + 0.0F), (double) (f2 + 4.0F - 9.765625E-4F), (double) (f24 + 8.0F))
								.tex((double) ((f21 + 0.0F) * 0.00390625F + f16),
										(double) ((f22 + 8.0F) * 0.00390625F + f17))
								.color(f3, f4, f5, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
						bufferbuilder
								.pos((double) (f23 + 8.0F), (double) (f2 + 4.0F - 9.765625E-4F), (double) (f24 + 8.0F))
								.tex((double) ((f21 + 8.0F) * 0.00390625F + f16),
										(double) ((f22 + 8.0F) * 0.00390625F + f17))
								.color(f3, f4, f5, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
						bufferbuilder
								.pos((double) (f23 + 8.0F), (double) (f2 + 4.0F - 9.765625E-4F), (double) (f24 + 0.0F))
								.tex((double) ((f21 + 8.0F) * 0.00390625F + f16),
										(double) ((f22 + 0.0F) * 0.00390625F + f17))
								.color(f3, f4, f5, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
						bufferbuilder
								.pos((double) (f23 + 0.0F), (double) (f2 + 4.0F - 9.765625E-4F), (double) (f24 + 0.0F))
								.tex((double) ((f21 + 0.0F) * 0.00390625F + f16),
										(double) ((f22 + 0.0F) * 0.00390625F + f17))
								.color(f3, f4, f5, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
					}

					if (l2 > -1) {
						for (int j3 = 0; j3 < 8; ++j3) {
							bufferbuilder
									.pos((double) (f23 + (float) j3 + 0.0F), (double) (f2 + 0.0F),
											(double) (f24 + 8.0F))
									.tex((double) ((f21 + (float) j3 + 0.5F) * 0.00390625F + f16),
											(double) ((f22 + 8.0F) * 0.00390625F + f17))
									.color(f25, f26, f27, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + (float) j3 + 0.0F), (double) (f2 + 4.0F),
											(double) (f24 + 8.0F))
									.tex((double) ((f21 + (float) j3 + 0.5F) * 0.00390625F + f16),
											(double) ((f22 + 8.0F) * 0.00390625F + f17))
									.color(f25, f26, f27, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + (float) j3 + 0.0F), (double) (f2 + 4.0F),
											(double) (f24 + 0.0F))
									.tex((double) ((f21 + (float) j3 + 0.5F) * 0.00390625F + f16),
											(double) ((f22 + 0.0F) * 0.00390625F + f17))
									.color(f25, f26, f27, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + (float) j3 + 0.0F), (double) (f2 + 0.0F),
											(double) (f24 + 0.0F))
									.tex((double) ((f21 + (float) j3 + 0.5F) * 0.00390625F + f16),
											(double) ((f22 + 0.0F) * 0.00390625F + f17))
									.color(f25, f26, f27, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
						}
					}

					if (l2 <= 1) {
						for (int k3 = 0; k3 < 8; ++k3) {
							bufferbuilder
									.pos((double) (f23 + (float) k3 + 1.0F - 9.765625E-4F), (double) (f2 + 0.0F),
											(double) (f24 + 8.0F))
									.tex((double) ((f21 + (float) k3 + 0.5F) * 0.00390625F + f16),
											(double) ((f22 + 8.0F) * 0.00390625F + f17))
									.color(f25, f26, f27, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + (float) k3 + 1.0F - 9.765625E-4F), (double) (f2 + 4.0F),
											(double) (f24 + 8.0F))
									.tex((double) ((f21 + (float) k3 + 0.5F) * 0.00390625F + f16),
											(double) ((f22 + 8.0F) * 0.00390625F + f17))
									.color(f25, f26, f27, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + (float) k3 + 1.0F - 9.765625E-4F), (double) (f2 + 4.0F),
											(double) (f24 + 0.0F))
									.tex((double) ((f21 + (float) k3 + 0.5F) * 0.00390625F + f16),
											(double) ((f22 + 0.0F) * 0.00390625F + f17))
									.color(f25, f26, f27, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + (float) k3 + 1.0F - 9.765625E-4F), (double) (f2 + 0.0F),
											(double) (f24 + 0.0F))
									.tex((double) ((f21 + (float) k3 + 0.5F) * 0.00390625F + f16),
											(double) ((f22 + 0.0F) * 0.00390625F + f17))
									.color(f25, f26, f27, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
						}
					}

					if (i3 > -1) {
						for (int l3 = 0; l3 < 8; ++l3) {
							bufferbuilder
									.pos((double) (f23 + 0.0F), (double) (f2 + 4.0F),
											(double) (f24 + (float) l3 + 0.0F))
									.tex((double) ((f21 + 0.0F) * 0.00390625F + f16),
											(double) ((f22 + (float) l3 + 0.5F) * 0.00390625F + f17))
									.color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + 8.0F), (double) (f2 + 4.0F),
											(double) (f24 + (float) l3 + 0.0F))
									.tex((double) ((f21 + 8.0F) * 0.00390625F + f16),
											(double) ((f22 + (float) l3 + 0.5F) * 0.00390625F + f17))
									.color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + 8.0F), (double) (f2 + 0.0F),
											(double) (f24 + (float) l3 + 0.0F))
									.tex((double) ((f21 + 8.0F) * 0.00390625F + f16),
											(double) ((f22 + (float) l3 + 0.5F) * 0.00390625F + f17))
									.color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + 0.0F), (double) (f2 + 0.0F),
											(double) (f24 + (float) l3 + 0.0F))
									.tex((double) ((f21 + 0.0F) * 0.00390625F + f16),
											(double) ((f22 + (float) l3 + 0.5F) * 0.00390625F + f17))
									.color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
						}
					}

					if (i3 <= 1) {
						for (int i4 = 0; i4 < 8; ++i4) {
							bufferbuilder
									.pos((double) (f23 + 0.0F), (double) (f2 + 4.0F),
											(double) (f24 + (float) i4 + 1.0F - 9.765625E-4F))
									.tex((double) ((f21 + 0.0F) * 0.00390625F + f16),
											(double) ((f22 + (float) i4 + 0.5F) * 0.00390625F + f17))
									.color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + 8.0F), (double) (f2 + 4.0F),
											(double) (f24 + (float) i4 + 1.0F - 9.765625E-4F))
									.tex((double) ((f21 + 8.0F) * 0.00390625F + f16),
											(double) ((f22 + (float) i4 + 0.5F) * 0.00390625F + f17))
									.color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + 8.0F), (double) (f2 + 0.0F),
											(double) (f24 + (float) i4 + 1.0F - 9.765625E-4F))
									.tex((double) ((f21 + 8.0F) * 0.00390625F + f16),
											(double) ((f22 + (float) i4 + 0.5F) * 0.00390625F + f17))
									.color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
							bufferbuilder
									.pos((double) (f23 + 0.0F), (double) (f2 + 0.0F),
											(double) (f24 + (float) i4 + 1.0F - 9.765625E-4F))
									.tex((double) ((f21 + 0.0F) * 0.00390625F + f16),
											(double) ((f22 + (float) i4 + 0.5F) * 0.00390625F + f17))
									.color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
						}
					}

					tessellator.draw();
				}
			}
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

}
