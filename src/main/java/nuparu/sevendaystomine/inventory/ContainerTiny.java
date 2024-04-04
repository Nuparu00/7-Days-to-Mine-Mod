package nuparu.sevendaystomine.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerTiny extends Container {

	public final IInventory tileEntity;

	public ContainerTiny(InventoryPlayer playerInventory, final IInventory tileEntity) {
		this.tileEntity = tileEntity;
		if (tileEntity instanceof TileEntity) {

			this.addSlotToContainer(new Slot(tileEntity, 0, 80, 54));

			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 9; ++j) {
					this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
				}
			}

			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 144));
			}
		} else {
			throw new IllegalArgumentException("Passed IInventory is not instance of TileEntity!");
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

			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
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
