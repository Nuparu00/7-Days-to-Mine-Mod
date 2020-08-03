package com.nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.electricity.EnumDeviceType;
import com.nuparu.sevendaystomine.electricity.IVoltage;
import com.nuparu.sevendaystomine.util.ITemperature;
import com.nuparu.sevendaystomine.util.ModConstants;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityGeneratorBase extends TileEntityLockableLoot
		implements ISidedInventory, IVoltage, ITemperature, ITickable {

	protected NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
	protected String customName;

	protected List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	protected List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();

	protected int burnTime;
	protected int currentBurnTime;
	protected int cookTime;
	protected int totalCookTime;

	protected double temperature = 0;
	protected double temperatureLimit = 0.6;
	protected static final double basePower = 250;


	public boolean isBurning = false;

	protected long capacity = 100000l;
	protected long voltage = 0;

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
		this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.inventory);

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}

		isBurning = compound.getBoolean("isBurning");
		temperature = compound.getDouble("temperature");
		temperatureLimit = compound.getDouble("tempLimit");
		voltage = compound.getLong("voltage");

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
		ItemStackHelper.saveAllItems(compound, this.inventory);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.customName);
		}

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
	public int getSizeInventory() {
		return this.inventory.size();
	}

	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
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
		return ItemStackHelper.getAndSplit(this.inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventory.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
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
	public void clear() {
		this.inventory.clear();
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
	public int[] getSlotsForFace(EnumFacing side) {
		if(side==EnumFacing.UP) {
			return new int[] {0};
		}
		return new int[]{};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
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
		return 300;
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

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inv) {
		return inv.getField(0) > 0;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.burnTime;
		case 1:
			return this.currentBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}

	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}

	}

	@Override
	public int getFieldCount() {
		return 4;
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
}
