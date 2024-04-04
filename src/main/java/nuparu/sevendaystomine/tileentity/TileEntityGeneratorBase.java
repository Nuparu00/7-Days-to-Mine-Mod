package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.ITemperature;
import nuparu.sevendaystomine.util.ModConstants;
import nuparu.sevendaystomine.util.Utils;

public abstract class TileEntityGeneratorBase extends TileEntityItemHandler<ItemHandlerNameable> implements IVoltage, ITemperature, ITickable {


	protected List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	protected List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();

	protected int burnTime;
	private int currentBurnTime;
	protected int cookTime;
	protected int totalCookTime;

	protected double temperature = 0;
	protected double temperatureLimit = 0.6;
	protected static final double basePower = 6;


	public boolean isBurning = false;

	protected long capacity = 100000l;
	protected long voltage = 0;
	
	public int soundCounter = 90;

	public TileEntityGeneratorBase() {

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

		isBurning = compound.getBoolean("isBurning");
		temperature = compound.getDouble("temperature");
		temperatureLimit = compound.getDouble("tempLimit");
		voltage = compound.getLong("voltage");
		
		this.burnTime = compound.getInteger("BurnTime");
		this.currentBurnTime = Utils.getItemBurnTime(this.getInventory().getStackInSlot(0));
		
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

		compound.setInteger("BurnTime", (short) this.burnTime);
		compound.setBoolean("isBurning", isBurning);
		compound.setDouble("temperature", temperature);
		compound.setDouble("temperatureLimit", temperatureLimit);
		compound.setLong("voltage", voltage);

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

	public int countAdjacentBlocks(Block block) {
		int count = 0;
		for (int i = 0; i < 6; i++) {
			if (this.world.getBlockState(this.pos.offset(EnumFacing.VALUES[i])).getBlock() == block) {
				count++;
			}
		}
		return count;
	}

	public int countAdjacentMats(Material mat) {
		int count = 0;
		for (int i = 0; i < 6; i++) {
			if (this.world.getBlockState(this.pos.offset(EnumFacing.VALUES[i])).getMaterial() == mat) {
				count++;
			}
		}
		return count;
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
	public void setTemperature(double temperature) {
		this.temperature = temperature;

	}

	@Override
	public void addTemperature(double delta) {
		this.temperature += delta;
	}

	@Override
	public double getTemperature() {
		return temperature;
	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.GENERATOR;
	}

	@Override
	public int getMaximalInputs() {
		return 0;
	}

	@Override
	public int getMaximalOutputs() {
		return 4;
	}

	@Override
	public List<ElectricConnection> getInputs() {
		return new ArrayList<ElectricConnection>(inputs);
	}

	@Override
	public List<ElectricConnection> getOutputs() {
		return new ArrayList<ElectricConnection>(outputs);
	}

	@Override
	public long getOutput() {
		if (this.getVoltageStored() >= getMaxOutput()) {
			return getMaxOutput();
		}
		return this.getVoltageStored();
	}

	public long getPowerPerUpdate() {
		long out = this.isBurning ? (long) (basePower + (basePower * temperature)) : 0;
		if (out > getMaxOutput()) {
			out = getMaxOutput();
		}
		return out;
	}

	@Override
	public long getMaxOutput() {
		return 120;
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

	public boolean isBurning() {
		return this.burnTime > 0;
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
	
	private static final Vec3d offset = new Vec3d(0.5, 0.5, 0.5);
	
	@Override
	public Vec3d getWireOffset() {
		return offset;
	}
	
	@Override
	public boolean isPassive() {
		return false;
	}

	public int getCurrentBurnTime() {
		return currentBurnTime;
	}
	
	public int getBurnTime() {
		return burnTime;
	}
	
	public void setDisplayName(String displayName) {
		inventory.setDisplayName(new TextComponentString(displayName));
	}
	
	@Override
	@Nullable
    public ITextComponent getDisplayName()
    {
        return inventory.getDisplayName();
    }
	
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.burnTime;
		case 1:
			return this.currentBurnTime;
		case 2:
			return (int) this.voltage;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentBurnTime = value;
			break;
		case 3:
			this.voltage = value;
		}
	}

	public int getFieldCount() {
		return 3;
	}
}
