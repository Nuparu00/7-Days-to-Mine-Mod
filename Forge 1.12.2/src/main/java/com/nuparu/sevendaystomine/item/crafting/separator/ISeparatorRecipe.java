package com.nuparu.sevendaystomine.item.crafting.separator;

import java.util.List;

import com.nuparu.sevendaystomine.tileentity.TileEntitySeparator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISeparatorRecipe {
	 boolean matches(TileEntitySeparator inv, World worldIn);
	 public List<ItemStack> getResult();
	 public List<ItemStack> getOutputs(TileEntitySeparator tileEntity);
	 public ItemStack getIngredient();
	 public void consumeInput(TileEntitySeparator tileEntity);
	 public int intGetXP(EntityPlayer player);
}
