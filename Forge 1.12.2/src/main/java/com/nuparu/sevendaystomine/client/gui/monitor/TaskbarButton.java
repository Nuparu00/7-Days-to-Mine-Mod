package com.nuparu.sevendaystomine.client.gui.monitor;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import com.nuparu.sevendaystomine.util.client.ColorRGBA;
import com.nuparu.sevendaystomine.util.client.RenderUtils;
import com.nuparu.sevendaystomine.util.computer.WindowedProcess;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TaskbarButton extends Button {

	public WindowedProcess process;
	
	public TaskbarButton(double x, double y, double width, double height, Screen screen,WindowedProcess process) {
		super(x, y, width, height, screen, "", 0);
		this.process = process;
		if(process.getApp() != null) {
		this.text = process.getApp().getLocalizedName();
		}
		this.hovered = new ColorRGBA(1,1,1);
		this.normal = new ColorRGBA(1,1,1);
	}

	@Override
	public void render(float partialTicks) {
		if (isDisabled() == false && isVisible() && process.getApp() != null) {
			ColorRGBA color = isHovered(screen.mouseX,screen.mouseY) ? hovered : normal;
			GL11.glPushMatrix();
			RenderUtils.drawTexturedRect(process.getApp().ICON, color, x+(width*0.1), y+(height*0.1), 0, 0, width*0.8, height*0.8, width*0.8,height*0.8, 1, zLevel + 1);
			GL11.glPopMatrix();

		}
	}

}
