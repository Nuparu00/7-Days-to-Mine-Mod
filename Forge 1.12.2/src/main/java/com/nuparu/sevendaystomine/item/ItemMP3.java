package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemMP3 extends Item {

	public ItemMP3() {
		this.setMaxStackSize(1);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (!playerIn.isSneaking()) {
			SevenDaysToMine.proxy.openClientOnlyGui(0, stack);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}
}
