package nuparu.sevendaystomine.block.repair;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.BreakSyncMessage;

public class BreakSavedData extends WorldSavedData {
	public static final String DATA_NAME = SevenDaysToMine.MODID + ":break_data";
	private ArrayList<BreakData> list = new ArrayList<BreakData>();

	long lastUpdate = 0;

	public BreakSavedData() {
		super(DATA_NAME);
	}

	public BreakSavedData(String s) {
		super(s);
	}

	public void addBreakData(BlockPos pos, World world, float delta) {
		if (list == null) {
			list = new ArrayList<BreakData>();
		}
		if (delta <= 0f) {
			return;
		}
		ArrayList<BreakData> list = getList();
		for (BreakData data : list) {
			if (data.pos == (pos.toLong())) {
				float original = data.state;
				data.state += delta;
				data.lastChange = world.getTotalWorldTime();
				markDirty();
				if ((int) (Math.floor(original * 10f)) != (int) (Math.floor((original + delta) * 10f))) {
					NBTTagCompound nbt = new NBTTagCompound();
					writeToNBT(nbt);
					PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt),
							world.provider.getDimension());
				}
				return;
			}
		}
		this.list.add(new BreakData(pos.toLong(), world.getTotalWorldTime(), delta));
		markDirty();
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt), world.provider.getDimension());
		return;
	}

	public void setBreakData(BlockPos pos, World world, float state) {
		if (list == null) {
			list = new ArrayList<BreakData>();
		}
		if (state == 0f) {
			removeBreakData(pos, world);
			return;
		}
		ArrayList<BreakData> list = getList();
		for (BreakData data : list) {
			if (data.pos == (pos.toLong())) {
				float original = data.state;
				data.state = state;
				data.lastChange = world.getTotalWorldTime();
				markDirty();
				if ((int) (Math.floor(original * 10f)) != (int) (Math.floor(state * 10f))) {
					NBTTagCompound nbt = new NBTTagCompound();
					writeToNBT(nbt);
					PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt),
							world.provider.getDimension());
				}
				return;
			}
		}
		this.list.add(new BreakData(pos.toLong(), world.getTotalWorldTime(), state));
		markDirty();
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt), world.provider.getDimension());
		return;
	}

	public void removeBreakData(BlockPos pos, World world) {
		if (list == null) {
			list = new ArrayList<BreakData>();
		}
		ArrayList<BreakData> list = getList();
		Iterator<BreakData> it = list.iterator();
		while (it.hasNext()) {
			BreakData data = (BreakData) it.next();
			if (data.pos == (pos.toLong())) {
				this.list.remove(data);
				markDirty();
				NBTTagCompound nbt = new NBTTagCompound();
				writeToNBT(nbt);
				PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(nbt), world.provider.getDimension());
				return;
			}
		}
	}

	public BreakData getBreakData(BlockPos pos, int dim) {
		if (list == null) {
			list = new ArrayList<BreakData>();
		}
		ArrayList<BreakData> list = getList();
		;
		Iterator<BreakData> it = list.iterator();
		while (it.hasNext()) {
			BreakData data = (BreakData) it.next();
			if (data.pos == (pos.toLong())) {
				return data;
			}
		}
		return null;
	}

	public void sync(EntityPlayerMP player) {
		PacketManager.blockBreakSync.sendTo(new BreakSyncMessage(writeToNBT(new NBTTagCompound())), player);
	}

	public void update(World world) {
		int decayRate = world.getGameRules().getInt("damageDecayRate");

		if (decayRate <= 0) {
			decayRate = ModConfig.world.damageDecayRate;
		}
		if (decayRate <= 0) {
			return;
		}
		if (Math.abs(world.getTotalWorldTime() - lastUpdate) <= decayRate)
			return;
		if (list == null) {
			list = new ArrayList<BreakData>();
		}

		ArrayList<BreakData> list = getList();

		ArrayList<BreakData> toRemove = new ArrayList<BreakData>();

		Iterator<BreakData> it = list.iterator();
		boolean dirty = false;
		while (it.hasNext()) {
			BreakData data = (BreakData) it.next();
			if (Math.abs(world.getTotalWorldTime() - data.lastChange) >= decayRate) {
				if (data.state > 0.4) {
					data.state += 0.1f;
					dirty = true;
					if (data.state >= 1) {
						world.destroyBlock(BlockPos.fromLong(data.pos), false);
						toRemove.add(data);
						continue;
					}
					data.lastChange = world.getTotalWorldTime();
				} else {
					data.state -= 0.1f;
					dirty = true;
					if (data.state <= 0) {
						toRemove.add(data);
						continue;
					}
					data.lastChange = world.getTotalWorldTime();
				}
			}
		}

		if (this.list != null) {
			this.list.removeAll(toRemove);
		}

		lastUpdate = world.getTotalWorldTime();
		if (dirty) {
			markDirty();
			PacketManager.blockBreakSync.sendToDimension(new BreakSyncMessage(writeToNBT(new NBTTagCompound())),
					world.provider.getDimension());
		}
	}

	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound tag = nbt.getCompoundTag("list");
		list = BreakHelper.readFromNBT(tag);
		lastUpdate = nbt.getLong("lastUpdate");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (list == null) {
			list = new ArrayList<BreakData>();
		}
		NBTTagCompound tag = BreakHelper.writeToNBT(list);
		nbt.setTag("list", tag);
		nbt.setLong("lastUpdate", lastUpdate);
		return nbt;
	}

	public static BreakSavedData get(World world) {
		if (world != null) {
			BreakSavedData data = (BreakSavedData) world.getPerWorldStorage().getOrLoadData(BreakSavedData.class,
					DATA_NAME);
			if (data == null) {
				data = new BreakSavedData();
				world.getPerWorldStorage().setData(DATA_NAME, (WorldSavedData) data);
			}
			return data;
		}
		return null;
	}

	public ArrayList<BreakData> getList() {
		return list == null ? new ArrayList<BreakData>() : (ArrayList<BreakData>) list.clone();
	}

}