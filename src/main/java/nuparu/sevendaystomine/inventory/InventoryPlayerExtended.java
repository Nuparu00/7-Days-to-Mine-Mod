package nuparu.sevendaystomine.inventory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class InventoryPlayerExtended extends InventoryPlayer {

	public boolean dirty;
	
    public final NonNullList<ItemStack> mainInventory = NonNullList.<ItemStack>withSize(36, ItemStack.EMPTY);
    public final NonNullList<ItemStack> armorInventory = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
    public final NonNullList<ItemStack> extendedInventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    public final NonNullList<ItemStack> offHandInventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    private final List<NonNullList<ItemStack>> allInventories;
    
    
	
	public InventoryPlayerExtended(EntityPlayer playerIn) {
		super(playerIn);
		this.allInventories = Arrays.<NonNullList<ItemStack>>asList(this.mainInventory, this.armorInventory, this.offHandInventory, this.extendedInventory);
	}
	
	
	public void copy(InventoryPlayerExtended copyTo) {
		for(int i = 0; i < extendedInventory.size(); i++) {
			copyTo.extendedInventory.set(i, extendedInventory.get(i));
		}
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
    {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            if (index < nonnulllist.size())
            {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }
        dirty = true;
        return list != null && !((ItemStack)list.get(index)).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
    }

	@Override
    public void deleteStack(ItemStack stack)
    {
        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            for (int i = 0; i < nonnulllist.size(); ++i)
            {
                if (nonnulllist.get(i) == stack)
                {
                    nonnulllist.set(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
        dirty = true;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        NonNullList<ItemStack> nonnulllist = null;
        dirty = true;
        for (NonNullList<ItemStack> nonnulllist1 : this.allInventories)
        {
            if (index < nonnulllist1.size())
            {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null && !((ItemStack)nonnulllist.get(index)).isEmpty())
        {
            ItemStack itemstack = nonnulllist.get(index);
            nonnulllist.set(index, ItemStack.EMPTY);
            return itemstack;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        NonNullList<ItemStack> nonnulllist = null;
        dirty = true;
        for (NonNullList<ItemStack> nonnulllist1 : this.allInventories)
        {
            if (index < nonnulllist1.size())
            {
                nonnulllist = nonnulllist1;
                break;
            }

            index -= nonnulllist1.size();
        }

        if (nonnulllist != null)
        {
            nonnulllist.set(index, stack);
        }
    }
    
    public int getSizeInventory()
    {
        return super.getSizeInventory() + this.extendedInventory.size();
    }
    
    @Override
    public ItemStack getStackInSlot(int index)
    {
        List<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : this.allInventories)
        {
            if (index < nonnulllist.size())
            {
                list = nonnulllist;
                break;
            }

            index -= nonnulllist.size();
        }

        return list == null ? ItemStack.EMPTY : (ItemStack)list.get(index);
    }
    @Override
    public void dropAllItems()
    {
        for (List<ItemStack> list : this.allInventories)
        {
            for (int i = 0; i < list.size(); ++i)
            {
                ItemStack itemstack = list.get(i);

                if (!itemstack.isEmpty())
                {
                    this.player.dropItem(itemstack, true, false);
                    list.set(i, ItemStack.EMPTY);
                }
            }
        }
        dirty = true;
    }
    @Override
    public void clear()
    {
        for (List<ItemStack> list : this.allInventories)
        {
            list.clear();
        }
        dirty = true;
    }
    @Override
    public boolean isEmpty()
    {
    	
    	for(ItemStack stack : this.extendedInventory)
        {
            if(!stack.isEmpty())
            {
                return false;
            }
        }
        return super.isEmpty();
    }
    @Override
    public boolean hasItemStack(ItemStack itemStackIn)
    {
        for (List<ItemStack> list : this.allInventories)
        {
            Iterator<ItemStack> iterator = list.iterator();

            while (true)
            {
                if (!iterator.hasNext())
                {
                	return false;
                }

                ItemStack itemstack = (ItemStack)iterator.next();

                if (!itemstack.isEmpty() && itemstack.isItemEqual(itemStackIn))
                {
                    break;
                }
            }

            return true;
        }

        return false;
    }
    
    @Override
	public NBTTagList writeToNBT(NBTTagList tagList) {
		tagList = super.writeToNBT(tagList);
		for (int i = 0; i < this.extendedInventory.size(); i++) {
			if (!this.extendedInventory.get(i).isEmpty()) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) (i + 200));
				this.extendedInventory.get(i).writeToNBT(compound);
				tagList.appendTag(compound);
			}
		}
		return tagList;
	}

	@Override
	public void readFromNBT(NBTTagList tagList) {
		super.readFromNBT(tagList);
		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound compound = tagList.getCompoundTagAt(i);
			int slot = compound.getByte("Slot") & 255;
			if (slot >= 200 && slot < this.extendedInventory.size() + 200) {
				ItemStack stack = new ItemStack(compound);
				if (!stack.isEmpty()) {
					this.extendedInventory.set(slot - 200, stack);
				}
			}
		}
	}

}
