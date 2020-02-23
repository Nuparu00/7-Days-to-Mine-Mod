package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCannedFood extends ItemFood {

	public ItemCannedFood(int amount, boolean isWolfFood) {
		super(amount, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
	}

	public ItemCannedFood(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
	}
	
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
		if(getContainerItem() != null) {
			player.addItemStackToInventory(new ItemStack(getContainerItem()));
		}
    }

}
