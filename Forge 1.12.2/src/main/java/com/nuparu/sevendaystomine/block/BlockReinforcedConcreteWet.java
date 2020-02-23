package com.nuparu.sevendaystomine.block;

import java.util.Random;

import com.nuparu.sevendaystomine.init.ModBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReinforcedConcreteWet extends BlockBase {

	public BlockReinforcedConcreteWet() {
		super(Material.IRON);
		setHardness(10.0F);
		setResistance(5.0F);
		setHarvestLevel("pickaxe", 2);
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
	public int tickRate(World world) {
		return 6000;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		if (world.isDaytime()) {
			world.setBlockState(pos, ModBlocks.REINFORCED_CONCRETE.getDefaultState(), 3);
		}

	}
}
