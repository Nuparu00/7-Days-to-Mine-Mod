package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTea extends ItemDrink {

	public ItemTea(int amount, int thirst, int stamina) {
		super(amount, thirst, stamina);
	}

	public ItemTea(int amount, float saturation, int thirst, int stamina) {
		super(amount, saturation, thirst, stamina);
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		player.removePotionEffect(Potions.dysentery);
	}

}
