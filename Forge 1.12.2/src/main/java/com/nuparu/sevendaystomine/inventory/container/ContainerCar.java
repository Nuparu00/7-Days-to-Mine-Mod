package com.nuparu.sevendaystomine.inventory.container;

import java.util.ArrayList;
import java.util.List;

import com.nuparu.sevendaystomine.entity.EntityCar;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.inventory.SlotMinibike;
import com.nuparu.sevendaystomine.inventory.SlotMinibikeChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCar extends Container {

	protected EntityCar car;
	protected InventoryPlayer playerInventory;
	protected ItemStackHandler inventory;
	
	public boolean addedChest = false;
	public ArrayList<Slot> chestSlots = new ArrayList<Slot>();
	
	public ContainerCar(InventoryPlayer playerInventory, EntityCar car) {
		this.playerInventory = playerInventory;
		this.car = car;

		inventory = car.getInventory();

		this.addSlotToContainer(new SlotMinibike(inventory, 0, 8, 8, ModItems.MINIBIKE_HANDLES));
		this.addSlotToContainer(new SlotMinibike(inventory, 1, 8, 26, Item.getItemFromBlock(ModBlocks.WHEELS)));
		this.addSlotToContainer(new SlotMinibike(inventory, 2, 8, 44, ModItems.MINIBIKE_SEAT));

		this.addSlotToContainer(new SlotMinibike(inventory, 3, 152, 8, ModItems.CAR_BATTERY));
		this.addSlotToContainer(new SlotMinibike(inventory, 4, 152, 26, ModItems.SMALL_ENGINE));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
		bindChest();
	}

	public void bindChest() {
			addedChest = true;
			int l = 0;
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {

					Slot slot = new SlotItemHandler(inventory, 6 + l, 180 + j * 18, 14 + i * 18);
					this.chestSlots.add(slot);
					this.addSlotToContainer(slot);
					l++;
				}
			}
	}
	
	public void unbindChest() {
	
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.car.getDistance(playerIn) <= 4;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			Item item = itemstack.getItem();

			if (index < 7) {
				if (!this.mergeItemStack(itemstack1, 7, 41, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
				if (!car.world.isRemote) {
					car.updateInventory();
				}
			} else {
				if (item == ModItems.SMALL_ENGINE) {
					if (!this.mergeItemStack(itemstack1, 0, 6, true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}
				if (item == ModItems.MINIBIKE_HANDLES) {
					if (!this.mergeItemStack(itemstack1, 0, 6, true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}
				if (item == ModItems.MINIBIKE_SEAT) {
					if (!this.mergeItemStack(itemstack1, 0, 6, true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}
				if (item == ModItems.CAR_BATTERY) {
					if (!this.mergeItemStack(itemstack1, 0, 6, true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}
				if (item == Item.getItemFromBlock(ModBlocks.WHEELS)) {
					if (!this.mergeItemStack(itemstack1, 0, 6, true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}
				if (item == Item.getItemFromBlock(Blocks.CHEST)) {
					if (!this.mergeItemStack(itemstack1, 0, 6, true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}

				if (index <= 32) {
					if (!this.mergeItemStack(itemstack1, 33, 41, true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				} else if (index <= 41) {
					if (!this.mergeItemStack(itemstack1, 6, 32, true)) {
						return ItemStack.EMPTY;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}
			}

		}
		return ItemStack.EMPTY;
	}

}
