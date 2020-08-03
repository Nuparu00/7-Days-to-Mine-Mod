package com.nuparu.sevendaystomine.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityGlobe extends TileEntity implements ITickable {

	private float speed;
	public float anglePrev;
	public float angle;

	public TileEntityGlobe() {

	}

	@Override
	public void update() {
		this.anglePrev = angle;
		if (speed > 0) {
			speed -= 0.005F * (speed);
		}
		if (speed < 0) {
			speed = 0;
		}
		angle += speed;
		if (angle > 360) {
			angle = 0;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.speed = compound.getFloat("speed");
		this.angle = compound.getFloat("angle");
		this.anglePrev = compound.getFloat("anglePrev");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setFloat("angle", this.angle);
		compound.setFloat("speed", this.speed);
		compound.setFloat("anglePrev", this.anglePrev);
		return compound;

	}

	public void addSpeed() {
		this.speed += 2.0F;
	}

	public float getSpeed() {
		return this.speed;
	}
}
