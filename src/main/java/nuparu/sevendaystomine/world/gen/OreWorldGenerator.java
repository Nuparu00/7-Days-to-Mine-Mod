package nuparu.sevendaystomine.world.gen;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModBlocks;

public class OreWorldGenerator implements IWorldGenerator {

	private final Predicate<IBlockState> stonePredicate = new Predicate<IBlockState>() {
		@Override
		public boolean apply(IBlockState state) {
			return state.getBlock() == net.minecraft.init.Blocks.STONE
					&& state.getValue(BlockStone.VARIANT) != BlockStone.EnumType.ANDESITE_SMOOTH
					&& state.getValue(BlockStone.VARIANT) != BlockStone.EnumType.DIORITE_SMOOTH
					&& state.getValue(BlockStone.VARIANT) != BlockStone.EnumType.GRANITE_SMOOTH;
		}
	};

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		case -1:
			generateNether(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			break;
		case 0:
			generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			break;
		case 1:
			generateEnd(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			break;
		}
	}

	private void generateNether(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
	}

	private void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		addOreSpawn(ModBlocks.ORE_COPPER.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 16, 12, ModConfig.worldGen.copperOreGenerationRate, 10,
				138, stonePredicate);
		addOreSpawn(ModBlocks.ORE_TIN.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 16, 8, ModConfig.worldGen.tinOreGenerationRate, 6, 96,
				stonePredicate);
		addOreSpawn(ModBlocks.ORE_ZINC.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 16, 8, ModConfig.worldGen.zincOreGenerationRate, 0, 90,
				stonePredicate);
		addOreSpawn(ModBlocks.ORE_LEAD.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 16, 5, ModConfig.worldGen.leadOreGenerationRate, 0, 76,
				stonePredicate);
		addOreSpawn(ModBlocks.ORE_POTASSIUM.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 16, 8, ModConfig.worldGen.potassiumOreGenerationRate,
				32, 128, stonePredicate);
		addOreSpawn(ModBlocks.ORE_CINNABAR.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 16, 4, ModConfig.worldGen.cinnabarOreGenerationRate,
				24, 96, stonePredicate);

	}

	private void generateEnd(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {

	}

	private void addOreSpawn(IBlockState block, World world, Random random, int blockXPos, int blockZPos, int maxX,
			int maxZ, int maxVeinSize, int chance, int minY, int maxY, Predicate<IBlockState> blockToSpawnIn) {
		int diffMinMaxY = maxY - minY;
		for (int x = 0; x < chance; x++) {
			int posX = blockXPos + random.nextInt(maxX);
			int posY = minY + random.nextInt(diffMinMaxY);
			int posZ = blockZPos + random.nextInt(maxZ);
			(new WorldGenMinable(block, maxVeinSize, blockToSpawnIn)).generate(world, random,
					new BlockPos(posX, posY, posZ));
		}
	}

}
