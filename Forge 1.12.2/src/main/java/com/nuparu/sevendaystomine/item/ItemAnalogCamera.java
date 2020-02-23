package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemAnalogCamera extends Item {

	public ItemAnalogCamera() {
		this.setMaxStackSize(1);
		this.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);

	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			int dur = this.getMaxItemUseDuration(stack) - timeLeft;
			if (dur <= this.getMaxItemUseDuration(stack) * 0.1f) {
				if (worldIn.isRemote) {
					SevenDaysToMine.proxy.tryToTakePhoto();
				}
			}
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 82000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemStack) {
		return EnumAction.BOW;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }
}
