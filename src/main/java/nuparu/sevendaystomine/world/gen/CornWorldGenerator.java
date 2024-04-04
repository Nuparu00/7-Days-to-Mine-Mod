package nuparu.sevendaystomine.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.gen.feature.WorldGenCorn;

public class CornWorldGenerator implements IWorldGenerator {

	WorldGenerator generator = new WorldGenCorn();
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
		int num = MathUtils.getIntInRange(rand, ModConfig.worldGen.cornGenerationRateMin, ModConfig.worldGen.cornGenerationRateMax+1);
		for (int i = 0; i < num; i++) {
			int randX = blockX + rand.nextInt(16);
			int randZ = blockZ + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(randX, 24, randZ));

		}
	}

	private void generateEnd(World world, Random rand, int blockX, int blockZ) {

	}

	public static int getGroundFromAbove(World world, int x, int z) {
		int y = 255;
		boolean foundGround = false;
		while (!foundGround && y-- >= 0) {
			Block blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = blockAt == Blocks.FARMLAND || blockAt == Blocks.GRASS;
		}

		return y;
	}

}
