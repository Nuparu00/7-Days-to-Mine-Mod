package nuparu.sevendaystomine.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.init.ModBiomes;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.Utils;

public class WorldGenCinderBlock extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos pos) {
		pos = pos.add(8, 0, 8);
		if (worldIn.provider.getBiomeForCoords(pos) != ModBiomes.WASTELAND)
			return false;
		IBlockState toPlace = nuparu.sevendaystomine.init.ModBlocks.CINDER.getDefaultState()
				.withProperty(BlockHorizontalBase.FACING, EnumFacing.getHorizontal(rand.nextInt(4)));
		int y = Utils.getTopSolidGroundHeight(pos, worldIn);

		BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
		IBlockState state = worldIn.getBlockState(blockpos);
		Block toReplace = state.getBlock();
		if ((toReplace == Blocks.AIR || state.getMaterial() == Material.PLANTS) && worldIn.getBlockState(blockpos.down()).getBlock() == ModBlocks.DRY_GROUND) {
			worldIn.setBlockState(blockpos, toPlace, 2);
		}
		return false;
	}

}
