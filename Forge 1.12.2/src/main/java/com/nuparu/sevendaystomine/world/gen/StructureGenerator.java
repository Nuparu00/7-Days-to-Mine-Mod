package com.nuparu.sevendaystomine.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedChunk;
import com.nuparu.sevendaystomine.init.ModBiomes;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.City;
import com.nuparu.sevendaystomine.world.gen.city.CitySavedData;
import com.nuparu.sevendaystomine.world.gen.city.building.Building;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingAirplane;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingCargoShip;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingFactory;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingHelicopter;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingMilitaryBase;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingWindTurbine;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenLookoutBurnt;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenStick;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

public class StructureGenerator implements IWorldGenerator {

	Building FACTORY = new BuildingFactory(new ResourceLocation(SevenDaysToMine.MODID, "factory_garage"), 30)
			.setAllowedBiomes(ModBiomes.BURNT_FOREST, ModBiomes.WASTELAND);
	Building LOOKOUT = new Building(new ResourceLocation(SevenDaysToMine.MODID, "lookout"), 400)
			.setAllowedBiomes(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).stream().toArray(Biome[]::new))
			.setAllowedBlocks(Blocks.GRASS);
	Building LOOKOUT_BURNT = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "lookout_burnt"), 400))
			.setAllowedBiomes(ModBiomes.BURNT_FOREST).setAllowedBlocks(Blocks.GRASS);
	Building RUINED_HOUSE = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house"), 400))
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
			.setAllowedBlocks(Blocks.GRASS);
	Building RUINED_HOUSE_1 = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_1"), 400))
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
			.setAllowedBlocks(Blocks.GRASS);
	Building BANDIT_CAMP = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "bandit_camp"), 80, 0,
			Blocks.STONE.getDefaultState()))
					.setAllowedBiomes(Utils
							.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH))
							.stream().toArray(Biome[]::new))
					.setAllowedBlocks(Blocks.GRASS);
	Building MILITARY_BASE = new BuildingMilitaryBase(0, -3)
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
			.setAllowedBlocks(Blocks.GRASS);

	Building RUINED_HOUSE_2 = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_2"), 400))
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
			.setAllowedBlocks(Blocks.GRASS);
	Building RUINED_HOUSE_ICY_2 = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_icy_2"), 400))
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY)).stream()
					.toArray(Biome[]::new));

	Building RUINED_HOUSE_ICY = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_icy"), 400))
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY)).stream()
					.toArray(Biome[]::new));

	Building RUINED_HOUSE_DESERT_1 = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_desert_1"),
			400)).setAllowedBiomes(
					Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY)).stream()
							.toArray(Biome[]::new));
	Building CARGO_SHIP = (new BuildingCargoShip(400, -4)).setAllowedBiomes(
			Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN)).stream().toArray(Biome[]::new));
	Building AIRPLANE = (new BuildingAirplane(
			400, -4))
					.setAllowedBiomes(Utils
							.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
									BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH))
							.stream().toArray(Biome[]::new));
	Building HELICOPTER = new BuildingHelicopter(new ResourceLocation(SevenDaysToMine.MODID, "helicopter"), 0, -4)
			.setHasPedestal(false)
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new));
	Building OBSERVATORY = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "observatory"), 400))
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
			.setAllowedBlocks(Blocks.GRASS);;

	Building WIND_TURBINE = (new BuildingWindTurbine(400, 0, ModBlocks.MARBLE.getDefaultState()))
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
			.setAllowedBlocks(Blocks.GRASS);;

	Building WELL_BUNKER = (new Building(new ResourceLocation(SevenDaysToMine.MODID, "well_bunker"), 400, -15))
			.setHasPedestal(false)
			.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
					BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new));

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, random, chunkX, chunkZ);
			break;
		case 0:
			generateOverworld(world, random, chunkX, chunkZ);
			break;
		case 1:
			generateEnd(world, random, chunkX, chunkZ);
			break;
		}
	}

	private void generateNether(World world, Random rand, int chunkX, int blockZ) {
	}

	private void generateOverworld(World world, Random rand, int chunkX, int chunkZ) {
		if (world.getWorldType() == WorldType.FLAT) {
			return;
		}

		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;

		CitySavedData data = CitySavedData.get(world);
		if (data.isCityNearby(new BlockPos(blockX, 0, blockZ), 2500))
			return;

		if (generateStructure(LOOKOUT_BURNT, world, rand, blockX, blockZ, 600)) {
			return;
		}

		if (generateStructure(LOOKOUT, world, rand, blockX, blockZ, 756)) {
			return;
		}

		if (generateStructure(RUINED_HOUSE, world, rand, blockX, blockZ, 700)) {
			return;
		}

		if (generateStructure(RUINED_HOUSE_1, world, rand, blockX, blockZ, 512)) {
			return;
		}

		if (generateStructure(BANDIT_CAMP, world, rand, blockX, blockZ, 1400)) {
			return;
		}

		if (generateStructure(MILITARY_BASE, world, rand, blockX, blockZ, 2500)) {
			return;
		}

		if (generateStructure(RUINED_HOUSE_2, world, rand, blockX, blockZ, 512)) {
			return;
		}

		if (generateStructure(RUINED_HOUSE_ICY_2, world, rand, blockX, blockZ, 512)) {
			return;
		}

		if (generateStructure(RUINED_HOUSE_ICY, world, rand, blockX, blockZ, 512)) {
			return;
		}

		if (generateStructure(RUINED_HOUSE_DESERT_1, world, rand, blockX, blockZ, 512)) {
			return;
		}

		if (generateStructure(CARGO_SHIP, world, rand, blockX, blockZ, 1080)) {
			return;
		}

		if (generateStructure(AIRPLANE, world, rand, blockX, blockZ, 22200)) {
			return;
		}

		if (generateStructure(HELICOPTER, world, rand, blockX, blockZ, 1200)) {
			return;
		}
		
		if (generateStructure(WELL_BUNKER, world, rand, blockX, blockZ, 2000)) {
			return;
		}

		if (generateStructure(OBSERVATORY, world, rand, blockX, blockZ, 3000)) {
			return;
		}

		if (generateStructure(WIND_TURBINE, world, rand, blockX, blockZ, 1000)) {
			return;
		}

		if (generateStructure(FACTORY, world, rand, blockX, blockZ, 1440)) {
			return;
		}

	}

	private void generateEnd(World world, Random rand, int chunkX, int blockZ) {

	}

	private boolean generateStructure(Building building, World world, Random rand, int blockX, int blockZ, int chance) {
		if (rand.nextInt(chance) != 0)
			return false;
		BlockPos pos = Utils.getTopGroundBlock(new BlockPos(blockX, 64, blockZ), world, true);
		if (building.allowedBlocks == null || building.allowedBlocks.length == 0
				|| Arrays.asList(building.allowedBlocks).contains(world.getBlockState(pos).getBlock())) {

			Biome biome = world.provider.getBiomeForCoords(pos);
			if (building.allowedBiomes == null || building.allowedBiomes.isEmpty()|| building.allowedBiomes.contains(biome)) {
				building.generate(world, pos, EnumFacing.getHorizontal(rand.nextInt(4)), rand.nextBoolean());
				return true;
			}
		}
		return false;
	}
}
