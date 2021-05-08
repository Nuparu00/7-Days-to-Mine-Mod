package com.nuparu.sevendaystomine.client.gui;

import com.nuparu.sevendaystomine.client.util.RenderUtils;
import com.nuparu.sevendaystomine.client.util.ResourcesHelper;
import com.nuparu.sevendaystomine.client.util.ResourcesHelper.Image;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.PhotoRequestMessage;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPhoto extends GuiScreen {

	private String path;
	private Image image = null;

	private long nextUpdate = 0;

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
			// -1 == (width > height) ; 0 == (width == height) ; 1 == (width < height)
			int shape = image.width > image.height ? -1 : (image.width == image.height ? 0 : 1);
			
			int w = width;
			int h = height;
			
			if(shape == -1) {
				w = (int)Math.floor(w*0.75f);
				h = (int)Math.floor(((float)image.height/(float)image.width)*w);
			}
			else if(shape == 0) {
				h = (int)Math.floor(h*0.75f);
				w = h;
			}
			else if(shape == 1) {
				h = (int)Math.floor(h*0.75f);
				w = (int)Math.floor(((float)image.width/(float)image.height)*h);
			}
			
			RenderUtils.drawTexturedRect(image.res, (width/2)-(w/2), (height/2)-(h/2), w,h, w,h, w,h, 1, 1);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
