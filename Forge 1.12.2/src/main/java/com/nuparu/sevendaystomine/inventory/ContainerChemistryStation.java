package com.nuparu.sevendaystomine.inventory;

import com.nuparu.sevendaystomine.item.ItemScrap;
import com.nuparu.sevendaystomine.item.crafting.forge.ForgeRecipeManager;
import com.nuparu.sevendaystomine.item.crafting.forge.IForgeRecipe;
import com.nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;

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

public class ContainerChemistryStation extends Container {
	public final IInventory tileEntity;

	private int cookTime;
	private int totalCookTime;
	private int burnTime;
	private int currentItemBurnTime;

	public ContainerChemistryStation(InventoryPlayer playerInventory, final IInventory tileEntity) {
		this.tileEntity = tileEntity;

		this.addSlotToContainer(new Slot(tileEntity, TileEntityChemistryStation.slotEnum.INPUT_SLOT.ordinal(), 78, 11) {
			@Override
			public void onSlotChanged() {
				tileEntity.markDirty();
			}
		});
		this.addSlotToContainer(new Slot(tileEntity, TileEntityChemistryStation.slotEnum.INPUT_SLOT2.ordinal(), 97, 11) {
			@Override
			public void onSlotChanged() {
				tileEntity.markDirty();
			}
		});

		this.addSlotToContainer(new Slot(tileEntity, TileEntityChemistryStation.slotEnum.INPUT_SLOT3.ordinal(), 78, 29) {
			@Override
			public void onSlotChanged() {
				tileEntity.markDirty();
			}
		});
		this.addSlotToContainer(new Slot(tileEntity, TileEntityChemistryStation.slotEnum.INPUT_SLOT4.ordinal(), 97, 29) {
			@Override
			public void onSlotChanged() {
				tileEntity.markDirty();
			}
		});
		this.addSlotToContainer(new SlotChemistryOutput(playerInventory.player, tileEntity,
				TileEntityChemistryStation.slotEnum.OUTPUT_SLOT.ordinal(), 148, 42) {
			@Override
			public void onSlotChanged() {
				tileEntity.markDirty();
			}
		});
		this.addSlotToContainer(new SlotChemistryFuel(tileEntity, TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal(), 88, 63) {
			@Override
			public void onSlotChanged() {
				tileEntity.markDirty();
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileEntity);
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icontainerlistener = this.listeners.get(i);

			if (this.cookTime != this.tileEntity.getField(2)) {
				icontainerlistener.sendWindowProperty(this, 2, this.tileEntity.getField(2));
			}

			if (this.burnTime != this.tileEntity.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, this.tileEntity.getField(0));
			}

			if (this.currentItemBurnTime != this.tileEntity.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, this.tileEntity.getField(1));
			}

			if (this.totalCookTime != this.tileEntity.getField(3)) {
				icontainerlistener.sendWindowProperty(this, 3, this.tileEntity.getField(3));
			}
		}

		this.cookTime = this.tileEntity.getField(2);
		this.burnTime = this.tileEntity.getField(0);
		this.currentItemBurnTime = this.tileEntity.getField(1);
		this.totalCookTime = this.tileEntity.getField(3);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.tileEntity.setField(id, data);
	}

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

            if (index <= TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal())
            {
                if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+1, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index > TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal())
            {
            	if (TileEntityChemistryStation.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal(), TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	else if (isMold(itemstack1.getItem()))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal(), TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	else if (isIngredient(itemstack1.getItem()))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.slotEnum.INPUT_SLOT.ordinal(), TileEntityChemistryStation.slotEnum.INPUT_SLOT4.ordinal()+1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	
            	else if (index >= TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+1 && index < TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+27)
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+27, TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+27 && index < TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+36 && !this.mergeItemStack(itemstack1, TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+1, TileEntityChemistryStation.slotEnum.FUEL_SLOT.ordinal()+27, false))
                {
                    return ItemStack.EMPTY;
                }
            }
		}
		return ItemStack.EMPTY;
	}
	
	public boolean isMold(Item item) {
		for(IForgeRecipe recipe : ForgeRecipeManager.getInstance().getRecipes()) {
			if(recipe.getMold() != null && !recipe.getMold().isEmpty() && recipe.getMold().getItem() == item) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isIngredient(Item item) {
		if(item instanceof ItemScrap) return true;
		for(IForgeRecipe recipe : ForgeRecipeManager.getInstance().getRecipes()) {
			if(recipe.getIngredients() != null) {
				for(ItemStack stack : recipe.getIngredients()) {
					if(!stack.isEmpty() && stack.getItem() == item) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
