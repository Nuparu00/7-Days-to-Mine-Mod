package com.nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBoards extends BlockHorizontalBase {

	public BlockBoards() {
		super(Material.WOOD);
		this.setResistance(6f);
		this.setHardness(7f);
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
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumFacing) blockState.getValue(FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, 1F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1F, 0.125F);
		case WEST:
			return new AxisAlignedBB(0.875F, 0.0F, 0F, 1F, 1F, 1F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.125F, 1F, 1F);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, 1F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1F, 0.125F);
		case WEST:
			return new AxisAlignedBB(0.875F, 0.0F, 0F, 1F, 1F, 1F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.125F, 1F, 1F);
		}
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

}
