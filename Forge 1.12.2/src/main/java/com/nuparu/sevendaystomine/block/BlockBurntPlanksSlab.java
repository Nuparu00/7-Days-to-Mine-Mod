package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.item.ItemSlabBase;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockBurntPlanksSlab extends BlockSlabBase {

	public BlockBurntPlanksSlab(boolean isdouble) {
		super(Material.WOOD, isdouble);
		setSoundType(SoundType.WOOD);
		if (!isdouble) {
			setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		}
		Blocks.FIRE.setFireInfo(this, 5, 20);
	}

	@Override
	public ItemBlock createItemBlock() {
		if (this.isDouble())
			return null;
		return new ItemSlabBase(this, (BlockSlab) ModBlocks.BURNT_PLANKS_SLAB,
				(BlockSlab) ModBlocks.BURNT_PLANKS_SLAB_DOUBLE);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return ModBlocks.BURNT_PLANKS_SLAB.getItem(world, pos, state);
	}
}