package com.nuparu.sevendaystomine.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPickable extends BlockBase {
	public BlockPickable(Material mat) {
		super(mat);
	}
	
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canBlockStay(worldIn, pos))
        {
        	dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 1);
    		worldIn.setBlockToAir(pos);
        }
    }
    
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(!playerIn.isSneaking()) {
		dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 1);
		worldIn.setBlockToAir(pos);
		}
		return true;
	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
    }
	
	public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos.down());
        return state.isBlockNormalCube() && !worldIn.getBlockState(pos.up()).getMaterial().isLiquid();
    }
	
}
