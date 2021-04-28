package com.nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.advancements.ModTriggers;
import com.nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import com.nuparu.sevendaystomine.tileentity.TileEntityForge.EnumSlots;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntitySafe extends TileEntityItemHandler<ItemHandlerNameable> implements ITickable {

	public static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.safe");

	private String customName;
	public boolean locked = true;
	public boolean init = false;
	


	public TileEntitySafe() {
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(9, DEFAULT_NAME);
	}

	@Override
	public void onContainerOpened(EntityPlayer player) {
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		
		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}
		this.locked = compound.getBoolean("Locked");
		this.init = compound.getBoolean("Init");
		NBTTagList nbtList = compound.getTagList("openedBy", Constants.NBT.TAG_INT);
		for (int i = 0; i < nbtList.tagCount(); i++) {
			int id = nbtList.getIntAt(i);
			Entity entity = world.getEntityByID(id);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("Locked", this.locked);
		compound.setBoolean("Init", this.init);
		NBTTagList nbtList = new NBTTagList();
		compound.setTag("openedBy", nbtList);

		return compound;
	}

	public void setCustomInventoryName(String name) {
		this.customName = name;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	public abstract boolean tryToUnlock();

	public void unlock() {

	}

	public void lock() {

	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
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
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.getNbtCompound();
		readFromNBT(tag);
		if (hasWorld()) {
			world.notifyBlockUpdate(pos, world.getBlockState(this.pos), world.getBlockState(this.pos), 2);
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (!locked && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (!locked && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}

		return null;
	}

}
