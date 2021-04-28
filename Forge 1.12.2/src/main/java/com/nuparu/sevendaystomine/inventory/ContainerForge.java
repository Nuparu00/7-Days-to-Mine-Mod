package com.nuparu.sevendaystomine.inventory;

import com.nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import com.nuparu.sevendaystomine.crafting.forge.IForgeRecipe;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.item.ItemScrap;
import com.nuparu.sevendaystomine.tileentity.TileEntityForge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class ContainerForge extends Container {
	public final TileEntityForge forge;
	public CombinedInvWrapper blockInventory;
	public IItemHandlerNameable playerInventory;

	private int cookTime;
	private int totalCookTime;
	private int burnTime;
	private int currentItemBurnTime;

	public ContainerForge(IItemHandlerNameable playerInventory, CombinedInvWrapper combinedInvWrapper,
			TileEntityForge forge, EntityPlayer player) {
		this.forge = forge;
		this.playerInventory = playerInventory;
		this.blockInventory = combinedInvWrapper;
		this.addSlotToContainer(
				new SlotItemHandler(combinedInvWrapper, TileEntityForge.EnumSlots.INPUT_SLOT.ordinal(), 78, 11) {
					@Override
					public void onSlotChanged() {
						forge.markDirty();
					}
				});
		this.addSlotToContainer(
				new SlotItemHandler(combinedInvWrapper, TileEntityForge.EnumSlots.INPUT_SLOT2.ordinal(), 97, 11) {
					@Override
					public void onSlotChanged() {
						forge.markDirty();
					}
				});

		this.addSlotToContainer(
				new SlotItemHandler(combinedInvWrapper, TileEntityForge.EnumSlots.INPUT_SLOT3.ordinal(), 78, 29) {
					@Override
					public void onSlotChanged() {
						forge.markDirty();
					}
				});
		this.addSlotToContainer(
				new SlotItemHandler(combinedInvWrapper, TileEntityForge.EnumSlots.INPUT_SLOT4.ordinal(), 97, 29) {
					@Override
					public void onSlotChanged() {
						forge.markDirty();
					}
				});
		this.addSlotToContainer(
				new SlotForgeOutput(player, combinedInvWrapper, TileEntityForge.EnumSlots.OUTPUT_SLOT.ordinal(), 148, 42) {
					@Override
					public void onSlotChanged() {
						forge.markDirty();
					}
				});
		this.addSlotToContainer(
				new SlotItemHandler(combinedInvWrapper, TileEntityForge.EnumSlots.FUEL_SLOT.ordinal(), 88, 63) {
					@Override
					public void onSlotChanged() {
						forge.markDirty();
					}
				});
		this.addSlotToContainer(
				new SlotItemHandler(combinedInvWrapper, TileEntityForge.EnumSlots.MOLD_SLOT.ordinal(), 45, 42) {
					@Override
					public void onSlotChanged() {
						forge.markDirty();
					}

					@Override
					public int getSlotStackLimit() {
						return 1;
					}
				});

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
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendWindowProperty(this, 2, forge.getField(2));
		listener.sendWindowProperty(this, 0, forge.getField(0));
		listener.sendWindowProperty(this, 1, forge.getField(1));
		listener.sendWindowProperty(this, 3, forge.getField(3));
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icontainerlistener = this.listeners.get(i);

			if (this.cookTime != forge.getField(2)) {
				icontainerlistener.sendWindowProperty(this, 2, forge.getField(2));
			}

			if (this.burnTime != forge.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, forge.getField(0));
			}

			if (this.currentItemBurnTime != forge.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, forge.getField(1));
			}

			if (this.totalCookTime != forge.getField(3)) {
				icontainerlistener.sendWindowProperty(this, 3, forge.getField(3));
			}
		}

		this.cookTime = forge.getField(2);
		this.burnTime = forge.getField(0);
		this.currentItemBurnTime = forge.getField(1);
		this.totalCookTime = forge.getField(3);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		forge.setField(id, data);
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.forge.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index <= TileEntityForge.EnumSlots.MOLD_SLOT.ordinal()) {
				slot.onSlotChange(itemstack1, itemstack);
				if (!this.mergeItemStack(itemstack1, TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 1, 39, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index > TileEntityForge.EnumSlots.MOLD_SLOT.ordinal()) {
				if (TileEntityForge.isItemFuel(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, TileEntityForge.EnumSlots.FUEL_SLOT.ordinal(),
							TileEntityForge.EnumSlots.FUEL_SLOT.ordinal() + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (isMold(itemstack1.getItem())) {
					if (!this.mergeItemStack(itemstack1, TileEntityForge.EnumSlots.MOLD_SLOT.ordinal(),
							TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (isIngredient(itemstack1.getItem())) {
					if (!this.mergeItemStack(itemstack1, TileEntityForge.EnumSlots.INPUT_SLOT.ordinal(),
							TileEntityForge.EnumSlots.INPUT_SLOT4.ordinal() + 1, false)) {
						return ItemStack.EMPTY;
					}
				}

				else if (index >= TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 1
						&& index < TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 27) {
					if (!this.mergeItemStack(itemstack1, TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 27,
							TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 36, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 27
						&& index < TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 36
						&& !this.mergeItemStack(itemstack1, TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 1,
								TileEntityForge.EnumSlots.MOLD_SLOT.ordinal() + 27, false)) {
					return ItemStack.EMPTY;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	public boolean isMold(Item item) {
		for (IForgeRecipe recipe : ForgeRecipeManager.getInstance().getRecipes()) {
			if (recipe.getMold() != null && !recipe.getMold().isEmpty() && recipe.getMold().getItem() == item) {
				return true;
			}
		}
		return false;
	}

	public boolean isIngredient(Item item) {
		if (item instanceof ItemScrap)
			return true;
		for (IForgeRecipe recipe : ForgeRecipeManager.getInstance().getRecipes()) {
			if (recipe.getIngredients() != null) {
				for (ItemStack stack : recipe.getIngredients()) {
					if (!stack.isEmpty() && stack.getItem() == item) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
