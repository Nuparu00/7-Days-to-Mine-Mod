package com.nuparu.sevendaystomine.inventory.container;

import com.nuparu.sevendaystomine.inventory.IContainerCallbacks;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.tileentity.TileEntityGasGenerator;
import com.nuparu.sevendaystomine.tileentity.TileEntityGeneratorBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGenerator extends Container {

	public final TileEntityGeneratorBase tileEntity;
	public final IContainerCallbacks callbacks;
	
	private int voltage;
	private int burnTime;
	private int currentBurnTime;

	public ContainerGenerator(IItemHandlerNameable playerInventoryWrapper, ItemStackHandler inventory,
			EntityPlayer player,IContainerCallbacks containerCallbacks) {
		tileEntity = (TileEntityGeneratorBase)containerCallbacks;
		callbacks = containerCallbacks;
		callbacks.onContainerOpened(player);
		this.addSlotToContainer(new SlotItemHandler(inventory, 0, 29, 60));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(
						new SlotItemHandler(playerInventoryWrapper, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new SlotItemHandler(playerInventoryWrapper, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tileEntity.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < 9) {
				if (!this.mergeItemStack(itemstack1, 9, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendWindowProperty(this, 2, tileEntity.getField(2));
		listener.sendWindowProperty(this, 0, tileEntity.getField(0));
		listener.sendWindowProperty(this, 1, tileEntity.getField(1));
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icontainerlistener = this.listeners.get(i);


			if (this.burnTime != tileEntity.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, tileEntity.getField(0));
			}

			if (this.currentBurnTime != tileEntity.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, tileEntity.getField(1));
			}

			if (this.voltage != tileEntity.getField(2)) {
				icontainerlistener.sendWindowProperty(this, 2, tileEntity.getField(2));
			}
		}

		this.burnTime = tileEntity.getField(0);
		this.currentBurnTime = tileEntity.getField(1);
		this.voltage = tileEntity.getField(2);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		tileEntity.setField(id, data);
	}

}
