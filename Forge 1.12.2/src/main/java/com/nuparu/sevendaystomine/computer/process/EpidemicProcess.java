package com.nuparu.sevendaystomine.computer.process;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.gui.monitor.Screen;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import com.nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import com.nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import com.nuparu.sevendaystomine.entity.EntityCameraView;
import com.nuparu.sevendaystomine.tileentity.TileEntityCamera;
import com.nuparu.sevendaystomine.util.ColorRGBA;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.client.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

public class EpidemicProcess extends WindowedProcess {

	public final ResourceLocation TITLE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/epidemic/title.png");
	public final ResourceLocation MENU = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/epidemic_menu.png");
	public final ResourceLocation MAP = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/backgrounds/epidemic_game.png");

	/*
	 * Elements
	 */

	public Button buttonNewGame;
	public TextField fieldCreateName;
	public Button buttonStart;

	/*
	 * Game Data
	 */

	public String diseaseName = "";
	public List<Country> countries = new ArrayList<Country>();
	public double day;
	public double speed;
	
	public static HashMap<String,BufferedImage> mapCache = new HashMap<String,BufferedImage>();

	public static enum EnumGameState {
		MENU("menu"), CREATE("create"), GAME("game"), WORLD("world"), DISEASE("disease"), END("end");

		String name;

		EnumGameState(String name) {
			this.name = name;
		}

		public static EnumGameState getByName(String name) {
			for (EnumGameState state : EnumGameState.values()) {
				if (state.name.equals(name))
					return state;
			}
			return MENU;
		}

		public String getName() {
			return name;
		}
	}

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

		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0, zLevel);

		ResourceLocation bgr = MENU;

		if (gameState == EnumGameState.GAME) {
			bgr = MAP;
		}

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
		}
		if (gameState == EnumGameState.GAME) {
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			for (Country country : countries) {
				float g = ((float) country.getHealthy() / (float) country.population);
				float r = 1f - g;
				GL11.glColor3f(r, g, 0);
				RenderUtils.drawTexturedRect(country.texture, x, y + (Screen.screen.ySize * title_bar_height), 0, 0,
						width, height - (Screen.screen.ySize * title_bar_height), width,
						height - (Screen.screen.ySize * title_bar_height), 1, 2);
				GL11.glColor3f(1, 1, 1);
			}
			GlStateManager.disableBlend();
			GlStateManager.disableAlpha();
			
			GlStateManager.translate(0, 0, 1);
			RenderUtils.drawString("Day " + (int)Math.floor(this.day) ,x+2, y + (Screen.screen.ySize * title_bar_height)+2, 0xffffff);
			RenderUtils.drawString(speed + "x" ,x+2, y + (Screen.screen.ySize * title_bar_height)+2 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, 0xffffff);
			RenderUtils.drawCenteredString(diseaseName ,x+width/2, y + height - (Screen.screen.ySize * title_bar_height)*5 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, 0xffffff);
			GlStateManager.translate(0, 0, -1);
			
			if (isMouseInside()) {
				for (Country country : countries) {
					double rX = (screen.mouseX - x) / width;
					double rY = (screen.mouseY - (y + (Screen.screen.ySize * title_bar_height)))
							/ (height - (Screen.screen.ySize * title_bar_height));
					if (rX < 0 || rY < 0 || rX > 1 || rY > 1)
						continue;
					Color color = getColorAt(country.texture, rX, rY);

					if (color.getAlpha() != 0) {
						GlStateManager.translate(0, 0, 2);
						this.renderTooltip(screen.mouseX, screen.mouseY, Minecraft.getMinecraft().fontRenderer,
								country.name, "Pop " + country.population, "Inf " +country.infected, "Density " +country.getDensity(), "Area " +country.area);
						GlStateManager.translate(0, 0, -2);
						break;
					}
				}
			}
		}
		super.render(partialTicks);
		GlStateManager.translate(0, 0, -zLevel);
		GlStateManager.popMatrix();

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
				for (Country country : new ArrayList<Country>(countries)) {
					country.update(delta);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {
		super.clientInit();

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

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initWindow() {
		super.initWindow();
		if (elements.size() < 2) {
			return;
		}

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
		NBTTagList list = new NBTTagList();
		for (Country country : new ArrayList<Country>(countries)) {
			list.appendTag(country.writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("countries", list);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		System.out.println(hashCode() + " " + nbt.toString());
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
		if (nbt.hasKey("countries")) {
			this.countries.clear();
			NBTTagList list = nbt.getTagList("countries", Constants.NBT.TAG_COMPOUND);
			Iterator<NBTBase> it = list.iterator();
			while (it.hasNext()) {
				NBTBase base = it.next();
				if (base instanceof NBTTagCompound) {
					Country country = Country.fromNBT((NBTTagCompound) base, this);
					countries.add(country);
				}
			}

			/*
			 * Makes connections between the countries
			 */
			for (Country country : new ArrayList<Country>(countries)) {
				for (String s : country.adjacentTemp) {
					for (Country other : new ArrayList<Country>(countries)) {
						if (other.name.equals(s)) {
							country.adjacent.add(other);
							break;
						}
					}
				}
				for (String s : country.airTemp) {
					for (Country other : new ArrayList<Country>(countries)) {
						if (other.name.equals(s)) {
							country.air.add(other);
							break;
						}
					}
				}
				for (String s : country.navalTemp) {
					for (Country other : new ArrayList<Country>(countries)) {
						if (other.name.equals(s)) {
							country.naval.add(other);
							break;
						}
					}
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
			this.gameState = EnumGameState.GAME;
			this.day = 0;
			this.speed = 1;
			initCountries();
			this.sync();
		}

	}
	
	public long getGlobalPopulation() {
		long pop = 0;
		for (Country country : new ArrayList<Country>(countries)) {
			pop+=country.population;
		}
		return pop;
	}

	public void initCountries() {
		countries.clear();
		Country canada = new Country("canada", this);
		canada.population = 38718254;
		canada.airports = 13;
		canada.ports = 15;
		//Since most of canadians live close to the US border, we will just pretend the country is a bit smaller (original are 11703)
		canada.area=7500;
		countries.add(canada);

		Country usa = new Country("usa", this);
		usa.population = 327529274;
		usa.infected = 1;
		usa.airports = 16;
		usa.ports = 10;
		usa.area=8116;
		countries.add(usa);

		Country mexico = new Country("mexico", this);
		mexico.population = 128649550;
		mexico.airports = 5;
		mexico.ports = 4;
		mexico.area=1973;
		countries.add(mexico);

		Country central_america = new Country("central_america", this);
		central_america.population = 47448336;
		central_america.airports = 2;
		central_america.ports = 5;
		central_america.area=521;
		countries.add(central_america);

		Country colombia = new Country("colombia", this);
		colombia.population = 50372424;
		colombia.airports = 2;
		colombia.ports = 3;
		colombia.area=1143;
		countries.add(colombia);

		Country venezuela = new Country("venezuela", this);
		venezuela.population = 28887118;
		venezuela.airports = 2;
		venezuela.ports = 3;
		venezuela.area=916;
		countries.add(venezuela);

		Country brazil = new Country("brazil", this);
		brazil.population = 213665677;
		brazil.airports = 2;
		brazil.ports = 7;
		brazil.area=8752;
		countries.add(brazil);

		Country guyana = new Country("guyana", this);
		guyana.population = 1653072;
		guyana.airports = 2;
		guyana.ports = 4;
		guyana.area=460;
		countries.add(guyana);

		Country bolivia = new Country("bolivia", this);
		bolivia.population = 11428245;
		bolivia.airports = 2;
		bolivia.ports = 0;
		bolivia.area=1099;
		countries.add(bolivia);

		Country peru = new Country("peru", this);
		peru.population = 49872716;
		peru.airports = 2;
		peru.ports = 3;
		peru.area=1568;
		countries.add(peru);

		Country greenland = new Country("greenland", this);
		greenland.population = 56081;
		greenland.airports = 1;
		greenland.ports = 2;
		//2166
		greenland.area=10;
		countries.add(greenland);

		Country patagonia = new Country("patagonia", this);
		patagonia.population = 69805387;
		patagonia.airports = 2;
		patagonia.ports = 4;
		patagonia.area=3944;
		countries.add(patagonia);

		Country caribbean = new Country("caribbean", this);
		caribbean.population = 41510745;
		caribbean.airports = 1;
		caribbean.ports = 8;
		caribbean.area=300;
		countries.add(caribbean);

		Country iceland = new Country("iceland", this);
		iceland.population = 364134;
		iceland.airports = 1;
		iceland.ports = 1;
		iceland.area=103;
		countries.add(iceland);

		Country british_isles = new Country("british_isles", this);
		british_isles.population = 74458732;
		british_isles.airports = 6;
		british_isles.ports = 12;
		british_isles.area=326;
		countries.add(british_isles);

		Country iberia = new Country("iberia", this);
		iberia.population = 57727165;
		iberia.airports = 4;
		iberia.ports = 4;
		iberia.area=597;
		countries.add(iberia);

		Country western_europe = new Country("western_europe", this);
		western_europe.population = 203238488;
		western_europe.airports = 20;
		western_europe.ports = 18;
		western_europe.area=1242;
		countries.add(western_europe);

		Country italy = new Country("italy", this);
		italy.population = 60905037;
		italy.airports = 8;
		italy.ports = 8;
		italy.area=301;
		countries.add(italy);

		Country central_europe = new Country("central_europe", this);
		central_europe.population = 64304391;
		central_europe.airports = 8;
		central_europe.ports = 4;
		central_europe.area=532;
		countries.add(central_europe);

		Country balkans = new Country("balkans", this);
		balkans.population = 41476418;
		balkans.airports = 4;
		balkans.ports = 8;
		balkans.area=500;
		countries.add(balkans);

		Country eastern_europe = new Country("eastern_europe", this);
		eastern_europe.population = 75354149;
		eastern_europe.airports = 7;
		eastern_europe.ports = 6;
		eastern_europe.area=1081;
		countries.add(eastern_europe);
		
		Country australia = new Country("australia", this);
		australia.population = 25718700;
		australia.airports = 3;
		australia.ports = 5;
		australia.area=7692;
		countries.add(australia);
		
		Country new_zealand = new Country("new_zealand", this);
		new_zealand.population = 5108220;
		new_zealand.airports = 3;
		new_zealand.ports = 5;
		new_zealand.area=268;
		countries.add(new_zealand);
		
		Country russia = new Country("russia", this);
		russia.population = 143392231;
		russia.airports = 8;
		russia.ports = 8;
		russia.area=17083;
		countries.add(russia);
		
		Country scandinavia = new Country("scandinavia", this);
		scandinavia.population = 21263549;
		scandinavia.airports = 2;
		scandinavia.ports = 12;
		scandinavia.area=1173;
		countries.add(scandinavia);
		
		Country china = new Country("china", this);
		china.population = 1400050000;
		china.airports = 6;
		china.ports = 18;
		china.area=8596;
		countries.add(china);
		
		Country indonesia = new Country("indonesia", this);
		indonesia.population = 276605543;
		indonesia.airports = 0;
		indonesia.ports = 6;
		indonesia.area=2566;
		countries.add(indonesia);
		
		Country indochina = new Country("indochina", this);
		indochina.population = 277196068;
		indochina.airports = 0;
		indochina.ports = 8;
		indochina.area=2070;
		countries.add(indochina);
		
		Country india = new Country("india", this);
		india.population = 1564672090;
		india.airports = 6;
		india.ports = 6;
		india.area=3685;
		countries.add(india);
		
		Country pakistan = new Country("pakistan", this);
		pakistan.population = 212228286;
		pakistan.airports = 4;
		pakistan.ports = 3;
		pakistan.area=881;
		countries.add(pakistan);
		
		Country afghanistan = new Country("afghanistan", this);
		afghanistan.population = 32225560;
		afghanistan.airports = 1;
		afghanistan.ports = 0;
		afghanistan.area=652;
		countries.add(afghanistan);
		
		Country iran = new Country("iran", this);
		iran.population = 83183741;
		iran.airports = 2;
		iran.ports = 3;
		iran.area=1648;
		countries.add(iran);
		
		Country baltic_states = new Country("baltic_states", this);
		baltic_states.population = 7026584;
		baltic_states.airports = 1;
		baltic_states.ports = 2;
		baltic_states.area=189;
		countries.add(baltic_states);
		
		Country turkey = new Country("turkey", this);
		turkey.population = 83154997;
		turkey.airports = 2;
		turkey.ports = 6;
		turkey.area=783;
		countries.add(turkey);
		
		Country caucasia = new Country("caucasia", this);
		caucasia.population = 16801632;
		caucasia.airports = 1;
		caucasia.ports = 1;
		caucasia.area=164;
		countries.add(caucasia);
		
		Country mongolia = new Country("mongolia", this);
		mongolia.population = 3353470;
		mongolia.airports = 1;
		mongolia.ports = 0;
		mongolia.area=1566;
		countries.add(mongolia);
		
		Country kazakhstan = new Country("kazakhstan", this);
		kazakhstan.population = 18711200;
		kazakhstan.airports = 1;
		kazakhstan.ports = 2;
		kazakhstan.area=2724;
		countries.add(kazakhstan);
		
		Country central_asia = new Country("central_asia", this);
		central_asia.population = 55726041;
		central_asia.airports = 0;
		central_asia.ports = 1;
		central_asia.area=1283;
		countries.add(central_asia);
		
		Country levant = new Country("levant", this);
		levant.population = 87301488;
		levant.airports = 2;
		levant.ports = 2;
		levant.area=747;
		countries.add(levant);
		
		Country japan = new Country("japan", this);
		japan.population = 125960000;
		japan.airports = 8;
		japan.ports = 8;
		japan.area=377;
		countries.add(japan);
		
		Country korea = new Country("korea", this);
		korea.population = 77258702;
		korea.airports = 3;
		korea.ports = 3;
		korea.area=220;
		countries.add(korea);
		
		Country philippines = new Country("philippines", this);
		philippines.population = 106651394;
		philippines.airports = 0;
		philippines.ports = 5;
		philippines.area=300;
		countries.add(philippines);
		
		Country arabia = new Country("arabia", this);
		arabia.population = 86221765;
		arabia.airports = 4;
		arabia.ports = 8;
		arabia.area=3237;
		countries.add(arabia);
		
		Country egypt = new Country("egypt", this);
		egypt.population = 100075480;
		egypt.airports = 4;
		egypt.ports = 8;
		egypt.area=1010;
		countries.add(egypt);
		
		Country libya = new Country("libya", this);
		libya.population = 6871287;
		libya.airports = 0;
		libya.ports = 2;
		libya.area=1759;
		countries.add(libya);
		
		Country maghreb = new Country("maghreb", this);
		maghreb.population = 92720450;
		maghreb.airports = 1;
		maghreb.ports = 2;
		maghreb.area=3254;
		countries.add(maghreb);
		
		Country mauritania = new Country("mauritania", this);
		mauritania.population = 43185053;
		mauritania.airports = 0;
		mauritania.ports = 2;
		mauritania.area=2742;
		countries.add(mauritania);
		
		Country dr_congo = new Country("dr_congo", this);
		dr_congo.population = 89561404 ;
		dr_congo.airports = 0;
		dr_congo.ports = 2;
		dr_congo.area=2345;
		countries.add(dr_congo);
		
		Country angola = new Country("angola", this);
		angola.population = 31127674 ;
		angola.airports = 0;
		angola.ports = 2;
		angola.area=1246;
		countries.add(angola);
		
		Country namibia = new Country("namibia", this);
		namibia.population = 2746745 ;
		namibia.airports = 1;
		namibia.ports = 2;
		namibia.area=825;
		countries.add(namibia);
		
		Country nigeria = new Country("nigeria", this);
		nigeria.population = 206630269 ;
		nigeria.airports = 0;
		nigeria.ports = 2;
		nigeria.area=923;
		countries.add(nigeria);
		
		Country niger = new Country("niger", this);
		niger.population = 22442831  ;
		niger.airports = 1;
		niger.ports = 2;
		niger.area=1267;
		countries.add(niger);
		
		Country sudan = new Country("sudan", this);
		sudan.population = 54370789;
		sudan.airports = 1;
		sudan.ports = 2;
		sudan.area=2505;
		countries.add(sudan);
		
		Country madagascar = new Country("madagascar", this);
		madagascar.population = 26262313;
		madagascar.airports = 1;
		madagascar.ports = 4;
		madagascar.area=587;
		countries.add(madagascar);
		
		Country horn_of_africa = new Country("horn_of_africa", this);
		horn_of_africa.population = 131222029;
		horn_of_africa.airports = 1;
		horn_of_africa.ports = 4;
		horn_of_africa.area=1881;
		countries.add(horn_of_africa);
		
		Country south_africa = new Country("south_africa", this);
		south_africa.population = 65121027;
		south_africa.airports = 4;
		south_africa.ports = 5;
		south_africa.area=1849;
		countries.add(south_africa);
		
		Country guinea = new Country("guinea", this);
		guinea.population = 126723945;
		guinea.airports = 4;
		guinea.ports = 5;
		guinea.area=1468;
		countries.add(guinea);
		
		Country cameroon = new Country("cameroon", this);
		cameroon.population = 26545864 ;
		cameroon.airports = 4;
		cameroon.ports = 5;
		cameroon.area=475;
		countries.add(cameroon);
		
		Country chad = new Country("chad", this);
		chad.population = 18336452;
		chad.airports = 4;
		chad.ports = 0;
		chad.area=1906;
		countries.add(chad);
		
		Country east_africa = new Country("east_africa", this);
		east_africa.population = 170846988;
		east_africa.airports = 4;
		east_africa.ports = 0;
		east_africa.area=1821;
		countries.add(east_africa);
		
		Country mozambique = new Country("mozambique", this);
		mozambique.population = 80764117;
		mozambique.airports = 0;
		mozambique.ports = 4;
		mozambique.area=2061;
		countries.add(mozambique);
		
		Country gabon = new Country("gabon", this);
		gabon.population = 9029451;
		gabon.airports = 0;
		gabon.ports = 4;
		gabon.area=638;
		countries.add(gabon);

		canada.addLandAdjacency(usa);
		usa.addLandAdjacency(mexico);
		mexico.addLandAdjacency(central_america);
		central_america.addLandAdjacency(colombia);
		colombia.addLandAdjacency(venezuela);
		colombia.addLandAdjacency(peru);
		colombia.addLandAdjacency(brazil);
		brazil.addLandAdjacency(venezuela);
		venezuela.addLandAdjacency(guyana);
		brazil.addLandAdjacency(guyana);
		brazil.addLandAdjacency(peru);
		brazil.addLandAdjacency(bolivia);
		brazil.addLandAdjacency(patagonia);
		peru.addLandAdjacency(bolivia);
		peru.addLandAdjacency(patagonia);
		bolivia.addLandAdjacency(patagonia);
		iberia.addLandAdjacency(western_europe);
		western_europe.addLandAdjacency(italy);
		western_europe.addLandAdjacency(central_europe);
		western_europe.addLandAdjacency(balkans);
		italy.addLandAdjacency(balkans);
		central_europe.addLandAdjacency(balkans);
		central_europe.addLandAdjacency(eastern_europe);
		central_europe.addLandAdjacency(baltic_states);
		eastern_europe.addLandAdjacency(baltic_states);
		balkans.addLandAdjacency(eastern_europe);
		eastern_europe.addLandAdjacency(russia);
		russia.addLandAdjacency(scandinavia);
		russia.addLandAdjacency(china);
		russia.addLandAdjacency(baltic_states);
		china.addLandAdjacency(indochina);
		china.addLandAdjacency(india);
		indochina.addLandAdjacency(india);
		china.addLandAdjacency(afghanistan);
		china.addLandAdjacency(pakistan);
		india.addLandAdjacency(pakistan);
		iran.addLandAdjacency(afghanistan);
		iran.addLandAdjacency(pakistan);
		iran.addLandAdjacency(turkey);
		balkans.addLandAdjacency(turkey);
		iran.addLandAdjacency(caucasia);
		turkey.addLandAdjacency(caucasia);
		russia.addLandAdjacency(caucasia);
		russia.addLandAdjacency(mongolia);
		china.addLandAdjacency(mongolia);
		kazakhstan.addLandAdjacency(mongolia);
		kazakhstan.addLandAdjacency(mongolia);
		kazakhstan.addLandAdjacency(russia);
		kazakhstan.addLandAdjacency(china);
		kazakhstan.addLandAdjacency(central_asia);
		china.addLandAdjacency(central_asia);
		afghanistan.addLandAdjacency(central_asia);
		iran.addLandAdjacency(central_asia);
		turkey.addLandAdjacency(levant);
		iran.addLandAdjacency(levant);
		china.addLandAdjacency(korea);
		russia.addLandAdjacency(korea);
		levant.addLandAdjacency(arabia);
		levant.addLandAdjacency(egypt);
		egypt.addLandAdjacency(libya);
		libya.addLandAdjacency(maghreb);
		maghreb.addLandAdjacency(mauritania);
		dr_congo.addLandAdjacency(angola);
		angola.addLandAdjacency(namibia);
		libya.addLandAdjacency(niger);
		maghreb.addLandAdjacency(niger);
		mauritania.addLandAdjacency(niger);
		niger.addLandAdjacency(nigeria);
		egypt.addLandAdjacency(sudan);
		dr_congo.addLandAdjacency(sudan);
		horn_of_africa.addLandAdjacency(sudan);
		namibia.addLandAdjacency(south_africa);
		mauritania.addLandAdjacency(guinea);
		nigeria.addLandAdjacency(guinea);
		niger.addLandAdjacency(guinea);
		nigeria.addLandAdjacency(cameroon);
		dr_congo.addLandAdjacency(cameroon);
		chad.addLandAdjacency(cameroon);
		chad.addLandAdjacency(libya);
		chad.addLandAdjacency(sudan);
		chad.addLandAdjacency(dr_congo);
		chad.addLandAdjacency(niger);
		chad.addLandAdjacency(nigeria);
		horn_of_africa.addLandAdjacency(east_africa);
		sudan.addLandAdjacency(east_africa);
		dr_congo.addLandAdjacency(east_africa);
		dr_congo.addLandAdjacency(mozambique);
		east_africa.addLandAdjacency(mozambique);
		angola.addLandAdjacency(mozambique);
		south_africa.addLandAdjacency(mozambique);
		namibia.addLandAdjacency(mozambique);
		dr_congo.addLandAdjacency(gabon);
		chad.addLandAdjacency(gabon);
		cameroon.addLandAdjacency(gabon);
		
		iceland.addNavalConnection(greenland);
		iceland.addNavalConnection(british_isles);
		usa.addNavalConnection(caribbean);
		usa.addNavalConnection(brazil);
		usa.addNavalConnection(british_isles);
		usa.addNavalConnection(iberia);
		usa.addNavalConnection(western_europe);
		usa.addNavalConnection(china);
		canada.addNavalConnection(greenland);
		canada.addNavalConnection(british_isles);
		patagonia.addNavalConnection(australia);
		patagonia.addNavalConnection(new_zealand);
		australia.addNavalConnection(new_zealand);
		scandinavia.addNavalConnection(western_europe);
		scandinavia.addNavalConnection(british_isles);
		scandinavia.addNavalConnection(iceland);
		scandinavia.addNavalConnection(baltic_states);
		australia.addNavalConnection(china);
		australia.addNavalConnection(indonesia);
		china.addNavalConnection(indonesia);
		china.addNavalConnection(indochina);
		indonesia.addNavalConnection(indochina);
		china.addNavalConnection(india);
		australia.addNavalConnection(india);
		turkey.addNavalConnection(italy);
		turkey.addNavalConnection(iberia);
		kazakhstan.addNavalConnection(caucasia);
		kazakhstan.addNavalConnection(central_asia);
		caucasia.addNavalConnection(central_asia);
		korea.addNavalConnection(japan);
		japan.addNavalConnection(china);
		japan.addNavalConnection(philippines);
		indonesia.addNavalConnection(philippines);
		usa.addNavalConnection(philippines);
		arabia.addNavalConnection(india);
		arabia.addNavalConnection(australia);
		egypt.addNavalConnection(iberia);
		egypt.addNavalConnection(india);
		libya.addNavalConnection(italy);
		india.addNavalConnection(madagascar);
		australia.addNavalConnection(madagascar);
		horn_of_africa.addNavalConnection(madagascar);
		horn_of_africa.addNavalConnection(arabia);
		horn_of_africa.addNavalConnection(india);
		south_africa.addNavalConnection(india);
		south_africa.addNavalConnection(madagascar);
		south_africa.addNavalConnection(australia);
		south_africa.addNavalConnection(patagonia);
		south_africa.addNavalConnection(mozambique);
		madagascar.addNavalConnection(mozambique);
		arabia.addNavalConnection(mozambique);
		
		for (Country other : new ArrayList<Country>(countries)) {
			if (other != usa && other.airports > 0) {
				usa.addAirConnection(other);
			}
		}
		
		australia.addAirConnection(new_zealand);
		australia.addAirConnection(china);
		british_isles.addAirConnection(russia);
		british_isles.addAirConnection(india);
		british_isles.addAirConnection(western_europe);
		british_isles.addAirConnection(turkey);
		australia.addAirConnection(india);
		china.addAirConnection(iran);
		china.addAirConnection(turkey);
		iran.addAirConnection(turkey);
		russia.addAirConnection(kazakhstan);
		russia.addAirConnection(caucasia);
		russia.addAirConnection(iran);
		russia.addAirConnection(russia);
		russia.addAirConnection(china);
		british_isles.addAirConnection(egypt);

	}

	public static class Country {
		String name;
		ResourceLocation texture;

		public long population;
		//In thousands km sq
		public int area;
		public long infected;
		public long dead = 0;
		public int airports;
		public int ports;
		public boolean quarantine;
		public boolean martialLaw;

		private List<Country> adjacent = new ArrayList<Country>();
		private List<Country> naval = new ArrayList<Country>();
		private List<Country> air = new ArrayList<Country>();

		private List<String> adjacentTemp = new ArrayList<String>();
		private List<String> navalTemp = new ArrayList<String>();
		private List<String> airTemp = new ArrayList<String>();

		EpidemicProcess parent;

		private Country(EpidemicProcess parent) {
			this.parent = parent;
		}

		public Country(String name, EpidemicProcess parent) {
			this(parent);
			this.name = name;
			this.texture = new ResourceLocation(SevenDaysToMine.MODID,
					"textures/backgrounds/epidemic/" + name.toLowerCase() + ".png");
		}

		public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
			nbt.setString("name", name);
			nbt.setInteger("area", area);
			nbt.setLong("population", population);
			nbt.setLong("infected", infected);
			nbt.setLong("dead", dead);
			nbt.setInteger("ports", ports);
			nbt.setInteger("airports", airports);
			nbt.setBoolean("quarantine", quarantine);
			nbt.setBoolean("martialLaw", martialLaw);

			NBTTagList adjacents = new NBTTagList();
			for (Country country : new ArrayList<Country>(adjacent)) {
				adjacents.appendTag(new NBTTagString(country.name));
			}

			nbt.setTag("adjacent", adjacents);

			NBTTagList airs = new NBTTagList();
			for (Country country : new ArrayList<Country>(air)) {
				airs.appendTag(new NBTTagString(country.name));
			}

			nbt.setTag("air", airs);

			NBTTagList navals = new NBTTagList();
			for (Country country : new ArrayList<Country>(naval)) {
				navals.appendTag(new NBTTagString(country.name));
			}

			nbt.setTag("naval", navals);

			return nbt;
		}

		public void readFromNBT(NBTTagCompound nbt) {
			this.adjacent.clear();
			this.adjacentTemp.clear();

			this.air.clear();
			this.airTemp.clear();

			this.naval.clear();
			this.navalTemp.clear();

			name = nbt.getString("name");
			area = nbt.getInteger("area");
			population = nbt.getLong("population");
			infected = nbt.getLong("infected");
			dead = nbt.getLong("dead");
			ports = nbt.getInteger("ports");
			airports = nbt.getInteger("airports");
			quarantine = nbt.getBoolean("quarantine");
			martialLaw = nbt.getBoolean("martialLaw");

			NBTTagList adjacents = nbt.getTagList("adjacent", Constants.NBT.TAG_STRING);
			Iterator<NBTBase> it = adjacents.iterator();
			while (it.hasNext()) {
				NBTBase base = it.next();
				if (base instanceof NBTTagString) {
					this.adjacentTemp.add(((NBTTagString) base).getString());
				}
			}

			NBTTagList airs = nbt.getTagList("air", Constants.NBT.TAG_STRING);
			it = airs.iterator();
			while (it.hasNext()) {
				NBTBase base = it.next();
				if (base instanceof NBTTagString) {
					this.airTemp.add(((NBTTagString) base).getString());
				}
			}

			NBTTagList navals = nbt.getTagList("naval", Constants.NBT.TAG_STRING);
			it = navals.iterator();
			while (it.hasNext()) {
				NBTBase base = it.next();
				if (base instanceof NBTTagString) {
					this.navalTemp.add(((NBTTagString) base).getString());
				}
			}

			this.texture = new ResourceLocation(SevenDaysToMine.MODID,
					"textures/backgrounds/epidemic/" + name.toLowerCase() + ".png");
		}

		public void addLandAdjacency(Country country) {
			addLandAdjacency(country, true);
		}

		public void addNavalConnection(Country country) {
			addNavalConnection(country, true);
		}

		public void addAirConnection(Country country) {
			addAirConnection(country, true);
		}

		public void addLandAdjacency(Country country, boolean bothDirections) {
			this.adjacent.add(country);
			if (bothDirections) {
				country.adjacent.add(this);
			}
		}

		public void addNavalConnection(Country country, boolean bothDirections) {
			if (this.ports == 0 || country.ports == 0)
				return;
			this.naval.add(country);
			if (bothDirections) {
				country.naval.add(this);
			}
		}

		public void addAirConnection(Country country, boolean bothDirections) {
			if (this.airports == 0 || country.airports == 0)
				return;
			this.air.add(country);
			if (bothDirections) {
				country.air.add(this);
			}
		}

		/*
		 * Called when at least a one day has passed
		 */
		public void update(double delta) {
			if (infected > 0) {
				if (getHealthy() > 0) {
					long deltaInfected = (long) (infected*(0.2)*(1+(getDensity()/100d))*delta);
					if(deltaInfected == 0 && this.parent.computerTE.getWorld().rand.nextInt(20) == 0) {
						deltaInfected = 1;
					}
					if(deltaInfected > getHealthy()) {
						deltaInfected = getHealthy();
					}
					infected+=deltaInfected;
					
				}
				if (!quarantine) {
					for (Country destination : new ArrayList<Country>(adjacent)) {
						if(destination.quarantine || destination.getHealthy() == 0) continue;
						if((5*this.parent.computerTE.getWorld().rand.nextFloat()*parent.getGlobalPopulation())/500 <= infected) {
							destination.infected++;
						}
					}
					
					for (Country destination : new ArrayList<Country>(air)) {
						if(destination.quarantine || destination.getHealthy() == 0) continue;
						if((50*this.parent.computerTE.getWorld().rand.nextFloat()*parent.getGlobalPopulation())/airports <= infected) {
							destination.infected++;
						}
					}
					
					for (Country destination : new ArrayList<Country>(naval)) {
						if(destination.quarantine || destination.getHealthy() == 0) continue;
						if((50*this.parent.computerTE.getWorld().rand.nextFloat()*parent.getGlobalPopulation())/ports <= infected) {
							destination.infected++;
						}
					}
				}
			}
		}

		public long getHealthy() {
			return population - (infected + dead);
		}
		
		public long getAlive() {
			return population - dead;
		}
		
		public double getDensity() {
			return getAlive()/(double)(area*1000);
		}

		public static Country fromNBT(NBTTagCompound nbt, EpidemicProcess parent) {
			Country country = new Country(parent);
			country.readFromNBT(nbt);
			return country;
		}

	}
	
	/*
	 * A modified version of RenderUtils.getColorAt()
	 */
	public static Color getColorAt(ResourceLocation res, double relativeX, double relativeY) {
		
		if(mapCache.containsKey(res.toString())) {
			BufferedImage image = mapCache.get(res.toString());
			int x = (int)Math.round(relativeX*image.getWidth());
			int y = (int)Math.round(relativeY*image.getHeight());
			int rgb = image.getRGB(x, y);
			return new Color(rgb, true);
		}
		
		InputStream is = null;
		BufferedImage image;

		try {
			is = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
			image = ImageIO.read(is);

			int x = (int)Math.round(relativeX*image.getWidth());
			int y = (int)Math.round(relativeY*image.getHeight());
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
