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

public class ItemCannedFood extends ItemFood {

	public ItemCannedFood(int amount, boolean isWolfFood, int maxBites) {
		super(amount, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxBites);
	}

	public ItemCannedFood(int amount, float saturation, boolean isWolfFood, int maxBites) {
		super(amount, saturation, isWolfFood);
		this.setContainerItem(ModItems.EMPTY_CAN);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxBites);
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
		super.onFoodEaten(stack, worldIn, player);
		if(stack.getItemDamage() == stack.getMaxDamage() && getContainerItem() != null) {
			player.addItemStackToInventory(new ItemStack(getContainerItem()));
		}
    }
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().addStats(this, stack);
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }
    	stack.damageItem(1, entityLiving);
        return stack;
    }

}
