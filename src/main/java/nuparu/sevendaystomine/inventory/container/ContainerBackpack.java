package nuparu.sevendaystomine.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.block.BlockSafe;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.item.ItemBackpack;

public class ContainerBackpack extends Container {

	public final IItemHandlerExtended backpack;
	public final IItemHandlerNameable playerInventory;

	public ContainerBackpack(IItemHandlerNameable playerInventory, IItemHandlerExtended backpack) {
		this.backpack = backpack;
		this.playerInventory = playerInventory;

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new SlotItemHandler(backpack, j + i * 3, 8 + (j + 3) * 18, 18 + i * 18) {
					public boolean isItemValid(ItemStack stack) {
						if (stack.isEmpty())
							return false;
						Item item = stack.getItem();
						if (item instanceof ItemBackpack)
							return false;
						if (item instanceof ItemBlock) {
							ItemBlock block = (ItemBlock) item;
							if (block.getBlock() instanceof BlockSafe)
								return false;
						}
						return true;
					}
				});
			}
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new SlotItemHandler(playerInventory, k, 8 + k * 18, 144));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
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
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		InventoryPlayer inventoryplayer = player.inventory;

		if (slotId == -999)
        {
            if (inventoryplayer.getItemStack().getItem() == ModItems.BACKPACK)
            {
            	return ItemStack.EMPTY;
            }
        }
		else if(slotId >= 0) {
			Slot slot = this.getSlot(slotId);
			if(slot != null && slot.getHasStack() && slot.getStack().getItem() == ModItems.BACKPACK) {
            	return ItemStack.EMPTY;
			}
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

}
