package nuparu.sevendaystomine.init;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModFluids {

	public static Fluid GASOLINE;
	public static Fluid MERCURY;

	static {
		GASOLINE = new Fluid("gasoline", new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_still"),
				new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_flow"),
				new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_overlay")).setLuminosity(0).setDensity(135790)
						.setViscosity(500).setRarity(EnumRarity.RARE).setUnlocalizedName("gasoline");
		
		MERCURY = new Fluid("mercury", new ResourceLocation(SevenDaysToMine.MODID, "blocks/mercury_still"),
				new ResourceLocation(SevenDaysToMine.MODID, "blocks/mercury_flow"),
				new ResourceLocation(SevenDaysToMine.MODID, "blocks/mercury_overlay")).setLuminosity(0).setDensity(13534)
						.setViscosity(700).setRarity(EnumRarity.RARE).setUnlocalizedName("mercury");
	}
}
