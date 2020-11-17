package com.nuparu.sevendaystomine.tileentity;

import com.nuparu.sevendaystomine.block.BlockTorchEnhanced;

import net.minecraft.util.ITickable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTorch extends TileEntity implements ITickable {
	public int temp = 22000;

	public TileEntityTorch() {

	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.temp = compound.getInteger("temp");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("temp", this.temp);
		return compound;
	}

	@Override
	public void update() {
		if (world != null) {
			if (this.temp <= 0 || (world.isRaining() && world.canSeeSky(this.pos))) {
				BlockTorchEnhanced.extinguish(world, pos);
			} else {
				--temp;
			}
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
	    NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
	    return new SPacketUpdateTileEntity(getPos(), 0, nbtTag);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
	    NBTTagCompound tag = pkt.getNbtCompound();
	    readFromNBT(tag);
	    if(hasWorld()) {
	    	world.notifyBlockUpdate(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), 2);
	    }
	}
}
