package nuparu.sevendaystomine.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.client.util.ResourcesHelper.Image;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PhotoRequestMessage;
import nuparu.sevendaystomine.util.ColorRGBA;

@SideOnly(Side.CLIENT)
public class GuiPhoto extends GuiScreen {

	private String path;
	private Image image = null;

	private long nextUpdate = 0;

	private static final ColorRGBA background = new ColorRGBA(1.0,1.0,1.0);

	public GuiPhoto(String path) {
		this.path = path;
		image = ResourcesHelper.INSTANCE.getImage(path);
		if (image == null) {
			PacketManager.photoRequest.sendToServer(new PhotoRequestMessage(path));
			image = ResourcesHelper.INSTANCE.getImage(path);
		}
	}

	@Override
	public void updateScreen() {
		if (image == null) {
			if (System.currentTimeMillis() >= nextUpdate) {
				image = ResourcesHelper.INSTANCE.tryToGetImage(path);
				nextUpdate = System.currentTimeMillis() + 1000;
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (image != null) {


			int shape = Integer.compare(image.height, image.width);

			int w = (int) (width * 0.75f);
			int h = (int) (height * 0.75f);

			if (shape < 0) {
				w = (int) Math.floor(w * 0.75f);
				h = (int) Math.floor(((float) image.height / (float) image.width) * w);
			} else if (shape == 0) {
				h = (int) Math.floor(h * 0.75f);
				w = h;
			} else {
				h = (int) Math.floor(h * 0.75f);
				w = (int) Math.floor(((float) image.width / (float) image.height) * h);
			}


			h *= 0.75;
			w *= 0.75;
			if (image.res != null) {
				//RenderSystem.setShader(GameRenderer::getPositionColorShader);
				//RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

				int startX = (width/2)-(w/2);
				int startY = (height/2)-(h/2);


				RenderUtils.drawColoredRect(background,startX - 10, startY - 10, w + 20,h + 20,0);
				//RenderSystem.setShader(GameRenderer::getPositionTexShader);
				//RenderSystem.setShaderTexture(0, image.res());
				//guiGraphics.blit(image.res(), startX, startY,w, h,0,0, w,h,w,h);
				RenderUtils.drawTexturedRect(image.res, startX, startY, w,h, w,h, w,h, 1, 1);
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
