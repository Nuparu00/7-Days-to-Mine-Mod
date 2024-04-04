package nuparu.sevendaystomine.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerComputer extends Container {

	private IInventory inv;
	@SuppressWarnings("unused")
	private InventoryPlayer playerInventory;
	public ContainerComputer(InventoryPlayer playerInventory, IInventory inv) {
		this.playerInventory = playerInventory;
		this.inv = inv;
		
		addSlotToContainer(new Slot(inv, 0, 12, 28));
		addSlotToContainer(new Slot(inv, 1, 152, 33));
		addSlotToContainer(new Slot(inv, 2, 152, 12));
		addSlotToContainer(new Slot(inv, 3, 12, 56));
		addSlotToContainer(new Slot(inv, 4, 152, 63));
		addSlotToContainer(new Slot(inv, 5, 120, 8));
		addSlotToContainer(new Slot(inv, 6, 191, 25));
		
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}

		}

		for (i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return inv.isUsableByPlayer(playerIn);
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

            if (index < 7)
            {
                if (!this.mergeItemStack(itemstack1, 7, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 6, false))
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
