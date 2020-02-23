package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemAntibiotics extends ItemBandage{
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			int dur = this.getMaxItemUseDuration(stack) - timeLeft;
			if (dur <= this.getMaxItemUseDuration(stack) * 0.1f) {
				if (!entityplayer.capabilities.isCreativeMode) {
					stack.shrink(1);
					if (stack.isEmpty()) {
						entityplayer.inventory.deleteStack(stack);
					}
				}
				
			}
		}
	}
}
