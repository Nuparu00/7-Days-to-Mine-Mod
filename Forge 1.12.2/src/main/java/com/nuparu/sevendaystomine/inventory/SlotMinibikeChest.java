package com.nuparu.sevendaystomine.inventory;

import javax.annotation.Nonnull;

import com.nuparu.sevendaystomine.inventory.container.ContainerMinibike;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotMinibikeChest extends SlotItemHandler {

	protected Item validItem;
	protected ContainerMinibike container;

	public SlotMinibikeChest(IItemHandler inventoryIn, int index, int xPosition, int yPosition, Item validItem,
			ContainerMinibike container) {
		super(inventoryIn, index, xPosition, yPosition);
		this.validItem = validItem;
		this.container = container;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == validItem;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount) {

	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		if (this.getHasStack()) {
			if (this.getStack().getItem() == Item.getItemFromBlock(Blocks.CHEST)) {
				if (!container.addedChest) {
					container.bindChest();
				}
			}
		} else {
			if (container.addedChest) {
				container.unbindChest();
			}
		}
	}
}
