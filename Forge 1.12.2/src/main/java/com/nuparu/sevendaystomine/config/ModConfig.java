package com.nuparu.sevendaystomine.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.nuparu.sevendaystomine.SevenDaysToMine;

@Config(modid = SevenDaysToMine.MODID, category="")
@Config.LangKey("sevendaystomine.config.title")
public class ModConfig {

	public static CategoryPlayer players = new CategoryPlayer();
	public static CategoryWorld world = new CategoryWorld();
	public static CategoryClient client = new CategoryClient();
	public static CategoryMobs mobs = new CategoryMobs();
	
	public static class CategoryClient {
		@Config.Comment("Should use custom sky (for bloodmons,etc..)")
		public boolean customSky = true;
		@Config.Comment("Should use the custom menu?")
		public boolean customMenu = true;
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
	}
	
	public static class CategoryMobs {
		@Config.Comment("Zombies run mode: 0 = never run, 1 = run in the dark, 2 = always run")
		@Config.RangeInt(min = 0, max = 2)
		public int zombiesRunMode = 1;
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
		@Config.RangeInt(min = 0)
		@Config.Comment("How many streets can a city have")
		public int maxCitySize = 14;
		
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
