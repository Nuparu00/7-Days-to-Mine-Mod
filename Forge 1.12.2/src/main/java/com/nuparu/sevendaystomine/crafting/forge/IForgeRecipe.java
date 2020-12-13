package com.nuparu.sevendaystomine.crafting.forge;

import java.util.List;

import com.nuparu.sevendaystomine.tileentity.TileEntityForge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IForgeRecipe {
	 boolean matches(TileEntityForge inv, World worldIn);
	 ItemStack getResult();
	 public ItemStack getOutput(TileEntityForge tileEntity);
	 public ItemStack getMold();
	 public List<ItemStack> getIngredients();
	 public void consumeInput(TileEntityForge tileEntity);
	 public int intGetXP(EntityPlayer player);
	 
}