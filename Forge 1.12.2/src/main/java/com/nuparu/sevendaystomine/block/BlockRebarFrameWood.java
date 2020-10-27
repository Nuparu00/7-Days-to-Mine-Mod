package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.advancements.ModTriggers;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRebarFrameWood extends BlockBase {

	public BlockRebarFrameWood() {
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(2.33f);
		setResistance(10f);
		setHarvestLevel("pickaxe", 1);
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if (stack.getItem() == ModItems.CONCRETE_MIX) {
			worldIn.setBlockState(pos, ModBlocks.REINFORCED_CONCRETE_WET.getDefaultState());
			if (!worldIn.isRemote) {
				ModTriggers.BLOCK_UPGRADE.trigger((EntityPlayerMP) playerIn, ModBlocks.REINFORCED_CONCRETE_WET.getDefaultState());
			}
			playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
			if (!playerIn.isCreative()) {
				stack.shrink(1);
				if (stack.getCount() <= 0) {
					playerIn.setHeldItem(hand, ItemStack.EMPTY);
				}
			}
		}
		return true;
	}
}
