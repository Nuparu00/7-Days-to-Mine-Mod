package com.nuparu.sevendaystomine.block;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;

public interface ISalvageable {

	public List<ItemStack> getItems(World world, BlockPos pos, IBlockState oldState, EntityPlayer player);

	public SoundEvent getSound();
	
	public float getUpgradeRate(World world, BlockPos pos, IBlockState state, EntityPlayer player);

	public void onSalvage(World world, BlockPos pos, IBlockState oldState);

}