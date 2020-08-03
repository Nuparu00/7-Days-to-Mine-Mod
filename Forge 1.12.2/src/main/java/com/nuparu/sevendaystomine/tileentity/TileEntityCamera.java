package com.nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import com.nuparu.sevendaystomine.electricity.network.INetwork;
import com.nuparu.sevendaystomine.entity.EntityCameraView;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class TileEntityCamera extends TileEntity implements ITickable, INetwork {

	private ArrayList<BlockPos> network = new ArrayList<BlockPos>();
	private ArrayList<EntityPlayer> viewers = new ArrayList<EntityPlayer>();
	private String customName;

	public static TileEntityCamera TEST;

	private EntityCameraView cameraView;

	public TileEntityCamera() {
		TEST = this;
	}

	@Override
	public void update() {
		if (cameraView == null && world != null) {
			cameraView = new EntityCameraView(world, pos.getX(), pos.getY(), pos.getZ(), this);
			if (!world.isRemote) {
				world.spawnEntity(cameraView);
			}
		}
	}

	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomInventoryName(String name) {
		this.customName = name;
	}

	public String getCustomName() {
		return this.customName;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (hasCustomName()) {
			compound.setString("custom_name", customName);
		}
		NBTTagList list = new NBTTagList();
		for (BlockPos net : getConnections()) {
			list.appendTag(new NBTTagLong(net.toLong()));
		}
		compound.setTag("network", list);

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("custom_name", 8)) {
			customName = compound.getString("custom_name");
		}
		if (world != null) {
			network.clear();
			NBTTagList list = compound.getTagList("network", Constants.NBT.TAG_LONG);
			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagLong nbt = (NBTTagLong) list.get(i);
				BlockPos blockPos = BlockPos.fromLong(nbt.getLong());
				network.add(blockPos);
			}
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

	public void closeView(EntityPlayer player) {
		viewers.remove(player);
	}

	public float getHeadRotation() {
		return (float) (90f * Math.sin(world.getTotalWorldTime() / 450d));
	}

	public float getHeadRotationPrev() {
		return (float) (90f * Math.sin(world.getTotalWorldTime() / 450d));
	}

	public EntityCameraView getCameraView(EntityPlayer player) {
		viewers.add(player);
		cameraView.prevRotationYaw = cameraView.rotationYaw;
		cameraView.rotationYaw = cameraView.initRotation + getHeadRotation() * cameraView.direction;
		return cameraView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlockPos> getConnections() {
		return (List<BlockPos>) network.clone();
	}

	@Override
	public void connectTo(INetwork toConnect) {
		if (!isConnectedTo(toConnect)) {
			network.add(toConnect.getPosition());
			toConnect.connectTo(this);
			markDirty();
		}
	}

	@Override
	public void disconnect(INetwork toDisconnect) {
		if (isConnectedTo(toDisconnect)) {
			network.remove(toDisconnect.getPosition());
			toDisconnect.disconnect(this);
			markDirty();
		}
	}

	@Override
	public boolean isConnectedTo(INetwork net) {
		return network.contains(net.getPosition());
	}

	@Override
	public void disconnectAll() {
		for (BlockPos pos : getConnections()) {
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof INetwork) {
				((INetwork)te).disconnect(this);
			}
		}
	}

	@Override
	public BlockPos getPosition() {
		return this.getPos();
	}

}
