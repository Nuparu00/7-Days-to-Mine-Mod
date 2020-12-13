package com.nuparu.sevendaystomine.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.nuparu.sevendaystomine.SevenDaysToMine;

@Config(modid = SevenDaysToMine.MODID)
@Config.LangKey("sevendaystomine.config.title")
public class ModConfig {

	public static CategoryPlayer players = new CategoryPlayer();
	public static CategoryWorld world = new CategoryWorld();

	public static class CategoryPlayer {
		@Config.Comment("Cotrols rendering of player's items (weapons, tools).")
		public boolean renderPlayerInventory = true;
	}
	
	public static class CategoryWorld {
		@Config.Comment("How many days between individual bloodmoons (0 = disabled)")
		public int bloodmoonFrequency = 7;
		@Config.Comment("How many waves bloodmoon horde has")
		public int bloodmoonHordeWaves = 8;
		@Config.Comment("How many zombies bloodmoon horde wave has")
		public int bloodmoonHordeZombiesPerWave = 8;
		
		@Config.Comment("How many days between individual wolf hordes (0 = disabled)")
		public int wolfHordeFrequency = 5;
		@Config.Comment("How many waves bloodmoon horde has")
		public int wolfHordeWaves = 8;
		@Config.Comment("How many zombies wolf horde wave has")
		public int wolfHordeZombiesPerWave = 8;
		@Config.Comment("How many ticks until a corpe decays")
		public int corpseLifespan = 20000;
		
		@Config.Comment("How many days between individual bloodmoons (0 = disabled)")
		public int airdropFrequency = 3;
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
