package nuparu.sevendaystomine.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.SlotItemHandlerOutput;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;

public class ContainerSeparator extends Container {
	

	public final IContainerCallbacks callbacks;

	private final IItemHandlerNameable playerInventory;

	private final IItemHandlerNameable blockInventory;

	public ContainerSeparator(IItemHandlerNameable playerInventory, IItemHandlerNameable blockInventory, EntityPlayer player, IContainerCallbacks containerCallbacks) {
		this.playerInventory = playerInventory;
		this.blockInventory = blockInventory;

		callbacks = containerCallbacks;
		callbacks.onContainerOpened(player);

		this.addSlotToContainer(new SlotItemHandler(blockInventory, 0, 80, 42));
		this.addSlotToContainer(new SlotItemHandlerOutput(blockInventory, 1, 12, 42));
		this.addSlotToContainer(new SlotItemHandlerOutput(blockInventory, 2, 148, 42));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new SlotItemHandler(playerInventory, k, 8 + k * 18, 142));
		}

	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		final Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack stack = slot.getStack();
			final ItemStack originalStack = stack.copy();

			if (index < 3) {
				if (!this.mergeItemStack(stack, 3, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(stack, 0, 3, false)) {
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


	public IItemHandlerNameable getBlockInventory() {
		return blockInventory;
	}
}