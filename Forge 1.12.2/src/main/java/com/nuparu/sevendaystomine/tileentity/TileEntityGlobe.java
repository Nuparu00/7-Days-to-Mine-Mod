package com.nuparu.sevendaystomine.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityGlobe extends TileEntity implements ITickable {

	private double speed;
	public double anglePrev;
	public double angle;

	public TileEntityGlobe() {

	}

	@Override
	public void update() {
		this.anglePrev = angle;
		if (speed != 0) {
			speed = 0.99999987f * speed;
		}
		angle += speed;
		while (angle > 360) {
			angle -=360;
			anglePrev -= 360;
		}
		while (angle < 0) {
			angle +=360;
			anglePrev += 360;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.speed = compound.getDouble("speed");
		this.angle = compound.getDouble("angle");
		this.anglePrev = compound.getDouble("anglePrev");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setDouble("angle", this.angle);
		compound.setDouble("speed", this.speed);
		compound.setDouble("anglePrev", this.anglePrev);
		return compound;

	}

	public void addSpeed(double speed) {
		this.speed += speed;
		markForUpdate();
	}

	public double getSpeed() {
		return this.speed;
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net,
			net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos), 1);
	}
	
	public void markForUpdate() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		markDirty();
	}
}
