package nuparu.sevendaystomine.client.gui.monitor.elements;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.monitor.IScreenElement;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.process.TickingProcess;
import nuparu.sevendaystomine.util.ColorRGBA;

@SideOnly(Side.CLIENT)
public class TextField implements IScreenElement {

	private double x;
	private double y;
	private int zLevel = 1;
	private double width;
	private double height;
	private int maxStringLength = 32;
	public int numberOfLines;

	private String contentText = "";
	private String displayText = "";
	private String defaultText = "";

	private int lineScrollOffset;
	private int maxLength = 32;
	private int cursorCounter;
	private boolean canLoseFocus = true;
	private boolean isFocused = false;
	private boolean isEnabled = true;
	private boolean isDisabled = false;
	private int cursorPosition;
	private int selectionEnd;
	public int enabledColor = 0x000000;
	public int disabledColor = 0x808080;
	private boolean enableBackgroundDrawing = true;

	private Screen screen;
	protected TickingProcess process;
	private FontRenderer fontRenderer;

	public ColorRGBA backgroundColor = new ColorRGBA(1d, 1d, 1d);
	public ColorRGBA cursorColor = new ColorRGBA(0d, 0d, 1d);

	public TextField(double x, double y, double width, double height, Screen screen) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.screen = screen;

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

	public void setMaxStringLength(int length) {
		this.maxStringLength = length;

		if (this.contentText.length() > length) {
			this.contentText = this.contentText.substring(0, length);
		}
	}

	public int getMaxStringLength() {
		return this.maxStringLength;
	}

	@Override
	public void update() {
		if (this.isFocused && !isDisabled()) {
			updateCursorCounter();
		}
	}

	@Override
	public void render(float partialTicks) {
		if (isVisible() && isDisabled() == false) {
			GL11.glPushMatrix();
			RenderUtils.drawColoredRect(backgroundColor, x, y, width, height, zLevel);

			int textColor = this.isFocused && isEnabled ? enabledColor : disabledColor;
			String textToDisplay = "";
			if (getContentText() != null && getContentText().isEmpty()) {
				if (!this.isFocused) {

					textToDisplay = defaultText;
				}

			} else {
				textToDisplay = getContentText();
			}
			int j = this.cursorPosition - this.lineScrollOffset;
			int k = this.selectionEnd - this.lineScrollOffset;
			String s = this.fontRenderer.trimStringToWidth(textToDisplay.substring(Math.max(0, this.lineScrollOffset)),
					(int) Math.ceil(Math.max(0, this.getWidth())));
			if (k > s.length()) {
				k = s.length();
			}
			if (!s.isEmpty()) {
				GL11.glTranslated(x, y, zLevel + 1);
				RenderUtils.drawString(this.fontRenderer.getStringWidth(s) > getWidth() ? s.substring(j) : s, 0, 0,
						textColor);
				GL11.glTranslated(-x, -y, -(zLevel + 1));
			}

			GL11.glPopMatrix();
			if (this.isFocused && this.cursorCounter / 6 % 2 == 0) {
				if (cursorPosition >= lineScrollOffset && lineScrollOffset >= 0
						&& (cursorPosition) <= textToDisplay.length()) {
					String ss = textToDisplay.substring(this.lineScrollOffset, cursorPosition);
					renderCursor(x + this.fontRenderer.getStringWidth(ss), y, 1, fontRenderer.FONT_HEIGHT, zLevel + 2);
				}

			}
		}
	}

	public void renderCursor(double x, double y, double width, double height, double zLevel) {
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double) x, (double) y + height, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.pos((double) x + width, (double) y + height, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.pos((double) x + width, (double) y, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		bufferbuilder.pos((double) x, (double) y, zLevel)
				.color((float) cursorColor.R, (float) cursorColor.G, (float) cursorColor.B, (float) cursorColor.A)
				.endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
		GL11.glPopMatrix();
	}

	public void updateCursorCounter() {
		++this.cursorCounter;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (!isDisabled()) {
			if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
				this.isFocused = true;
				String s = this.fontRenderer.trimStringToWidth(this.contentText.substring(this.lineScrollOffset),
						(int) Math.ceil(this.getWidth()));
				int i = mouseX - (int) Math.ceil(this.x);
				this.cursorPosition = (this.fontRenderer.trimStringToWidth(s, i).length() + this.lineScrollOffset);
				this.cursorCounter = 0;
			} else {
				this.isFocused = false;
			}
		}
	}

	public void writeText(String t) {
		if (contentText.length() >= maxLength) {
			return;
		}
		String s = "";
		String s1 = ChatAllowedCharacters.filterAllowedCharacters(t);
		int i = this.cursorPosition;
		int k = this.getContentText().length() - i;
		int l;
		if (i < this.getContentText().length() && i >= 0) {
			String halfA = this.getContentText().substring(0, i);
			String halfB = this.getContentText().substring(i, this.getContentText().length());
			s = halfA + s1 + halfB;
		} else {
			s = getContentText() + s1;
		}
		l = s1.length();
		this.setContentText(s);
		setCursorPosition(i + l);
	}

	public void setCursorPosition(int pos) {
		this.cursorPosition = pos;
		int i = this.contentText.length();
		this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
		this.setSelectionPos(this.cursorPosition);
	}

	public void setSelectionPos(int position) {
		int i = this.contentText.length();

		if (position > i) {
			position = i;
		}

		if (position < 0) {
			position = 0;
		}

		this.selectionEnd = position;

		if (this.fontRenderer != null) {
			if (this.lineScrollOffset > i) {
				this.lineScrollOffset = i;
			}

			double j = this.getWidth();
			String s = this.fontRenderer.trimStringToWidth(this.contentText.substring(this.lineScrollOffset),
					(int) Math.ceil(j));
			int k = s.length() + this.lineScrollOffset;

			if (position == this.lineScrollOffset) {
				this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.contentText, (int) Math.ceil(j), true)
						.length();
			}

			if (position > k) {
				this.lineScrollOffset += position - k;
			} else if (position <= this.lineScrollOffset) {
				this.lineScrollOffset -= this.lineScrollOffset - position;
			}

			this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (this.isFocused && !isDisabled()) {
			if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
				this.writeText(Character.toString(typedChar));
			}
			if (keyCode == 14) {
				if (getContentText() == null || getContentText().length() == 0) {
					return;
				}
				int i = this.cursorPosition;
				int l;
				String s = "";
				if (i < this.getContentText().length()) {
					String halfA = this.getContentText().substring(0, i);
					String halfB = this.getContentText().substring(i, this.getContentText().length());
					if (halfA.length() > 0) {
						s = halfA.substring(0, halfA.length() - 1) + halfB;
						l = halfA.length() - 1;
					} else {
						s = halfA.substring(0, halfA.length()) + halfB;
						l = halfA.length();
					}

				} else {
					if (getContentText().length() > 0) {
						s = getContentText().substring(0, getContentText().length() - 1);
						l = s.length();
					} else {
						s = getContentText().substring(0, getContentText().length());
						l = s.length();
					}
				}

				setContentText(s);
				setCursorPosition(l);
			}
			if (keyCode == 203) {
				if (cursorPosition > 0) {
					setCursorPosition(--cursorPosition);
				}
			}
			if (keyCode == 205) {
				if (cursorPosition < getContentText().length()) {
					setCursorPosition(++cursorPosition);
				}
			}
			if (GuiScreen.isKeyComboCtrlV(keyCode)) {
				if (this.isEnabled) {
					this.writeText(GuiScreen.getClipboardString());
				}

			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {

	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean isHovered(int mouseX, int mouseY) {
		return (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height);
	}

	@Override
	public boolean isFocused() {
		return this.isFocused;
	}

	@Override
	public boolean isDisabled() {
		return this.isDisabled;
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

	public void setDefaultText(String text) {
		this.defaultText = text;
	}

	@Override
	public void setProcess(TickingProcess process) {
		this.process = process;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		if (contentText == null) {
			contentText = "";
		}
		this.contentText = contentText;
	}

	public String getDefaultText() {
		return this.defaultText;
	}

	public void setFocus(boolean focus) {
		this.isFocused = focus;
	}
}
