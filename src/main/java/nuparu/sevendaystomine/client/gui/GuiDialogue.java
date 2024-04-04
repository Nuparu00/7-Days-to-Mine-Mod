package nuparu.sevendaystomine.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.entity.EntityHuman;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.DialogueSelectionMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.dialogue.Dialogue;
import nuparu.sevendaystomine.util.dialogue.DialogueTree;
import nuparu.sevendaystomine.util.dialogue.Dialogues;

@SideOnly(Side.CLIENT)
public class GuiDialogue extends GuiScreen {

	public EntityHuman entity;

	public static int STYLING_COLOR = 0xff00ff00;

	private float currentScroll;

	public GuiDialogue(EntityHuman entity) {
		this.entity = entity;
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		STYLING_COLOR = 0xffd43131;
		Dialogues dialogues = entity.getDialogues();
		String currentTree = entity.getCurrentDialogue();
		DialogueTree tree = dialogues.getTreeByName(currentTree);
		if (tree == null) {
			
			return;
		}
		ArrayList<Dialogue> options = tree.getOptions();
		for (int i = 0; i < options.size(); i++) {
			Dialogue dialogue = options.get(i);
			addDialogueButton(i, dialogue.getUnloclaizedName());
		}
	}

	/*
	 * DIALOGUE BUTTONS HAVE TO BE ADDED BEFORE ANY OTHER BUTTONS!!
	 */
	public void addDialogueButton(int id, String text) {
		int rectLeft = (this.width / 4);
		int rectRight = (this.width / 4) * 3;
		int rectTop = Math.round(this.height / 2f);

		int x = rectLeft + 5;
		int y = 0;
		if (buttonList.size() != 0) {
			GuiButton button = this.buttonList.get(buttonList.size() - 1);
			y = button.y + button.height;
		} else {
			y = rectTop + (5 * (buttonList.size() + 1)) + 5;
		}

		this.buttonList.add(new GuiDialogueOption(id, x, y, (rectRight - rectLeft) - 5, text, this));
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		int rectLeft = Math.round(this.width / 4f);
		int rectRight = Math.round(this.width / 4f * 3f);
		int rectTop = Math.round(this.height / 2f);
		int rectBottom = Math.round(this.height - 40 - (this.height / 10f));

		GlStateManager.pushMatrix();
		GlStateManager.translate(rectLeft, rectTop - 15, 0);
		GlStateManager.scale(1.5, 1.5, 1.5);
		this.drawString(fontRenderer, entity.getDisplayName().getFormattedText(), 0, 0, STYLING_COLOR);
		GlStateManager.popMatrix();

		this.drawGradientRect(rectLeft, rectTop, rectRight, rectBottom, -1072689136, -804253680);
		drawRect(rectLeft, rectTop - 2, rectRight, rectTop, STYLING_COLOR);
		drawRect(rectLeft, rectBottom, rectRight, rectBottom + 2, STYLING_COLOR);

		GL11.glPushMatrix();

		double ratio = (double) mc.displayWidth / (double) width;

		// CUTS ALL TEXT OUTSIDE OF THE RECT
		GL11.glScissor((int) Math.round(rectLeft * ratio), (int) Math.round((39 + (this.height / 10f)) * ratio),
				(int) Math.round((rectRight - rectLeft) * ratio), (int) Math.round((getContentRectHeight()) * ratio));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		// RenderUtils.drawColoredRect(new ColorRGBA(1,1,1), 0, 0, 2000, 2000, 0);
		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();

	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0 && this.needsScrollBars()) {
			int j = Math.round((getContentRectHeight()) / 10);

			int h = getContentHeight();

			if (i > 0) {
				i = 1;
			}

			if (i < 0) {
				i = -1;
			}

			this.currentScroll = (float) ((double) this.currentScroll - (double) i / (double) j);
			this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);

			int deltaY = (int) Math.round(currentScroll / 2 * h);
			for (GuiButton button : buttonList) {
				if (button instanceof GuiDialogueOption) {
					((GuiDialogueOption) button).setDeltaY(deltaY);
				}
			}
		}

	}

	public boolean needsScrollBars() {
		return buttonList.size() > 0 && getContentHeight() > getContentRectHeight();
	}

	public int getContentHeight() {
		GuiButton last = buttonList.get(buttonList.size() - 1);
		return last.y + last.height - buttonList.get(0).y;
	}

	public int getContentRectHeight() {
		return Math.round((this.height - 40 - (this.height / 10f)) - (this.height / 2f));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof GuiDialogueOption) {
			GuiDialogueOption option = (GuiDialogueOption) button;
			PacketManager.dialogueSelection.sendToServer(new DialogueSelectionMessage(option.dialogueName, entity));
		}
	}

	public class GuiDialogueOption extends GuiButton {

		protected List<String> lines = new ArrayList<String>();
		private final GuiDialogue gui;

		private int rectLeft;
		private int rectRight;
		private int rectTop;
		private int rectBottom;

		private final int defaultX;
		private final int defaultY;

		protected int deltaX = 0;
		protected int deltaY = 0;

		public final String dialogueName;

		public GuiDialogueOption(int buttonId, int x, int y, int widthIn, String dialogueName, GuiDialogue gui) {
			super(buttonId, x, y, widthIn, 10, dialogueName);
			this.gui = gui;
			this.defaultX = x;
			this.defaultY = y;

			this.dialogueName = dialogueName;

			String text = SevenDaysToMine.proxy.localize(dialogueName + ".text");

			// SPLITS THE TEXT TO FIT THE RECT WITHOUT BREAKING WORDS
			if (mc.fontRenderer.getStringWidth(text) <= widthIn) {
				lines.add(text);
			} else {
				while (mc.fontRenderer.getStringWidth(text) > widthIn) {
					for (int i = 2; i < text.length(); i++) {
						String sub = text.substring(0, i);
						if (mc.fontRenderer.getStringWidth(sub) > widthIn) {
							boolean isPreviousEmpty = text.charAt(i - 2) == ' ';
							boolean isThisEmpty = text.charAt(i - 1) == ' ';
							boolean isNextEmpty = text.length() - 1 >= i + 1 ? text.charAt(i) == ' ' : true;

							if (!isThisEmpty && !isPreviousEmpty) {
								for (int j = i - 3; j > 2; j--) {
									boolean b = text.charAt(j) == ' ';
									if (b) {
										i = j;
										break;
									}
								}
							}

							lines.add(text.substring(0, i).trim());
							text = text.substring(i);

							break;
						}
					}
				}
				if (mc.fontRenderer.getStringWidth(text) > 0) {
					lines.add(text.trim());
				}
			}

			this.height = 10 + lines.size() * mc.fontRenderer.FONT_HEIGHT;

			rectLeft = Math.round(gui.width / 4f);
			rectRight = Math.round(gui.width / 4f * 3f);
			rectTop = Math.round(gui.height / 2f);
			rectBottom = Math.round(gui.height - 40 - (gui.height / 10f));

		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			this.y = defaultY - deltaY;

			if (this.visible) {
				FontRenderer fontrenderer = mc.fontRenderer;

				int rectLeft = Math.round(gui.width / 4f);
				int rectRight = Math.round(gui.width / 4f * 3f);
				int rectTop = Math.round(gui.height / 2f);

				this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height)
						&& Utils.isInArea(mouseX, mouseY, rectLeft, rectTop, rectRight - rectLeft,
								gui.getContentRectHeight());
				this.mouseDragged(mc, mouseX, mouseY);
				int j = 14737632;

				if (packedFGColour != 0) {
					j = packedFGColour;
				} else if (!this.enabled) {
					j = 10526880;
				} else if (this.hovered) {
					j = 16777120;
				}

				for (int i = 0; i < lines.size(); i++) {
					String s = lines.get(i);
					this.drawString(fontrenderer, s, this.x, this.y + i * 10, j);
				}

			}
		}

		@Override
		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			return super.mousePressed(mc, mouseX, mouseY)
					&& Utils.isInArea(mouseX, mouseY, rectLeft, rectTop, rectRight - rectLeft, rectBottom - rectTop);
		}

		public void setDeltaY(int delta) {
			this.deltaY = delta;
		}

		public int getDeltaY() {
			return this.deltaY;
		}
	}
}
