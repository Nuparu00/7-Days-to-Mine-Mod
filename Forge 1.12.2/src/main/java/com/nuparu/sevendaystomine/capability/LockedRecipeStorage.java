package com.nuparu.sevendaystomine.capability;

import net.minecraftforge.common.capabilities.Capability.IStorage;

import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.util.EnumFacing;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;

import java.util.Map;

public class LockedRecipeStorage implements IStorage<ILockedRecipe> {

	@Override
	public NBTBase writeNBT(Capability<ILockedRecipe> capability, ILockedRecipe instance, EnumFacing side)

	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for (Map.Entry<String, Boolean> entry : instance.getRecipes().entrySet()) {
			NBTTagCompound tag = new NBTTagCompound();
			nbt.setString("name", entry.getKey());
			nbt.setBoolean("lock", entry.getValue());
			list.appendTag(tag);
		}
		nbt.setTag("recipes", list);
		return nbt;
	}

	@Override
	public void readNBT(Capability<ILockedRecipe> capability, ILockedRecipe instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		NBTTagList list = (NBTTagList) tag.getTag("recipes");
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound value = (NBTTagCompound) list.get(i);
			instance.setLock(value.getString("name"), value.getBoolean("lock"));
		}
	}
}