package com.nuparu.sevendaystomine.tileentity;

import com.nuparu.sevendaystomine.inventory.IContainerCallbacks;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityItemHandler<INVENTORY extends IItemHandler & INBTSerializable<NBTTagCompound>> extends TileEntity implements IContainerCallbacks, ILootContainer{

	protected final INVENTORY inventory = createInventory();
	
	protected abstract INVENTORY createInventory();
	
	public abstract Container createContainer(EntityPlayer player);
	
	public NonNullList<ItemStack> getDrops() {
		return Utils.dropItemHandlerContents(inventory, getWorld().rand);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag("ItemHandler"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("ItemHandler", inventory.serializeNBT());
		return compound;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}

		return super.getCapability(capability, facing);
	}
	
	public <INVENTORY extends IItemHandler & INBTSerializable<NBTTagCompound>> INVENTORY getInventory(){
		return (INVENTORY) inventory;
	}
}
