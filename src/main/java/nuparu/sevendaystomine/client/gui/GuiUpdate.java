package nuparu.sevendaystomine.client.gui;

import java.io.File;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.proxy.CommonProxy;
import nuparu.sevendaystomine.util.Utils;

@SideOnly(Side.CLIENT)
public class GuiUpdate extends GuiYesNo implements GuiYesNoCallback {

	public GuiUpdate(GuiYesNoCallback p_i1084_1_, int p_i1084_3_) {
		super(p_i1084_1_, I18n.format("mod.new.update", new Object[0]), "", p_i1084_3_);
		this.confirmButtonText = I18n.format("gui.not.remind", new Object[0]);
		this.cancelButtonText = I18n.format("gui.later", new Object[0]);

	}

	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 50 - 105, 200, 100, 20, this.confirmButtonText));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 50 + 105, 200, 100, 20, this.cancelButtonText));
		this.buttonList.add(new GuiButtonTransparent(2,
				this.width / 2 - fontRenderer.getStringWidth(I18n.format("gui.changelog", new Object[0])) / 2,
				145, this.fontRenderer.getStringWidth(I18n.format("gui.changelog", new Object[0])), 20,
				I18n.format("gui.changelog", new Object[0])));
		this.buttonList.add(new GuiButtonTransparent(3,
				this.width / 2 - fontRenderer.getStringWidth(I18n.format("gui.download", new Object[0])) / 2,
				160, this.fontRenderer.getStringWidth(I18n.format("gui.download", new Object[0])), 20,
				I18n.format("gui.download", new Object[0])));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			try {

				NBTTagCompound nbt = new NBTTagCompound();

				nbt.setString("lastCheck", CommonProxy.getVersionChecker().getLatestVersion());
				nbt.setBoolean("hasNotify", true);
				File file = new File("config/7days/version.dat");
				file.getParentFile().mkdirs();
				CompressedStreamTools.write(nbt, file);
			} catch (Exception e) {

				e.printStackTrace();
			}
			this.mc.displayGuiScreen(new GuiMainMenu());
		}
		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiMainMenu());
		}
		if (button.id == 2) {
			String URL = "https://raw.githubusercontent.com/Nuparu00/7-Days-to-Mine/master/changelog";
			Utils.tryOpenWebsite(URL);
		}
		if (button.id == 3) {
			String URL = "https://minecraft.curseforge.com/projects/days-to-mine/files";
			Utils.tryOpenWebsite(URL);
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glPushMatrix();
		GL11.glScalef(2F, 2F, 2F);
		this.drawCenteredString(this.fontRenderer, I18n.format("gui.warning", new Object[0]), this.width / 4, 20,
				13107200);
		GL11.glPopMatrix();
		this.fontRenderer.drawSplitString(
				TextFormatting.GRAY + "" + TextFormatting.ITALIC
						+ I18n.format("mod.new.explain", new Object[0]),
				this.width / 2 - fontRenderer.getStringWidth(I18n.format("mod.new.update", new Object[0])) / 2,
				80, 218, 16777215);
		this.drawString(this.fontRenderer,
				I18n.format("gui.lastVersion", new Object[0]) + TextFormatting.GREEN
						+ CommonProxy.getVersionChecker().getLatestVersion() + "(" + CommonProxy.getVersionChecker().gameVersion + ")",
				this.width / 2 - fontRenderer.getStringWidth(I18n.format("mod.new.update", new Object[0])) / 2,
				140, 16777215);
		this.drawString(this.fontRenderer,
				I18n.format("gui.version", new Object[0]) + TextFormatting.RED + CommonProxy.getVersionChecker().current + "("
						+ Loader.instance().getMCVersionString().split(" ")[1] + ")",
				this.width / 2 - fontRenderer.getStringWidth(I18n.format("mod.new.update", new Object[0])) / 2,
				130, 16777215);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	
	@SideOnly(Side.CLIENT)
	public static class GuiButtonTransparent extends GuiButton {

		public GuiButtonTransparent(int buttonId, int x, int y, String buttonText) {
			super(buttonId, x, y, 200, 20, buttonText);
		}

		public GuiButtonTransparent(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);

		}

		@Override
		 public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				FontRenderer fontrenderer = mc.fontRenderer;

				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.hovered = mouseX >= this.x && mouseY >= this.y
						&& mouseX < this.x + this.width && mouseY < this.y + this.height;

				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);

				this.mouseDragged(mc, mouseX, mouseY);
				int j = 14737632;

				if (packedFGColour != 0) {
					j = packedFGColour;
				} else if (!this.enabled) {
					j = 10526880;
				} else if (this.hovered) {
					j = 16777120;
				}

				this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2,
						this.y + (this.height - 8) / 2, j);
			}
		}
	}
	
}