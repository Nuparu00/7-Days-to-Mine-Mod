package nuparu.sevendaystomine.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityCarSlave extends TileEntityCar implements ITickable {

	public BlockPos masterPos;

	private TileEntityCarMaster masterTE;
	private int index = 0;

	public TileEntityCarSlave() {

	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("masterPos")) {
			this.masterPos = BlockPos.fromLong(compound.getLong("masterPos"));
		} else {
			this.masterPos = null;
		}

		if (world != null && masterPos != null) {
			TileEntity TE = world.getTileEntity(masterPos);
			if (TE != null && TE instanceof TileEntityCarMaster) {
				masterTE = (TileEntityCarMaster) TE;
			}
		}
		this.index = compound.getInteger("index");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (masterPos != null) {
			compound.setLong("masterPos", this.masterPos.toLong());
		}

		compound.setInteger("index", this.index);

		return compound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net,
			net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
		if (this.masterTE == null) {
			TileEntity TE = world.getTileEntity(masterPos);
			if (TE instanceof TileEntityCarMaster) {
				this.masterTE = (TileEntityCarMaster) TE;
			}
		}
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 1);
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

	public void setIndex(int i) {
		this.index = i;
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 1);
		markDirty();
	}

	public int getIndex() {
		return this.index;
	}

	public void setMaster(BlockPos pos, TileEntityCarMaster masterTE) {
		if (!world.isRemote) {
			this.masterPos = pos;
			this.masterTE = masterTE;
		}
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 1);
		markDirty();
	}

	@Override
	public TileEntityCarMaster getMaster() {
		if (this.masterTE == null && masterPos != null) {
			TileEntity TE = world.getTileEntity(masterPos);
			if (TE instanceof TileEntityCarMaster) {
				this.masterTE = (TileEntityCarMaster) TE;
			}
		}
		return this.masterTE;
	}

	@Override
	public void update() {

	}

}
