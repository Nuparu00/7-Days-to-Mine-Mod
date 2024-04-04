package nuparu.sevendaystomine.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import nuparu.sevendaystomine.SevenDaysToMine;

public class MiscSavedData extends WorldSavedData {
	public static final String DATA_NAME = SevenDaysToMine.MODID + ":misc_data";

	protected int dim = Integer.MIN_VALUE;

	private int lastAirdrop = 0;

	public MiscSavedData() {
		super(DATA_NAME);
	}

	public MiscSavedData(String s) {
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		dim = compound.getInteger("dim");
		setLastAirdrop(compound.getInteger("lastAirdrop"));

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();

		compound.setInteger("dim", dim);
		compound.setInteger("lastAirdrop", getLastAirdrop());
		return compound;
	}

	public static MiscSavedData get(World world) {
		if (world != null) {
			MiscSavedData data = (MiscSavedData) world.getPerWorldStorage().getOrLoadData(MiscSavedData.class,
					DATA_NAME);
			if (data == null) {
				data = new MiscSavedData();
				world.getPerWorldStorage().setData(DATA_NAME, (WorldSavedData) data);
			}
			return data;
		}
		return null;
	}

	public int getLastAirdrop() {
		return lastAirdrop;
	}

	public void setLastAirdrop(int lastAirdrop) {
		this.lastAirdrop = lastAirdrop;
		this.setDirty(true);
	}

}
