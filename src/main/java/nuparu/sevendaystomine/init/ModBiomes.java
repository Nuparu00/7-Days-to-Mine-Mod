package nuparu.sevendaystomine.init;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.world.biome.BiomeBurntForest;
import nuparu.sevendaystomine.world.biome.BiomeBurntJungle;
import nuparu.sevendaystomine.world.biome.BiomeBurntTaiga;
import nuparu.sevendaystomine.world.biome.BiomeWasteland;
import nuparu.sevendaystomine.world.biome.BiomeWastelandBeach;
import nuparu.sevendaystomine.world.biome.BiomeWastelandDesert;
import nuparu.sevendaystomine.world.biome.BiomeWastelandForest;
import nuparu.sevendaystomine.world.biome.BiomeWastelandOcean;
import nuparu.sevendaystomine.world.biome.BiomeWastelandRiver;

public class ModBiomes {

public static final Biome BURNT_FOREST = new BiomeBurntForest();
	public static final Biome BURNT_JUNGLE = new BiomeBurntJungle();
	public static final Biome BURNT_TAIGA = new BiomeBurntTaiga();
	public static final Biome WASTELAND = new BiomeWasteland();
	public static final Biome WASTELAND_FOREST = new BiomeWastelandForest();
	public static final Biome WASTELAND_RIVER = new BiomeWastelandRiver();
	public static final Biome WASTELAND_OCEAN = new BiomeWastelandOcean(new BiomeProperties("Wasteland Ocean").setTemperature(2.0F).setRainfall(0.4F).setWaterColor(0x404736).setBaseHeight(-1.0F).setHeightVariation(0.1F));
	public static final Biome WASTELAND_DEEP_OCEAN = new BiomeWastelandOcean(new BiomeProperties("Wasteland Deep Ocean").setTemperature(2.0F).setRainfall(0.4F).setWaterColor(0x404736).setBaseHeight(-1.8F).setHeightVariation(0.1F));
	public static final Biome WASTELAND_DESERT = new BiomeWastelandDesert((new Biome.BiomeProperties("Wasteland Desert")).setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(2.0F).setRainfall(0.0F).setRainDisabled().setWaterColor(0x404736));
	public static final Biome WASTELAND_BEACH = new BiomeWastelandBeach((new Biome.BiomeProperties("Wasteland Beach")).setBaseHeight(0.0F).setHeightVariation(0.025F).setTemperature(0.8F).setRainfall(0.4F).setWaterColor(0x404736));
	
	public static void init() {
		register(BURNT_FOREST, "Burnt Forest", BiomeType.WARM, ModConfig.worldGen.burntForestWeight, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY, Type.FOREST);
		register(BURNT_JUNGLE, "Burnt Jungle", BiomeType.WARM, ModConfig.worldGen.burntJungleWeight, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY, Type.JUNGLE, Type.DENSE);
		register(WASTELAND, "Wasteland", BiomeType.DESERT, ModConfig.worldGen.wastelandWeight, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY);
		register(WASTELAND_RIVER, "Wasteland River", BiomeType.DESERT, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY, Type.RIVER);
		register(BURNT_TAIGA, "Burnt Cold Taiga", BiomeType.ICY, Type.COLD, Type.DEAD, Type.WASTELAND, Type.SPOOKY, Type.SNOWY);
		register(WASTELAND_OCEAN, "Wasteland Ocean", BiomeType.COOL, Type.OCEAN, Type.DEAD, Type.WASTELAND, Type.WATER);
		register(WASTELAND_DEEP_OCEAN, "Wasteland Deep Ocean", BiomeType.COOL, Type.OCEAN, Type.DEAD, Type.WASTELAND, Type.WATER);
		register(WASTELAND_DESERT, "Wasteland Desert", BiomeType.DESERT, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY, Type.SANDY);
		register(WASTELAND_BEACH, "Wasteland Beach", BiomeType.DESERT, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY, Type.BEACH);
		register(WASTELAND_FOREST, "Dead Forest", BiomeType.WARM, ModConfig.worldGen.deadForestWeight, Type.DRY, Type.DEAD, Type.WASTELAND, Type.SPOOKY, Type.FOREST);
	}

	public static void register(Biome biome, String name, BiomeType type, int weight, Type... types) {
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		BiomeManager.addBiome(type, new BiomeEntry(biome, weight));
		BiomeManager.addSpawnBiome(biome);
		BiomeManager.addStrongholdBiome(biome);
	}
	
	public static void register(Biome biome, String name, BiomeType type, Type... types) {
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		BiomeManager.addSpawnBiome(biome);
		BiomeManager.addStrongholdBiome(biome);
	}

}
