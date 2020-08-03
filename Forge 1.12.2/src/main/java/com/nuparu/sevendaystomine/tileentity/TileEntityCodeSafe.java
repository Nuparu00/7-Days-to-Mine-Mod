package com.nuparu.sevendaystomine.tileentity;

import java.util.Random;

import com.nuparu.sevendaystomine.block.BlockCodeSafe;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.inventory.ContainerSafe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCodeSafe extends TileEntitySafe {

	private int correctCode = 000;
	private int selectedCode = 000;

	public TileEntityCodeSafe() {

	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		correctCode = compound.getInteger("CorrectCode");
		selectedCode = compound.getInteger("SelectedCode");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("CorrectCode", correctCode);
		compound.setInteger("SelectedCode", selectedCode);
		return compound;
	}

	@Override
	public void update() {
		if (!world.isRemote) {
			super.update();
			if (!init) {
				while (correctCode == selectedCode && locked) {
					Random rand = world.rand;
					correctCode = rand.nextInt(1000);
					selectedCode = rand.nextInt(1000);
				}
				init = !init;
				markDirty();
			}
			tryToUnlock();
		}
	}

	public boolean tryToUnlock() {
		if (correctCode == selectedCode && locked) {
			unlock();
			return true;
		} else if (correctCode != selectedCode && !locked) {
			lock();
			return false;
		}
		return false;

	}

	public void unlock() {
		locked = false;
		markDirty();
		BlockCodeSafe.setState(locked, world, pos);
	}

	public void lock() {
		locked = true;
		markDirty();
		BlockCodeSafe.setState(locked, world, pos);

	}
	
	public void setInit(boolean init) {
		this.init = init;
		markDirty();
	}

	@Override
	public int getField(int id) {
		if (id == 0) {
			return locked == false ? 0 : 1;
		}
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		if (id == 0 && (value == 0 || value == 1)) {
			locked = value == 0 ? false : true;
			markDirty();
		}
	}

	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerSafe(playerInventory, this);
	}

	public int getSelectedCode() {
		return this.selectedCode;
	}

	public int superSecretMethod() {
		return this.correctCode;
	}

	public void setSelectedCode(int code) {
		this.selectedCode = code;
		markDirty();
	}
	
	public void setCorrectCode(int code) {
		this.correctCode = code;
		markDirty();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		/*
		 * if (newState.getBlock() == Blocks.CODE_SAFE &&
		 * oldState.getProperties().get(BlockCodeSafe.FACING) ==
		 * newState.getProperties().get(BlockCodeSafe.FACING) ) { if(hasWorld()) {
		 * world.notifyBlockUpdate(pos, newState, newState, 3); } return false; }
		 */
		return true;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		nbtTag.removeTag("CorrectCode");
		return new SPacketUpdateTileEntity(getPos(), 0, nbtTag);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
		nbt.removeTag("CorrectCode");
		return nbt;
	}


}
