package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTelevisionBroken extends BlockHorizontalBase {

	public BlockTelevisionBroken() {
		super(Material.ROCK);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setHardness(5);
		this.setResistance(4F);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

}
