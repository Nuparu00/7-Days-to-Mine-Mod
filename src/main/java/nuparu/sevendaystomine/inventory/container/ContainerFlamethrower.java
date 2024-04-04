package nuparu.sevendaystomine.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityFlamethrower;

public class ContainerFlamethrower extends Container {

	private static final int SLOTS_PER_ROW = 3;

	private final TileEntityFlamethrower callbacks;

	private final IItemHandlerNameable playerInventory;

	private final IItemHandlerNameable blockInventory;


	public ContainerFlamethrower(IItemHandlerNameable playerInventory, IItemHandlerNameable blockInventory,
			EntityPlayer player, TileEntityFlamethrower containerCallbacks) {
		this.playerInventory = playerInventory;
		this.blockInventory = blockInventory;

		callbacks = containerCallbacks;
		callbacks.onContainerOpened(player);

		addSlotToContainer(new SlotItemHandler(blockInventory, 0, 29, 60));

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				addSlotToContainer(
						new SlotItemHandler(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
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

			if (index == 0) {
				if (!this.mergeItemStack(stack, 1, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(stack, 0, 1, false)) {
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
	
	public TileEntityFlamethrower getTileEntity() {
		return callbacks;
	}
}