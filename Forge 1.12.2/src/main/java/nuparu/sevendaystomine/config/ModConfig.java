package nuparu.sevendaystomine.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.EnumHudPosition;

@Config(modid = SevenDaysToMine.MODID, category="")
@Config.LangKey("sevendaystomine.config.title")
public class ModConfig {

	public static CategoryPlayer players = new CategoryPlayer();
	public static CategoryWorld world = new CategoryWorld();
	public static CategoryWorldGen worldGen = new CategoryWorldGen();
	public static CategoryClient client = new CategoryClient();
	public static CategoryMobs mobs = new CategoryMobs();
	
	public static class CategoryClient {
		@Config.RequiresMcRestart
		@Config.Comment("Should use custom sky (for bloodmons,etc..)")
		public boolean customSky = true;
		@Config.Comment("Should use the custom menu?")
		public boolean customMenu = true;
		@Config.Comment("Should use the custom player renderer (used for aiming in 3rd person, rotating the player while riding the minibike, etc..)?")
		public boolean customPlayerRenderer = true;
		@Config.Comment("Should use custom hand rendeing in first person (with guns)? Not recommended to turn off, as results might be questionable. Could be useful with mods that affect hand rendering (maybe something like Vivecraft)")
		public boolean customGunHands = true;
		@Config.Comment("Should the camera roll while turning a minibike?")
		public boolean minibikeCameraRoll = true;
		@Config.Comment("Should draw the muzzle flash?")
		public boolean muzzleFlash = true;
		@Config.Comment("Thirst and stamina bar position")
		public EnumHudPosition hudPosition = EnumHudPosition.LEFT_BOTTOM;
		@Config.Comment("Should use vanilla code for rendering CCTV cameras instead of the code possibly injected by mods like Optfine")
		public boolean useVanillaCameraRendering = true;
		@Config.Comment("Should use custom particles?")
		public boolean particles = true;
		@Config.Comment("Should use draw the void-fog-like particles in burnt biomes?")
		public boolean burntForestParticles = true;
		@Config.Comment("Should render the fog in the wasteland/burnt biomes?")
		public boolean wastelandFog = true;
		@Config.Comment("Should use the molotov flame entites spawn the flame particles?")
		public boolean molotovParticles = true;
		@Config.Comment("Should use GLSL sahders for drunk/bleeding effects?")
		public boolean postprocessingShaders = true;
	}

	public static class CategoryPlayer {
		@Config.Comment("Cotrols rendering of player's items (weapons, tools). If false on a server, no one will be able to see the items regardless of their settings")
		public boolean renderPlayerInventory = true;
		@Config.Comment("Can players take photos with the Analog Camera item?")
		public boolean allowPhotos = true;
		@Config.Comment("Can players use cameras (the block)?")
		public boolean allowCameras = true;
		@Config.Comment("Should players receive the Survival Guide on their first spawn?")
		public boolean survivalGuide = true;
		@Config.Comment("Makes block breaking slower and more immersive")
		public boolean immersiveBlockBreaking = true;
		@Config.Comment("Affects the block breaking speed when immersiveBlockBreaking is true. The higher the number, the slower the breaking is")
		public float immersiveBlockBreakingModifier = 32;
		@Config.Comment("Controls how much scrap you get from scrapping in inventory")
		@Config.RangeDouble(min = 0, max = 1) 
		public double scrapCoefficient = 0.5;
		@Config.Comment("Do recipes have to be unlocked using the recipe books?")
		public boolean recipeBooksRequired = true;
		@Config.Comment("Disables crafting of ingots from gold/iron blocks and nuggets from gold/iron ingots")
		@Config.RequiresMcRestart
		public boolean disableBlockToIngot = true;
		@Config.Comment("Makes certain Vanilla blocks and items (tools, armors, furnace...) uncraftable")
		@Config.RequiresMcRestart
		public boolean disableVanillaBlocksAndItems = true;
		@Config.Comment("How many XP per 1 Quality point")
		@Config.RangeDouble(min = 0) 
		public double xpPerQuality = 5;
		@Config.Comment("Maximal possible Quality")
		@Config.RangeInt(min = 0) 
		public int maxQuality = 600;
		@Config.Comment("Should use the thirst system?")
		public boolean thirstSystem = true;
		@Config.Comment("Should use the stamina system?")
		public boolean staminaSystem = true;
		@Config.Comment("Should use the quality system?")
		public boolean qualitySystem = true;
		@Config.Comment("The larger the value, the less likely a player is to get infected")
		@Config.RangeInt(min = 1) 
		public int infectionChanceModifier = 6;
		@Config.Comment("Should add the backpack slot to the player inventory (does not affect the texture, if you turn this off, you should used a resourcepack where the slot is removed from the texture)")
		public boolean backpackSlot = true;
		@Config.Comment("Can a player break their legs on fall?")
		public boolean fragileLegs = true;
		@Config.Comment("Does the baneberry give the poison status effect?")
		public boolean extraPoisonousBerries = false;
		@Config.Comment("Should disable stacking of certain food items?")
		@Config.RequiresMcRestart
		public boolean disableFoodStacking = true;
		
		@Config.Comment("The duration of the individual infection stages")
		public int[] infectionStagesDuration = new int[] {24000,24000,24000,24000,24000,24000,24000};
		
		@Config.Comment("Controls the health and damages of mobs, guns, etc...")
		@Config.RequiresMcRestart
		@Config.RangeDouble(min = 0) 
		public double balanceModifier = 1;
	}
	
	public static class CategoryMobs {
		@Config.Comment("Zombies run mode: 0 = never run, 1 = run in the dark, 2 = always run")
		@Config.RangeInt(min = 0, max = 2)
		public int zombiesRunMode = 1;
		@Config.Comment("Can zombies break blocks?")
		public boolean zombiesBreakBlocks = true;
		@Config.Comment("Can bullets break blocks?")
		public boolean bulletsBreakBlocks = true;
		@Config.Comment("Should zombies attack animals?")
		public boolean zombiesAttackAnimals = true;
		@Config.Comment("The larger the value, the less likely an entity is to start bleeding")
		@Config.RangeInt(min = 1) 
		public int bleedingChanceModifier = 10;
		@Config.Comment("Should zombies spawn a corpse on death?")
		public boolean zombieCorpses = true;
		
		@Config.Comment("The spawn weight of the Reanimated Corpse")
		public int spawnWeightReanimatedCorpse = 70;
		
		@Config.Comment("The spawn weight of the Bloated Zombie")
		public int spawnWeightBloatedZombie = 70;
		
		@Config.Comment("The spawn weight of the Plagued Nurse")
		public int spawnWeightPlaguedNurse = 60;
		
		@Config.Comment("The spawn weight of the Zombie Crawler")
		public int spawnWeightZombieCrawler = 50;
		
		@Config.Comment("The spawn weight of the Spider Zombie")
		public int spawnWeightSpiderZombie = 20;
		
		@Config.Comment("The spawn weight of the Frozen Lumberjack")
		public int spawnWeightFrozenLumberjack = 25;
		
		@Config.Comment("The spawn weight of the Frigid Hunter")
		public int spawnWeightFrigidHunter = 40;
		
		@Config.Comment("The spawn weight of the Frostbitten Worker")
		public int spawnWeightFrostbittenWorker = 40;
		
		@Config.Comment("The spawn weight of the Zombie Wolf")
		public int spawnWeightZombieWolf = 20;
		
		@Config.Comment("The spawn weight of the Zombie Pig")
		public int spawnWeightZombiePig= 20;
		
		@Config.Comment("The spawn weight of the Burnt Zombie")
		public int spawnWeightBurntZombie = 20;
		
		@Config.Comment("The spawn weight of the Zombie Miner")
		public int spawnWeightZombieMiner = 30;
		
		@Config.Comment("The spawn weight of the Feral Zombie")
		public int spawnWeightFeralZombie = 1;
		
		@Config.Comment("The minimal number of Reanimated Corpses")
		public int spawnMinReanimatedCorpse = 3;
		
		@Config.Comment("The minimal number of Bloated Zombies")
		public int spawnMinBloatedZombie = 1;
		
		@Config.Comment("The minimal number of Plagued Nurses")
		public int spawnMinPlaguedNurse = 3;
		
		@Config.Comment("The minimal number of Zombie Crawlers")
		public int spawnMinZombieCrawler = 2;
		
		@Config.Comment("The minimal number of Spider Zombies")
		public int spawnMinSpiderZombie = 1;
		
		@Config.Comment("The minimal number of Frozen Lumberjacks")
		public int spawnMinFrozenLumberjack = 1;
		
		@Config.Comment("The minimal number of Frigid Hunters")
		public int spawnMinFrigidHunter = 1;
		
		@Config.Comment("The minimal number of Frostbitten Workers")
		public int spawnMinFrostbittenWorker = 2;
		
		@Config.Comment("The minimal number of Zombie Wolfs")
		public int spawnMinZombieWolf = 1;
		
		@Config.Comment("The minimal number of Zombie Pigs")
		public int spawnMinZombiePig= 1;
		
		@Config.Comment("The minimal number of Burnt Zombies")
		public int spawnMinBurntZombie = 1;
		
		@Config.Comment("The minimal number of Zombie Miners")
		public int spawnMinZombieMiner = 1;
		
		@Config.Comment("The minimal number of Feral Zombies")
		public int spawnMinFeralZombie = 1;
		
		@Config.Comment("The maximal number of Reanimated Corpses")
		public int spawnMaxReanimatedCorpse = 7;
		
		@Config.Comment("The maximal number of Bloated Zombies")
		public int spawnMaxBloatedZombie = 7;
		
		@Config.Comment("The maximal number of Plagued Nurses")
		public int spawnMaxPlaguedNurse = 7;
		
		@Config.Comment("The maximal number of Zombie Crawlers")
		public int spawnMaxZombieCrawler = 9;
		
		@Config.Comment("The maximal number of Spider Zombies")
		public int spawnMaxSpiderZombie = 2;
		
		@Config.Comment("The maximal number of Frozen Lumberjacks")
		public int spawnMaxFrozenLumberjack = 3;
		
		@Config.Comment("The maximal number of Frigid Hunters")
		public int spawnMaxFrigidHunter = 3;
		
		@Config.Comment("The maximal number of Frostbitten Workers")
		public int spawnMaxFrostbittenWorker = 5;
		
		@Config.Comment("The maximal number of Zombie Wolfs")
		public int spawnMaxZombieWolf = 3;
		
		@Config.Comment("The maximal number of Zombie Pigs")
		public int spawnMaxZombiePig= 3;
		
		@Config.Comment("The maximal number of Burnt Zombies")
		public int spawnMaxBurntZombie = 2;
		
		@Config.Comment("The maximal number of Zombie Miners")
		public int spawnMaxZombieMiner = 1;
		
		@Config.Comment("The maximal number of Feral Zombies")
		public int spawnMaxFeralZombie = 1;
		
		@Config.Comment("The knocback resistance of all the zombies")
		@Config.RangeDouble(min = 0) 
		public double zombieKnockbackResistance = 0.25;
		
		@Config.Comment("Should vanilla (and modded) monsters that attack the player also attack the human NPCs)?")
		public boolean monstersAttackHumanNPCs = true;
		
		
	}
	
	public static class CategoryWorld {
		@Config.Comment("How many days between individual bloodmoons (0 = disabled)")
		@Config.RangeInt(min = 0)
		public int bloodmoonFrequency = 7;
		@Config.Comment("How many waves bloodmoon horde has")
		@Config.RangeInt(min = 0)
		public int bloodmoonHordeWaves = 8;
		@Config.Comment("Minimal number of zombies the bloodmoon horde wave has - used during the first few bloodmoons")
		@Config.RangeInt(min = 0)
		public int bloodmoonHordeZombiesPerWaveMin = 5;
		@Config.Comment("Maximal number of zombies the bloodmoon horde wave has - used  after the fifth bloodmoon")
		@Config.RangeInt(min = 0)
		public int bloodmoonHordeZombiesPerWaveMax = 15;
		
		
		@Config.Comment("How many days between individual wolf hordes (0 = disabled)")
		@Config.RangeInt(min = 0)
		public int wolfHordeFrequency = 5;
		@Config.Comment("How many waves wolf horde has")
		@Config.RangeInt(min = 0)
		public int wolfHordeWaves = 8;
		@Config.Comment("How many zombies wolf horde wave has")
		@Config.RangeInt(min = 0)
		public int wolfHordeZombiesPerWave = 8;
		
		@Config.Comment("Chance of a generic random horde spawning every tick, unless a horde has already spawnd that day for the player (0 = disabled)")
		@Config.RangeDouble(min = 0, max = 1) 
		public double genericHordeChance = 0.00001;
		@Config.Comment("How many waves bloodmoon horde has")
		@Config.RangeInt(min = 0)
		public int genericHordeWaves = 4;
		@Config.Comment("Minimal number of zombies the horde wave has - used during the first few bloodmoons")
		@Config.RangeInt(min = 0)
		public int genericHordeZombiesPerWaveMin = 4;
		@Config.Comment("Maximal number of zombies the horde wave has - reached after the fifth bloodmoon")
		@Config.RangeInt(min = 0)
		public int genericHordeZombiesPerWaveMax = 8;
		
		@Config.Comment("How many ticks until a corpe decays")
		@Config.RangeInt(min = 0)
		public int corpseLifespan = 20000;
		@Config.Comment("How many ticks until a torch burns out (-1 = infinity)")
		@Config.RangeInt(min = -1)
		public int torchBurnTime = 22000;
		@Config.Comment("Does rain extinguish burning torches?")
		public boolean torchRainExtinguish = true;
		
		@Config.Comment("How many days between individual bloodmoons (0 = disabled)")
		@Config.RangeInt(min = 0)
		public int airdropFrequency = 3;
		@Config.Comment("Should remove vanilla zombies (and skeletons, husks)?")
		@Config.RequiresMcRestart
		public boolean removeVanillaZommbies = true;

		@Config.Comment("The minimal distance from player that a horde can spawn")
		@Config.RangeInt(min = 0)
		public int hordeMinDistance = 30;
		@Config.Comment("The maximal distance from player that a horde can spawn")
		@Config.RangeInt(min = 0)
		public int hordeMaxDistance = 30;
		@Config.Comment("The delay in ticks between individual waves")
		@Config.RangeInt(min = 0)
		public int hordeWaveDelay = 200;
		@Config.Comment("The rate of damged blocks decay, how often does the decay update (12000 = every 12000 ticks - twice a day). Non-poisitive values disable the ddecay. Can be overriden by the damageDecayRate gamerule")
		@Config.RangeInt(min = -1)
		public int damageDecayRate = 12000;
		
	}
	
	public static class CategoryWorldGen {
	
		@Config.RangeInt(min = 0)
		@Config.Comment("How many streets can a city have")
		public int maxCitySize = 14;
		@Config.Comment("How many chunks between potential City locations")
		public int citySpacing = 32;
		@Config.Comment("Should generate roads?")
		public boolean generateRoads = true;
		@Config.RangeInt(min = 0)
		public int roadMinY = 63;
		@Config.RangeInt(min = 0)
		public int roadMaxY = 80;
		
		@Config.Comment("Should generate a pedestal under structures (so they don't float)?")
		public boolean structurePedestal = true;
		@Config.Comment("Should generate snow/sand cover in snowy/sandy biomes?")
		public boolean snowSandBuildingCover = true;
		@Config.Comment("Should generate sand cover in sadny biomes over the intercity roads?")
		public boolean sandRoadCover = true;
		@Config.Comment("Should generate only the smaller structures?")
		@Config.RequiresMcRestart
		public boolean smallStructuresOnly = false;
		
		@Config.RangeDouble(min = 0)
		@Config.Comment("The higher the value, the rarer scattered structures are (0 = disabled)")
		public double scattereedStructureChanceModifier = 1;
		@Config.RangeInt(min = 0)
		@Config.Comment("The miminal distance between scattered structures squared (100 = 10 blocks)")
		public int minScatteredDistanceSq = 120000;
		@Config.RangeInt(min = 0)
		@Config.Comment("The miminal in chunks from the nearest city")
		public int minScatteredDistanceFromCities = 16;
		
		@Config.RangeInt(min = 0)
		@Config.Comment("How common Copper Ore is (0 = disabled)")
		public int copperOreGenerationRate = 34;
		@Config.RangeInt(min = 0)
		@Config.Comment("How common Tin Ore is (0 = disabled)")
		public int tinOreGenerationRate = 29;
		@Config.RangeInt(min = 0)
		@Config.Comment("How common Zinc Ore is (0 = disabled)")
		public int zincOreGenerationRate = 22;
		@Config.RangeInt(min = 0)
		@Config.Comment("How common Lead Ore is (0 = disabled)")
		public int leadOreGenerationRate = 25;
		@Config.RangeInt(min = 0)
		@Config.Comment("How common Potassium Ore is (0 = disabled)")
		public int potassiumOreGenerationRate = 20;
		@Config.RangeInt(min = 0)
		@Config.Comment("How common Cinnabar Ore is (0 = disabled)")
		public int cinnabarOreGenerationRate = 16;
		@Config.Comment("The minimal number of Large Rocks in a chunk)")
		public int largeRockGenerationRateMin = 0;
		@Config.Comment("The maximal number of Large Rocks in a chunk)")
		public int largeRockGenerationRateMax = 2;
		@Config.Comment("The minimal number of Small Rocks in a chunk)")
		public int smallRockGenerationRateMin = 0;
		@Config.Comment("The maximal number of Small Rocks in a chunk)")
		public int smallRockGenerationRateMax = 4;
		@Config.Comment("The minimal number of Sticks in a chunk)")
		public int stickGenerationRateMin = 0;
		@Config.Comment("The maximal number of Sticks in a chunk)")
		public int stickGenerationRateMax = 5;
		@Config.Comment("The minimal number of the (modded) Dead Bushes in a chunk)")
		public int deadBushGenerationRateMin = 0;
		@Config.Comment("The maximal number of the (modded) Dead Bushes in a chunk)")
		public int deadBushGenerationRateMax = 2;
		@Config.Comment("The minimal number of Cinder Blocks in a chunk)")
		public int cinderBlockGenerationRateMin = 0;
		@Config.Comment("The maximal number of Cinder Blocks in a chunk)")
		public int cinderBlockGenerationRateMax = 6;
		@Config.Comment("The minimal number of Berry Bushes in a chunk)")
		public int berryGenerationRateMin = 0;
		@Config.Comment("The maximal number of Berry Bushes in a chunk)")
		public int berryGenerationRateMax = 6;
		@Config.Comment("The minimal number of Goldenrod Plants in a chunk)")
		public int goldenrodGenerationRateMin = 0;
		@Config.Comment("The maximal number of Goldenrod Plants in a chunk)")
		public int goldenrodGenerationRateMax = 3;
		@Config.Comment("The minimal number of Corn Plants in a chunk)")
		public int cornGenerationRateMin = 0;
		@Config.Comment("The maximal number of Corn Plants in a chunk)")
		public int cornGenerationRateMax = 4;
		@Config.RequiresMcRestart
		@Config.Comment("The weighted probability of the Burnt Forest biome")
		public int burntForestWeight = 2;
		@Config.RequiresMcRestart
		@Config.Comment("The weighted probability of the Burnt Jungle biome")
		public int burntJungleWeight = 2;
		@Config.RequiresMcRestart
		@Config.Comment("The weighted probability of the Dead Forest biome")
		public int deadForestWeight = 1;
		@Config.RequiresMcRestart
		@Config.Comment("The weighted probability of the Wasteland biome")
		public int wastelandWeight = 1;
	}

	@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(SevenDaysToMine.MODID)) {
				ConfigManager.sync(SevenDaysToMine.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
