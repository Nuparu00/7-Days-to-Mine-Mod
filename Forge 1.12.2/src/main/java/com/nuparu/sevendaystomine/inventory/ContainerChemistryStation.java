package com.nuparu.sevendaystomine.inventory;

import com.nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeManager;
import com.nuparu.sevendaystomine.crafting.chemistry.IChemistryRecipe;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.item.ItemScrap;
import com.nuparu.sevendaystomine.tileentity.TileEntityCampfire;
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
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class ContainerChemistryStation extends Container {
	public final TileEntityChemistryStation chemistry;
	public CombinedInvWrapper blockInventory;
	public IItemHandlerNameable playerInventory;
	
	private int cookTime;
	private int totalCookTime;
	private int burnTime;
	private int currentItemBurnTime;

	public ContainerChemistryStation(IItemHandlerNameable playerInventory, CombinedInvWrapper combinedInvWrapper,
			TileEntityChemistryStation chemistry, EntityPlayer player) {
		this.chemistry = chemistry;
		this.playerInventory = playerInventory;
		this.blockInventory = combinedInvWrapper;

		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityChemistryStation.EnumSlots.INPUT_SLOT.ordinal(), 78, 11) {
			@Override
			public void onSlotChanged() {
				chemistry.markDirty();
			}
		});
		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityChemistryStation.EnumSlots.INPUT_SLOT2.ordinal(), 97, 11) {
			@Override
			public void onSlotChanged() {
				chemistry.markDirty();
			}
		});

		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityChemistryStation.EnumSlots.INPUT_SLOT3.ordinal(), 78, 29) {
			@Override
			public void onSlotChanged() {
				chemistry.markDirty();
			}
		});
		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityChemistryStation.EnumSlots.INPUT_SLOT4.ordinal(), 97, 29) {
			@Override
			public void onSlotChanged() {
				chemistry.markDirty();
			}
		});
		this.addSlotToContainer(new SlotChemistryOutput(player, combinedInvWrapper,
				TileEntityChemistryStation.EnumSlots.OUTPUT_SLOT.ordinal(), 148, 42) {
			@Override
			public void onSlotChanged() {
				chemistry.markDirty();
			}
		});
		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal(), 88, 63) {
			@Override
			public void onSlotChanged() {
				chemistry.markDirty();
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
		listener.sendWindowProperty(this, 2, chemistry.getField(2));
		listener.sendWindowProperty(this, 0, chemistry.getField(0));
		listener.sendWindowProperty(this, 1, chemistry.getField(1));
		listener.sendWindowProperty(this, 3, chemistry.getField(3));
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icontainerlistener = this.listeners.get(i);

			if (this.cookTime != chemistry.getField(2)) {
				icontainerlistener.sendWindowProperty(this, 2, chemistry.getField(2));
			}

			if (this.burnTime != chemistry.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, chemistry.getField(0));
			}

			if (this.currentItemBurnTime != chemistry.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, chemistry.getField(1));
			}

			if (this.totalCookTime != chemistry.getField(3)) {
				icontainerlistener.sendWindowProperty(this, 3, chemistry.getField(3));
			}
		}

		this.cookTime = chemistry.getField(2);
		this.burnTime = chemistry.getField(0);
		this.currentItemBurnTime = chemistry.getField(1);
		this.totalCookTime = chemistry.getField(3);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.chemistry.setField(id, data);
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.chemistry.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index <= TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal())
            {
                if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+1, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index > TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal())
            {
            	if (TileEntityChemistryStation.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal(), TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	else if (isIngredient(itemstack1.getItem()))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.EnumSlots.INPUT_SLOT.ordinal(), TileEntityChemistryStation.EnumSlots.INPUT_SLOT4.ordinal()+1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	
            	else if (index >= TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+1 && index < TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+27)
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+27, TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+27 && index < TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+36 && !this.mergeItemStack(itemstack1, TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+1, TileEntityChemistryStation.EnumSlots.FUEL_SLOT.ordinal()+27, false))
                {
                    return ItemStack.EMPTY;
                }
            }
		}
		return ItemStack.EMPTY;
	}
	
	
	public boolean isIngredient(Item item) {
		if(item instanceof ItemScrap) return true;
		for(IChemistryRecipe recipe : ChemistryRecipeManager.getInstance().getRecipes()) {
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
