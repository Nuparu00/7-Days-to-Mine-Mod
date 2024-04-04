package nuparu.sevendaystomine.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import nuparu.sevendaystomine.util.Utils;

public class WorldGenBush extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos) {
		pos = pos.add(8, 0, 8);
		Biome biome = worldIn.provider.getBiomeForCoords(pos);
		IBlockState toPlace = nuparu.sevendaystomine.init.ModBlocks.BUSH.getDefaultState();
		int y = Utils.getTopSolidGroundHeight(pos, worldIn);

		BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
		IBlockState stateToReplace = worldIn.getBlockState(blockpos);
		Block toReplace = stateToReplace.getBlock();
		
		if(toReplace != Blocks.AIR) return false;
		
		IBlockState underState = worldIn.getBlockState(blockpos.down());
		
		if(biome.topBlock.getBlock() == underState.getBlock() &&  stateToReplace.getMaterial().isReplaceable()
				&& underState.isSideSolid(worldIn, blockpos.down(), EnumFacing.UP)) {
			worldIn.setBlockState(blockpos, toPlace, 2);
		}
		return false;
	}
}
