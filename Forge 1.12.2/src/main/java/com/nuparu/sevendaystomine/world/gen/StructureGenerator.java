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
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.City;
import com.nuparu.sevendaystomine.world.gen.city.CitySavedData;
import com.nuparu.sevendaystomine.world.gen.city.building.Building;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingAirplane;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingCargoShip;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingFactory;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingHelicopter;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingLargeBanditCamp;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingMilitaryBase;
import com.nuparu.sevendaystomine.world.gen.city.building.BuildingSettlement;
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
import net.minecraft.util.math.MathHelper;
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
	
	
	public static List<Building> buildings = new ArrayList<Building>();
	
	
	public static void loadBuildings() {
		buildings.add(new BuildingFactory(new ResourceLocation(SevenDaysToMine.MODID, "factory_garage"), 30)
				.setAllowedBiomes(ModBiomes.BURNT_FOREST, ModBiomes.WASTELAND));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "lookout_birch"), 280)
				.setAllowedBiomes(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "lookout_dark_oak"), 280)
				.setAllowedBiomes(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add((new Building(new ResourceLocation(SevenDaysToMine.MODID, "lookout_burnt"), 280))
				.setAllowedBiomes(ModBiomes.BURNT_FOREST).setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "burnt_house"), 300, -5)
		.setAllowedBiomes(ModBiomes.BURNT_FOREST).setAllowedBlocks(Blocks.GRASS)
		.setPedestal(Blocks.STONE.getDefaultState()));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house"), 300)
		.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
		.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_1"), 300)
		.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
		.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "bandit_camp"), 80, 0,
				Blocks.STONE.getDefaultState())
		.setAllowedBiomes(Utils
				.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH))
				.stream().toArray(Biome[]::new))
		.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new BuildingLargeBanditCamp(20)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new BuildingMilitaryBase(20, -3)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_2"), 300)
		.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
		.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_icy_2"), 300)
		.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY)).stream()
				.toArray(Biome[]::new)));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_icy"), 300)
		.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY)).stream()
				.toArray(Biome[]::new)));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "ruined_house_desert_1"),
				300).setAllowedBiomes(
						Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY)).stream()
								.toArray(Biome[]::new)));
		buildings.add(new BuildingCargoShip(60, -4).setAllowedBiomes(
				Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN)).stream().toArray(Biome[]::new)));
		buildings.add(new BuildingAirplane(80, -4).setAllowedBiomes(Utils.combine(
				BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH), BiomeDictionary.getBiomes(BiomeDictionary.Type.BEACH),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD), BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY))
				.stream().toArray(Biome[]::new)));
		buildings.add(new BuildingHelicopter(new ResourceLocation(SevenDaysToMine.MODID, "helicopter"), 50, -3)
				.setHasPedestal(false)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new)));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "observatory"), 150)
		.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
		.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new BuildingWindTurbine(200, 0, ModBlocks.MARBLE.getDefaultState())
		.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
		.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new Building(new ResourceLocation(SevenDaysToMine.MODID, "well_bunker"), 40, -31)
		.setHasPedestal(false)
		.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
				BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new)));
		buildings.add(new BuildingSettlement(20, -3)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new))
				.setAllowedBlocks(Blocks.GRASS));
		buildings.add(new BuildingHelicopter(new ResourceLocation(SevenDaysToMine.MODID, "tank_01"), 300, -1)
				.setHasPedestal(false)
				.setAllowedBiomes(Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.LUSH)).stream().toArray(Biome[]::new)));
	}


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
		if (data.isCityNearby(new BlockPos(blockX, 0, blockZ), 22500)
				|| data.isScatteredNearby(new BlockPos(blockX, 128, blockZ), 12321))
			return;
		Building building = getRandomBuilding(rand);
		generateStructure(building, world, rand, blockX, blockZ, 24);

	}

	private void generateEnd(World world, Random rand, int chunkX, int blockZ) {

	}

	private boolean generateStructure(Building building, World world, Random rand, int blockX, int blockZ, int chance) {
		if (rand.nextInt(chance) != 0)
			return false;
		BlockPos pos = Utils.getTopBiomeGroundBlock(new BlockPos(blockX, 64, blockZ), world);
		if(pos.getY() < 0) return false;
		if (building.allowedBlocks == null || building.allowedBlocks.length == 0
				|| Arrays.asList(building.allowedBlocks).contains(world.getBlockState(pos).getBlock())) {

			Biome biome = world.provider.getBiomeForCoords(pos);
			if (building.allowedBiomes == null || building.allowedBiomes.isEmpty()
					|| building.allowedBiomes.contains(biome)) {
				EnumFacing facing = EnumFacing.getHorizontal(rand.nextInt(4));
				boolean mirror = building.canBeMirrored ? rand.nextBoolean() : false;
				BlockPos dimensions = building.getDimensions(world, facing);
				BlockPos end = pos
						.offset(facing, facing.getAxis() == EnumFacing.Axis.Z ? dimensions.getZ() : dimensions.getX())
						.offset(mirror ? facing.rotateY() : facing.rotateYCCW(),
								facing.getAxis() == EnumFacing.Axis.X ? dimensions.getZ() : dimensions.getX());
				end = Utils.getTopBiomeGroundBlock(end, world);
				BlockPos pos2 = new BlockPos(pos.getX(), MathUtils.lerp(pos.getY(), end.getY(), 0.5f), pos.getZ());
				building.generate(world,
						pos2, facing,
						mirror);
				CitySavedData.get(world).addScattered(pos);
				return true;
			}
		}
		return false;
	}
	
	public int getBuildingsCount() {
		return buildings.size();
	}

	public int getBuildingIndex(Building building) {
		return buildings.indexOf(building);
	}

	public Building getBuildingByIndex(int i) {
		return buildings.get(MathHelper.clamp(i, 0, buildings.size()));
	}

	public static Building getRandomBuilding(Random rand) {
		int total = 0;
		for (Building building : buildings) {
			total += building.weight;
		}
		int i = rand.nextInt(total);
		for (Building building : buildings) {
			i -= building.weight;
			if (i <= 0) {
				return building;
			}
		}
		// Failsafe
		return buildings.get(rand.nextInt(buildings.size()));
	}

	public static List<Building> getBuildings() {
		List<Building> list = new ArrayList<Building>(buildings);
		return list;
	}
}
