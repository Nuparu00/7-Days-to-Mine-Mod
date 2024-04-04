package nuparu.sevendaystomine.client.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.book.BookData;
import nuparu.sevendaystomine.util.book.BookDataParser;
import nuparu.sevendaystomine.util.book.BookData.CraftingMatrix;
import nuparu.sevendaystomine.util.book.BookData.Image;
import nuparu.sevendaystomine.util.book.BookData.Page;
import nuparu.sevendaystomine.util.book.BookData.Stack;
import nuparu.sevendaystomine.util.book.BookData.TextBlock;
import nuparu.sevendaystomine.util.book.BookData.CraftingMatrix.GhostIngredient;

@SideOnly(Side.CLIENT)
public class GuiBook extends GuiScreen {
	final int xSize = 256;
	final int ySize = 192;
	int pageIndex = 0;
	BookData data;

	GuiNextButton buttonNextPage;
	GuiNextButton buttonPreviousPage;

	public GuiBook(ResourceLocation res) {
		try {
			this.data = BookDataParser.INSTANCE.getBookDataFromResource(res);
		} catch (Exception e) {
			e.printStackTrace();
			if (data == null) {
				Utils.getLogger().error("No book data found for" + res.toString());
				Minecraft.getMinecraft().currentScreen = null;
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (data == null) {
			return;
		}
		Page page = data.pages.get(pageIndex);
		if (page == null || page.res == null)
			return;
		mc.getTextureManager().bindTexture(page.res);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		drawTexturedModalRect(marginHorizontal, marginVertical, 0, 0, xSize, ySize);

		super.drawScreen(mouseX, mouseY, partialTicks);

		for (TextBlock tb : page.textBlocks) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(tb.x + marginHorizontal, tb.y + marginVertical, tb.z);
			GlStateManager.scale(tb.scale, tb.scale, tb.scale);
			List<ITextComponent> l = GuiUtilRenderComponents.splitText(
					new TextComponentString(tb.unlocalized ? SevenDaysToMine.proxy.localize(tb.text) : tb.text),
					(int) Math.floor(tb.width / tb.scale), mc.fontRenderer, true, true);
			for (int i = 0; i < l.size(); i++) {
				ITextComponent component = l.get(i);

				String s = "";
				if (tb.formatting != null) {
					for (TextFormatting tf : tb.formatting) {
						if (tf == null)
							continue;
						s += tf;
					}
				}
				s += component.getFormattedText();

				int color = tb.color;
				if (tb.hoverColor != -1
						&& Utils.isInArea(mouseX, mouseY, marginHorizontal + tb.x - (tb.centered ? tb.width / 2 : 0),
								marginVertical + tb.y, tb.width, tb.height)) {
					color = tb.hoverColor;
				}

				if (tb.centered) {
					RenderUtils.drawCenteredString(s, 0, i * (mc.fontRenderer.FONT_HEIGHT + 1) * tb.scale, color,
							tb.shadow);
				} else {
					RenderUtils.drawString(s, 0, i * (mc.fontRenderer.FONT_HEIGHT + 1) * tb.scale, color, tb.shadow);
				}
			}
			GlStateManager.popMatrix();
		}

		for (Image img : page.images) {
			GlStateManager.pushMatrix();
			RenderUtils.drawTexturedRect(img.res, img.x + marginHorizontal, img.y + marginVertical, 0, 0, img.width,
					img.height, img.width, img.height, 1, img.z);
			GlStateManager.popMatrix();
		}
		for (CraftingMatrix crafting : page.crafting) {

			crafting.render(mc, marginHorizontal + crafting.x, marginVertical + crafting.y, true, partialTicks);

		}
		for (Stack stack : page.stacks) {

			stack.render(mc, marginHorizontal + stack.x, marginVertical + stack.y, partialTicks);
		}
		for (CraftingMatrix crafting : page.crafting) {

			ItemStack itemstack = null;

			for (int i = 0; i < crafting.size(); ++i) {
				GhostIngredient ingredient = crafting.get(i);
				int j = ingredient.getX() + marginHorizontal + crafting.x;
				int k = ingredient.getY() + marginVertical + crafting.y;

				if (mouseX >= j && mouseY >= k && mouseX < j + 16 && mouseY < k + 16) {
					itemstack = ingredient.getItem();
				}
			}

			if (itemstack != null && this.mc.currentScreen != null) {

				this.mc.currentScreen.drawHoveringText(this.mc.currentScreen.getItemToolTip(itemstack), mouseX, mouseY);
			}

		}
		for (Stack stack : page.stacks) {

			int j = marginHorizontal + stack.x;
			int k = marginVertical + stack.y;

			if (mouseX >= j && mouseY >= k && mouseX < j + 16 && mouseY < k + 16 && this.mc.currentScreen != null) {

				this.mc.currentScreen.drawHoveringText(this.mc.currentScreen.getItemToolTip(stack.stack), mouseX,
						mouseY);
			}
		}
	}

	@Override
	public void updateScreen() {
		if (buttonPreviousPage != null) {
			buttonPreviousPage.enabled = pageIndex > 0;
		}
		if (buttonNextPage != null && data != null) {
			buttonNextPage.enabled = data.pages.size() - 1 > pageIndex;
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		int offsetFromScreenLeft = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		buttonList.add(buttonNextPage = new GuiNextButton(1, offsetFromScreenLeft + xSize - 10 - 23,
				marginVertical + 160, true, this));
		buttonList.add(buttonPreviousPage = new GuiNextButton(2, offsetFromScreenLeft + 10, marginVertical + 160, false,
				this));
		
		buttonNextPage.enabled = false;
		buttonPreviousPage.enabled = false;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Page page = data.pages.get(pageIndex);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;

		ScaledResolution sr = new ScaledResolution(mc);

		for (TextBlock tb : page.textBlocks) {
			if (tb.link >= 0 && tb.link < data.pages.size()
					&& Utils.isInArea(mouseX, mouseY, marginHorizontal + tb.x - (tb.centered ? tb.width / 2 : 0),
							marginVertical + tb.y, tb.width, tb.height)) {
				pageIndex = tb.link;
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {

		if (button == buttonNextPage) {
			if (pageIndex < data.pages.size() - 1) {
				++pageIndex;
			}
		} else if (button == buttonPreviousPage) {
			if (pageIndex > 0) {
				--pageIndex;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	class GuiNextButton extends GuiButton {
		private final boolean isNextButton;
		private GuiBook gui;

		public GuiNextButton(int id, int x, int y, boolean isNextButton, GuiBook gui) {
			super(id, x, y, 23, 13, "");
			this.isNextButton = isNextButton;
			this.gui = gui;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (visible && enabled) {
				boolean isButtonPressed = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(gui.data.pages.get(gui.pageIndex).res);
				int textureX = 0;
				int textureY = 192;

				if (isButtonPressed) {
					textureX += 23;
				}

				if (!isNextButton) {
					textureY += 13;
				}

				drawTexturedModalRect(x, y, textureX, textureY, 23, 13);
			}
		}
	}
}
