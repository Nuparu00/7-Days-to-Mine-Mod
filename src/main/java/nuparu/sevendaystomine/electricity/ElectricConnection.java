package nuparu.sevendaystomine.electricity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ElectricConnection {

	private BlockPos from;
	private BlockPos to;

	public ElectricConnection() {
		this.from = BlockPos.ORIGIN;
		this.to = BlockPos.ORIGIN;
	}

	public ElectricConnection(BlockPos from, BlockPos to) {
		this.from = from;
		this.to = to;
	}

	public NBTTagCompound writeNBT(NBTTagCompound nbt) {
		nbt.setLong("from", from.toLong());
		nbt.setLong("to", to.toLong());
		return nbt;
	}

	public void readNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("from", Constants.NBT.TAG_LONG)) {
			from = BlockPos.fromLong(nbt.getLong("from"));
		}
		if (nbt.hasKey("to", Constants.NBT.TAG_LONG)) {
			to = BlockPos.fromLong(nbt.getLong("to"));
		}
	}

	public BlockPos getFrom() {
		return this.from;
	}

	public BlockPos getTo() {
		return this.to;
	}
	
	public double getDistance() {
		return from.getDistance(to.getX(),to.getY(),to.getZ());
	}

	@Override
	public String toString() {
		return "ElectricConnection [ " + from.getX() + ";" + from.getY() + ";" + from.getZ() + " //" + to.getX() + ";"
				+ to.getY() + ";" + to.getZ() + "]";
	}

	public IVoltage getFrom(World world) {
		TileEntity te = world.getTileEntity(getFrom());
		if (te != null && te instanceof IVoltage) {
			return (IVoltage) te;
		}
		return null;
	}

	public IVoltage getTo(World world) {
		TileEntity te = world.getTileEntity(getTo());
		if (te != null && te instanceof IVoltage) {
			return (IVoltage) te;
		}
		return null;
	}

	public Vec3d getRenderFrom(World world) {
		if (world == null)
			return new Vec3d(from.getX(), from.getY(), from.getZ());

		IVoltage voltage = getFrom(world);
		if (voltage == null) {
			return new Vec3d(from.getX(), from.getY(), from.getZ());
		}

		return voltage.getWireOffset().addVector(from.getX(), from.getY(), from.getZ());
	}

	public Vec3d getRenderTo(World world) {
		if (world == null)
			return new Vec3d(to.getX(), to.getY(), to.getZ());

		IVoltage voltage = getTo(world);
		if (voltage == null) {
			return new Vec3d(to.getX(), to.getY(), to.getZ());
		}


		return voltage.getWireOffset().addVector(to.getX(), to.getY(), to.getZ());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElectricConnection other = (ElectricConnection) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
}
