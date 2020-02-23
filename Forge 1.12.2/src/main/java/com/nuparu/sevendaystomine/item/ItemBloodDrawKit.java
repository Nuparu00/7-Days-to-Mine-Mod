package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.potions.Potions;
import com.nuparu.sevendaystomine.util.DamageSources;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBloodDrawKit extends Item {

	public ItemBloodDrawKit() {
		setMaxDamage(0);
		maxStackSize = 16;
		setCreativeTab(SevenDaysToMine.TAB_MEDICINE);
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
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			int dur = this.getMaxItemUseDuration(stack) - timeLeft;
			if (dur <= this.getMaxItemUseDuration(stack) * 0.1f) {
				ItemStack bloodBag = new ItemStack(ModItems.BLOOD_BAG);
				if (!entityplayer.inventory.addItemStackToInventory(bloodBag)) {
					entityplayer.dropItem(bloodBag, false);
				}
				entityplayer.attackEntityFrom(DamageSources.bleeding, 4);
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
}
