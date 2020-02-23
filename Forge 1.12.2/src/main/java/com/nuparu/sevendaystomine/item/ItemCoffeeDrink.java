package com.nuparu.sevendaystomine.item;

import java.util.ArrayList;

import com.nuparu.sevendaystomine.potions.Potions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemCoffeeDrink extends ItemDrink {

	public ItemCoffeeDrink(int amount, int thirst, int stamina) {
		super(amount, thirst, stamina);
	}

	public ItemCoffeeDrink(int amount, float saturation, int thirst, int stamina) {
		super(amount, saturation, thirst, stamina);
	}
	
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		PotionEffect effect_new = new PotionEffect(Potions.caffeineBuzz, 6000, 4, false, false);
		effect_new.setCurativeItems(new ArrayList<ItemStack>());
		player.addPotionEffect(effect_new);
		
	}

}
