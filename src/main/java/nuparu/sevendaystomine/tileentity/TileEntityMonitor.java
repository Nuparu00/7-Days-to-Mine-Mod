package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import nuparu.sevendaystomine.block.BlockMonitor;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntityMonitor extends TileEntity implements ITickable, IVoltage {

	private boolean on = false;
	private long voltage = 0;
	private long capacity = 40;
	private BlockPos compPos = BlockPos.ORIGIN;
	private TileEntityComputer computerTE;

	private ArrayList<EntityPlayer> lookingPlayers = new ArrayList<EntityPlayer>();

	public TileEntityMonitor() {

	}

	@SuppressWarnings("unchecked")
	public ArrayList<EntityPlayer> getLookingPlayers() {
		return (ArrayList<EntityPlayer>) lookingPlayers.clone();
	}

	public void addPlayer(EntityPlayer player) {
		lookingPlayers.add(player);
		PacketManager.syncTileEntity.sendTo(new SyncTileEntityMessage(writeToNBT(new NBTTagCompound()), pos),
				(EntityPlayerMP) player);
		if (computerTE != null) {
			PacketManager.syncTileEntity.sendTo(
					new SyncTileEntityMessage(computerTE.writeToNBT(new NBTTagCompound()), computerTE.getPos()),
					(EntityPlayerMP) player);
		}
	}

	public void removePlayer(EntityPlayer player) {
		lookingPlayers.remove(player);
	}

	@Override
	public void update() {

		// Check for available computer

		if (computerTE == null) {
			if (compPos != BlockPos.ORIGIN) {
				TileEntity TE = world.getTileEntity(pos.add(compPos));
				if (TE != null && TE instanceof TileEntityComputer
						&& ((TileEntityComputer) TE).getMonitorTE() == null) {
					computerTE = (TileEntityComputer) TE;
					computerTE.setMonitorTE(this);
				} else {
					compPos = BlockPos.ORIGIN;
				}
			} else {
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						for (int k = -1; k <= 1; k++) {
							if (i == 0 && j == 0 && k == 0)
								continue;

							BlockPos pos2 = pos.add(i, j, k);
							TileEntity TE = world.getTileEntity(pos2);

							if (TE != null && TE instanceof TileEntityComputer) {
								TileEntityComputer compTE = (TileEntityComputer) TE;
								if (compTE.getMonitorTE() != null) {
									BlockPos pos3 = compTE.getMonitorTE().getPos();
									if(pos3.getX() == pos.getX() && pos3.getY() == pos.getY() && pos3.getZ() == pos.getZ()) {
										computerTE = compTE;
										computerTE.setMonitorTE(this);
										compPos = pos2.subtract(pos);
										this.markForUpdate();
									}
								}
								else if (compTE.getMonitorTE() == null) {
									computerTE = compTE;
									computerTE.setMonitorTE(this);
									compPos = pos2.subtract(pos);
									this.markForUpdate();
									break;
								}
							}
						}
					}
				}
			}
		}

		if (world.isRemote) {
			return;
		}
		
		if(computerTE != null && world.getTileEntity(pos.add(compPos)) == null){
			computerTE = null;
			compPos = BlockPos.ORIGIN;
		}

		// handles the block/texture change
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		NBTTagCompound nbt = this.writeToNBT(new NBTTagCompound());
		if (computerTE != null) {
			if (this.isOn() && computerTE.isOn() && computerTE.isCompleted()) {
				EnumSystem system = computerTE.getSystem();
				if (system == EnumSystem.NONE) {
					if (block != ModBlocks.MONITOR_OFF) {
						world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_OFF, state));
						world.getTileEntity(pos).readFromNBT(nbt);
						if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
							TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
							computerTE.setMonitorTE(te);
							te.computerTE = computerTE;
						}
					}
				} else if (system == EnumSystem.LINUX) {
					if (block != ModBlocks.MONITOR_LINUX) {
						world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_LINUX, state));
						world.getTileEntity(pos).readFromNBT(nbt);
						if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
							TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
							computerTE.setMonitorTE(te);
							te.computerTE = computerTE;
						}
					}
				} else if (system == EnumSystem.MAC) {
					if (block != ModBlocks.MONITOR_MAC) {
						world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_MAC, state));
						world.getTileEntity(pos).readFromNBT(nbt);
						if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
							TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
							computerTE.setMonitorTE(te);
							te.computerTE = computerTE;
						}
					}
				} else if (system == EnumSystem.WIN98) {
					if (block != ModBlocks.MONITOR_WIN98) {
						world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_WIN98, state));
						world.getTileEntity(pos).readFromNBT(nbt);
						if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
							TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
							computerTE.setMonitorTE(te);
							te.computerTE = computerTE;
						}
					}
				} else if (system == EnumSystem.WINXP) {
					if (block != ModBlocks.MONITOR_WINXP) {
						world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_WINXP, state));
						world.getTileEntity(pos).readFromNBT(nbt);
						if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
							TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
							computerTE.setMonitorTE(te);
							te.computerTE = computerTE;
						}
					}
				} else if (system == EnumSystem.WIN7) {
					if (block != ModBlocks.MONITOR_WIN7) {
						world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_WIN7, state));
						world.getTileEntity(pos).readFromNBT(nbt);
						if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
							TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
							computerTE.setMonitorTE(te);
							te.computerTE = computerTE;
						}
					}
				} else if (system == EnumSystem.WIN8) {
					if (block != ModBlocks.MONITOR_WIN8) {
						world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_WIN8, state));
						world.getTileEntity(pos).readFromNBT(nbt);
						if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
							TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
							computerTE.setMonitorTE(te);
							te.computerTE = computerTE;
						}
					}
				} else if (system == EnumSystem.WIN10) {
					if (block != ModBlocks.MONITOR_WIN10) {
						world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_WIN10, state));
						world.getTileEntity(pos).readFromNBT(nbt);
						if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
							TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
							computerTE.setMonitorTE(te);
							te.computerTE = computerTE;
						}
					}
				}
				this.voltage -= this.getRequiredPower();
			} else {
				if (block != ModBlocks.MONITOR_OFF) {
					world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_OFF, state));
					world.getTileEntity(pos).readFromNBT(nbt);
					if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
						TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
						computerTE.setMonitorTE(te);
						te.computerTE = computerTE;
					}
				}
			}
		} else {
			if (block != ModBlocks.MONITOR_OFF) {
				world.setBlockState(pos, applyPropertiesToState(ModBlocks.MONITOR_OFF, state));
				world.getTileEntity(pos).readFromNBT(nbt);
				if (computerTE != null && world.getTileEntity(pos) instanceof TileEntityMonitor) {
					TileEntityMonitor te = (TileEntityMonitor) world.getTileEntity(pos);
					computerTE.setMonitorTE(te);
					te.computerTE = computerTE;
				}
			}
		}

	}
	
	public boolean isOn() {
		return this.on && this.voltage >= this.getRequiredPower();
	}

	private IBlockState applyPropertiesToState(Block newState, IBlockState oldState) {
		return applyPropertiesToState(newState.getDefaultState(), oldState);
	}

	private IBlockState applyPropertiesToState(IBlockState newState, IBlockState oldState) {
		return newState.withProperty(BlockMonitor.FACING, oldState.getValue(BlockMonitor.FACING));
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net,
			net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 1);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.compPos = BlockPos.fromLong(compound.getLong("compPos"));
		this.on = compound.getBoolean("on");
		this.voltage = compound.getLong("power");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setLong("compPos", compPos.toLong());
		compound.setBoolean("on", on);
		compound.setLong("power", voltage);
		return compound;
	}

	public void setState(boolean state) {
		this.on = state;
		markForUpdate();
	}

	public void setComputer(TileEntityComputer computer) {
		this.computerTE = computer;
		this.compPos = computer.getPos().subtract(pos);
		markForUpdate();
	}

	public void setComputerPos(BlockPos pos2) {
		this.compPos = pos2;
		markForUpdate();
	}

	public boolean getState() {
		return this.on;
	}

	public TileEntityComputer getComputer() {
		return this.computerTE;
	}

	public BlockPos getComputerPos() {
		return this.compPos;
	}

	public void markForUpdate() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		this.markDirty();
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
		return 6;
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
		if(!this.on) return 0;
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
	public boolean disconnect(IVoltage voltage) {
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
