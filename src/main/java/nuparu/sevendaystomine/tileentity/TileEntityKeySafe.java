package nuparu.sevendaystomine.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nuparu.sevendaystomine.inventory.container.ContainerSmall;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;

public class TileEntityKeySafe extends TileEntitySafe {

	//-180 -- +180
	private float lockAngle;
	//In degrees
	private final float tolerance = 10;

	//-180 -- +180
	private float sweetPoint;
	
	private float force;
	
	private final float forceThreshold = 150;

	public TileEntityKeySafe() {
	}

	public boolean tryToUnlock() {
		if(Math.abs(lockAngle-sweetPoint)<=tolerance && force >= forceThreshold) {
			return true;
		}
		return false;
	}

	public void unlock() {

	}

	public void lock() {

	}
	
	public boolean tryToBreakPick(EntityPlayer player) {
		float diff = Math.abs(sweetPoint-lockAngle);
		if(force >= forceThreshold*(1-diff/180f)) {
			return true;
		}
		return false;
	}

	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.lockAngle = compound.getFloat("LockAngle");
		this.sweetPoint = compound.getFloat("SweetPoint");
		this.force = compound.getFloat("Force");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setFloat("LockAngle", this.lockAngle);
		compound.setFloat("SweetPoint", this.sweetPoint);
		compound.setFloat("Force", this.force);
		
		return compound;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		nbtTag.removeTag("SweetPoint");
		return new SPacketUpdateTileEntity(getPos(), 0, nbtTag);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = writeToNBT(new NBTTagCompound());
		nbt.removeTag("SweetPoint");
		return nbt;
	}
	
	public String superSecretMethod() {
		return sweetPoint + " +- " + tolerance;
	}
	
	public void setAngle(float angle) {
		this.lockAngle = angle;
	}
	public void setForce(float force) {
		this.force = force;
	}
	
	public float getForce() {
		return this.force;
	}
	
	@Override
	public Container createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerSmall(playerInventoryWrapper, inventory, player, this);
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}

}
