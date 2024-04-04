package nuparu.sevendaystomine.client.gui.monitor;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.process.WindowedProcess;
import nuparu.sevendaystomine.util.ColorRGBA;

@SideOnly(Side.CLIENT)
public class TaskbarButton extends Button {

	public WindowedProcess process;
	public boolean marked = false;

	public TaskbarButton(double x, double y, double width, double height, Screen screen, WindowedProcess process) {
		super(x, y, width, height, screen, "", 0);
		this.process = process;
		if (process.getApp() != null) {
			this.setText(process.getApp().getLocalizedName());
		}
		this.hovered = new ColorRGBA(1d, 1d, 1d);
		this.normal = new ColorRGBA(1d, 1d, 1d);
	}

	@Override
	public void render(float partialTicks) {
		if (isDisabled() == false && isVisible() && process.getApp() != null) {
			if (process.isFocused()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE, GL11.GL_ONE_MINUS_DST_COLOR);
				RenderUtils.drawColoredRect(hovered, x, y, width, height, zLevel);
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
			ColorRGBA color = isHovered(screen.mouseX, screen.mouseY) ? hovered : normal;
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderUtils.drawTexturedRect(process.getApp().ICON, color, x + (width * 0.1), y + (height * 0.1), 0, 0,
					width * 0.8, height * 0.8, width * 0.8, height * 0.8, 1, zLevel + 1);
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();

		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (isHovered(mouseX, mouseY) && !isDisabled()) {
			// marks the process to be focused the next tick, because in this tick all
			// processes become unfocused
			marked = true;
		}
	}

	@Override
	public void update() {
		super.update();
		if (marked) {
			process.setMinimized(false);
			process.tryToPutOnTop();
			process.setFocused(true);
			marked = false;
		}
	}

}
