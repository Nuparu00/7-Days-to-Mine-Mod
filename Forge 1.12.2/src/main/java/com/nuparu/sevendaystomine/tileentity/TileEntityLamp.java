package com.nuparu.sevendaystomine.tileentity;

import java.util.List;

import com.nuparu.sevendaystomine.block.BlockLamp;
import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.electricity.EnumDeviceType;
import com.nuparu.sevendaystomine.electricity.IVoltage;
import com.nuparu.sevendaystomine.util.ModConstants;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;

public class TileEntityLamp extends TileEntity implements ITickable, IVoltage {

	private long voltage = 0;
	private long capacity = 30;

	private boolean on = false;

	public TileEntityLamp() {

	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.CONSUMER;
	}

	@Override
	public int getMaximalInputs() {
		return 0;
	}

	@Override
	public int getMaximalOutputs() {
		return 0;
	}

	@Override
	public List<ElectricConnection> getInputs() {
		return null;
	}

	@Override
	public List<ElectricConnection> getOutputs() {
		return null;
	}

	@Override
	public long getOutput() {
		return 0;
	}

	@Override
	public long getMaxOutput() {
		return 0;
	}

	@Override
	public long getOutputForConnection(ElectricConnection connection) {
		return 0;
	}

	@Override
	public boolean tryToConnect(ElectricConnection connection) {
		return false;
	}

	@Override
	public boolean canConnect(ElectricConnection connection) {
		return false;
	}

	@Override
	public long getRequiredPower() {
		return 1;
	}

	@Override
	public long getCapacity() {
		return this.capacity;
	}

	@Override
	public long getVoltageStored() {
		return this.voltage;
	}

	@Override
	public void storePower(long power) {
		this.voltage += power;
		if (this.voltage > this.getCapacity()) {
			this.voltage = this.getCapacity();
		}
		if (this.voltage < 0) {
			this.voltage = 0;
		}
	}

	@Override
	public long tryToSendPower(long power, ElectricConnection connection) {
		long canBeAdded = capacity - voltage;
		long delta = Math.min(canBeAdded, power);
		long lost = 0;
		if (connection != null) {
			lost = (long) Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
		}
		long realDelta = delta - lost;
		this.voltage += realDelta;

		return delta;
	}

	@Override
	public Vec3d getWireOffset() {
		return null;
	}

	@Override
	public boolean isPassive() {
		return true;
	}

	@Override
	public void update() {
		if(world.isRemote) return;
		if (on && this.voltage >= this.getRequiredPower()) {
			if (!BlockLamp.isLit(world, pos)) {
				BlockLamp.setState(world, pos, true);
				markForUpdate();
				world.markBlockRangeForRenderUpdate(pos.add(-15, -15, -15), pos.add(15,15,15));
			}
			this.voltage -= this.getRequiredPower();
		} else {
			if (BlockLamp.isLit(world, pos)) {
				BlockLamp.setState(world, pos, false);
				markForUpdate();
				world.markBlockRangeForRenderUpdate(pos.add(-15, -15, -15), pos.add(15,15,15));
			}
		}
		if (this.voltage < 0) {
			this.voltage = 0;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		this.voltage = compound.getLong("power");
		this.on = compound.getBoolean("on");

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setLong("power", voltage);
		compound.setBoolean("on", on);
		return compound;
	}

	public void setState(boolean state) {
		this.on = state;
		markForUpdate();
	}

	public boolean getState() {
		return this.on;
	}

	public void markForUpdate() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		this.markDirty();
	}

}
