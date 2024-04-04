package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.inventory.container.ContainerBatteryStation;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import nuparu.sevendaystomine.util.ModConstants;
import nuparu.sevendaystomine.util.Utils;

public class TileEntityBatteryStation extends TileEntityItemHandler<ItemStackHandler> implements ITickable, IVoltage {
	private List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	private List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();
	private long capacity = 10000;
	private long voltage = 0;

	public TileEntityBatteryStation() {

	}

	@Override
	public void update() {

		// 
		if(world.isRemote)return;
		boolean flag = false;

		if (voltage < capacity) {
			ItemStack input = inventory.getStackInSlot(0);
			if (!input.isEmpty() && input.getItem() instanceof IBattery) {
				IBattery battery = (IBattery) input.getItem();
				long inputVoltage = battery.getVoltage(input, world);
				if (inputVoltage > 0) {
					long deltaVoltage = Math.min((capacity - voltage), Math.min(10, inputVoltage));
					if (deltaVoltage > 0) {
						battery.drainVoltage(input, world, deltaVoltage);
						this.voltage += deltaVoltage;
						flag = true;
					}
				}
			}
		}

		ItemStack output = inventory.getStackInSlot(1);
		if (!output.isEmpty() && output.getItem() instanceof IBattery) {
			IBattery battery = (IBattery) output.getItem();
			long outputVoltage = battery.getVoltage(output, world);
			long outputCapacity = battery.getCapacity(output, world);
			if (outputVoltage < outputCapacity) {
				long deltaVoltage = Math.min((outputCapacity - outputVoltage), Math.min(10, voltage));
				if (deltaVoltage > 0) {
					this.voltage -= (deltaVoltage - battery.tryToAddVoltage(output, world, deltaVoltage));
					flag = true;
				}

			}
		}

		Iterator<ElectricConnection> iterator = outputs.iterator();
		while (iterator.hasNext()) {
			ElectricConnection connection = iterator.next();
			IVoltage voltage = connection.getTo(world);
			if (voltage != null) {
				long l = voltage.tryToSendPower(getOutputForConnection(connection), connection);
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
			this.markForUpdate();
		}
	}
	
	public void markForUpdate() {
        if (world != null && pos != null) {
            world.markBlockRangeForRenderUpdate(pos, pos);
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
        }
        this.markDirty();
    }

	@Override
	protected ItemStackHandler createInventory() {
		return new ItemStackHandler(2);
	}

	@Override
	public ContainerBatteryStation createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerBatteryStation(playerInventoryWrapper, inventory, player, this);
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
		world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, Blocks.AIR.getDefaultState(), world.getBlockState(pos), 1);
        world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
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

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.STORAGE;
	}

	@Override
	public int getMaximalInputs() {
		return 16;
	}

	@Override
	public int getMaximalOutputs() {
		return 164;
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
		return 80;
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
		if(realDelta > 0) {
			markForUpdate();
		}

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

	@Override
	public void onContainerOpened(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
	}

	public NonNullList<ItemStack> getDrops() {
		return Utils.dropItemHandlerContents(inventory, getWorld().rand);
	}

	@Override
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean disconnect(IVoltage voltage) {
		for(ElectricConnection input : getInputs()) {
			if(input.getFrom().equals(voltage.getPos())) {
				this.inputs.remove(input);
				markDirty();
				world.markBlockRangeForRenderUpdate(pos, pos);
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
				world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
				return true;
			}
		}
		
		for(ElectricConnection output : getOutputs()) {
			if(output.getTo().equals(voltage.getPos())) {
				this.outputs.remove(output);
				markDirty();
				world.markBlockRangeForRenderUpdate(pos, pos);
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
				world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		long toAdd = Math.min(this.capacity-this.voltage, maxReceive);
		if(!simulate) {
			this.voltage+=toAdd;
		}
		return (int)toAdd;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		long toExtract = Math.min(this.voltage, maxExtract);
		if(!simulate) {
			this.voltage-=toExtract;
		}
		return (int)toExtract;
	}

	@Override
	public int getEnergyStored() {
		return (int) this.voltage;
	}

	@Override
	public int getMaxEnergyStored() {
		return (int) this.capacity;
	}

	@Override
	public boolean canExtract() {
		return this.capacity > 0;
	}

	@Override
	public boolean canReceive() {
		return this.voltage < this.capacity;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this);
		}

		return super.getCapability(capability, facing);
	}

}
