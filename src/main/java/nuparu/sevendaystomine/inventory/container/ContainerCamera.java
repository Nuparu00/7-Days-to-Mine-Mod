package nuparu.sevendaystomine.inventory.container;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;

public class ContainerCamera extends Container {

	public final IItemHandlerExtended camera;
	public final IItemHandlerNameable playerInventory;
	
	public ContainerCamera(IItemHandlerNameable playerInventory, IItemHandlerExtended camera) {
		this.camera = camera;
		this.playerInventory = playerInventory;
			

			
			this.addSlotToContainer(new SlotItemHandler(camera, 0, 80, 54){
				
				@Nullable
				@SideOnly(Side.CLIENT)
				public String getSlotTexture() {
					return SevenDaysToMine.MODID + ":items/empty_paper_slot";
				}
				
				public boolean isItemValid(ItemStack stack)
			    {
			        return !stack.isEmpty() && stack.getItem() == Items.PAPER;
			    }
			});
			
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 9; ++j) {
					this.addSlotToContainer(new SlotItemHandler(playerInventory, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
				}
			}

			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new SlotItemHandler(playerInventory, k, 8 + k * 18, 144));
			}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
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
