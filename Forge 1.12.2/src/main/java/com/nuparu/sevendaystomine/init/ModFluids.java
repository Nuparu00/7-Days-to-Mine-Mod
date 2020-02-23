package com.nuparu.sevendaystomine.init;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class ModFluids {

	public static Fluid GASOLINE;

	static {
		GASOLINE = new Fluid("gasoline", new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_still"),
				new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_flow"),
				new ResourceLocation(SevenDaysToMine.MODID, "blocks/gasoline_overlay")).setLuminosity(0).setDensity(135790)
						.setViscosity(500).setRarity(EnumRarity.RARE).setUnlocalizedName("gasoline");
	}
}
