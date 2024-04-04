package nuparu.sevendaystomine.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import nuparu.sevendaystomine.block.BlockTorchEnhanced;
import nuparu.sevendaystomine.config.ModConfig;

public class TileEntityTorch extends TileEntity implements ITickable {
	public int age = 0;

	public TileEntityTorch() {

	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.age = compound.getInteger("age");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("age", this.age);
		return compound;
	}

	@Override
	public void update() {
		if (world != null) {
			if ((this.age++ >= ModConfig.world.torchBurnTime && ModConfig.world.torchBurnTime != -1) || (ModConfig.world.torchRainExtinguish && world.isRaining() && world.canSeeSky(this.pos))) {
				BlockTorchEnhanced.extinguish(world, pos);
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
