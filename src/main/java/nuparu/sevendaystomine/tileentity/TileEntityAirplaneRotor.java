package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import nuparu.sevendaystomine.block.BlockAirplaneRotor;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntityAirplaneRotor extends TileEntity implements ITickable, IVoltage {
	private List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	private List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();
	private long capacity = 1000l;
	private long voltage = 0;

	public float speed;
	public double voltPre;
	public boolean on;
	public float range = 8f;
	public float thrust = 0f;
	public float angle;
	public float anglePrev;
	private long nextSound = (long) 0;
	private AxisAlignedBB windBB;

	EnumFacing facing;

	public TileEntityAirplaneRotor() {

	}

	@Override
	public void update() {

		boolean dirty = false;


		anglePrev = angle;

		if (facing == null) {
			this.facing = world.getBlockState(pos).getValue(BlockAirplaneRotor.FACING).getOpposite();
			dirty = true;
		}

		if (thrust < 0f) {
			thrust = 0f;
			dirty = true;
		}

		if (on) {
			if (voltage >= getRequiredPower()) {
				float thrustOld = thrust;
				if (thrust < 1f * (voltage / getRequiredPower())) {
					thrust += 0.01f;
				}
				else {
					thrust = 1f * (voltage / getRequiredPower());
				}
				if(Math.abs(thrust-thrustOld) >= 0.001) {
					dirty = true;
				}
				voltage-=getRequiredPower();
			}
		}
		if (!on || voltage < getRequiredPower()) {
			if (thrust > 0.004) {
				thrust -= 0.1f;
				dirty = true;
			} else {
				thrust = 0;
				dirty = true;
			}

		}

		if (thrust != 0) {

			final double maxForce = thrust*0.004;
			range = (float) Math.PI * thrust*0.4f;
			speed = (float) (3.6 * (thrust));
			angle += 45;
			dirty = true;

			List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, getEntitySearchBox());

			if (!entities.isEmpty()) {

				double angle = Math.toRadians(getBlowAngle() - 90);
				final Vec3d blockPos = MathUtils.getConeApex(pos, angle);
				final Vec3d basePos = MathUtils.getConeBaseCenter(pos, angle, range);
				final Vec3d coneAxis = new Vec3d(basePos.x - blockPos.x, basePos.y - blockPos.y,
						basePos.z - blockPos.z);

				for (Entity entity : entities) {

					if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).capabilities.isFlying) {

						Vec3d directionVec = new Vec3d(entity.posX - blockPos.x, entity.posY - blockPos.y,
								entity.posZ - blockPos.z);

						if (MathUtils.isInCone(coneAxis, directionVec, 0.6)) {
							final double distToOrigin = directionVec.lengthVector();
							final double force = (1.0 - distToOrigin / range) * maxForce;
							if (force <= 0)
								continue;
							Vec3d normal = directionVec.normalize();
							entity.motionX += force * normal.x;
							entity.motionZ += force * normal.z;
						}
					}
				}
				BlockPos front = pos.offset(facing);
				List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class,
						new AxisAlignedBB((double) front.getX(), (double) front.getY(), (double) front.getZ(),
								(double) (front.getX() + 1), (double) (front.getY() + 1), (double) (front.getZ() + 1)));
				for (int i = 0; i < list.size(); i++) {
					EntityLivingBase entity = list.get(i);

					entity.attackEntityFrom(DamageSources.blade, 3);

				}
			}

		}
		if(dirty) {
			markDirty();
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
			world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
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

		this.anglePrev = compound.getFloat("anglePrev");
		this.angle = compound.getFloat("angle");
		this.speed = compound.getFloat("speed");
		this.range = compound.getFloat("range");
		this.thrust = compound.getFloat("thrust");
		this.on = compound.getBoolean("on");
		if (compound.hasKey("facing", 3)) {
			this.facing = EnumFacing.getFront(compound.getInteger("facing"));
		}

		this.voltage = compound.getLong("power");
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

		compound.setFloat("anglePrev", this.anglePrev);
		compound.setFloat("angle", this.angle);
		compound.setFloat("speed", this.speed);
		compound.setFloat("range", this.range);
		compound.setFloat("thrust", this.thrust);
		compound.setBoolean("on", this.on);
		if (facing != null) {
			compound.setInteger("facing", this.facing.getIndex());
		}

		compound.setLong("power", voltage);

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

	public void switchState() {
		this.on = !this.on;
		this.markDirty();
	}

	private AxisAlignedBB getEntitySearchBox() {
		windBB = null;
		if (windBB != null) {
			return windBB;
		}
		BlockPos posStart = pos.offset(facing).offset(facing.rotateYCCW(),1).down(1);
		BlockPos posEnd = pos.offset(facing,(int) range).offset(facing.rotateY(),1).up(1);
		windBB = new AxisAlignedBB(posStart.getX() ,posStart.getY(),posStart.getZ(),posEnd.getX()+1,posEnd.getY()+1,posEnd.getZ()+1);

		return windBB;
	}

	public double getBlowAngle() {
		switch (facing) {
		case NORTH:
			return 0d;
		case WEST:
			return 270d;
		case SOUTH:
			return 180d;
		case EAST:
			return 90d;
		default:
			return 0d;

		}

	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.CONSUMER;
	}

	@Override
	public int getMaximalInputs() {
		return 4;
	}

	@Override
	public int getMaximalOutputs() {
		return 0;
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
		return 60;
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

	@Override
	public boolean disconnect(IVoltage voltage) {
		for (ElectricConnection input : getInputs()) {
			if (input.getFrom().equals(voltage.getPos())) {
				this.inputs.remove(input);
				markDirty();
				world.markBlockRangeForRenderUpdate(pos, pos);
				world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
				world.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
				return true;
			}
		}

		for (ElectricConnection output : getOutputs()) {
			if (output.getTo().equals(voltage.getPos())) {
				
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
