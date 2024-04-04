package nuparu.sevendaystomine.computer.process.epidemic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.Screen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.computer.process.WindowedProcess;
import nuparu.sevendaystomine.util.ColorRGBA;

public class EpidemicProcess extends WindowedProcess {

	public final ResourceLocation TITLE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/epidemic/title.png");

	/*
	 * Elements
	 */

	public Button buttonNewGame;
	public TextField fieldCreateName;
	public Button buttonStart;
	public Button buttonDisease;
	public Button buttonBack;
	public Button buttonCoughing;
	public Button buttonSneezing;
	public Button buttonVomiting;
	public Button buttonSeizures;
	public Button buttonNecrosis;
	public Button buttonOrganFailure;
	public Button buttonFaster;
	public Button buttonSlower;
	public Button buttonStop;
	public Button buttonWorld;
	public Button buttonShowInfected;
	public Button buttonMainMenu;

	/*
	 * Game Data
	 */

	public String diseaseName = "";
	public List<EpidemicCountry> countries = new ArrayList<EpidemicCountry>();
	public List<EnumUpgrade> upgrades = new ArrayList<EnumUpgrade>();

	public double day;
	public double speed;
	public int points;
	public int vaccineProgress;
	public boolean discovered;

	public static HashMap<String, BufferedImage> mapCache = new HashMap<String, BufferedImage>();

	public EnumGameState gameState = EnumGameState.MENU;

	public EpidemicProcess() {
		this(0, 0, 0, 0);
	}

	public EpidemicProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("epidemic");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks) {
		drawWindow(getTitle(), new ColorRGBA(0d, 0d, 0d), new ColorRGBA(0.8, 0.8, 0.8));

		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, zLevel);

		ResourceLocation bgr = gameState.background;

		RenderUtils.drawTexturedRect(bgr, x, y + (Screen.screen.ySize * title_bar_height), 0, 0, width,
				height - (Screen.screen.ySize * title_bar_height), width,
				height - (Screen.screen.ySize * title_bar_height), 1, 1);
		if (gameState == EnumGameState.MENU || gameState == EnumGameState.CREATE) {
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			RenderUtils.drawTexturedRect(TITLE, x + width / 2 - width / 8,
					y + (Screen.screen.ySize * title_bar_height) + 1, 0, 0, width / 4, width / 16, width / 4,
					width / 16, 1, 2);
			GlStateManager.disableBlend();
			GlStateManager.disableAlpha();
		} else if (gameState == EnumGameState.GAME || gameState == EnumGameState.SELECT_START) {
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			for (EpidemicCountry country : countries) {
				float g = ((float) country.getHealthy() / (float) country.population);
				float r = 1f - g;
				g *= ((float) country.getAlive() / country.population);
				r *= ((float) country.getAlive() / country.population);
				GL11.glColor3f(r, g, 0);
				RenderUtils.drawTexturedRect(country.texture, x, y + (Screen.screen.ySize * title_bar_height), 0, 0,
						width, height - (Screen.screen.ySize * title_bar_height), width,
						height - (Screen.screen.ySize * title_bar_height), 1, 2);
				GL11.glColor3f(1, 1, 1);
			}
			GlStateManager.disableBlend();
			GlStateManager.disableAlpha();

			GlStateManager.translate(0, 0, 2);
			RenderUtils.drawString("Day " + (int) Math.floor(this.day) + " (" + speed + "x)", x + 2,
					y + (Screen.screen.ySize * title_bar_height) + 2, 0xffffff);

			String inf = formatNumber(this.getGlobalInfected()) + " Infected";
			String dead = formatNumber(this.getGlobalDead()) + " Dead";

			RenderUtils.drawString(inf, x + 2, y + height - fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawString(dead, x + width - fontRenderer.getStringWidth(dead),
					y + height - fontRenderer.FONT_HEIGHT, 0xffffff);
			GlStateManager.translate(0, 0, -2);

			EpidemicCountry country = getHoveredEpidemicCountry();
			if (country != null) {
				GlStateManager.translate(0, 0, 2);
				if(GuiScreen.isShiftKeyDown()){
					this.renderTooltip(screen.mouseX, screen.mouseY, fontRenderer, country.name,
							"Pop " + formatNumber(country.getAlive()), "Inf " + formatNumber(country.infected),
							"Density " + country.getDensity(), "Area " + country.area);
					}
					else {
						this.renderTooltip(screen.mouseX, screen.mouseY, fontRenderer, country.name,
								"Pop " + formatNumber(country.getAlive()), "Inf " + formatNumber(country.infected));
					}
				GlStateManager.translate(0, 0, -2);
			}
		} else if (gameState == EnumGameState.DISEASE) {
			GlStateManager.translate(0, 0, 1);
			RenderUtils.drawCenteredString(diseaseName, x + width / 2, y + fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawString("Infectivity", x + width / 2 - 1.5 * fontRenderer.getStringWidth("Infectivity"),
					y + 3 * fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawString("Lethality", x + width / 2 + 0.5 * fontRenderer.getStringWidth("Lethality"),
					y + 3 * fontRenderer.FONT_HEIGHT, 0xffffff);
			GlStateManager.translate(0, 0, -1);
		} else if (gameState == EnumGameState.WORLD) {
			GlStateManager.translate(0, 0, 1);
			RenderUtils.drawCenteredString("World", x + width / 2, y + fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawString("Alive: " + formatNumber(this.getGlobalAlive()), x + 5,
					y + 3 * fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawString("Healthy: " + formatNumber(this.getGlobalHealthy()), x + width / 2,
					y + 3 * fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawString("Infected: " + formatNumber(this.getGlobalInfected()), x + 5,
					y + 4 * fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawString("Dead: " + formatNumber(this.getGlobalDead()), x + width / 2,
					y + 4 * fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawCenteredString("Vaccine Progress: " + vaccineProgress + "%", x + width / 2,
					y + 6 * fontRenderer.FONT_HEIGHT, 0xffffff);
			GlStateManager.translate(0, 0, -1);
		} else if (gameState == EnumGameState.INFECTED_MAP) {
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			for (EpidemicCountry country : countries) {

				if (country.getAlive() == 0) {
					GL11.glColor3f(0.1f, 0, 0);
				} else if (country.isInfected()) {
					GL11.glColor3f(0.75f, 0, 0);
				} else {
					GL11.glColor3f(0, 0.66f, 0.92f);
				}

				RenderUtils.drawTexturedRect(country.texture, x, y + (Screen.screen.ySize * title_bar_height), 0, 0,
						width, height - (Screen.screen.ySize * title_bar_height), width,
						height - (Screen.screen.ySize * title_bar_height), 1, 2);
				GL11.glColor3f(1, 1, 1);
			}
			GlStateManager.disableBlend();
			GlStateManager.disableAlpha();

			GlStateManager.translate(0, 0, 2);
			GlStateManager.translate(0, 0, -2);

			EpidemicCountry country = getHoveredEpidemicCountry();
			if (country != null) {
				GlStateManager.translate(0, 0, 2);
				if(GuiScreen.isShiftKeyDown()){
				this.renderTooltip(screen.mouseX, screen.mouseY, fontRenderer, country.name,
						"Pop " + formatNumber(country.getAlive()), "Inf " + formatNumber(country.infected),
						"Density " + country.getDensity(), "Area " + country.area);
				}
				else {
					this.renderTooltip(screen.mouseX, screen.mouseY, fontRenderer, country.name,
							"Pop " + formatNumber(country.getAlive()), "Inf " + formatNumber(country.infected));
				}
				GlStateManager.translate(0, 0, -2);
			}
		} else if (gameState == EnumGameState.END) {
			boolean win = this.getGlobalAlive() == 0;
			GlStateManager.translate(0, 0, 1);
			RenderUtils.drawCenteredString(win ? SevenDaysToMine.proxy.localize("computer.app.epidemic.win") : SevenDaysToMine.proxy.localize("computer.app.epidemic.lost") , x + width / 2, y + 2*fontRenderer.FONT_HEIGHT, 0xffffff);
			String epilog = "";
			String vaccine = SevenDaysToMine.proxy.localize("computer.app.epidemic.lost.info.vaccine", this.vaccineProgress);
			
			if(win) {
				epilog = SevenDaysToMine.proxy.localize("computer.app.epidemic.win.epilog", this.diseaseName, Math.round(this.day));
				RenderUtils.drawCenteredString(vaccine, x + width/2,y + 6 * fontRenderer.FONT_HEIGHT, 0xffffff);
			}
			else {
				epilog = this.vaccineProgress == 100 ? SevenDaysToMine.proxy.localize("computer.app.epidemic.lost.vaccine", this.diseaseName, Math.round(this.day)) : SevenDaysToMine.proxy.localize("computer.app.epidemic.lost.died", this.diseaseName, Math.round(this.day));
				String killed = SevenDaysToMine.proxy.localize("computer.app.epidemic.lost.info.killed", this.getGlobalDead(), Math.round(((double)this.getGlobalDead()/this.getGlobalPopulation())*100));
				RenderUtils.drawCenteredString(killed, x + width/2,y + 6 * fontRenderer.FONT_HEIGHT, 0xffffff);
				RenderUtils.drawCenteredString(vaccine, x + width/2,y + 7 * fontRenderer.FONT_HEIGHT, 0xffffff);
			}
			RenderUtils.drawCenteredString(epilog, x + width/2,
					y + 4 * fontRenderer.FONT_HEIGHT, 0xffffff);
			GlStateManager.translate(0, 0, -1);

		}
		super.render(partialTicks);
		GlStateManager.translate(0, 0, -zLevel);
		GlStateManager.popMatrix();

	}

	public EpidemicCountry getHoveredEpidemicCountry() {
		if (isMouseInside()) {
			for (EpidemicCountry country : countries) {
				double rX = (screen.mouseX - x) / width;
				double rY = (screen.mouseY - (y + (Screen.screen.ySize * title_bar_height)))
						/ (height - (Screen.screen.ySize * title_bar_height));
				if (rX < 0 || rY < 0 || rX > 1 || rY > 1)
					continue;
				Color color = getColorAt(country.texture, rX, rY);

				if (color.getAlpha() != 0) {
					return country;
				}
			}
		}
		return null;
	}

	public String formatNumber(long l) {
		return NumberFormat.getNumberInstance(Locale.US).format(l);
	}

	@Override
	public void tick() {
		super.tick();

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientTick() {
		super.clientTick();
		if (gameState == EnumGameState.GAME) {
			double dayPrev = day;
			day += speed * 0.01;
			double delta = Math.floor(day) - Math.floor(dayPrev);
			if (delta > 0) {
				for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
					country.update(delta);
				}

				boolean dirty = false;

				if (discovered && getRand().nextInt(14) == 0 && ThreadLocalRandom.current().nextLong(Math.max(1,
						(long) ((getGlobalAlive() - 5 * getGlobalDead()) / (getLethality() * delta)))) == 0) {
					vaccineProgress++;
					dirty = true;
				}
				if (this.getGlobalAlive() == 0 || this.getGlobalInfected() == 0 || vaccineProgress == 100) {
					this.gameState = EnumGameState.END;
					dirty = true;
				}
				if (dirty) {
					sync();
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		buttonNewGame = new Button(x + width / 2 - 25, y + 30, 50, 10, screen, "computer.epidemic.new_game", 1) {

			@Override
			public boolean isDisabled() {
				return ((EpidemicProcess) tickingProcess).gameState != EnumGameState.MENU;
			}

		};
		buttonNewGame.background = false;
		buttonNewGame.border = false;
		buttonNewGame.textNormal = 0xffffff;
		buttonNewGame.setFontSize(0.7);
		buttonNewGame.setProcess(this);
		elements.add(buttonNewGame);

		fieldCreateName = new TextField(x + width / 2 - 25, y + 30, 50, 9, screen) {

			@Override
			public boolean isDisabled() {
				return ((EpidemicProcess) process).gameState != EnumGameState.CREATE;
			}

		};
		fieldCreateName.setDefaultText("Smallpox");
		fieldCreateName.setProcess(this);
		elements.add(fieldCreateName);

		buttonStart = new Button(x + width / 2 - 25, y + 40, 50, 10, screen, "computer.epidemic.start", 2) {

			@Override
			public boolean isDisabled() {
				return ((EpidemicProcess) tickingProcess).gameState != EnumGameState.CREATE;
			}

		};
		buttonStart.background = false;
		buttonStart.border = false;
		buttonNewGame.textNormal = 0xffffff;
		buttonStart.setFontSize(0.7);
		buttonStart.setProcess(this);
		elements.add(buttonStart);

		buttonDisease = new Button(x + width / 2 - fontRenderer.getStringWidth(diseaseName + " (" + points + ")") / 2,
				y + height - (Screen.screen.ySize * title_bar_height) * 5 + fontRenderer.FONT_HEIGHT,
				fontRenderer.getStringWidth(diseaseName + " (" + points + ")"),
				Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, screen, diseaseName + " (" + points + ")", 3) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.GAME && state != EnumGameState.SELECT_START;
			}

		};
		buttonDisease.background = false;
		buttonDisease.border = false;
		buttonDisease.textNormal = 0xffffff;
		buttonDisease.setFontSize(0.7);
		buttonDisease.setProcess(this);
		elements.add(buttonDisease);

		buttonBack = new Button(x + 2, y + height - fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth("Back"),
				fontRenderer.FONT_HEIGHT, screen, "Back", 4) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE && state != EnumGameState.WORLD
						&& state != EnumGameState.INFECTED_MAP;
			}

		};
		buttonBack.background = false;
		buttonBack.border = false;
		buttonBack.textNormal = 0xffffff;
		buttonBack.setFontSize(0.7);
		buttonBack.setProcess(this);
		elements.add(buttonBack);

		buttonCoughing = new Button(x + width / 2 - 1.5 * fontRenderer.getStringWidth("Infectivity"),
				y + 4.2 * fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth("Coughing"), fontRenderer.FONT_HEIGHT,
				screen, "Coughing", 5) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor() {
				return upgrades.contains(EnumUpgrade.COUGHING) ? 0x05ff00
						: (points < 10 ? 0xff0500 : super.getTextColor());
			}

		};
		buttonCoughing.background = false;
		buttonCoughing.border = false;
		buttonCoughing.textNormal = 0xffffff;
		buttonCoughing.setFontSize(0.7);
		buttonCoughing.setProcess(this);
		elements.add(buttonCoughing);

		buttonSneezing = new Button(x + width / 2 - 1.5 * fontRenderer.getStringWidth("Infectivity"),
				y + 5.2 * fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth("Sneezing"), fontRenderer.FONT_HEIGHT,
				screen, "Sneezing", 6) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor() {
				return upgrades.contains(EnumUpgrade.SNEEZING) ? 0x05ff00
						: (points < 20 ? 0xff0500 : super.getTextColor());
			}

		};
		buttonSneezing.background = false;
		buttonSneezing.border = false;
		buttonSneezing.textNormal = 0xffffff;
		buttonSneezing.setFontSize(0.7);
		buttonSneezing.setProcess(this);
		elements.add(buttonSneezing);

		buttonVomiting = new Button(x + width / 2 - 1.5 * fontRenderer.getStringWidth("Infectivity"),
				y + 6.2 * fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth("Vomiting"), fontRenderer.FONT_HEIGHT,
				screen, "Vomiting", 7) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor() {
				return upgrades.contains(EnumUpgrade.VOMITING) ? 0x05ff00
						: (points < 30 ? 0xff0500 : super.getTextColor());
			}

		};
		buttonVomiting.background = false;
		buttonVomiting.border = false;
		buttonVomiting.textNormal = 0xffffff;
		buttonVomiting.setFontSize(0.7);
		buttonVomiting.setProcess(this);
		elements.add(buttonVomiting);

		buttonSeizures = new Button(x + width / 2 + 0.5 * fontRenderer.getStringWidth("Lethality"),
				y + 4.2 * fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth("Seizures"), fontRenderer.FONT_HEIGHT,
				screen, "Seizures", 8) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor() {
				return upgrades.contains(EnumUpgrade.SEIZURES) ? 0x05ff00
						: (points < 10 ? 0xff0500 : super.getTextColor());
			}

		};
		buttonSeizures.background = false;
		buttonSeizures.border = false;
		buttonSeizures.textNormal = 0xffffff;
		buttonSeizures.setFontSize(0.7);
		buttonSeizures.setProcess(this);
		elements.add(buttonSeizures);

		buttonNecrosis = new Button(x + width / 2 + 0.5 * fontRenderer.getStringWidth("Lethality"),
				y + 5.2 * fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth("Necrosis"), fontRenderer.FONT_HEIGHT,
				screen, "Necrosis", 9) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor() {
				return upgrades.contains(EnumUpgrade.NECROSIS) ? 0x05ff00
						: (points < 20 ? 0xff0500 : super.getTextColor());
			}

		};
		buttonNecrosis.background = false;
		buttonNecrosis.border = false;
		buttonNecrosis.textNormal = 0xffffff;
		buttonNecrosis.setFontSize(0.7);
		buttonNecrosis.setProcess(this);
		elements.add(buttonNecrosis);

		buttonOrganFailure = new Button(x + width / 2 + 0.5 * fontRenderer.getStringWidth("Lethality"),
				y + 6.2 * fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth("Organ Failure"),
				fontRenderer.FONT_HEIGHT, screen, "Organ Failure", 10) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor() {
				return upgrades.contains(EnumUpgrade.ORGAN_FAILURE) ? 0x05ff00
						: (points < 30 ? 0xff0500 : super.getTextColor());
			}

		};
		buttonOrganFailure.background = false;
		buttonOrganFailure.border = false;
		buttonOrganFailure.textNormal = 0xffffff;
		buttonOrganFailure.setFontSize(0.7);
		buttonOrganFailure.setProcess(this);
		elements.add(buttonOrganFailure);

		buttonFaster = new Button(x + 4 + fontRenderer.getStringWidth("<<"),
				y + (Screen.screen.ySize * title_bar_height) + 2 + fontRenderer.FONT_HEIGHT,
				fontRenderer.getStringWidth(">>"), fontRenderer.FONT_HEIGHT, screen, ">>", 11) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.GAME && state != EnumGameState.SELECT_START;
			}

		};
		buttonFaster.background = false;
		buttonFaster.border = false;
		buttonFaster.textNormal = 0xffffff;
		buttonFaster.setFontSize(0.7);
		buttonFaster.setProcess(this);
		elements.add(buttonFaster);

		buttonSlower = new Button(x + 2, y + (Screen.screen.ySize * title_bar_height) + 2 + fontRenderer.FONT_HEIGHT,
				fontRenderer.getStringWidth("<"), fontRenderer.FONT_HEIGHT, screen, "<<", 12) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.GAME && state != EnumGameState.SELECT_START;
			}

		};
		buttonSlower.background = false;
		buttonSlower.border = false;
		buttonSlower.textNormal = 0xffffff;
		buttonSlower.setFontSize(0.7);
		buttonSlower.setProcess(this);
		elements.add(buttonSlower);

		buttonWorld = new Button(x + width - fontRenderer.getStringWidth("World"),
				y + (Screen.screen.ySize * title_bar_height) + 2, fontRenderer.getStringWidth("World"),
				fontRenderer.FONT_HEIGHT, screen, "World", 13) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.GAME && state != EnumGameState.SELECT_START;
			}

		};
		buttonWorld.background = false;
		buttonWorld.border = false;
		buttonWorld.textNormal = 0xffffff;
		buttonWorld.setFontSize(0.7);
		buttonWorld.setProcess(this);
		elements.add(buttonWorld);

		buttonShowInfected = new Button(x + width - fontRenderer.getStringWidth("Show Infected Countries"),
				y + height - fontRenderer.FONT_HEIGHT, fontRenderer.getStringWidth("Show Infected Countries"),
				fontRenderer.FONT_HEIGHT, screen, "Show Infected Countries", 14) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.WORLD;
			}

		};
		buttonShowInfected.background = false;
		buttonShowInfected.border = false;
		buttonShowInfected.textNormal = 0xffffff;
		buttonShowInfected.setFontSize(0.7);
		buttonShowInfected.setProcess(this);
		elements.add(buttonShowInfected);
		
		buttonMainMenu = new Button(x + width / 2 - 25, y + height - 3*fontRenderer.FONT_HEIGHT, 50, 10, screen, "computer.epidemic.main_menu", 1) {

			@Override
			public boolean isDisabled() {
				return ((EpidemicProcess) tickingProcess).gameState != EnumGameState.END;
			}

		};
		buttonMainMenu.background = false;
		buttonMainMenu.border = false;
		buttonMainMenu.textNormal = 0xffffff;
		buttonMainMenu.setFontSize(0.7);
		buttonMainMenu.setProcess(this);
		elements.add(buttonMainMenu);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		super.initWindow();
		if (elements.size() < 2) {
			return;
		}

		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		buttonNewGame.setX(x + width / 2 - 25);
		buttonNewGame.setY(y + 30);
		buttonNewGame.setWidth(50);
		buttonNewGame.setHeight(10);

		fieldCreateName.setX(x + width / 2 - 25);
		fieldCreateName.setY(y + 30);
		fieldCreateName.setWidth(50);
		fieldCreateName.setHeight(9);

		fieldCreateName.setX(x + width / 2 - 25);
		fieldCreateName.setY(y + 40);
		fieldCreateName.setWidth(50);
		fieldCreateName.setHeight(10);

		buttonDisease.setX(x + width / 2 - fontRenderer.getStringWidth(diseaseName + " (" + points + ")") / 2);
		buttonDisease.setY(y + height - (Screen.screen.ySize * title_bar_height) * 5 + fontRenderer.FONT_HEIGHT);
		buttonDisease.setWidth(fontRenderer.getStringWidth(diseaseName + " (" + points + ")"));
		buttonDisease.setHeight(fontRenderer.FONT_HEIGHT);

		buttonBack.setX(x + 2);
		buttonBack.setY(y + height - fontRenderer.FONT_HEIGHT);
		buttonBack.setWidth(fontRenderer.getStringWidth("Back"));
		buttonBack.setHeight(fontRenderer.FONT_HEIGHT);

		buttonCoughing.setX(x + width / 2 - 1.5 * fontRenderer.getStringWidth("Infectivity"));
		buttonCoughing.setY(y + 4.2 * fontRenderer.FONT_HEIGHT);
		buttonCoughing.setWidth(fontRenderer.getStringWidth("Coughing"));
		buttonCoughing.setHeight(fontRenderer.FONT_HEIGHT);

		buttonSneezing.setX(x + width / 2 - 1.5 * fontRenderer.getStringWidth("Infectivity"));
		buttonSneezing.setY(y + 5.2 * fontRenderer.FONT_HEIGHT);
		buttonSneezing.setWidth(fontRenderer.getStringWidth("Sneezing"));
		buttonSneezing.setHeight(fontRenderer.FONT_HEIGHT);

		buttonVomiting.setX(x + width / 2 - 1.5 * fontRenderer.getStringWidth("Infectivity"));
		buttonVomiting.setY(y + 5.2 * fontRenderer.FONT_HEIGHT);
		buttonVomiting.setWidth(fontRenderer.getStringWidth("Vomiting"));
		buttonVomiting.setHeight(fontRenderer.FONT_HEIGHT);

		buttonSeizures.setX(x + width / 2 + 0.5 * fontRenderer.getStringWidth("Lethality"));
		buttonSeizures.setY(y + 4.2 * fontRenderer.FONT_HEIGHT);
		buttonSeizures.setWidth(fontRenderer.getStringWidth("Seizures"));
		buttonSeizures.setHeight(fontRenderer.FONT_HEIGHT);

		buttonNecrosis.setX(x + width / 2 + 0.5 * fontRenderer.getStringWidth("Lethality"));
		buttonNecrosis.setY(y + 5.2 * fontRenderer.FONT_HEIGHT);
		buttonNecrosis.setWidth(fontRenderer.getStringWidth("Necrosis"));
		buttonNecrosis.setHeight(fontRenderer.FONT_HEIGHT);

		buttonOrganFailure.setX(x + width / 2 + 0.5 * fontRenderer.getStringWidth("Lethality"));
		buttonOrganFailure.setY(y + 5.2 * fontRenderer.FONT_HEIGHT);
		buttonOrganFailure.setWidth(fontRenderer.getStringWidth("Organ Failure"));
		buttonOrganFailure.setHeight(fontRenderer.FONT_HEIGHT);

		buttonFaster.setX(x + 4 + fontRenderer.getStringWidth("<<"));
		buttonFaster.setY(y + (Screen.screen.ySize * title_bar_height) + 2 + fontRenderer.FONT_HEIGHT);
		buttonFaster.setWidth(fontRenderer.getStringWidth(">>"));
		buttonFaster.setHeight(fontRenderer.FONT_HEIGHT);

		buttonSlower.setX(x + 2);
		buttonSlower.setY(y + (Screen.screen.ySize * title_bar_height) + 2 + fontRenderer.FONT_HEIGHT);
		buttonSlower.setWidth(fontRenderer.getStringWidth("<<"));
		buttonSlower.setHeight(fontRenderer.FONT_HEIGHT);

		buttonWorld.setX(x + width - fontRenderer.getStringWidth("World"));
		buttonWorld.setY(y + (Screen.screen.ySize * title_bar_height) + 2);
		buttonWorld.setWidth(fontRenderer.getStringWidth("World"));
		buttonWorld.setHeight(fontRenderer.FONT_HEIGHT);

		buttonShowInfected.setX(x + width - fontRenderer.getStringWidth("Show Infected Countries"));
		buttonShowInfected.setY(y + height - fontRenderer.FONT_HEIGHT);
		buttonShowInfected.setWidth(fontRenderer.getStringWidth("Show Infected Countries"));
		buttonShowInfected.setHeight(fontRenderer.FONT_HEIGHT);
		
		buttonNewGame.setX(x + width / 2 - 25);
		buttonNewGame.setY(y + height - 3*fontRenderer.FONT_HEIGHT);
		buttonNewGame.setWidth(50);
		buttonNewGame.setHeight(10);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDragReleased() {
		super.onDragReleased();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("state", gameState.getName());
		nbt.setString("diseaseName", diseaseName);
		nbt.setDouble("day", day);
		nbt.setDouble("speed", speed);
		nbt.setInteger("points", points);
		nbt.setBoolean("discovered", discovered);
		nbt.setInteger("vaccineProgress", vaccineProgress);
		NBTTagList list = new NBTTagList();
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			list.appendTag(country.writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("countries", list);
		list = new NBTTagList();
		for (EnumUpgrade upgrade : new ArrayList<EnumUpgrade>(upgrades)) {
			list.appendTag(new NBTTagString(upgrade.name));
		}
		nbt.setTag("upgrades", list);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		if (nbt.hasKey("state")) {
			gameState = EnumGameState.getByName(nbt.getString("state"));
		}
		if (nbt.hasKey("diseaseName")) {
			diseaseName = nbt.getString("diseaseName");
		}
		if (nbt.hasKey("day")) {
			day = nbt.getDouble("day");
		}
		if (nbt.hasKey("speed")) {
			speed = nbt.getDouble("speed");
		}
		if (nbt.hasKey("points")) {
			points = nbt.getInteger("points");
		}
		if (nbt.hasKey("discovered")) {
			discovered = nbt.getBoolean("discovered");
		}
		if (nbt.hasKey("vaccineProgress")) {
			vaccineProgress = nbt.getInteger("vaccineProgress");
		}
		if (nbt.hasKey("countries")) {
			this.countries.clear();
			NBTTagList list = nbt.getTagList("countries", Constants.NBT.TAG_COMPOUND);
			Iterator<NBTBase> it = list.iterator();
			while (it.hasNext()) {
				NBTBase base = it.next();
				if (base instanceof NBTTagCompound) {
					EpidemicCountry country = EpidemicCountry.fromNBT((NBTTagCompound) base, this);
					countries.add(country);
				}
			}

			/*
			 * Makes connections between the countries
			 */
			for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
				for (String s : country.adjacentTemp) {
					for (EpidemicCountry other : new ArrayList<EpidemicCountry>(countries)) {
						if (other.name.equals(s)) {
							country.adjacent.add(other);
							break;
						}
					}
				}
				for (String s : country.airTemp) {
					for (EpidemicCountry other : new ArrayList<EpidemicCountry>(countries)) {
						if (other.name.equals(s)) {
							country.air.add(other);
							break;
						}
					}
				}
				for (String s : country.navalTemp) {
					for (EpidemicCountry other : new ArrayList<EpidemicCountry>(countries)) {
						if (other.name.equals(s)) {
							country.naval.add(other);
							break;
						}
					}
				}
			}
		}
		if (nbt.hasKey("upgrades")) {
			upgrades.clear();
			NBTTagList list = nbt.getTagList("upgrades", Constants.NBT.TAG_STRING);
			Iterator<NBTBase> it = list.iterator();
			while (it.hasNext()) {
				NBTBase base = it.next();
				if (base instanceof NBTTagString) {
					upgrades.add(EnumUpgrade.getByName(((NBTTagString) base).getString()));
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		super.onButtonPressed(button, mouseButton);
		if (isMinimized())
			return;
		if (button == this.buttonNewGame) {
			this.gameState = EnumGameState.CREATE;
			this.sync();
		} else if (button == this.buttonStart) {
			this.diseaseName = this.fieldCreateName.getContentText();
			this.buttonDisease.setText(diseaseName);
			this.gameState = EnumGameState.SELECT_START;
			this.day = 0;
			this.speed = 1;
			this.points = 0;
			EpidemicHelper.initCountries(this);
			this.sync();
		} else if (button == this.buttonDisease) {
			this.gameState = EnumGameState.DISEASE;
			this.sync();
		} else if (button == this.buttonBack) {
			this.gameState = gameState == EnumGameState.INFECTED_MAP ? EnumGameState.WORLD : EnumGameState.GAME;
			this.sync();
		} else if (button == this.buttonCoughing) {
			if (this.points >= 10) {
				this.upgrades.add(EnumUpgrade.COUGHING);
				this.points -= 10;
				this.sync();
			}
			this.sync();
		}

		else if (button == this.buttonSneezing) {
			if (this.points >= 20) {
				this.upgrades.add(EnumUpgrade.SNEEZING);
				this.points -= 20;
				this.sync();
			}
			this.sync();
		} else if (button == this.buttonVomiting) {
			if (this.points >= 30) {
				this.upgrades.add(EnumUpgrade.VOMITING);
				this.points -= 30;
				this.sync();
			}
			this.sync();
		} else if (button == this.buttonSeizures) {
			if (this.points >= 10) {
				this.upgrades.add(EnumUpgrade.SEIZURES);
				this.points -= 10;
				this.sync();
			}
			this.sync();
		}

		else if (button == this.buttonNecrosis) {
			if (this.points >= 20) {
				this.upgrades.add(EnumUpgrade.NECROSIS);
				this.points -= 20;
				this.sync();
			}
			this.sync();
		} else if (button == this.buttonOrganFailure) {
			if (this.points >= 30) {
				this.upgrades.add(EnumUpgrade.ORGAN_FAILURE);
				this.points -= 30;
				this.sync();
			}
		} else if (button == this.buttonFaster) {
			if(++speed > 5) {
				speed = 5;
			}
			this.sync();
		} else if (button == this.buttonSlower) {
			if(--speed < 1) {
				speed = 1;
			}
			this.sync();
		} else if (button == this.buttonWorld) {
			this.gameState = EnumGameState.WORLD;
			this.sync();
		} else if (button == this.buttonShowInfected) {
			this.gameState = EnumGameState.INFECTED_MAP;
			this.sync();
		}else if (button == this.buttonMainMenu) {
			this.gameState = EnumGameState.MENU;
			this.day = 0;
			this.speed = 1;
			this.points = 0;
			this.countries.clear();
			this.upgrades.clear();
			this.sync();
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (gameState == EnumGameState.SELECT_START) {
			EpidemicCountry country = this.getHoveredEpidemicCountry();
			if (country != null) {
				country.infected = 1;
				this.gameState = EnumGameState.GAME;
				this.sync();
			}
		}
	}

	public double getInfectivity() {
		double infectivity = 1;
		for (EnumUpgrade upgrade : new ArrayList<EnumUpgrade>(upgrades)) {
			infectivity *= upgrade.infectivityModifer;
		}
		return infectivity;
	}

	public double getLethality() {
		double lethality = 1;
		for (EnumUpgrade upgrade : new ArrayList<EnumUpgrade>(upgrades)) {
			lethality *= upgrade.lethalityModifer;
		}
		return lethality - 1;
	}

	public double getDectibility() {
		double dectibility = 1;
		for (EnumUpgrade upgrade : new ArrayList<EnumUpgrade>(upgrades)) {
			dectibility *= upgrade.detectibilityModifer;
		}
		return dectibility;
	}

	public long getGlobalPopulation() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.population;
		}
		return pop;
	}

	public long getGlobalInfected() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.infected;
		}
		return pop;
	}

	public long getGlobalDead() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.dead;
		}
		return pop;
	}

	public long getGlobalHealthy() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.population - country.dead - country.infected;
		}
		return pop;
	}

	public long getGlobalAlive() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.population - country.dead;
		}
		return pop;
	}

	public void setPoints(int points) {
		this.points = points;
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		buttonDisease.setText(diseaseName + " (" + this.points + ")");
		buttonDisease.setX(x + width / 2 - fontRenderer.getStringWidth(diseaseName + " (" + this.points + ")") / 2);
		buttonDisease.setY(y + height - (Screen.screen.ySize * title_bar_height) * 5 + fontRenderer.FONT_HEIGHT);
		buttonDisease.setWidth(fontRenderer.getStringWidth(diseaseName + " (" + this.points + ")"));
	}

	public void addPoints(int delta) {
		setPoints(points + delta);
	}

	public void consumePoints(int delta) {
		setPoints(points - delta);
	}

	public Random getRand() {
		return computerTE.getWorld().rand;
	}

	/*
	 * A modified version of RenderUtils.getColorAt()
	 */
	public static Color getColorAt(ResourceLocation res, double relativeX, double relativeY) {

		if (mapCache.containsKey(res.toString())) {
			BufferedImage image = mapCache.get(res.toString());
			int x = Math.min(image.getWidth()-1,(int) Math.round(relativeX * image.getWidth()));
			int y = Math.min(image.getHeight()-1,(int) Math.round(relativeY * image.getHeight()));
			int rgb = image.getRGB(x, y);
			return new Color(rgb, true);
		}

		InputStream is = null;
		BufferedImage image;

		try {
			is = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
			image = ImageIO.read(is);

			int x = (int) Math.round(relativeX * image.getWidth());
			int y = (int) Math.round(relativeY * image.getHeight());
			mapCache.put(res.toString(), image);
			int rgb = image.getRGB(x, y);
			return new Color(rgb, true);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}
}
