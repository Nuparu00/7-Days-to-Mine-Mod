package com.nuparu.sevendaystomine.world.gen;

import java.util.Arrays;
import java.util.Random;

import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedChunk;
import com.nuparu.sevendaystomine.init.ModBiomes;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.city.City;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenLookoutBurnt;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenStick;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class StructureGenerator implements IWorldGenerator {

	public static final WorldGenerator LOOKOUT_BURNT = new WorldGenLookoutBurnt();

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
		generateStructure(LOOKOUT_BURNT, world, rand, chunkX, chunkZ, 314, null, ModBiomes.WASTELAND,
				ModBiomes.BURNT_FOREST);
	}

	private void generateEnd(World world, Random rand, int chunkX, int blockZ) {

	}

	private void generateStructure(WorldGenerator generator, World world, Random rand, int chunkX, int chunkZ,
			int chance, Block top, Biome... biomes) {
		if (rand.nextInt(chance) == 0) {
			int blockX = chunkX * 16;
			int blockZ = chunkZ * 16;
			BlockPos pos = Utils.getTopSolidGroundBlock(new BlockPos(blockX, 64, blockZ), world);

			Biome biome = world.provider.getBiomeForCoords(pos);
			if (biomes == null || biomes.length == 0 || Arrays.asList(biomes).contains(biome)) {
				generator.generate(world, rand, pos);
			}
		}
	}

}
