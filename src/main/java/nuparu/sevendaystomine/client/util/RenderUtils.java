package nuparu.sevendaystomine.client.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.ColorRGBA;

@SideOnly(Side.CLIENT)
public class RenderUtils {

	public static Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderers = Maps.newHashMap();

	public static void drawTexturedRect(ResourceLocation texture, double x, double y, int u, int v, double width,
			double height, double imageWidth, double imageHeight, double scale, double zLevel) {
		GL11.glPushMatrix();
		GlStateManager.color(1, 1, 1, 255.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		double minU = (double) u / (double) imageWidth;
		double maxU = (double) (u + width) / (double) imageWidth;
		double minV = (double) v / (double) imageHeight;
		double maxV = (double) (v + height) / (double) imageHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x + scale * (double) width, y + scale * (double) height, zLevel).tex(maxU, maxV).endVertex();
		worldrenderer.pos(x + scale * (double) width, y, zLevel).tex(maxU, minV).endVertex();
		worldrenderer.pos(x, y, zLevel).tex(minU, minV).endVertex();
		worldrenderer.pos(x, y + scale * (double) height, zLevel).tex(minU, maxV).endVertex();
		tessellator.draw();
		GL11.glPopMatrix();
	}

	public static void drawTexturedRect(ResourceLocation texture, ColorRGBA color, double x, double y, int u, int v,
			double width, double height, double imageWidth, double imageHeight, double scale, double zLevel) {
		GL11.glPushMatrix();
		GlStateManager.color(1, 1, 1, 255.0F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		double minU = (double) u / (double) imageWidth;
		double maxU = (double) (u + width) / (double) imageWidth;
		double minV = (double) v / (double) imageHeight;
		double maxV = (double) (v + height) / (double) imageHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		worldrenderer.pos(x + scale * (double) width, y + scale * (double) height, zLevel).tex(maxU, maxV)
				.color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
		worldrenderer.pos(x + scale * (double) width, y, zLevel).tex(maxU, minV)
				.color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
		worldrenderer.pos(x, y, zLevel).tex(minU, minV)
				.color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
		worldrenderer.pos(x, y + scale * (double) height, zLevel).tex(minU, maxV)
				.color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
		tessellator.draw();
		GL11.glPopMatrix();
	}

	public static void drawColoredRect(ColorRGBA color, double x, double y, double width, double height,
			double zLevel) {

		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		// GL11.glColor4d(color.R, color.G, color.B, color.A);
		GlStateManager.disableTexture2D();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldrenderer.pos((double) x, (double) y + height, zLevel)
				.color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
		worldrenderer.pos((double) x + width, (double) y + height, zLevel)
				.color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
		worldrenderer.pos((double) x + width, (double) y, zLevel)
				.color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
		worldrenderer.pos((double) x, (double) y, zLevel)
				.color((float) color.R, (float) color.G, (float) color.B, (float) color.A).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GL11.glPopMatrix();
	}

	public static void drawRect(double x, double y, int u, int v, double width, double height, double imageWidth,
			double imageHeight, double scale, double zLevel) {
		GL11.glPushMatrix();
		GlStateManager.color(1, 1, 1, 255.0F);
		double minU = (double) u / (double) imageWidth;
		double maxU = (double) (u + width) / (double) imageWidth;
		double minV = (double) v / (double) imageHeight;
		double maxV = (double) (v + height) / (double) imageHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		worldrenderer.pos(x + scale * (double) width, y + scale * (double) height, zLevel).tex(maxU, maxV).endVertex();
		worldrenderer.pos(x + scale * (double) width, y, zLevel).tex(maxU, minV).endVertex();
		worldrenderer.pos(x, y, zLevel).tex(minU, minV).endVertex();
		worldrenderer.pos(x, y + scale * (double) height, zLevel).tex(minU, maxV).endVertex();
		tessellator.draw();
		GL11.glPopMatrix();
	}

	public static void drawCenteredString(String s, double x, double y, int color) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		fontRenderer.drawString(s, (float) (x - fontRenderer.getStringWidth(s) / 2), (float) y, color, false);
		GL11.glPopMatrix();
	}

	public static void drawCenteredString(String s, float x, float y, int color) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		fontRenderer.drawString(s, (float) (x - fontRenderer.getStringWidth(s) / 2), (float) y, color, false);
		GL11.glPopMatrix();
	}

	public static void drawCenteredString(String s, double x, double y, int color, boolean shadow) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		fontRenderer.drawString(s, (float) (x - fontRenderer.getStringWidth(s) / 2), (float) y, color, shadow);
		GL11.glPopMatrix();
	}

	public static void drawString(String s, double x, double y, int color) {
		drawString(s, x, y, color, false);
	}

	public static void drawString(String s, double x, double y, int color, boolean shadow) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		fontRenderer.drawString(s, (float) x, (float) y, color, shadow);
		GL11.glPopMatrix();
	}

	public static void drawString(String s, float x, float y, int color) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		fontRenderer.drawString(s, x, y, color, false);
		GL11.glPopMatrix();
	}

	public static void glScissor(Minecraft mc, double x, double y, double width, double height) {
		glScissor(mc, (int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height));
	}

	public static void glScissor(Minecraft mc, int x, int y, int width, int height) {
		ScaledResolution r = new ScaledResolution(mc);
		int scale = r.getScaleFactor();
		GL11.glScissor(x * scale, (r.getScaledHeight() - y - height) * scale, width * scale, height * scale);
	}

	public static EntityRendererVanilla erv = null;

	public static void renderView(Minecraft mc, Entity entityView, int width, int height, int resWidth, int resHeight,
			float x, float y, float z, float partialTicks) {
		GuiScreen gui = mc.currentScreen;
		if (mc.skipRenderWorld)
			return;
		EntityRenderer entityRenderer = mc.entityRenderer;

		int pass = net.minecraftforge.client.MinecraftForgeClient.getRenderPass();

		Entity renderViewEntity = mc.getRenderViewEntity();
		boolean renderHand = ObfuscationReflectionHelper.getPrivateValue(EntityRenderer.class, entityRenderer,
				"field_175074_C");
		boolean hideGUI = mc.gameSettings.hideGUI;
		mc.gameSettings.hideGUI = true;
		mc.gameSettings.fboEnable = true;
		mc.currentScreen = null;
		ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, entityRenderer, false, "field_175074_C");
		ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, entityRenderer, Minecraft.getSystemTime(),
				"field_78508_Y");
		mc.setRenderViewEntity(entityView);

		ForgeHooksClient.setRenderPass(0);
		if (!OpenGlHelper.isFramebufferEnabled()) {
			return;
		}

		GL11.glPushMatrix();
		Framebuffer frameBuffer = null;
		try {
			GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPushMatrix();

			final float ALPHA_TEST_THRESHOLD = 0.1F;
			GL11.glAlphaFunc(GL11.GL_GREATER, ALPHA_TEST_THRESHOLD);

			final boolean USE_DEPTH = true;
			frameBuffer = new Framebuffer(resWidth, resHeight, USE_DEPTH);

			frameBuffer.bindFramebuffer(true);
			// mc.getRenderPartialTicks()

			// entityRenderer.loadShader(new ResourceLocation("shaders/post/green.json"));
			// mc.entityRenderer.updateCameraAndRender(1, System.nanoTime());
			if (ModConfig.client.useVanillaCameraRendering) {
				if (erv == null) {
					erv = new EntityRendererVanilla(mc, mc.getResourceManager());
				}
				erv.updateShaderGroupSize(resWidth, resHeight);
				erv.updateCameraAndRender(1, System.nanoTime(),frameBuffer,resWidth,resHeight);
			} else {
				mc.entityRenderer.updateCameraAndRender(1, System.nanoTime());
			}

			renderFrameBuffer(frameBuffer, width, height, true, x, y, z);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (frameBuffer != null) {
				frameBuffer.deleteFramebuffer();
			}
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glPopAttrib();

		GL11.glPopMatrix();
		ForgeHooksClient.setRenderPass(pass);
		mc.setRenderViewEntity(renderViewEntity);
		mc.gameSettings.hideGUI = hideGUI;
		mc.currentScreen = gui;
		mc.entityRenderer = entityRenderer;
		ObfuscationReflectionHelper.setPrivateValue(EntityRenderer.class, entityRenderer, renderHand, "field_175074_C");
	}

	public static void renderFrameBuffer(Framebuffer fbo, int width, int height, boolean p_178038_3_, float x, float y,
			float z) {
		GlStateManager.colorMask(true, true, true, false);
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, (double) width, (double) height, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		// z has to -2000 or less
		GlStateManager.translate(0, 0, z);
		GlStateManager.viewport((int) x, (int) y, width, height);
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableAlpha();

		if (p_178038_3_) {
			GlStateManager.disableBlend();
			GlStateManager.enableColorMaterial();
		}

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		fbo.bindFramebufferTexture();
		float f = (float) width;
		float f1 = (float) height;
		float f2 = (float) fbo.framebufferWidth / (float) fbo.framebufferTextureWidth;
		float f3 = (float) fbo.framebufferHeight / (float) fbo.framebufferTextureHeight;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(0.0D, (double) f1, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
		bufferbuilder.pos((double) f, (double) f1, 0.0D).tex((double) f2, 0.0D).color(255, 255, 255, 255).endVertex();
		bufferbuilder.pos((double) f, 0.0D, 0.0D).tex((double) f2, (double) f3).color(255, 255, 255, 255).endVertex();
		bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double) f3).color(255, 255, 255, 255).endVertex();

		tessellator.draw();

		fbo.unbindFramebufferTexture();
		GlStateManager.depthMask(true);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.viewport(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);

	}

	public static Color getColorAt(ResourceLocation res, int x, int y) {
		InputStream is = null;
		BufferedImage image;

		try {
			is = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
			image = ImageIO.read(is);

			return new Color(image.getRGB(x, y), true);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static Color getColorAt(ResourceLocation res, double relativeX, double relativeY) {
		InputStream is = null;
		BufferedImage image;

		try {
			is = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
			image = ImageIO.read(is);

			int x = (int) Math.round(relativeX * image.getWidth());
			int y = (int) Math.round(relativeY * image.getHeight());
			int rgb = image.getRGB(x, y);
			return new Color(rgb, true);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static void drawQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, ResourceLocation res, double angle) {
		if (angle % 360 != 0) {
			v1 = rotatePoint(v1, Vec3d.ZERO, angle);
			v2 = rotatePoint(v2, Vec3d.ZERO, angle);
			v3 = rotatePoint(v3, Vec3d.ZERO, angle);
			v4 = rotatePoint(v4, Vec3d.ZERO, angle);
		}

		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		Minecraft.getMinecraft().getTextureManager().bindTexture(res);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(v1.x, v1.y, v1.z).tex(0.0D, 1.0D).endVertex();
		worldrenderer.pos(v2.x, v2.y, v2.z).tex(1.0D, 1.0D).endVertex();
		worldrenderer.pos(v3.x, v3.y, v3.z).tex(1.0D, 0.0D).endVertex();
		worldrenderer.pos(v4.x, v4.y, v4.z).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static Vec3d rotatePoint(Vec3d point, Vec3d origin, double angle) {
		double s = Math.sin(angle);
		double c = Math.cos(angle);

		point.subtract(origin);

		double x = point.x * c - point.y * s;
		double y = point.x * s + point.y * c;

		return new Vec3d(x + origin.x, y + origin.y, point.z);
	}

}
