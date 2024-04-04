package nuparu.sevendaystomine.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityTurretAdvanced;

public class ContainerTurretAdvanced extends Container {

	public final TileEntityTurretAdvanced tileEntity;
	public final IContainerCallbacks callbacks;

	public ContainerTurretAdvanced(IItemHandlerNameable playerInventoryWrapper, ItemStackHandler inventory,
			EntityPlayer player, IContainerCallbacks containerCallbacks) {
		tileEntity = (TileEntityTurretAdvanced) containerCallbacks;
		callbacks = containerCallbacks;
		callbacks.onContainerOpened(player);

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 3; ++col) {
				addSlotToContainer(new SlotItemHandler(inventory, col + row * 3, 8 + (col + 3) * 18, 18 + row * 18));
			}
		}

		addSlotToContainer(new SlotItemHandler(inventory, 9, 125, 36));

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				addSlotToContainer(
						new SlotItemHandler(playerInventoryWrapper, col + row * 9 + 9, 8 + col * 18, 86 + row * 18));
			}
		}

		for (int col = 0; col < 9; ++col) {
			addSlotToContainer(new SlotItemHandler(playerInventoryWrapper, col, 8 + col * 18, 144));
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

}
