package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModBlocks;

public class BlockReinforcedConcreteWet extends BlockBase {

	public BlockReinforcedConcreteWet() {
		super(Material.IRON);
		setHardness(7.5F);
		setResistance(5.0F);
		setHarvestLevel("pickaxe", 2);
		setTickRandomly(true);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
		if (world.isDaytime() && random.nextBoolean()) {
			world.setBlockState(pos, ModBlocks.REINFORCED_CONCRETE.getDefaultState(), 3);
		}

	}
}
