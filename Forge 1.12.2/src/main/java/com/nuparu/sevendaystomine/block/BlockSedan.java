package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.init.ModBlocks;

import net.minecraft.block.state.IBlockState;

public class BlockSedan extends BlockCar{

	public BlockSedan() {
		super(1,2,2);
	}

	@Override
	public IBlockState getState() {
		return ModBlocks.SEDAN.getDefaultState();
	}

}
