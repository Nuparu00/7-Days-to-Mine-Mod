package nuparu.sevendaystomine.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityBigSignSlave extends TileEntity {

	protected BlockPos parent = BlockPos.ORIGIN;
	protected boolean slave = false;

	public TileEntityBigSignSlave() {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setLong("parent", this.parent.toLong());
		compound.setBoolean("slave", this.slave);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.slave = compound.getBoolean("slave");
		this.parent = BlockPos.fromLong(compound.getLong("parent"));
	}
	
	public void setParent(BlockPos pos) {
		this.parent = pos;
	}
	
	public BlockPos getParent() {
		return this.parent;
	}
}
