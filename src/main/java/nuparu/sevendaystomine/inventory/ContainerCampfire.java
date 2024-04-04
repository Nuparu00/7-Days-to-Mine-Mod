package nuparu.sevendaystomine.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import nuparu.sevendaystomine.crafting.campfire.CampfireRecipeManager;
import nuparu.sevendaystomine.crafting.campfire.ICampfireRecipe;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.tileentity.TileEntityCampfire;

public class ContainerCampfire extends Container {
	public final TileEntityCampfire campfire;
	public CombinedInvWrapper blockInventory;
	public IItemHandlerNameable playerInventory;

	private int cookTime;
	private int totalCookTime;
	private int burnTime;
	private int currentItemBurnTime;

	public ContainerCampfire(IItemHandlerNameable playerInventory, CombinedInvWrapper combinedInvWrapper,
			TileEntityCampfire campfire, EntityPlayer player) {
		this.campfire = campfire;
		this.playerInventory = playerInventory;
		this.blockInventory = combinedInvWrapper;

		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityCampfire.EnumSlots.INPUT_SLOT.ordinal(), 78, 11) {
			@Override
			public void onSlotChanged() {
				campfire.markDirty();
			}
		});
		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityCampfire.EnumSlots.INPUT_SLOT2.ordinal(), 97, 11) {
			@Override
			public void onSlotChanged() {
				campfire.markDirty();
			}
		});

		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityCampfire.EnumSlots.INPUT_SLOT3.ordinal(), 78, 29) {
			@Override
			public void onSlotChanged() {
				campfire.markDirty();
			}
		});
		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityCampfire.EnumSlots.INPUT_SLOT4.ordinal(), 97, 29) {
			@Override
			public void onSlotChanged() {
				campfire.markDirty();
			}
		});
		this.addSlotToContainer(new SlotCampfireOutput(player, combinedInvWrapper,
				TileEntityCampfire.EnumSlots.OUTPUT_SLOT.ordinal(), 148, 42) {
			@Override
			public void onSlotChanged() {
				campfire.markDirty();
			}
		});
		this.addSlotToContainer(
				new SlotItemHandler(combinedInvWrapper, TileEntityCampfire.EnumSlots.FUEL_SLOT.ordinal(), 88, 63) {
					@Override
					public void onSlotChanged() {
						campfire.markDirty();
					}
				});
		this.addSlotToContainer(new SlotItemHandler(combinedInvWrapper, TileEntityCampfire.EnumSlots.POT_SLOT.ordinal(), 45, 42) {
			@Override
			public void onSlotChanged() {
				campfire.markDirty();
			}
			@Override
			public int getSlotStackLimit()
		    {
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
		listener.sendWindowProperty(this, 2, campfire.getField(2));
		listener.sendWindowProperty(this, 0, campfire.getField(0));
		listener.sendWindowProperty(this, 1, campfire.getField(1));
		listener.sendWindowProperty(this, 3, campfire.getField(3));
	}

	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i) {
			IContainerListener icontainerlistener = this.listeners.get(i);

			if (this.cookTime != campfire.getField(2)) {
				icontainerlistener.sendWindowProperty(this, 2, campfire.getField(2));
			}

			if (this.burnTime != campfire.getField(0)) {
				icontainerlistener.sendWindowProperty(this, 0, campfire.getField(0));
			}

			if (this.currentItemBurnTime != campfire.getField(1)) {
				icontainerlistener.sendWindowProperty(this, 1, campfire.getField(1));
			}

			if (this.totalCookTime != campfire.getField(3)) {
				icontainerlistener.sendWindowProperty(this, 3, campfire.getField(3));
			}
		}

		this.cookTime = campfire.getField(2);
		this.burnTime = campfire.getField(0);
		this.currentItemBurnTime = campfire.getField(1);
		this.totalCookTime = campfire.getField(3);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		this.campfire.setField(id, data);
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.campfire.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index <= TileEntityCampfire.EnumSlots.POT_SLOT.ordinal())
            {
                if (!this.mergeItemStack(itemstack1, TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+1, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index > TileEntityCampfire.EnumSlots.POT_SLOT.ordinal())
            {
            	if (TileEntityCampfire.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityCampfire.EnumSlots.FUEL_SLOT.ordinal(), TileEntityCampfire.EnumSlots.FUEL_SLOT.ordinal()+1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	else if (isPot(itemstack1.getItem()))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityCampfire.EnumSlots.POT_SLOT.ordinal(), TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	else if (isIngredient(itemstack1.getItem()))
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityCampfire.EnumSlots.INPUT_SLOT.ordinal(), TileEntityCampfire.EnumSlots.INPUT_SLOT4.ordinal()+1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            	
            	else if (index >= TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+1 && index < TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+27)
                {
                    if (!this.mergeItemStack(itemstack1, TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+27, TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+27 && index < TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+36 && !this.mergeItemStack(itemstack1, TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+1, TileEntityCampfire.EnumSlots.POT_SLOT.ordinal()+27, false))
                {
                    return ItemStack.EMPTY;
                }
            }
		}
		return ItemStack.EMPTY;
	}
	
	public boolean isPot(Item item) {
		for(ICampfireRecipe recipe : CampfireRecipeManager.getInstance().getRecipes()) {
			if(recipe.getPot() != null && !recipe.getPot().isEmpty() && recipe.getPot().getItem() == item) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isIngredient(Item item) {
		for(ICampfireRecipe recipe : CampfireRecipeManager.getInstance().getRecipes()) {
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
