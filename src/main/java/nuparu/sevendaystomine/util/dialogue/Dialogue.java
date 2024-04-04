package nuparu.sevendaystomine.util.dialogue;

import net.minecraft.nbt.NBTTagCompound;
import nuparu.sevendaystomine.SevenDaysToMine;

public class Dialogue implements IDialogue {

	private String unlocalizedName;

	public Dialogue(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String getUnloclaizedName() {
		return this.unlocalizedName;
	}

	@Override
	public void setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String getLocalizedText() {
		return SevenDaysToMine.proxy.localize(unlocalizedName + ".text");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.unlocalizedName = nbt.getString("name");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setString("name", this.unlocalizedName);
		return nbt;
	}

}
