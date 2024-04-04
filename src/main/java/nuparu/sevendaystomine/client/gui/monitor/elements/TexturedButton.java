package nuparu.sevendaystomine.client.gui.monitor.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.ColorRGBA;

@SideOnly(Side.CLIENT)
public class TexturedButton extends Button {

	protected ResourceLocation res;

	public TexturedButton(double x, double y, double width, double height, Screen screen, String text,
			ResourceLocation res, int id) {
		super(x, y, width, height, screen, text, id);
		this.res = res;
	}

	@Override
	public void render(float partialTicks) {
		if (isDisabled() == false && isVisible()) {
			ColorRGBA color = isHovered(tickingProcess.getScreen().mouseX,tickingProcess.getScreen().mouseY) ? hovered : normal;
			GL11.glPushMatrix();
			RenderUtils.drawTexturedRect(res, color, x, y, 0, 0, width, height, width,height, 1, zLevel + 1);

			int textColor = textNormal;
			if (isHovered(screen.mouseX, screen.mouseY)) {
				textColor = textHovered;
			}
			GL11.glTranslated(x + (width / 2), y, zLevel + 2);

			String localized = SevenDaysToMine.proxy.localize(getText());
			if (fontSize != 1) {
				GlStateManager.scale(fontSize, fontSize, 1);
				GL11.glTranslated(0, (height * fontSize) / 2d, 0);
			}
			RenderUtils.drawCenteredString(SevenDaysToMine.proxy.localize(localized), 0, 0, textColor);
			GL11.glPopMatrix();

		}
	}
}