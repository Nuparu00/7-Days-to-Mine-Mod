package com.nuparu.sevendaystomine.tileentity;

import javax.annotation.Nonnull;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockCookware;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.inventory.ContainerWorkbench;
import com.nuparu.sevendaystomine.inventory.ContainerWorkbenchUncrafting;
import com.nuparu.sevendaystomine.inventory.container.ContainerSmall;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import com.nuparu.sevendaystomine.item.ItemCookware;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileEntityWorkbench extends TileEntityItemHandler<ItemHandlerNameable> {

	private static final int INVENTORY_SIZE = 1;
	private static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.workbench");

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME){
			@Override
		    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		    {
		        return !stack.isEmpty() && stack.getItem() == ModItems.IRON_SCRAP;
		    }
		};
	}

	@Override
	public Container createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);
		if (player.isSneaking()) {
			return new ContainerWorkbench(player, playerInventoryWrapper, getInventory(), this, this.getPos());
		} else {
			return new ContainerWorkbenchUncrafting(player, playerInventoryWrapper, getInventory(), this,
					this.getPos());
		}
	}
	
	public Container createContainer(EntityPlayer player, boolean crafting) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);
		System.out.println("createContainer " + crafting + " " + player.world.isRemote);
		if (crafting) {
			return new ContainerWorkbench(player, playerInventoryWrapper, getInventory(), this, this.getPos());
		} else {
			return new ContainerWorkbenchUncrafting(player, playerInventoryWrapper, getInventory(), this,
					this.getPos());
		}
	}

	@Override
	public void onContainerOpened(EntityPlayer player) {

	}

	@Override
	public void onContainerClosed(EntityPlayer player) {

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
	}

	public void setDisplayName(String displayName) {
		inventory.setDisplayName(new TextComponentString(displayName));
	}

	@Override
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return null;
	}

}