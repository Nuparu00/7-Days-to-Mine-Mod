package com.nuparu.sevendaystomine.util.client;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {

	public static Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderers = Maps.newHashMap();
	
	public static void drawTexturedRect(ResourceLocation texture, double x, double y, int u, int v, double width, double height,
			double imageWidth, double imageHeight, double scale, double zLevel) {
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
	
	public static void drawTexturedRect(ResourceLocation texture, ColorRGBA color, double x, double y, int u, int v, double width, double height,
			double imageWidth, double imageHeight, double scale, double zLevel) {
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
		worldrenderer.pos(x + scale * (double) width, y + scale * (double) height, zLevel).tex(maxU, maxV).color((float)color.R, (float)color.G, (float)color.B, (float)color.A).endVertex();
		worldrenderer.pos(x + scale * (double) width, y, zLevel).tex(maxU, minV).color((float)color.R, (float)color.G, (float)color.B, (float)color.A).endVertex();
		worldrenderer.pos(x, y, zLevel).tex(minU, minV).color((float)color.R, (float)color.G, (float)color.B, (float)color.A).endVertex();
		worldrenderer.pos(x, y + scale * (double) height, zLevel).tex(minU, maxV).color((float)color.R, (float)color.G, (float)color.B, (float)color.A).endVertex();
		tessellator.draw();
		GL11.glPopMatrix();
	}

	public static void drawColoredRect(ColorRGBA color, double x, double y, double width, double height, double zLevel) {

		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		//GL11.glColor4d(color.R, color.G, color.B, color.A);
		GlStateManager.disableTexture2D();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldrenderer.pos((double) x, (double) y + height, zLevel).color((float)color.R, (float)color.G, (float)color.B, (float)color.A).endVertex();
		worldrenderer.pos((double) x + width, (double) y + height, zLevel).color((float)color.R, (float)color.G, (float)color.B, (float)color.A).endVertex();
		worldrenderer.pos((double) x + width, (double) y, zLevel).color((float)color.R, (float)color.G, (float)color.B, (float)color.A).endVertex();
		worldrenderer.pos((double) x, (double) y, zLevel).color((float)color.R, (float)color.G, (float)color.B, (float)color.A).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
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
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		fontRenderer.drawString(s, (float) x, (float) y, color, false);
		GL11.glPopMatrix();
	}
	
	public static void drawString(String s, float x, float y, int color) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		GL11.glPushMatrix();
		fontRenderer.drawString(s, x, y, color, false);
		GL11.glPopMatrix();
	}
}
