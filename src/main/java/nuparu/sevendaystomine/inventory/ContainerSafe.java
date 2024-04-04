package nuparu.sevendaystomine.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntitySafe;

public class ContainerSafe extends Container {

	public final TileEntitySafe tileEntity;
	public final IContainerCallbacks callbacks;
	
	public ContainerSafe(IItemHandlerNameable playerInventoryWrapper, ItemStackHandler inventory,
			EntityPlayer player, IContainerCallbacks containerCallbacks) {
		tileEntity = (TileEntitySafe) containerCallbacks;
		callbacks = containerCallbacks;
		callbacks.onContainerOpened(player);
			
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					this.addSlotToContainer(new SlotItemHandler(inventory, j + i * 3, 8 + (j + 3) * 18, 18 + i * 18));
				}
			}
			
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 9; ++j) {
					this.addSlotToContainer(new SlotItemHandler(playerInventoryWrapper, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
				}
			}

			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new SlotItemHandler(playerInventoryWrapper, k, 8 + k * 18, 144));
			}

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tileEntity.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 9)
            {
                if (!this.mergeItemStack(itemstack1, 9, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 9, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
