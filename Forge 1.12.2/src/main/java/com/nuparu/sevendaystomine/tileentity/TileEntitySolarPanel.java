package com.nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nuparu.sevendaystomine.block.BlockSolarPanel;
import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.electricity.EnumDeviceType;
import com.nuparu.sevendaystomine.electricity.IVoltage;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.ModConstants;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;

public class TileEntitySolarPanel extends TileEntity implements ITickable, IVoltage {
	private List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	private List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();
	private long capacity = 1000l;
	private long voltage = 0;

	public EnumFacing facing;

	public TileEntitySolarPanel() {
		
	}
	
	public void update() {

		boolean flag = false;
		if (facing == null) {
			this.facing = world.getBlockState(pos).getValue(BlockSolarPanel.FACING).getOpposite();
			markDirty();
		}
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		int skylight = chunk.getLightFor(EnumSkyBlock.SKY, pos);
		if (skylight >= 11) {
			long delta = (long) (Math.sin((world.getCelestialAngle(0) + 0.25f) * 2 * Math.PI) * 180f * (skylight / 15f));
			if(facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
				delta*=0.66f;
			}
			long voltagePrev = voltage;
			voltage = MathUtils.clamp(voltage + delta, 0, capacity);
			if (voltagePrev != voltage) {
				flag = true;
			}
		}
		
		Iterator<ElectricConnection> iterator = outputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getTo(world);
			if (voltage != null) {
				long l = voltage.tryToSendPower(getOutputForConnection(connection),connection);
				this.voltage -= l;
				if (l != 0) {
					flag = true;
				}
			} else {
				iterator.remove();
				flag = true;
			}
		}

		iterator = inputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getFrom(world);
			if (voltage == null) {
				iterator.remove();
				flag = true;
			}
		}

		if (flag) {
			this.markDirty();
		}

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

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		voltage = compound.getLong("voltage");
		if (compound.hasKey("facing", 3)) {
			this.facing = EnumFacing.getFront(compound.getInteger("facing"));
		}
		if (compound.hasKey("inputs", Constants.NBT.TAG_COMPOUND)) {
			inputs.clear();
			NBTTagCompound in = compound.getCompoundTag("inputs");
			int size = in.getInteger("size");
			for (int i = 0; i < size; i++) {
				NBTTagCompound nbt = in.getCompoundTag("input_" + i);
				ElectricConnection connection = new ElectricConnection();
				connection.readNBT(nbt);
				inputs.add(connection);
			}
		}

		if (compound.hasKey("outputs", Constants.NBT.TAG_COMPOUND)) {
			outputs.clear();
			NBTTagCompound in = compound.getCompoundTag("outputs");
			int size = in.getInteger("size");
			for (int i = 0; i < size; i++) {
				NBTTagCompound nbt = in.getCompoundTag("output" + i);
				ElectricConnection connection = new ElectricConnection();
				connection.readNBT(nbt);
				outputs.add(connection);
			}
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setLong("voltage", voltage);
		if (facing != null) {
			compound.setInteger("facing", this.facing.getIndex());
		}
		NBTTagCompound in = new NBTTagCompound();

		in.setInteger("size", getInputs().size());
		for (int i = 0; i < inputs.size(); i++) {
			ElectricConnection connection = inputs.get(i);
			in.setTag("input_" + i, connection.writeNBT(new NBTTagCompound()));
		}

		NBTTagCompound out = new NBTTagCompound();

		out.setInteger("size", getOutputs().size());
		for (int i = 0; i < outputs.size(); i++) {
			ElectricConnection connection = outputs.get(i);
			out.setTag("output" + i, connection.writeNBT(new NBTTagCompound()));
		}

		compound.setTag("inputs", in);
		compound.setTag("outputs", out);

		return compound;
	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.GENERATOR;
	}

	@Override
	public int getMaximalInputs() {
		return 4;
	}

	@Override
	public int getMaximalOutputs() {
		return 4;
	}

	@Override
	public List<ElectricConnection> getInputs() {
		return inputs;
	}

	@Override
	public List<ElectricConnection> getOutputs() {
		return outputs;
	}

	@Override
	public long getOutput() {
		if (this.getVoltageStored() >= getMaxOutput()) {
			return getMaxOutput();
		}
		return this.getVoltageStored();
	}

	@Override
	public long getMaxOutput() {
		return 256;
	}

	@Override
	public long getOutputForConnection(ElectricConnection connection) {
		return getOutput();
	}

	@Override
	public boolean tryToConnect(ElectricConnection connection) {
		short type = -1;
		if (connection.getFrom().equals(pos)) {
			type = 0;
		} else if (connection.getTo().equals(pos)) {
			type = 1;
		}

		if (type == 0 && getOutputs().size() < getMaximalOutputs()) {
			this.outputs.add(connection);
			return true;
		}

		if (type == 1 && getInputs().size() < getMaximalInputs()) {
			this.inputs.add(connection);
			return true;
		}

		return false;
	}

	@Override
	public boolean canConnect(ElectricConnection connection) {
		short type = -1;
		if (connection.getFrom().equals(pos)) {
			type = 0;
		} else if (connection.getTo().equals(pos)) {
			type = 1;
		}

		if (type == 0 && getOutputs().size() < getMaximalOutputs()) {
			return true;
		}

		if (type == 1 && getInputs().size() < getMaximalInputs()) {
			return true;
		}

		return false;
	}

	@Override
	public long getRequiredPower() {
		return 0;
	}

	@Override
	public long getCapacity() {
		return capacity;
	}

	@Override
	public long getVoltageStored() {
		return voltage;
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

	private static final Vec3d offset = new Vec3d(0.5, 0.5, 0.5);

	@Override
	public Vec3d getWireOffset() {
		return offset;
	}

	@Override
	public boolean isPassive() {
		return false;
	}

}