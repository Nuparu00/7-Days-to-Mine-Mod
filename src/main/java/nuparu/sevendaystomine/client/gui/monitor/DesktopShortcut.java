package nuparu.sevendaystomine.client.gui.monitor;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.Application;
import nuparu.sevendaystomine.computer.process.DesktopProcess;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.computer.process.WindowedProcess;
import nuparu.sevendaystomine.computer.process.WindowsDesktopProcess;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;

public class DesktopShortcut implements IDesktopElement, IDraggable {

	protected double x;
	protected double y;

	protected double old_x;
	protected double old_y;

	protected double offsetX;
	protected double offsetY;

	protected int zLevel;
	protected double width;
	protected double height;

	protected Screen screen;
	protected FontRenderer fontRenderer;

	protected boolean isHovered = false;
	protected boolean isDisabled = false;
	protected boolean isFocused = false;
	protected boolean isDragged = false;

	protected double fontSize = 0.4;

	public DesktopProcess process;

	public Application app;

	private long lastClick = 0l;

	public DesktopShortcut(double x, double y, double width, double height, Screen screen, DesktopProcess process,
			Application app) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.screen = screen;

		this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
		this.process = process;

		this.app = app;
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

	@Override
	public void render(float partialTicks) {
		if (isDisabled() == false && isVisible() && app != null) {
			GL11.glPushMatrix();
			GlStateManager.color(1, 1, 1);
			RenderUtils.drawTexturedRect(app.ICON, new ColorRGBA(1d, 1d, 1d), x + 2, y + 2, 0, 0, width - 4, height - 4,
					width - 4, height - 4, 1, zLevel + 1);

			int textColor = 0xffffff;
			GL11.glTranslated(x + (width / 2), y + height + ((height * fontSize) / 3d), zLevel + 2);

			String localizedName = app.getLocalizedName();
			if (fontSize != 1) {
				GlStateManager.scale(fontSize, fontSize, 1);
				GL11.glTranslated(0, (height * fontSize) / 2d, 0);
			}
			if (localizedName.length() > 16) {
				localizedName = localizedName.subSequence(0, 16) + "...";
			}
			List<ITextComponent> l = GuiUtilRenderComponents.splitText(new TextComponentString(localizedName),
					(int) Math.floor(width * 4), Screen.mc.fontRenderer, true, true);
			for (int l1 = 0; l1 < l.size(); ++l1) {
				ITextComponent textcompoenent = l.get(l1);

				RenderUtils.drawCenteredString(textcompoenent.getFormattedText(), 0,
						l1 * Screen.mc.fontRenderer.FONT_HEIGHT, textColor, true);
			}
			GL11.glPopMatrix();

		}

		if (isHovered(screen.mouseX, screen.mouseY) && isDragged == false
				&& WindowedProcess.isDesktopVisible(screen.mouseX, screen.mouseY, process.getTE())) {
			GL11.glPushMatrix();
			GlStateManager.color(1, 1, 1);
			RenderUtils.drawColoredRect(new ColorRGBA(1d, 1d, 1d), screen.mouseX, screen.mouseY,
					fontRenderer.getStringWidth(app.getLocalizedDesc()), 9, zLevel + 2);
			GL11.glTranslatef(0, 0, zLevel + 3);
			RenderUtils.drawString(app.getLocalizedDesc(), screen.mouseX, screen.mouseY, 0x000000);
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
		return isFocused;
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
		if (isDragged == true) {
			isDragged = false;
			if (mouseX < screen.getRelativeX(0) - 20 || mouseY < screen.getRelativeY(0) - 20
					|| mouseX > screen.getRelativeX(0) + screen.xSize + 20
					|| mouseY > screen.getRelativeY(0) + screen.ySize + 20) {
				x = old_x;
				y = old_y;
			} else {
				mouseX = (int) MathUtils.clamp(mouseX, screen.getRelativeX(0), screen.getRelativeX(0) + screen.xSize);
				mouseY = (int) MathUtils.clamp(mouseY, screen.getRelativeY(0), screen.getRelativeY(0) + screen.ySize);
				x = screen.getRelativeX(0) + Math.round((mouseX - screen.getRelativeX(0)) / width) * width;
				y = screen.getRelativeY(0) + Math.round((mouseY - screen.getRelativeY(0)) / height) * height;
				x = MathUtils.clamp(x, screen.getRelativeX(0), screen.getRelativeX(1) - width);

				if (this.process.getTE().getSystem() == EnumSystem.MAC) {
					y = MathUtils.clamp(y, screen.getRelativeY(0) - height + (process.getTaskbarHeight()) + (10d * fontSize),
							screen.getRelativeY(1));
				} else {
					y = MathUtils.clamp(y, screen.getRelativeY(0),
							screen.getRelativeY(1) - height - (process.getTaskbarHeight()) - (10d * fontSize));
				}

				double dX = Math.round((mouseX - screen.getRelativeX(0)) / width) * width;
				double dY = Math.round((mouseY - screen.getRelativeY(0)) / height) * height;

				if (dX > (screen.xSize) - width) {
					dX = (Math.round((screen.xSize) / width) - 1) * width;
					if (dX < 0) {
						dX = 0;
					}
				}

				double h = height + ((height * fontSize) / 2d);

				if (dY > (screen.ySize * 0.8d) - h) {
					dY = (Math.round((screen.ySize * 0.8d) / h) - 1) * h;
					if (dY < 0) {
						dY = 0;
					}
				}

				boolean flag = true;
				for (DesktopShortcut shortcut : process.shortcuts) {
					if (shortcut == this)
						continue;
					if (shortcut.getX() == dX && shortcut.getY() == dY) {
						x = old_x;
						y = old_y;
						// flag = false;
						break;
					}
				}
				if (flag) {
					process.onIconMove(app, dX, dY);
				}
			}
			setOffsetX(0);
			setOffsetY(0);
		}
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (clickedMouseButton == 0 && isFocused()) {
			if (isDragged == false) {
				isDragged = true;
			}
			x = MathUtils.clamp(mouseX - getOffsetX(), screen.getRelativeX(0), screen.getRelativeX(1) - width);

			if (this.process.getTE().getSystem() == EnumSystem.MAC) {
				y = MathUtils.clamp(mouseY - getOffsetY(),
						screen.getRelativeY(0) - height + (process.getTaskbarHeight()) + (10d * fontSize),
						screen.getRelativeY(1));
			} else {
				y = MathUtils.clamp(mouseY - getOffsetY(), screen.getRelativeY(0),
						screen.getRelativeY(1) - height - (process.getTaskbarHeight()) - (10d * fontSize));
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (isHovered(mouseX, mouseY)
				&& WindowedProcess.isDesktopVisible(screen.mouseX, screen.mouseY, process.getTE())) {
			if (mouseButton == 0) {
				isFocused = true;
				old_x = x;
				old_y = y;
				setOffsetX(mouseX - x);
				setOffsetY(mouseY - y);
				if (System.currentTimeMillis() - lastClick <= 500) {
					isDragged = false;
					setOffsetX(0);
					setOffsetY(0);

					open();
				}
				lastClick = System.currentTimeMillis();

			} else {
				isFocused = false;
			}
		} else {
			isFocused = false;
		}
	}

	public void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}

	@Override
	public void setProcess(TickingProcess process) {
		if (process instanceof WindowsDesktopProcess) {
			this.process = (WindowsDesktopProcess) process;
		}
	}

	@Override
	public boolean isDragged() {
		return isDragged;
	}

	@Override
	public void setOffsetX(double offset) {
		this.offsetX = offset;
	}

	@Override
	public void setOffsetY(double offset) {
		this.offsetY = offset;
	}

	@Override
	public double getOffsetX() {
		return this.offsetX;
	}

	@Override
	public double getOffsetY() {
		return this.offsetY;
	}

	public void open() {
		process.tryToRunProcess(app.key);
	}

	public void setFocus(boolean focus) {
		this.isFocused = focus;
	}

}
