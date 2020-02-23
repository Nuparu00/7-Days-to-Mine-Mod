package com.nuparu.sevendaystomine.tileentity;

import com.nuparu.sevendaystomine.block.BlockWoodenSpikes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWoodenSpikes extends TileEntity{

	public int health = 365;
	
	public TileEntityWoodenSpikes() {
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
		health-=damage;
		if(health <= 0) {
			BlockWoodenSpikes.degradeBlock(pos, world);
		}
	}
}
