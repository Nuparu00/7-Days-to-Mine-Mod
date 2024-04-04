package nuparu.sevendaystomine.computer.process;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.util.ColorRGBA;
import nuparu.sevendaystomine.util.MathUtils;

public class NotesProcess extends WindowedProcess {

	private String text = "";
	private String prevText = "";
	@SideOnly(Side.CLIENT)
	private int updateCount;
	
	private int syncTimer = 150;


	int scrollProgress;
	int lines;

	public NotesProcess() {
		super();
	}

	public NotesProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("notes");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
	}

	@SideOnly(Side.CLIENT)
	public void clientTick() {
		super.clientTick();
		++updateCount;
		if (syncTimer > 0) {
			syncTimer--;
		}
		if (syncTimer == 0) {
			if (!prevText.equals(text)) {
				prevText = text;
				syncTimer = 150;
				sync();
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("text", text);
		nbt.setInteger("progress", scrollProgress);
		nbt.setInteger("lines", lines);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		text = nbt.getString("text");
		prevText = text;
		lines = nbt.getInteger("lines");
		scrollProgress = nbt.getInteger("progress");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		super.render(partialTicks);
		drawWindow(getTitle(), new ColorRGBA(1, 0.949, 0.671), new ColorRGBA(1, 0.921, 0.505));
		String s = text;
		if (this.isFocused()) {
			if (this.updateCount / 6 % 2 == 0) {
				s = s + "" + TextFormatting.BLACK + "_";
			} else {
				s = s + "" + TextFormatting.GRAY + "_";
			}
		}

		List<ITextComponent> l = GuiUtilRenderComponents.splitText(new TextComponentString(s), (int) Math.floor(width),
				Screen.mc.fontRenderer, true, true);
		int k1 = Math.min(8192 / Screen.mc.fontRenderer.FONT_HEIGHT, l.size());
		lines = k1;
		scrollProgress = MathUtils.clamp(scrollProgress, 0, k1);

		GL11.glPushMatrix();
		RenderUtils.glScissor(Screen.mc, x, y + Screen.screen.ySize * title_bar_height + 2, width,
				height - Screen.screen.ySize * title_bar_height - 2);
		GlStateManager.translate(0, -(scrollProgress * Screen.mc.fontRenderer.FONT_HEIGHT), 0);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		for (int l1 = 0; l1 < k1; ++l1) {
			ITextComponent itextcomponent2 = l.get(l1);
			GL11.glPushMatrix();
			GL11.glTranslated(0, 0, offsetRelativeZ(2));
			RenderUtils.drawString(itextcomponent2.getUnformattedText(), x + 2,
					y + 2 + (Screen.screen.ySize * title_bar_height) + l1 * Screen.mc.fontRenderer.FONT_HEIGHT,
					0x000000);

			GL11.glTranslated(0, 0, -offsetRelativeZ(2));
			GL11.glPopMatrix();
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();
	}

	private void pageInsertIntoCurrent(String p_146459_1_) {
		String s1 = text + p_146459_1_;
		int i = Screen.mc.fontRenderer.getWordWrappedHeight(s1 + "" + TextFormatting.BLACK + "_",
				(int) Math.floor(width));

		if (s1.length() < 8192 && !text.equals(s1)) {
			text = s1;
		}
	}

	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
		if (!isFocused() || isMinimized())
			return;
		if (GuiScreen.isKeyComboCtrlV(keyCode)) {
			this.pageInsertIntoCurrent(GuiScreen.getClipboardString());
		} else if (keyCode == 201) {
			scrollProgress = MathUtils.clamp(--scrollProgress, 0, lines);
		} else if (keyCode == 209) {
			scrollProgress = MathUtils.clamp(++scrollProgress, 0, lines);
		} else {
			switch (keyCode) {
			case 14:
				String s = text;

				if (!s.isEmpty()) {
					text = (s.substring(0, s.length() - 1));
				}

				return;
			case 28:
			case 156:
				this.pageInsertIntoCurrent("\n");
				return;
			default:

				if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
					this.pageInsertIntoCurrent(Character.toString(typedChar));
				}
			}
		}
	}
}
