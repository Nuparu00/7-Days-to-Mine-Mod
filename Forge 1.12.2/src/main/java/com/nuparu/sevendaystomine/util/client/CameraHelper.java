package com.nuparu.sevendaystomine.util.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.PhotoToServerMessage;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CameraHelper {
	private IntBuffer pixelBuffer;
	private int[] pixelValues;
	public static final CameraHelper INSTANCE = new CameraHelper();

	public void saveScreenshot(int width, int height, Framebuffer buffer, EntityPlayer playerIn) {
		saveScreenshot((String) null, width, height, buffer);
	}

	public void saveScreenshot(String screenshotName, int width, int height, Framebuffer buffer) {

		try {

			if (OpenGlHelper.isFramebufferEnabled()) {
				width = buffer.framebufferTextureWidth;
				height = buffer.framebufferTextureHeight;
			}

			int i = width * height;

			if (pixelBuffer == null || pixelBuffer.capacity() < i) {
				pixelBuffer = BufferUtils.createIntBuffer(i);
				pixelValues = new int[i];
			}

			GlStateManager.glPixelStorei(3333, 1);
			GlStateManager.glPixelStorei(3317, 1);
			pixelBuffer.clear();

			if (OpenGlHelper.isFramebufferEnabled()) {
				GlStateManager.bindTexture(buffer.framebufferTexture);
				GlStateManager.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
			} else {
				GlStateManager.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
			}

			pixelBuffer.get(pixelValues);
			TextureUtil.processPixelValues(pixelValues, width, height);
			BufferedImage bufferedimage = new BufferedImage(width, height, 1);
			bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);

			sendFile(bufferedimage);

		} catch (Exception exception) {
			exception.printStackTrace();

		}

	}

	public void sendFile(BufferedImage img) {
		byte[] bytes = Utils.getBytes(img);
		List<byte[]> chunks = Utils.divideArray(bytes, 30000);
		int parts = chunks.size();
		String ID = UUID.randomUUID().toString();
		for (int i = 0; i < parts; i++) {
			PacketManager.photoToServer.sendToServer(new PhotoToServerMessage(chunks.get(i), i, parts, ID));
		}
	}

}
