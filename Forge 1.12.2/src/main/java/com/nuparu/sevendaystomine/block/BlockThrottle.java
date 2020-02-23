package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockThrottle extends BlockArmchair {
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			return Utils.mountBlock(worldIn, pos, playerIn,0.25d);
		}
		return false;
	}
}
