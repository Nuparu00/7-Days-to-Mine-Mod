package com.nuparu.sevendaystomine.tileentity;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockBookshelfEnhanced;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBookshelf extends TileEntityLockableLoot implements ISidedInventory {

	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
	private String customName;

	public TileEntityBookshelf() {
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.inventory);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.customName);
		}

		return compound;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.size();
	}

	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.inventory) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack = ItemStackHelper.getAndSplit(this.inventory, index, count);
		updateBlock();
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = ItemStackHelper.getAndRemove(this.inventory, index);
		updateBlock();
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventory.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		updateBlock();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
					(double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.inventory.clear();
		updateBlock();
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.bookshelf";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomInventoryName(String name) {
		this.customName = name;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}

	@Override
	public String getGuiID() {
		return SevenDaysToMine.MODID + ":bookshelf";
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}

	public void updateBlock() {
		IBlockState iblockstate = this.world.getBlockState(this.pos);
		boolean state = iblockstate.getValue(BlockBookshelfEnhanced.FULL);
		if (isEmpty() && state) {
			world.setBlockState(pos, iblockstate.withProperty(BlockBookshelfEnhanced.FULL, false));
		} else if (!isEmpty() && !state) {
			world.setBlockState(pos, iblockstate.withProperty(BlockBookshelfEnhanced.FULL, true));
		}
	}

	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return (oldState.getBlock() != newState.getBlock())
				|| oldState.getValue(BlockBookshelfEnhanced.FACING) != newState.getValue(BlockBookshelfEnhanced.FACING);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.inventory;
	}
}
