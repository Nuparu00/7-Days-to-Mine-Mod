package nuparu.sevendaystomine.client.gui.inventory.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.client.util.ResourcesHelper.Image;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.PhotoRequestMessage;
import org.jetbrains.annotations.NotNull;

public class PhotoScreen extends Screen {

	private final String path;
	private Image image = null;

	private long nextUpdate = 0;

	public PhotoScreen(String path) {
		super(Component.translatable("screen.photo"));
		minecraft = Minecraft.getInstance();
		this.path = path;
		image = ResourcesHelper.INSTANCE.getImage(path);
		if (image == null) {
			PacketManager.photoRequest.sendToServer(new PhotoRequestMessage(path));
			image = ResourcesHelper.INSTANCE.getImage(path);
		}
	}

	@Override
	public void tick() {
		if (image == null) {
			if (System.currentTimeMillis() >= nextUpdate) {
				image = ResourcesHelper.INSTANCE.tryToGetImage(path);
				nextUpdate = System.currentTimeMillis() + 1000;
			}
		}
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		renderBackground(guiGraphics);
		if (image != null) {
			// -1 == (width > height) ; 0 == (width == height) ; 1 == (width < height)
			int shape = Integer.compare(image.height(), image.width());

			int w = width;
			int h = height;

			if (shape < 0) {
				w = (int) Math.floor(w * 0.75f);
				h = (int) Math.floor(((float) image.height() / (float) image.width()) * w);
			} else if (shape == 0) {
				h = (int) Math.floor(h * 0.75f);
				w = h;
			} else {
				h = (int) Math.floor(h * 0.75f);
				w = (int) Math.floor(((float) image.width() / (float) image.height()) * h);
			}


			h *= 0.75;
			w *= 0.75;
			if (image.res() != null) {
				RenderSystem.setShader(GameRenderer::getPositionColorShader);
				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

				int startX = (width/2)-(w/2);
				int startY = (height/2)-(h/2);

				blitColored(guiGraphics, startX - 10, startX + w + 10,startY - 10,startY + h + 10,0,0xffffff);
				//RenderSystem.setShader(GameRenderer::getPositionTexShader);
				//RenderSystem.setShaderTexture(0, image.res());
				guiGraphics.blit(image.res(), startX, startY,w, h,0,0, w,h,w,h);
			}
		}
	}

	public static void blitColored(GuiGraphics graphics, float p_93114_, float p_93115_, float p_93116_, float p_93117_, float p_93118_, int color) {
		Matrix4f matrix4f = graphics.pose().last().pose();

		int r = (color & 0xFF0000) >> 16;
		int g = (color & 0xFF00) >> 8;
		int b = (color & 0xFF);

		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		bufferbuilder.vertex(matrix4f, p_93114_, p_93117_, p_93118_).color(r,g,b,255).endVertex();
		bufferbuilder.vertex(matrix4f, p_93115_, p_93117_, p_93118_).color(r,g,b,255).endVertex();
		bufferbuilder.vertex(matrix4f, p_93115_, p_93116_, p_93118_).color(r,g,b,255).endVertex();
		bufferbuilder.vertex(matrix4f, p_93114_, p_93116_, p_93118_).color(r,g,b,255).endVertex();
		BufferUploader.drawWithShader(bufferbuilder.end());
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
