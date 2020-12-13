package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemScrewdriver extends ItemQuality {

	public ItemScrewdriver() {
		this.setMaxDamage(8);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			TileEntity TE = worldIn.getTileEntity(pos);
			if (TE != null) {
				if(TE instanceof TileEntityComputer) {
					itemstack.damageItem(1, player);
					player.openGui(SevenDaysToMine.instance, 7, worldIn, pos.getX(), pos.getY(), pos.getZ());
				}
			}
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		int i = 0;
		if (stack.getTagCompound() != null) {
			i = getQuality(stack);
		}
		return super.getMaxDamage(stack) + i;
	}
}
