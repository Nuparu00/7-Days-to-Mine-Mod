package com.nuparu.sevendaystomine.world.gen;

import java.util.Random;

import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenBush;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenCinderBlock;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenRock;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenSmallRock;
import com.nuparu.sevendaystomine.world.gen.feature.WorldGenStick;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class SmallFeatureWorldGenerator implements IWorldGenerator {

	WorldGenerator smallRock = new WorldGenSmallRock();
	WorldGenerator rock = new WorldGenRock();
	WorldGenerator stick = new WorldGenStick();
	WorldGenerator bush = new WorldGenBush();
	WorldGenerator cinder = new WorldGenCinderBlock();

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
		if (world.getWorldType() == WorldType.FLAT) {
			return;
		}
		for (int i = 0; i < MathUtils.getIntInRange(rand, ModConfig.worldGen.smallRockGenerationRateMin, ModConfig.worldGen.smallRockGenerationRateMax+1); i++) {
			int randX = blockX + rand.nextInt(16);
			int randZ = blockZ + rand.nextInt(16);
			smallRock.generate(world, rand, new BlockPos(randX, 24, randZ));
		}
		for (int i = 0; i < MathUtils.getIntInRange(rand, ModConfig.worldGen.largeRockGenerationRateMin, ModConfig.worldGen.largeRockGenerationRateMax+1); i++) {
			int randX = blockX + rand.nextInt(16);
			int randZ = blockZ + rand.nextInt(16);
			rock.generate(world, rand, new BlockPos(randX, 24, randZ));
		}
		for (int i = 0; i < MathUtils.getIntInRange(rand, ModConfig.worldGen.stickGenerationRateMin, ModConfig.worldGen.stickGenerationRateMax+1); i++) {
			int randX = blockX + rand.nextInt(16);
			int randZ = blockZ + rand.nextInt(16);
			stick.generate(world, rand, new BlockPos(randX, 24, randZ));
		}
		for (int i = 0; i < MathUtils.getIntInRange(rand, ModConfig.worldGen.deadBushGenerationRateMin, ModConfig.worldGen.deadBushGenerationRateMax+1); i++) {
			int randX = blockX + rand.nextInt(16);
			int randZ = blockZ + rand.nextInt(16);
			bush.generate(world, rand, new BlockPos(randX, 24, randZ));
		}
		for (int i = 0; i < MathUtils.getIntInRange(rand, ModConfig.worldGen.cinderBlockGenerationRateMin, ModConfig.worldGen.cinderBlockGenerationRateMax+1); i++) {
			int randX = blockX + rand.nextInt(16);
			int randZ = blockZ + rand.nextInt(16);
			cinder.generate(world, rand, new BlockPos(randX, 24, randZ));
		}
	}

	private void generateEnd(World world, Random rand, int blockX, int blockZ) {

	}
}
