package com.nuparu.sevendaystomine.world.gen;

import java.util.Random;

import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.util.OpenSimplexNoise;
import com.nuparu.sevendaystomine.util.SimplexNoise;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenRock;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenSmallRock;

import it.unimi.dsi.fastutil.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class RoadWorldGenerator implements IWorldGenerator {

	private SimplexNoise noise;
	private static final int FEATURE_SIZE = 1;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int blockX = chunkX << 4;
		int blockZ = chunkZ << 4;
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(world, random, blockX, blockZ);
			break;
		case 0:
			generateOverworld(world, random, blockX, blockZ);
			break;
		case 1:
			generateEnd(world, random, blockX, blockZ);
			break;
		}
	}

	private void generateNether(World world, Random rand, int blockX, int blockZ) {
	}

	private void generateOverworld(World world, Random rand, int blockX, int blockZ) {
		if (world.getWorldType() == WorldType.FLAT)
			return;

		if (noise == null || noise.seed != world.getSeed()) {
			noise = new SimplexNoise(world.getSeed());
		}
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				BlockPos posA = new BlockPos(blockX + x, 180, rand.nextDouble());
				BlockPos pos = getTopSolidGroundBlock(posA, world);
				double value = Math.abs(getNoiseValue((int) ((blockX + x)), (int) ((blockZ + y)), 0));

				if (value < 0.005d) {
					Chunk chunk = world.getChunkFromBlockCoords(pos);
					world.setBlockState(pos.down(), ModBlocks.ASPHALT.getDefaultState(), 3);
				}
			}
		}
	}

	private void generateEnd(World world, Random rand, int blockX, int blockZ) {

	}

	public double getNoiseValue(int x, int y, int z) {
		double q1 = noise.getNoise(x, y, z);
		/*
		 * double q2 = noise.getNoise(x + 1.3, y + 0.7, 0.0);
		 * 
		 * double r1 = noise.getNoise(x + 1 * q1 + 1.7, y + 1 * q2 + 9.2, 0.0); double
		 * r2 = noise.getNoise(x + 1 * q1 + 8.3, y + 1 * q2 + 2.8, 0.0);
		 * 
		 * double value = noise.getNoise(x + 2 * q1, y + 2 * q2, z);
		 * 
		 */
		return q1;
	}

	public static BlockPos getTopSolidGroundBlock(BlockPos pos, World world) {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		BlockPos blockpos;
		BlockPos blockpos1;

		for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos
				.getY() >= 0; blockpos = blockpos1) {
			blockpos1 = blockpos.down();
			IBlockState block = chunk.getBlockState(blockpos1);

			if (block.getMaterial().isLiquid()) {
				break;
			}
			if (block.getMaterial().blocksMovement() && !block.getBlock().isLeaves(block, world, blockpos1)
					&& !block.getBlock().isFoliage(world, blockpos1) && block.getMaterial() != Material.SNOW) {
				break;
			}
		}

		return blockpos;
	}
}
