package nuparu.sevendaystomine.client.util;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.List;
import java.util.UUID;

import org.lwjgl.BufferUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemAnalogCamera;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PhotoToServerMessage;
import nuparu.sevendaystomine.util.Utils;

@SideOnly(Side.CLIENT)
public class CameraHelper {
	private IntBuffer pixelBuffer;
	private int[] pixelValues;
	public static final CameraHelper INSTANCE = new CameraHelper();

	public void saveScreenshot(int width, int height, Framebuffer buffer, EntityPlayer playerIn) {
		if (ModConfig.players.allowPhotos) {
			saveScreenshot((String) null, width, height, buffer);
		}
	}

	public void saveScreenshot(String screenshotName, int width, int height, Framebuffer buffer) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player == null)
			return;
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (!stack.isEmpty() && stack.getItem() == ModItems.ANALOG_CAMERA) {
			double dW = 1 - ItemAnalogCamera.getWidth(stack, player);
			double dH = 1 - ItemAnalogCamera.getHeight(stack, player);

			saveScreenshot(screenshotName, (int) (width * dW / 2d), (int) (height * dH / 2d),
					(int) (width - width * dW / 2d), (int) (height - height * dH / 2d), width, height, buffer);
		}
	}

	public void saveScreenshot(String screenshotName, int x, int y, int xx, int yy, int screenWidth, int screenHeight,
			Framebuffer buffer) {

		int width = xx - x;
		int height = yy - y;
		try {

			if (OpenGlHelper.isFramebufferEnabled()) {
				screenWidth = buffer.framebufferTextureWidth;
				screenHeight = buffer.framebufferTextureHeight;
			}

			int i = screenWidth * screenHeight;

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
				GlStateManager.glReadPixels(xx, yy, screenWidth, screenHeight, 32993, 33639, pixelBuffer);
			}

			pixelBuffer.get(pixelValues);
			TextureUtil.processPixelValues(pixelValues, screenWidth, screenHeight);
			BufferedImage bufferedimage = new BufferedImage(screenWidth, screenHeight, 1);
			bufferedimage.setRGB(0, 0, screenWidth, screenHeight, pixelValues, 0, screenWidth);
			if (x != 0 || y != 0) {
				bufferedimage = bufferedimage.getSubimage(x, y, width, height);
			}
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
