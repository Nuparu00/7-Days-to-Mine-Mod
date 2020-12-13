package com.nuparu.sevendaystomine.init;

import com.nuparu.sevendaystomine.world.biome.BiomeBurntForest;
import com.nuparu.sevendaystomine.world.biome.BiomeWasteland;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;

public class ModBiomes {

	public static final Biome BURNT_FOREST = new BiomeBurntForest();
	public static final Biome WASTELAND = new BiomeWasteland();

	public static void init() {
		register(BURNT_FOREST, "Burnt Forest", BiomeType.DESERT, 5, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY);
		register(WASTELAND, "Wasteland", BiomeType.DESERT, 3, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY);
	}

	public static void register(Biome biome, String name, BiomeType type, int weight, Type... types) {
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		BiomeManager.addBiome(type, new BiomeEntry(biome, weight));
		BiomeManager.addSpawnBiome(biome);
	}

}
