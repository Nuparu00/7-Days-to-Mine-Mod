package com.nuparu.sevendaystomine.world.gen;

import java.util.Random;

import com.nuparu.sevendaystomine.world.gen.feature.WorldGenRock;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenSmallRock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class RockWorldGenerator implements IWorldGenerator {

	WorldGenerator generator = new WorldGenRock();
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
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
		if(world.getWorldType()==WorldType.FLAT) {
			return;
		}
		int MIN = 0;
		int MAX = 4;
		int num = MIN + rand.nextInt(MAX - MIN);
		for (int i = 0; i < num; i++) {
			int randX = blockX + rand.nextInt(16);
			int randZ = blockZ + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(randX, 24, randZ));

		}
	}

	private void generateEnd(World world, Random rand, int blockX, int blockZ) {

	}
}
