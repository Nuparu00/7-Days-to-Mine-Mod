package com.nuparu.sevendaystomine.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {

	public static float mouseSensitivityFactor;
	public static boolean menu;
	public static boolean useCustomSky;
	public static boolean checkForUpdates;
	public static boolean renderPlayerInv;

	public static void loadConfig(FMLPreInitializationEvent event) {

		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		// loading the configuration from its file
		config.load();
		menu = config
				.get(Configuration.CATEGORY_CLIENT, "Menu", true, "Should use custom menu? (true = yes ; false = no)")
				.getBoolean(true);
		useCustomSky = config.get(Configuration.CATEGORY_CLIENT, "Custom Sky", true,
				"Should use custom sky (for bloodmoons)? (true = yes ; false = no) (Quite recommend to toggle this off while using shaders.)")
				.getBoolean(true);
		checkForUpdates = config
				.get(Configuration.CATEGORY_CLIENT, "Check For Updates", true, "Should check for mod updates?")
				.getBoolean(true);
		mouseSensitivityFactor = config.getFloat("Mouse Sensitivity Factor", "Guns", 50.0F, 0.0F, 100.0F,
				"Factor of mouse sensitivity while scoping. (Final Sensitivity = (Normal Sensitivity/100)*Mouse Sensitivity Factor)");
		renderPlayerInv = config.get(Configuration.CATEGORY_GENERAL, "Render Player Accessories", true,
				"Should render player items (backpack, guns ,etc..)? Disabling this on server will disable redenring even for clients. (true = yes ; false = no)")
				.getBoolean(true);

		// saving the configuration to its file
		config.save();
	}

}
