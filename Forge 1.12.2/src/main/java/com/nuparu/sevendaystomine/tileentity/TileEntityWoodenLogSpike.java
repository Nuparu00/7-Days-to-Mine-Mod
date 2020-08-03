package com.nuparu.sevendaystomine.tileentity;

import com.nuparu.sevendaystomine.block.BlockWoodenLogSpike;
import com.nuparu.sevendaystomine.block.BlockWoodenSpikes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWoodenLogSpike extends TileEntity{

	public int health = 1000;
	
	public TileEntityWoodenLogSpike() {
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.health = compound.getInteger("health");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);
		compound.setInteger("health", this.health);
		return compound;
	}

	public void dealDamage(int damage) {
		if(world.isRemote) return;
		health-=damage;
		if(health <= 0) {
			BlockWoodenLogSpike.degradeBlock(pos, world);
		}
	}
}
