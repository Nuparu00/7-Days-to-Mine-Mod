package nuparu.sevendaystomine.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;

public class ContainerBatteryStation extends Container {
	
	private static final int SLOTS_PER_ROW = 3;

	public final IContainerCallbacks callbacks;

	private final IItemHandlerNameable playerInventory;

	private final ItemStackHandler blockInventory;

	public ContainerBatteryStation(IItemHandlerNameable playerInventory, ItemStackHandler blockInventory, EntityPlayer player, IContainerCallbacks containerCallbacks) {
		this.playerInventory = playerInventory;
		this.blockInventory = blockInventory;

		callbacks = containerCallbacks;
		callbacks.onContainerOpened(player);

		addSlotToContainer(
				new SlotItemHandler(blockInventory, 0, 62, 48));
		addSlotToContainer(
				new SlotItemHandler(blockInventory, 1, 98, 48));

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				addSlotToContainer(new SlotItemHandler(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}

		for (int col = 0; col < 9; ++col) {
			addSlotToContainer(new SlotItemHandler(playerInventory, col, 8 + col * 18, 142));
		}

	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		final Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack stack = slot.getStack();
			final ItemStack originalStack = stack.copy();

			if (index < 2) {
				if (!this.mergeItemStack(stack, 2, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(stack, 0, 2, false)) {
				return ItemStack.EMPTY;
			}

			if (stack.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			return originalStack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return callbacks.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		callbacks.onContainerClosed(playerIn);
	}


	public IItemHandlerNameable getPlayerInventory() {
		return playerInventory;
	}


	public ItemStackHandler getBlockInventory() {
		return blockInventory;
	}

}