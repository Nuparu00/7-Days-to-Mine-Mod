package nuparu.sevendaystomine.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static ForgeConfigSpec.BooleanValue minibikeCameraRoll;
    public static ForgeConfigSpec.BooleanValue solarPanelHum;
    public static ForgeConfigSpec.DoubleValue windTurbineRotationSpeedMultiplier;

    public static void init(ForgeConfigSpec.Builder server) {
        minibikeCameraRoll=server.comment("Should the camera roll while turning a minibike?").define("render.camera_roll",true);
        solarPanelHum=server.comment("Should solar panels make a humming sound while generating power?").define("sound.solar_panel_hum",true);
        windTurbineRotationSpeedMultiplier=server.comment("The speed of the wind turbine blades is multiplied by this value. This is purely cosmetic, it does not affect the produced electricity.").defineInRange("render.windTurbineRotationSpeedMultiplier",1f,0f,Double.MAX_VALUE);
    }
}
