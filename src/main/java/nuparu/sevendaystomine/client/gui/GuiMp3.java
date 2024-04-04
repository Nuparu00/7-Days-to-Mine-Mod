package nuparu.sevendaystomine.client.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.FilenameUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.MP3Helper;
import nuparu.sevendaystomine.client.util.MP3Helper.Audio;
import nuparu.sevendaystomine.client.util.MP3Helper.AudioPlayer;
import nuparu.sevendaystomine.client.util.MP3Helper.EnumAudioMode;

@SideOnly(Side.CLIENT)
public class GuiMp3 extends GuiScreen {
	private final int imageHeight = 194;
	private final int imageWidth = 176;
	NumberFormat formatter = new DecimalFormat("00");
	int guiLeft = 0;
	int guiTop = 0;
	static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID, "textures/gui/mp3player.png");
	private GuiButtonPause buttonPause;
	private GuiButtonStop buttonStop;
	private GuiButtonRepeat buttonRepeat;
	private GuiButtonForward buttonForward;
	private GuiButtonBackward buttonBackward;
	Slider slider = new Slider();
	VolumeSlider volumeSlider = new VolumeSlider();
	GuiScrollbar scrollbar;
	String textNormal;
	String textShifted;
	protected List<GuiButton> buttons = Lists.<GuiButton>newArrayList();
	int i;
	int pages;
	int page = 1;
	boolean playCompleted;
	float volume = 0.0f;
	long lastPress = 0L;
	int move = 0;

	public GuiMp3() {
		this.height = 194;
	}

	public void selectModIndex(int index) {

		if (index <= MP3Helper.files.size()) {
			long now = System.nanoTime();
			if (index == MP3Helper.selected) {
				if ((now - lastPress) / 1000000 < 500) {
					playMusic(MP3Helper.files.get(index));
					lastPress = 0L;
				} else {
					lastPress = now;
				}

			} else {
				MP3Helper.selected = index;
				lastPress = now;
			}
		} else {
			MP3Helper.selected = MP3Helper.files.size();
			lastPress = 0L;
		}

	}

	public boolean modIndexSelected(int index) {
		return index == MP3Helper.selected;
	}

	public void getFiles() {
		MP3Helper.def.mkdirs();
		Path dir = Paths.get("./resources/audio/");

		try {

			ArrayList<Audio> arrayList = new ArrayList<Audio>();
			DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{wav}");
			/*
			 * int previousY = this.guiTop+42-20; i = 0;
			 */
			for (Path entry : stream) {
				Audio audio = new Audio();
				audio.setPath(entry);
				audio.setName(FilenameUtils.removeExtension(entry.getFileName().toString()));
				// audio.duration =
				AudioInputStream audioStream = null;
				try {
					File audioFile = new File(entry.toString());
					audioStream = AudioSystem.getAudioInputStream(audioFile);
					AudioFormat format = audioStream.getFormat();
					long frames = audioStream.getFrameLength();
					audio.setDuration((frames + 0.0) / format.getFrameRate());
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (audioStream != null) {
						audioStream.close();
					}
				}
				arrayList.add(audio);
			}
			MP3Helper.files = arrayList;
			stream.close();

		} catch (IOException x) {
			throw new RuntimeException(String.format("error reading folder %s: %s", dir, x.getMessage()), x);
		}
	}

	Minecraft getMinecraftInstance() {
		return mc;
	}

	FontRenderer getFontRenderer() {
		return fontRenderer;
	}

	@Override
	public void initGui() {
		getFiles();
		this.guiLeft = (this.width - 176) / 2;
		this.guiTop = (this.height - 166) / 2;
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		int posX = (this.width) / 2;
		this.scrollbar = new GuiScrollbar(this, posX - 75, this.guiTop + 45, MP3Helper.files, 150);
		buttonPause = new GuiButtonPause(0, posX - 10, this.guiTop + 155);

		buttonBackward = new GuiButtonBackward(1, posX - 35, this.guiTop + 155);

		buttonStop = new GuiButtonStop(2, posX - 60, this.guiTop + 155);

		buttonRepeat = new GuiButtonRepeat(3, posX - 85, this.guiTop + 155);

		buttonForward = new GuiButtonForward(4, posX + 15, this.guiTop + 155);

		slider.x = posX - 75;
		slider.y = this.guiTop + 20;
		slider.xSize = 150;
		slider.ySize = 10;
		slider.zLevel = zLevel + 1;

		volumeSlider.x = posX + 36;
		volumeSlider.y = this.guiTop + 160;
		volumeSlider.xSize = 50;
		volumeSlider.ySize = 10;
		volumeSlider.zLevel = zLevel + 1;

		buttonList.add(buttonPause);
		buttonList.add(buttonStop);
		buttonList.add(buttonRepeat);
		buttonList.add(buttonBackward);
		buttonList.add(buttonForward);

	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		if (MP3Helper.getAudioPlayer().getAudioClip() != null) {
			double currentSecond = MP3Helper.getAudioPlayer().getAudioClip().getMicrosecondPosition() / 1000000;
			if (MP3Helper.getAudioPlayer().getDuration() != 0) {
				slider.setValue((double) (currentSecond / MP3Helper.getAudioPlayer().getDurationInSecs()));
			}
		}
		volumeSlider.setValue(MP3Helper.audioVolume);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int parWidth, int parHeight, float p_73863_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.renderEngine.bindTexture(TEXTURE);
		int k = (this.width - this.imageWidth) / 2;
		int l = (this.height - this.imageHeight) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.imageWidth, this.imageHeight);
		if (this.scrollbar != null) {
			this.scrollbar.drawScreen(parWidth, parHeight, p_73863_3_);
		}
		if (volumeSlider != null) {
			slider.render();
		}
		if (volumeSlider != null) {
			volumeSlider.render();
		}
		double currentSecond = 0d;
		double durationSecond = 0d;

		if (MP3Helper.files == null || MP3Helper.files.size() <= 0) {
			this.drawCenteredString(fontRenderer,
					new TextComponentTranslation("mp3.nofile.0", new Object[0]).getUnformattedText(), this.guiLeft + 88,
					this.guiTop + 50, 5921370);
			this.drawCenteredString(fontRenderer,
					new TextComponentTranslation("mp3.nofile.1", new Object[0]).getUnformattedText(), this.guiLeft + 88,
					this.guiTop + 62, 5921370);
			fontRenderer.drawSplitString(MP3Helper.def.getAbsolutePath(), this.guiLeft + 18, this.guiTop + 74, 140,
					5921370);
		}

		if (MP3Helper.getAudioPlayer().getAudioClip() != null) {
			currentSecond = MP3Helper.getAudioPlayer().getAudioClip().getMicrosecondPosition() / 1000000;
			durationSecond = MP3Helper.getAudioPlayer().getDurationInSecs();
			if (MP3Helper.selected < MP3Helper.files.size()) {
				if (MP3Helper.isPlaying) {
					if (textNormal == null) {
						textNormal = MP3Helper.files.get(MP3Helper.selected).getName();
						textShifted = textNormal + " ";
					}
					if (!textNormal.equals(MP3Helper.files.get(MP3Helper.selected).getName())) {
						textNormal = MP3Helper.files.get(MP3Helper.selected).getName();
						textShifted = textNormal + " ";
					}
					if (textShifted == null) {
						textShifted = textNormal + " ";
					}
					if (textShifted != null) {
						if (move >= 20) {
							textShifted = cyclicLeftShift(textShifted, 1);
							move = 0;
						} else {
							move++;
						}
						this.drawCenteredString(fontRenderer,
								fontRenderer.trimStringToWidth(
										textShifted.substring(0, Math.min(textShifted.length(), 50)), 150),
								this.guiLeft + 88, this.guiTop + 5, 5921370);
					}
				} else {
					textShifted = null;
				}
			}
		}
		String string = new StringBuilder("").append(formatter.format(Math.floor(currentSecond / 60))).append(":")
				.append(formatter.format(currentSecond % 60)).append("/")
				.append(formatter.format(Math.floor(durationSecond / 60))).append(":")
				.append(formatter.format(durationSecond % 60)).toString();
		this.fontRenderer.drawString(string, this.guiLeft + 13, this.guiTop + 32, 4210752);

		// zLevel = 100.0F;

		super.drawScreen(parWidth, parHeight, p_73863_3_);
	}

	public String cyclicLeftShift(String s, int k) {
		k = k % s.length();
		return s.substring(k) + s.substring(0, k);
	}

	/**
	 * Called when a mouse button is pressed and the mouse is moved around.
	 * Parameters are : mouseX, mouseY, lastButtonClicked & timeSinceMouseClick.
	 */
	@Override
	protected void mouseClickMove(int parMouseX, int parMouseY, int parLastButtonClicked, long parTimeSinceMouseClick) {

	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		slider.mouseClicked(mouseX, mouseY, mouseButton);
		volumeSlider.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton parButton) {
		if (parButton == buttonPause) {
			if (!MP3Helper.isPlaying) {

				if (MP3Helper.selected < MP3Helper.files.size()) {
					textShifted = MP3Helper.files.get(MP3Helper.selected).getName();
					playMusic(MP3Helper.files.get(MP3Helper.selected));
				}
			} else {
				if (MP3Helper.getAudioPlayer().isPaused()) {
					MP3Helper.getAudioPlayer().resume();
				} else {
					MP3Helper.getAudioPlayer().pause();
				}

			}
		}
		if (parButton == buttonStop) {
			if (MP3Helper.isPlaying) {
				MP3Helper.getAudioPlayer().stop();
			}
		}
		if (parButton == buttonRepeat) {
			if (MP3Helper.mode == EnumAudioMode.PLAY_ONCE) {
				MP3Helper.mode = EnumAudioMode.LOOP;
			} else if (MP3Helper.mode == EnumAudioMode.LOOP) {
				MP3Helper.mode = EnumAudioMode.CYCLE;
			} else if (MP3Helper.mode == EnumAudioMode.CYCLE) {
				MP3Helper.mode = EnumAudioMode.PLAY_ONCE;
			}

		}
		if (parButton == buttonForward) {
			if (MP3Helper.selected < MP3Helper.files.size()) {
				if (MP3Helper.isPlaying) {

					MP3Helper.getAudioPlayer().stop();
				}
				if (MP3Helper.selected + 1 < MP3Helper.files.size()) {
					MP3Helper.selected++;
				} else {
					MP3Helper.selected = 0;
				}
				playMusic(MP3Helper.files.get(MP3Helper.selected));
			}
		}
		if (parButton == buttonBackward) {
			if (MP3Helper.selected < MP3Helper.files.size()) {
				if (MP3Helper.isPlaying) {

					MP3Helper.getAudioPlayer().stop();
				}
				if (MP3Helper.selected > 0) {
					MP3Helper.selected--;
				} else {
					MP3Helper.selected = MP3Helper.files.size() - 1;
				}
				playMusic(MP3Helper.files.get(MP3Helper.selected));
			}
		}
	}

	public static void playMusic(final Audio audio) {

		if (MP3Helper.isPlaying) {

			MP3Helper.getAudioPlayer().getAudioClip().setMicrosecondPosition(0L);
			MP3Helper.getAudioPlayer().stop();
		}

		MP3Helper.playbackThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					MP3Helper.getAudioPlayer().load(audio.getPath().toString());
					MP3Helper.getAudioPlayer().setAudio(audio);
					MP3Helper.getAudioPlayer().play();

				} catch (UnsupportedAudioFileException ex) {

					ex.printStackTrace();
				} catch (LineUnavailableException ex) {

					ex.printStackTrace();
				} catch (IOException ex) {

					ex.printStackTrace();
				} catch (Exception ex) {

					ex.printStackTrace();
				}

			}
		});

		MP3Helper.playbackThread.start();
	}

	public void setVolume(float volume) {
		MP3Helper.audioVolume = volume;
		MP3Helper.getAudioPlayer().setVolume(MP3Helper.audioVolume);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed() {

	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	class GuiButtonPause extends GuiButton {
		public GuiButtonPause(int buttonID, int xPos, int yPos) {
			super(buttonID, xPos, yPos, 20, 20, "");
		}

		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {

				if (!MP3Helper.isPlaying || MP3Helper.isPlaying && MP3Helper.getAudioPlayer().isPaused()) {
					mc.getTextureManager().bindTexture(TEXTURE);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 38;

					if (flag) {
						i += this.height;
					}

					this.drawTexturedModalRect(this.x, this.y, 176, i, this.width, this.height);
				} else {
					mc.getTextureManager().bindTexture(TEXTURE);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 38;

					if (flag) {
						i += this.height;
					}

					this.drawTexturedModalRect(this.x, this.y, 196, i, this.width, this.height);
				}
			}
		}
	}

	class GuiButtonForward extends GuiButton {
		public GuiButtonForward(int buttonID, int xPos, int yPos) {
			super(buttonID, xPos, yPos, 20, 20, "");
		}

		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				mc.getTextureManager().bindTexture(TEXTURE);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;
				int i = 78;

				if (flag) {
					i += this.height;
				}

				this.drawTexturedModalRect(this.x, this.y, 176, i, this.width, this.height);
			}
		}
	}

	class GuiButtonBackward extends GuiButton {

		public GuiButtonBackward(int buttonID, int xPos, int yPos) {
			super(buttonID, xPos, yPos, 20, 20, "");
		}

		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				mc.getTextureManager().bindTexture(TEXTURE);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;
				int i = 78;

				if (flag) {
					i += this.height;
				}

				this.drawTexturedModalRect(this.x, this.y, 196, i, this.width, this.height);
			}
		}
	}

	class GuiButtonRepeat extends GuiButton {
		public GuiButtonRepeat(int buttonID, int xPos, int yPos) {
			super(buttonID, xPos, yPos, 20, 20, "");
		}

		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {

				if (MP3Helper.mode == EnumAudioMode.PLAY_ONCE) {
					mc.getTextureManager().bindTexture(TEXTURE);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 38;

					if (flag) {
						i += this.height;
					}

					this.drawTexturedModalRect(this.x, this.y, 236, i, this.width, this.height);
				} else if (MP3Helper.mode == EnumAudioMode.LOOP) {
					mc.getTextureManager().bindTexture(TEXTURE);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 78;

					if (flag) {
						i -= this.height;
					}

					this.drawTexturedModalRect(this.x, this.y, 236, i, this.width, this.height);
				} else if (MP3Helper.mode == EnumAudioMode.CYCLE) {
					mc.getTextureManager().bindTexture(TEXTURE);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 98;

					if (flag) {
						i += this.height;
					}

					this.drawTexturedModalRect(this.x, this.y, 236, i, this.width, this.height);
				}
			}
		}
	}

	class GuiButtonStop extends GuiButton {

		public GuiButtonStop(int buttonID, int xPos, int yPos) {
			super(buttonID, xPos, yPos, 20, 20, "");
		}

		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {

				mc.getTextureManager().bindTexture(TEXTURE);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;
				int i = 38;

				if (flag) {
					i += this.height;
				}

				this.drawTexturedModalRect(this.x, this.y, 216, i, this.width, this.height);

			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static class GuiScrollbar extends GuiScrollingList {
		private GuiMp3 parent;
		private ArrayList<Audio> mods;

		public GuiScrollbar(GuiMp3 parent, int x, int y, ArrayList<Audio> mods, int listWidth) {
			super(parent.getMinecraftInstance(), 150, 100, y, y + 100, x, 35, parent.width, parent.height);
			this.parent = parent;
			this.mods = mods;

		}

		@Override
		protected int getSize() {
			return mods.size();
		}

		@Override
		protected void elementClicked(int index, boolean doubleClick) {
			this.parent.selectModIndex(index);
		}

		@Override
		protected boolean isSelected(int index) {
			return this.parent.modIndexSelected(index);
		}

		@Override
		protected void drawBackground() {
			// this.parent.drawDefaultBackground();
		}

		@Override
		protected int getContentHeight() {
			return (this.getSize()) * 35 + 1;
		}

		@Override
		protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
			FontRenderer font = this.parent.getFontRenderer();
			font.drawString(font.trimStringToWidth(mods.get(idx).getName(), listWidth - 10), this.left + 3, top + 2,
					0xFFFFFF);
			font.drawString(font.trimStringToWidth(
					new StringBuilder("").append(parent.formatter.format(Math.floor(mods.get(idx).getDuration() / 60)))
							.append(":").append(parent.formatter.format(mods.get(idx).getDuration() % 60)).toString(),
					listWidth - 10), this.left + 3, top + 12, 0xCCCCCC);

		}
	}

	@SideOnly(Side.CLIENT)
	public static class Slider {
		double x;
		double y;
		double xSize;
		double ySize;
		double zLevel;

		double value;

		Vec3d color = new Vec3d(0.65, 0.65, 0.65);

		public Slider() {
		}

		public Slider(double x, double y, double xSize, double ySize, double zLevel) {
			this.x = x;
			this.y = y;
			this.xSize = xSize;
			this.ySize = ySize;
			this.zLevel = zLevel;
		}

		public void render() {
			drawColoredRect(color, x, y, xSize, ySize, zLevel);
			drawCursor(x + (xSize * value), y, 1, 10, zLevel + 1);
		}

		public void drawCursor(double x, double y, double width, double height, double zLevel) {
			GL11.glPushMatrix();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldrenderer = tessellator.getBuffer();
			GlStateManager.color(0.0F, 0.0F, 0.0F, 255.0F);
			GlStateManager.disableTexture2D();

			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos((double) x, (double) y + height, zLevel).endVertex();
			worldrenderer.pos((double) x + width, (double) y + height, zLevel).endVertex();
			worldrenderer.pos((double) x + width, (double) y, zLevel).endVertex();
			worldrenderer.pos((double) x, (double) y, zLevel).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GL11.glPopMatrix();
		}

		public void drawColoredRect(Vec3d color, double x, double y, double width, double height, double zLevel) {

			GL11.glPushMatrix();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldrenderer = tessellator.getBuffer();
			GlStateManager.color((float) color.x, (float) color.y, (float) color.z, 255.0F);
			GlStateManager.disableTexture2D();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos((double) x, (double) y + height, zLevel).endVertex();
			worldrenderer.pos((double) x + width, (double) y + height, zLevel).endVertex();
			worldrenderer.pos((double) x + width, (double) y, zLevel).endVertex();
			worldrenderer.pos((double) x, (double) y, zLevel).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GL11.glPopMatrix();
		}

		public void setValue(double value) {
			this.value = value;
		}

		public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
			if (mouseX >= x && mouseX <= x + xSize && mouseY >= y && mouseY <= y + ySize) {
				double part = (double) (mouseX - x) / xSize;
				AudioPlayer player = MP3Helper.getAudioPlayer();
				if (player == null || player.getAudioClip() == null)
					return;
				player.getAudioClip().setMicrosecondPosition(
						(long) ((MP3Helper.getAudioPlayer().getDurationInSecs() * 1000000) * part));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static class VolumeSlider {
		double x;
		double y;
		double xSize;
		double ySize;
		double zLevel;

		float value;

		Vec3d color = new Vec3d(0.65, 0.65, 0.65);

		public VolumeSlider() {
		}

		public VolumeSlider(double x, double y, double xSize, double ySize, double zLevel) {
			this.x = x;
			this.y = y;
			this.xSize = xSize;
			this.ySize = ySize;
			this.zLevel = zLevel;
		}

		public void render() {
			drawColoredRect(color, x, y, xSize, ySize, zLevel);
			drawCursor(x + (xSize * value), y, 1, ySize, zLevel + 1);
		}

		public void drawCursor(double x, double y, double width, double height, double zLevel) {
			GL11.glPushMatrix();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldrenderer = tessellator.getBuffer();
			GlStateManager.color(0.0F, 0.0F, 0.0F, 255.0F);
			GlStateManager.disableTexture2D();

			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos((double) x, (double) y + height, zLevel).endVertex();
			worldrenderer.pos((double) x + width, (double) y + height, zLevel).endVertex();
			worldrenderer.pos((double) x + width, (double) y, zLevel).endVertex();
			worldrenderer.pos((double) x, (double) y, zLevel).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GL11.glPopMatrix();
		}

		public void drawColoredRect(Vec3d color, double x, double y, double width, double height, double zLevel) {

			GL11.glPushMatrix();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldrenderer = tessellator.getBuffer();
			GlStateManager.color((float) color.x, (float) color.y, (float) color.z, 255.0F);
			GlStateManager.disableTexture2D();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos((double) x, (double) y + height, zLevel).endVertex();
			worldrenderer.pos((double) x + width, (double) y + height, zLevel).endVertex();
			worldrenderer.pos((double) x + width, (double) y, zLevel).endVertex();
			worldrenderer.pos((double) x, (double) y, zLevel).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GL11.glPopMatrix();
		}

		public void setValue(float value) {
			this.value = value;
		}

		public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
			if (mouseX >= x && mouseX <= x + xSize && mouseY >= y && mouseY <= y + ySize) {
				float part = (float) ((mouseX - x) / xSize);
				MP3Helper.audioVolume = part;
				MP3Helper.getAudioPlayer().setVolume(Math.abs(MP3Helper.audioVolume - 1));
			}
		}
	}

}