package com.nuparu.sevendaystomine.item;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFoodBitable extends ItemFood {

	public ItemFoodBitable(int amount, boolean isWolfFood, int maxBites) {
		super(amount, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxBites);
	}

	public ItemFoodBitable(int amount, float saturation, boolean isWolfFood, int maxBites) {
		super(amount, saturation, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxBites);
	}
}
