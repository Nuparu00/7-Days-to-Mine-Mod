package nuparu.sevendaystomine.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import nuparu.sevendaystomine.block.BlockFruitBush;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.biome.BiomeWastelandBase;

public class WorldGenBlueberry extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos) {
		Biome biome = worldIn.getBiome(pos);
		if(biome instanceof BiomeWastelandBase) return false;
		pos = pos.add(8, 0, 8);
		IBlockState toPlace = (rand.nextBoolean() ? ModBlocks.BANEBERRY_PLANT : ModBlocks.BLUEBERRY_PLANT).getDefaultState().withProperty(BlockFruitBush.AGE,
				rand.nextInt(8));
		int y = Utils.getTopSolidGroundHeight(pos, worldIn);

		BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
		IBlockState stateToReplace = worldIn.getBlockState(blockpos);
		Block toReplace = stateToReplace.getBlock();
		
		if(toReplace != Blocks.AIR) return false;

		IBlockState underState = worldIn.getBlockState(blockpos.down());

		if (stateToReplace.getMaterial().isReplaceable()
				&& ((BlockFruitBush) ModBlocks.BLUEBERRY_PLANT).canSustainBush(underState)) {
			worldIn.setBlockState(blockpos, toPlace, 2);
			return true;
		}
		return false;
	}

}
