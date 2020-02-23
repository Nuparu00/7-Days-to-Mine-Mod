package com.nuparu.sevendaystomine.capability;

import net.minecraftforge.common.capabilities.Capability.IStorage;

import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.util.EnumFacing;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;

public class ExtendedInventoryStorage implements IStorage<IItemHandlerExtended> {

	@Override
	public NBTBase writeNBT(Capability<IItemHandlerExtended> capability, IItemHandlerExtended instance, EnumFacing side)
	{
		NBTTagCompound nbt = instance.serializeNBT();
		return nbt;
	}

	@Override
	public void readNBT(Capability<IItemHandlerExtended> capability, IItemHandlerExtended instance, EnumFacing side,
			NBTBase nbt) {
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.deserializeNBT(tag);
	}
}