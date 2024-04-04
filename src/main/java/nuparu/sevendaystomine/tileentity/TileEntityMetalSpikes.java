package nuparu.sevendaystomine.tileentity;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;

public class TileEntityMetalSpikes extends TileEntity{

	public int health = 900;
	private boolean retracted = true;
	
	public TileEntityMetalSpikes() {
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.health = compound.getInteger("health");
		this.retracted = compound.getBoolean("retracted");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);
		compound.setInteger("health", this.health);
		compound.setBoolean("retracted", retracted);
		return compound;
	}

	public void dealDamage(int damage) {
		if(world.isRemote) return;
		health-=damage;
		if(health <= 0) {
			world.destroyBlock(pos, false);
		}
	}
	
	public void setRetracted(boolean state) {
		if(state != retracted) {
			retracted = state;
			world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_SHEEP_SHEAR,
					SoundCategory.BLOCKS, 1.5f, 0.5f);
			markForUpdate();
		}
	}
	
	public void markForUpdate() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		markDirty();
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

	public boolean isRetracted() {
		return retracted;
	}
}
