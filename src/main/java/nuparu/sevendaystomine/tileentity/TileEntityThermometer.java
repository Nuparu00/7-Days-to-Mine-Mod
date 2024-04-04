package nuparu.sevendaystomine.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import nuparu.sevendaystomine.block.BlockThermometer;
import nuparu.sevendaystomine.util.ITemperature;

public class TileEntityThermometer extends TileEntity implements ITickable {

	EnumFacing facing;

	public TileEntityThermometer() {

	}

	@Override
	public void update() {
		boolean emit = world.getBlockState(pos).getValue(BlockThermometer.EMIT);
		if (facing == null) {
			this.facing = world.getBlockState(pos).getValue(BlockThermometer.FACING).getOpposite();
		}
		boolean flag = false;
		BlockPos blockPos = pos.offset(facing);
		TileEntity te = world.getTileEntity(blockPos);
		if (te != null) {
			
			if (te instanceof ITemperature) {
				ITemperature it = (ITemperature) te;
				if (it.getTemperature() >= 0.9) {
					flag = true;
					if (!emit) {
						world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockThermometer.EMIT, true));
					}
				}
			}
		}
		if(!flag && emit) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockThermometer.EMIT, false));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("facing", 3)) {
			this.facing = EnumFacing.getFront(compound.getInteger("facing"));
		}

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (facing != null) {
			compound.setInteger("facing", this.facing.getIndex());
		}

		return compound;
	}

}
