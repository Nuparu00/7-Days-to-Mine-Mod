package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemMurkyWater extends ItemDrink {

	public ItemMurkyWater(int amount, int thirst, int stamina) {
		super(amount, thirst, stamina);
	}

	public ItemMurkyWater(int amount, float saturation, int thirst, int stamina) {
		super(amount, saturation, thirst, stamina);
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		if (worldIn.rand.nextInt(5) == 0) {
			player.addPotionEffect((new PotionEffect(Potions.dysentery, 48000, 4, false, false)));
		}

	}

}
