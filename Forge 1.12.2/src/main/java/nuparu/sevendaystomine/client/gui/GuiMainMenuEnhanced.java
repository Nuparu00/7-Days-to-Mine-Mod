package nuparu.sevendaystomine.client.gui;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.gui.NotificationModUpdateScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.MathUtils;

@SideOnly(Side.CLIENT)
public class GuiMainMenuEnhanced extends GuiScreen implements GuiYesNoCallback {
	private static final Logger logger = LogManager.getLogger();
	private static final Random RANDOM = new Random();

	/** The splash message. */
	private String splashText;
	private GuiButton buttonResetDemo;

	/**
	 * The Object object utilized as a thread lock when performing non thread-safe
	 * operations
	 */
	private final Object threadLock = new Object();
	/** OpenGL graphics card warning. */
	private String openGLWarning1;
	/** OpenGL graphics card warning. */
	private String openGLWarning2;
	/** Link to the Mojang Support about minimum requirements */
	private String openGLWarningLink;
	public static final String MORE_INFO_TEXT = "Please click " + TextFormatting.UNDERLINE + "here"
			+ TextFormatting.RESET + " for more information.";
	private static final ResourceLocation SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/minecraft_7d.png");
	/** An array of all the paths to the panorama pictures. */
	public static final ResourceLocation BGR_DAY = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/background/background_day.png");
	public static final ResourceLocation BGR_SNOW = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/background/background_snow.png");
	public static final ResourceLocation BGR_BLOODMOON = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/background/background_bloodmoon.png");
	public static final ResourceLocation BGR_NIGHT = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/background/background_night.png");

	public static ResourceLocation background = BGR_DAY;
	public int bgr = 0;
	public static final String field_96138_a = "Please click " + TextFormatting.UNDERLINE + "here"
			+ TextFormatting.RESET + " for more information.";
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private GuiButton realmsButton;

	public String official_server_ip = "";
	public static final int MINIMAL_DUST_PARTICLES = 300;
	public static final int NATURAL_MAXIMUM_DUST_PARTICLES = 256;
	public List<Dust> dusts = new ArrayList<Dust>();
	public List<Dust> dustsToAdd = new ArrayList<Dust>();
	public List<Dust> dustsToRemove = new ArrayList<Dust>();
	public boolean drawDustMode = true;
	public boolean drawing = false;

	private int mX = 0;
	private int mY = 0;

	private boolean bday = false;
    private GuiButton modButton;
    private net.minecraftforge.client.gui.NotificationModUpdateScreen modUpdateNotification;

	public GuiMainMenuEnhanced() {
		this.openGLWarning2 = MORE_INFO_TEXT;
		this.splashText = "missingno";
		IResource iresource = null;

		try {
			List<String> list = Lists.<String>newArrayList();
			iresource = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
			BufferedReader bufferedreader = new BufferedReader(
					new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					list.add(s);
				}
			}

			if (!list.isEmpty()) {
				while (true) {
					this.splashText = list.get(RANDOM.nextInt(list.size()));

					if (this.splashText.hashCode() != 125780783) {
						break;
					}
				}
			}
		} catch (IOException var8) {
			;
		} finally {
			IOUtils.closeQuietly((Closeable) iresource);
		}

		this.openGLWarning1 = "";

		if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
			this.openGLWarning1 = I18n.format("title.oldgl1");
			this.openGLWarning2 = I18n.format("title.oldgl2");
			this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
		}
		
		background = BGR_DAY;
		bgr = 0;
		if (MathUtils.getIntInRange(0, 50) == 0) {
			background = BGR_NIGHT;
			bgr = 3;
		}

	}

	/** * Called from the main game loop to update the screen. */
	public void updateScreen() {
		ScaledResolution sr = new ScaledResolution(mc);

		// dusts.addAll(dustsToAdd);

		if (drawing && drawDustMode) {
			for(int i = 0; i < (GuiScreen.isShiftKeyDown() ? 5 : 1); i++) {
			Dust dust = summonDust(RANDOM, sr);
			dust.x = mX;
			dust.y = mY;
			dusts.add(dust);
			}
		}

	}

	/**
	 * * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * * Fired when a key is typed (except F11 which toggles full screen). This is
	 * the equivalent of * KeyListener.keyTyped(KeyEvent e). Args : character
	 * (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	/**
	 * * Adds the buttons (and other controls) to the screen in question. Called
	 * when the GUI is displayed and when the * window resizes, the buttonList is
	 * cleared beforehand.
	 */
	@SuppressWarnings("deprecation")
	public void initGui() {
		try {
			InputStream in = null;
			try {
				in = new URL("https://raw.githubusercontent.com/Nuparu00/7-Days-to-Mine/master/servers.txt")
						.openStream();

			}

			catch (MalformedURLException e) {
			}

			catch (IOException e) {
			} finally {
				in.close();
				try {
					List<String> content = IOUtils.readLines(in);
					official_server_ip = content.get(0);
				}

				catch (IOException e) {
					e.printStackTrace();
				}

				finally {
					IOUtils.closeQuietly(in);
				}
			}
		} catch (Exception e) {

		}

		dusts.clear();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int month = calendar.get(2) + 1;
		int day = calendar.get(5);
		if (month == 12 && day >= 24 && day <= 26) {
			if (day == 24) {
				this.splashText = "Merry X-mas!";
			}
			background = BGR_SNOW;
			bgr = 1;
		}

		else if (month == 1 && day == 1) {
			this.splashText = "Happy new year!";
		}

		else if (month == 10 && day == 31) {
			this.splashText = "OOoooOOOoooo! Spooky!";
			background = BGR_BLOODMOON;
			bgr = 2;
		}

		else if (month == 7 && day == 3) {
			this.splashText = "Happy birthday, 7 Days to Mine!";
			bday = true;
		}

		int j = this.height / 4;
		if (this.mc.isDemo()) {
			this.addDemoButtons(j, 18);
		}

		else {
			this.addSingleplayerMultiplayerButtons(j, 18);
		}

		if (official_server_ip == null || official_server_ip == "") {
			this.buttonList.add(new GuiButtonTransparent(0, 0, j + 54 + 18,
					this.fontRenderer.getStringWidth(I18n.format("menu.options", new Object[0])), 9,
					I18n.format("menu.options", new Object[0])));
			this.buttonList.add(new GuiButtonTransparent(4, 0, j + 54 + 36,
					this.fontRenderer.getStringWidth(I18n.format("menu.quit", new Object[0])), 9,
					I18n.format("menu.quit", new Object[0])));
		}

		else {
			this.buttonList.add(new GuiButtonTransparent(16, 0, j + 54 + 18,
					this.fontRenderer.getStringWidth(I18n.format("menu.fast_connect", new Object[0])), 9,
					I18n.format("menu.fast_connect", new Object[0])));
			this.buttonList.add(new GuiButtonTransparent(0, 0, j + 54 + 36,
					this.fontRenderer.getStringWidth(I18n.format("menu.options", new Object[0])), 9,
					I18n.format("menu.options", new Object[0])));
			this.buttonList.add(new GuiButtonTransparent(4, 0, j + 54 + 54,
					this.fontRenderer.getStringWidth(I18n.format("menu.quit", new Object[0])), 9,
					I18n.format("menu.quit", new Object[0])));
		}

		synchronized (this.threadLock) {
			this.field_92023_s = this.fontRenderer.getStringWidth(this.openGLWarning1);
			this.field_92024_r = this.fontRenderer.getStringWidth(this.openGLWarning2);
			int k = Math.max(this.field_92023_s, this.field_92024_r);
			this.field_92022_t = (this.width - k) / 2;
			this.field_92021_u = ((GuiButton) this.buttonList.get(0)).y - 24;
			this.field_92020_v = this.field_92022_t + k;
			this.field_92019_w = this.field_92021_u + 24;
		}

		this.mc.setConnectedToRealms(false);
        modUpdateNotification = getNotificationModUpdateScreen(this, modButton);
	}
	
    public static NotificationModUpdateScreen getNotificationModUpdateScreen(GuiScreen guiMainMenu, GuiButton modButton)
    {
        NotificationModUpdateScreen notificationModUpdateScreen = new NotificationModUpdateScreen(modButton);
        notificationModUpdateScreen.setGuiSize(guiMainMenu.width, guiMainMenu.height);
        notificationModUpdateScreen.initGui();
        return notificationModUpdateScreen;
    }

	/**
	 * * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have
	 * bought the game.
	 */
	private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
		this.buttonList.add(new GuiButtonTransparent(1, 0, p_73969_1_,
				this.fontRenderer.getStringWidth(I18n.format("menu.singleplayer", new Object[0])), 9,
				I18n.format("menu.singleplayer", new Object[0])));
		this.buttonList.add(new GuiButtonTransparent(2, 0, p_73969_1_ + p_73969_2_ * 1,
				this.fontRenderer.getStringWidth(I18n.format("menu.multiplayer", new Object[0])), 9,
				I18n.format("menu.multiplayer", new Object[0])));
		this.buttonList
				.add(this.realmsButton = new GuiButtonTransparent(14, 0, p_73969_1_ + p_73969_2_ * 2 + p_73969_2_,
						this.fontRenderer.getStringWidth(I18n.format("menu.online", new Object[0])), 9,
						I18n.format("menu.online", new Object[0])));
		modButton = new GuiButtonMods(6, 0, p_73969_1_ + p_73969_2_ * 2,
				this.fontRenderer.getStringWidth(I18n.format("fml.menu.mods", new Object[0])), 9,
				I18n.format("fml.menu.mods"),this);
		this.buttonList.add(modButton);
	}

	/** * Adds Demo buttons on Main Menu for players who are playing Demo. */
	private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
		this.buttonList.add(new GuiButtonTransparent(11, 0, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
		this.buttonList.add(this.buttonResetDemo = new GuiButtonTransparent(12, 0, p_73972_1_ + p_73972_2_ * 1,
				I18n.format("menu.resetdemo", new Object[0])));
		ISaveFormat isaveformat = this.mc.getSaveLoader();
		WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
		if (worldinfo == null) {
			this.buttonResetDemo.enabled = false;
		}

	}

	/**
	 * * Called by the controls from the buttonList when activated. (Mouse pressed
	 * for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if (button.id == 5) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
		}

		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiWorldSelection(this));
		}

		if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (button.id == 14 && this.realmsButton.visible) {
			this.switchToRealms();
		}

		if (button.id == 4) {
			this.mc.shutdown();
		}

		if (button.id == 6) {
			this.mc.displayGuiScreen(new net.minecraftforge.fml.client.GuiModList(this));
		}

		if (button.id == 15) {
			// this.mc.displayGuiScreen(new GuiModInfo(this));
		}

		if (button.id == 16) {
			String segments[] = official_server_ip.split(":");
			this.mc.displayGuiScreen(new GuiConnecting(this, mc, segments[0], Integer.parseInt(segments[1])));
		}

	}

	private void switchToRealms() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}

	public void confirmClicked(boolean result, int id) {
		if (result && id == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		}

		else if (id == 13) {
			if (result) {
				try {
					Class<?> oclass = Class.forName("java.awt.Desktop");
					Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class }

					).invoke(object, new Object[] { new URI(this.openGLWarningLink) }

					);
				}

				catch (Throwable throwable) {
					logger.error("Couldn\'t open link", throwable);
				}

			}

			this.mc.displayGuiScreen(this);
		}

	}

	public void renderStaticBgr() {
		ScaledResolution p_180476_1_ = new ScaledResolution(mc);
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableAlpha();
		mc.getTextureManager().bindTexture(background);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(0.0D, (double) p_180476_1_.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
		worldrenderer.pos((double) p_180476_1_.getScaledWidth(), (double) p_180476_1_.getScaledHeight(), -90.0D)
				.tex(1.0D, 1.0D).endVertex();
		worldrenderer.pos((double) p_180476_1_.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
		worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public Dust summonDust(Random rand, ScaledResolution sr) {
		float x = rand.nextFloat() * sr.getScaledWidth();
		float y = MathUtils.getFloatInRange(0f, 0.75f) * sr.getScaledHeight();
		float motionX = rand.nextFloat() * 0.6f - 0.15f;
		float motionY = rand.nextFloat();
		float[] rgb = new float[3];
		float opacity = MathUtils.getFloatInRange(0.1f, 0.5f);
		
		if (bday) {
			rgb[0] = RANDOM.nextFloat();
			rgb[1] = RANDOM.nextFloat();
			rgb[2] = RANDOM.nextFloat();
		} else {
			rgb[0] = 0.941F;
			rgb[1] = 0.902F;
			rgb[2] = 0.549F;

			if (bgr == 1 || bgr == 3) {
				float c = (rgb[0] + rgb[1] + rgb[2]) / 3;
				if(bgr == 3) {
					opacity/=2;
					c*=0.7f;
				}
				rgb[0] = c;
				rgb[1] = c;
				rgb[2] = c;
			}
			else if(bgr == 2) {
				rgb[0] = rgb[0]/1.2f;
				rgb[1] = rgb[1]/3;
				rgb[2] = rgb[2]/5;
				opacity/=1.5;
			}

		}

		return new Dust(x, y, motionX, motionY, MathUtils.getFloatInRange(0.017528f, 0.80f),
				opacity, rgb, this);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);

		this.mX = mouseX;
		this.mY = mouseY;

		dusts.addAll(dustsToAdd);
		dusts.removeAll(dustsToRemove);
		dustsToRemove.clear();
		dustsToAdd.clear();

		List<Dust> dustsClone = new ArrayList<Dust>();
		dustsClone.addAll(dusts);
		ListIterator<Dust> it = dustsClone.listIterator();
		while (it.hasNext()) {
			Dust dust = it.next();
			dust.update(mouseX, mouseY);
			if (dust.x - dust.scale > ((sr.getScaledWidth())) || dust.y - dust.scale > ((sr.getScaledHeight()))
					|| dust.x + dust.scale < 0 || dust.y + dust.scale < 0) {
				dust.opacity -= 0.05f;
			}
		}

		if ((dusts.size() + dustsToAdd.size()) < NATURAL_MAXIMUM_DUST_PARTICLES) {
			while ((dusts.size() + dustsToAdd.size()) < Math.min(
					ThreadLocalRandom.current().nextInt(MINIMAL_DUST_PARTICLES, MINIMAL_DUST_PARTICLES + 20),
					NATURAL_MAXIMUM_DUST_PARTICLES)) {
				dustsToAdd.add(summonDust(RANDOM, sr));
			}
		}

		GlStateManager.disableAlpha();

		this.renderStaticBgr();
		GlStateManager.enableAlpha();
		Iterator<Dust> it2 = dusts.iterator();
		while (it2.hasNext()) {
			Dust dust = it2.next();
			dust.draw();
		}

		int i = 274;
		int j = this.width / 2 - i / 2;
		int k = 15;

		// this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(j, k + 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(j + 155, k + 0, 0, 45, 155, 44);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
		GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
		float f = 1.8F - MathHelper.abs(
				MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
		f = f * 100.0F / (float) (this.fontRenderer.getStringWidth(this.splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, -256);
		GlStateManager.popMatrix();
		String s = "7 Days to Mine for Minecraft 1.12.2";
		if (this.mc.isDemo()) {
			s = s + " Demo";
		}

		List<String> brandings = new ArrayList<String>(
				net.minecraftforge.fml.common.FMLCommonHandler.instance().getBrandings(true));
		brandings.add(1, "7 Days to Mine " + SevenDaysToMine.VERSION);
		brandings = Lists.reverse(brandings);
		for (int brdline = 0; brdline < brandings.size(); brdline++) {
			String brd = brandings.get(brdline);
			if (!com.google.common.base.Strings.isNullOrEmpty(brd)) {
				this.drawString(this.fontRenderer, brd, 2,
						this.height - (10 + brdline * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
			}

		}

		String s1 = "Copyright Mojang AB. Do not distribute!";
		this.drawString(this.fontRenderer, s1, this.width - this.fontRenderer.getStringWidth(s1) - 2, this.height - 10,
				-1);
		if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1,
					1428160512);
			this.drawString(this.fontRenderer, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
			this.drawString(this.fontRenderer, this.openGLWarning2, (this.width - this.field_92024_r) / 2,
					((GuiButton) this.buttonList.get(0)).y - 12, -1);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/** * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		synchronized (this.threadLock) {
			if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v
					&& mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
				GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
				guiconfirmopenlink.disableSecurityWarning();
				this.mc.displayGuiScreen(guiconfirmopenlink);
			}

		}

		if (drawDustMode && mouseButton == 0) {
			drawing = true;
		}

	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		drawing = false;
	}

	@SideOnly(Side.CLIENT)
	public static class Dust {
		float x;
		float y;
		float motionX;
		float motionY;
		float scale;
		float opacity;
		int lifeSpan;
		float[] RGB;
		final Minecraft mc = Minecraft.getMinecraft();
		final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
				"textures/gui/title/background/dust.png");
		GuiMainMenuEnhanced gui;

		public Dust(float x, float y, float motionX, float motionY, float size, float opacity, float[] RGB,
				GuiMainMenuEnhanced gui) {
			this.x = x;
			this.y = y;
			this.motionX = motionX;
			this.motionY = motionY;
			this.scale = size;
			this.opacity = opacity;
			this.lifeSpan = 1200;
			this.RGB = RGB;
			this.gui = gui;
		}

		public void update(int mouseX, int mouseY) {
			this.x += +this.motionX;
			this.y += this.motionY;
			this.lifeSpan--;
			if (this.lifeSpan <= 0) {
				this.opacity -= 0.05f;
				this.lifeSpan = 0;
			} else if (opacity < 1 && RANDOM.nextInt(20) == 0) {
				// opacity = (float) MathHelper.clamp(opacity+RANDOM.nextFloat()*0.1, 0, 1);
			}
			if (mouseX - x < -500) {
				motionX += MathUtils.getFloatInRange(-0.0071258f, 0.0005f);
			} else if (mouseX - x > 500) {
				motionX += MathUtils.getFloatInRange(-0.0005f, 0.0071258f);
			}

			if (this.opacity <= 0) {
				this.gui.dustsToRemove.add(this);
			}

		}

		public void draw() {
			GlStateManager.pushMatrix();
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			GlStateManager.enableBlend();
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableAlpha();
			GlStateManager.color(RGB[0], RGB[1], RGB[2], this.opacity);
			GlStateManager.translate(x, y, 0.0F);
			GlStateManager.scale(scale, scale, scale);
			mc.getTextureManager().bindTexture(TEXTURE);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldrenderer = tessellator.getBuffer();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
			worldrenderer.pos(0d, 0d + 16D, -90.0D).tex(0.0D, 1.0D).endVertex();
			worldrenderer.pos(0d + 16D, 0d + 16D, -90.0D).tex(1.0D, 1.0D).endVertex();
			worldrenderer.pos(0d + 16D, 0d, -90.0D).tex(1.0D, 0.0D).endVertex();
			worldrenderer.pos(0.0D, 0d, -90.0D).tex(0.0D, 0.0D).endVertex();
			tessellator.draw();
			GlStateManager.depthMask(true);
			GlStateManager.enableDepth();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}

	}

	@SideOnly(Side.CLIENT)
	public static class GuiButtonTransparent extends GuiButton {
		String defText;

		public GuiButtonTransparent(int buttonId, int x, int y, String buttonText) {
			super(buttonId, x, y, 200, 20, buttonText);
			defText = buttonText;
		}

		public GuiButtonTransparent(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
			defText = buttonText;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				FontRenderer fontrenderer = mc.fontRenderer;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				this.mouseDragged(mc, mouseX, mouseY);
				int j = 14737632;
				if (packedFGColour != 0) {
					j = packedFGColour;
				}

				else if (!this.enabled) {
					j = 10526880;
				}

				else if (this.hovered) {
					j = 16777120;
					this.displayString = "    >>" + defText;
				}

				else {
					this.displayString = defText;
				}

				this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2,
						this.y + (this.height - 8) / 2, j);
			}

		}

	}
	
	@SideOnly(Side.CLIENT)
	public static class GuiButtonMods extends GuiButtonTransparent {

		GuiMainMenuEnhanced menu; 
		public GuiButtonMods(int buttonId, int x, int y, String buttonText, GuiMainMenuEnhanced menu) {
			super(buttonId, x, y, 200, 20, buttonText);
			this.menu =menu;
		}

		public GuiButtonMods(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, GuiMainMenuEnhanced menu) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
			this.menu =menu;
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			super.drawButton(mc, mouseX, mouseY, partialTicks);
			if (this.visible) {
				int offset = 10 + (this.hovered ? menu.fontRenderer.getStringWidth(" >>") : 0);
				GlStateManager.pushMatrix();
				GlStateManager.translate(offset, 0, 0);
		        menu.modUpdateNotification.drawScreen(mouseX, mouseY, partialTicks);
				GlStateManager.popMatrix();
			}

		}

	}

}
