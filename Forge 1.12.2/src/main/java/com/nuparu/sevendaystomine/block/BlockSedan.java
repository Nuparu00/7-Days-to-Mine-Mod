package com.nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSedan extends BlockCar {

	public BlockSedan() {
		super(new byte[][][] {{{1,1,1},{1,1,1},{1,1,1},{1,1,1},{1,1,1}},{{0,0,0},{1,1,1},{1,1,1},{1,1,1},{0,0,0}}});
		CityHelper.cars.add(this);
		setHardness(20.0F);
		setResistance(5.0F);
	}


	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		double height = 1;
		double width = 1;
		double length = 1;

		EnumFacing facing = state.getValue(BlockCar.FACING);
		if (world.getBlockState(pos.down()).getBlock() instanceof BlockSedan) {
			height = 0.5d;
			IBlockState front = world.getBlockState(pos.offset(facing,1));
			IBlockState back = world.getBlockState(pos.offset(facing,-1));
			if(!(front.getBlock() instanceof BlockCar)) {
				if(facing.getAxis() == EnumFacing.Axis.X) {
					if(facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
						width = 0.5f;
					}
					else {
						width = -0.5f;
						
					}
				}
				else {
					if(facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
						length = 0.5f;
					}
					else {
						length = -0.5f;
					}
				}
			}
			
			else if(!(back.getBlock() instanceof BlockCar)) {
				if(facing.getAxis() == EnumFacing.Axis.X) {
					if(facing.getAxisDirection() != EnumFacing.AxisDirection.POSITIVE) {
						width = 0.5f;
					}
					else {
						width = -0.5f;
						
					}
				}
				else {
					if(facing.getAxisDirection() != EnumFacing.AxisDirection.POSITIVE) {
						length = 0.5f;
					}
					else {
						length = -0.5f;
					}
				}
			}
		}
		else if(!(world.getBlockState(pos.up()).getBlock() instanceof BlockSedan)) {
			height = 0.65f;
		}
		EnumFacing rightDirection = facing.rotateY();
		IBlockState right = world.getBlockState(pos.offset(rightDirection,1));
		EnumFacing leftDirection = facing.rotateYCCW();
		IBlockState left = world.getBlockState(pos.offset(leftDirection,1));
		if(!(right.getBlock() instanceof BlockCar)) {
			if(rightDirection.getAxis() == EnumFacing.Axis.X) {
				if(rightDirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
					width = 0.5f;
				}
				else {
					width = -0.5f;
					
				}
			}
			else {
				if(rightDirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
					length = 0.5f;
				}
				else {
					length = -0.5f;
				}
			}
		}
		
		if(!(left.getBlock() instanceof BlockCar)) {
			if(leftDirection.getAxis() == EnumFacing.Axis.X) {
				if(leftDirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
					width = 0.5f;
				}
				else {
					width = -0.5f;
					
				}
			}
			else {
				if(leftDirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
					length = 0.5f;
				}
				else {
					length = -0.5f;
				}
			}
		}
		return new AxisAlignedBB(width > 0 ? 0 : -width, 0, length > 0 ? 0 : -length, width > 0 ? width : 1, height, length > 0 ? length : 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.getCollisionBoundingBox(state, world, pos);
	}

}
