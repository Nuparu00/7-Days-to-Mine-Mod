package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.ExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHoney extends ItemDrink {

	public ItemHoney() {
		super(5, 0, 300, 150);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		int time = iep.getInfectionTime();
		if (player instanceof EntityPlayerMP && time < ExtendedPlayer.INFECTION_STAGE_TWO_START) {
			player.removePotionEffect(Potions.infection);
			iep.setInfectionTime(-1);
		}
	}
}
