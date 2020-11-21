package com.nuparu.sevendaystomine.crafting.campfire;

import java.util.List;

import com.nuparu.sevendaystomine.tileentity.TileEntityCampfire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ICampfireRecipe {
	 boolean matches(TileEntityCampfire inv, World worldIn);
	 ItemStack getResult();
	 public ItemStack getOutput(TileEntityCampfire tileEntity);
	 public ItemStack getPot();
	 public List<ItemStack> getIngredients();
	 public void consumeInput(TileEntityCampfire tileEntity);
	 public int intGetXP(EntityPlayer player);
	 
}
