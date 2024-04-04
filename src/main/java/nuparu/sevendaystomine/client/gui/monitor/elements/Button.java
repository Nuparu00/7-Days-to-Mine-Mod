package nuparu.sevendaystomine.client.gui.monitor.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.util.ColorRGBA;

@SideOnly(Side.CLIENT)
public class Button implements IScreenElement {

	protected double x;
	protected double y;
	protected int zLevel;
	protected double width;
	protected double height;

	protected Screen screen;
	protected TickingProcess tickingProcess;
	protected FontRenderer fontRenderer;

	private String text = "";

	protected boolean isHovered = false;
	protected boolean isDisabled = false;

	public ColorRGBA normal = new ColorRGBA(0.8, 0.8, 0.8);
	public ColorRGBA hovered = new ColorRGBA(0.9, 0.9, 0.9);
	public ColorRGBA pressed = new ColorRGBA(0.6, 0.6, 0.6);

	public ColorRGBA borderColor = new ColorRGBA(0d, 0d, 0d);

	public boolean border = true;
	public boolean background = true;

	public int textNormal = 0x333333;
	public int textHovered = 0xffff99;

	protected double fontSize = 1;
	
	public final int ID;

	public Button(double x, double y, double width, double height, Screen screen, String text, int id) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.screen = screen;
		this.setText(text);
		this.ID = id;

		this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
	}

	@Override
	public int getZLevel() {
		return this.zLevel;
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public double getWidth() {
		return this.width;
	}

	@Override
	public double getHeight() {
		return this.height;
	}

	@Override
	public void setZLevel(int zLevel) {
		this.zLevel = zLevel;
	}

	@Override
	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public void setHeight(double height) {
		this.height = height;
	}
	
	public int getTextColor() {
		return isHovered(screen.mouseX, screen.mouseY) ? textHovered : textNormal;
	}

	@Override
	public void render(float partialTicks) {
		if (isDisabled() == false && isVisible()) {
			ColorRGBA color = isHovered(tickingProcess.getScreen().mouseX,tickingProcess.getScreen().mouseY) ? hovered : normal;
			GL11.glPushMatrix();
			if (border) {
				RenderUtils.drawColoredRect(borderColor, x - 1, y - 1, width + 2, height + 2, zLevel);
			}
			if (background) {
				RenderUtils.drawColoredRect(color, x, y, width, height, zLevel + 1);
			}
			int textColor = getTextColor();
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

	@Override
	public void update() {
		
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	public boolean isDisabled() {
		return this.isDisabled;
	}

	@Override
	public boolean isHovered(int mouseX, int mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {

	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {

	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (isHovered(mouseX, mouseY) && !isDisabled()) {
			tickingProcess.onButtonPressed(this, mouseButton);
		}
	}

	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}
	
	@Override
	public void setProcess(TickingProcess process) {
		this.tickingProcess = process;
	}
	
	public void setFocus(boolean focus) {
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
