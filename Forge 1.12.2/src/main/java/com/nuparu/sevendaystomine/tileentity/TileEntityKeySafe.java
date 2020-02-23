package com.nuparu.sevendaystomine.tileentity;

import com.nuparu.sevendaystomine.inventory.ContainerSafe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.NonNullList;

public class TileEntityKeySafe extends TileEntitySafe {

	//-180 -- +180
	private float lockAngle;
	//In degrees
	private final float tolerance = 10;

	//-180 -- +180
	private float sweetPoint;
	
	private float force;
	
	private final float forceThreshold = 150;

	public TileEntityKeySafe() {
	}

	public boolean tryToUnlock() {
		if(Math.abs(lockAngle-sweetPoint)<=tolerance && force >= forceThreshold) {
			return true;
		}
		return false;
	}

	public void unlock() {

	}

	public void lock() {

	}
	
	public boolean tryToBreakPick(EntityPlayer player) {
		float diff = Math.abs(sweetPoint-lockAngle);
		if(force >= forceThreshold*(1-diff/180f)) {
			return true;
		}
		return false;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.lockAngle = compound.getFloat("LockAngle");
		this.sweetPoint = compound.getFloat("SweetPoint");
		this.force = compound.getFloat("Force");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setFloat("LockAngle", this.lockAngle);
		compound.setFloat("SweetPoint", this.sweetPoint);
		compound.setFloat("Force", this.force);
		
		return compound;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		nbtTag.removeTag("SweetPoint");
		return new SPacketUpdateTileEntity(getPos(), 0, nbtTag);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
		nbt.removeTag("SweetPoint");
		return nbt;
	}
	
	public String superSecretMethod() {
		return sweetPoint + " +- " + tolerance;
	}
	
	public void setAngle(float angle) {
		this.lockAngle = angle;
	}
	public void setForce(float force) {
		this.force = force;
	}
	
	public float getForce() {
		return this.force;
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerSafe(playerInventory, this);
	}

}
