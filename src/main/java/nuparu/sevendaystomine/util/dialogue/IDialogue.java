package nuparu.sevendaystomine.util.dialogue;

import net.minecraft.nbt.NBTTagCompound;

public interface IDialogue {

	public String getUnloclaizedName();
	public void setUnlocalizedName(String unlocalizedName);
	public String getLocalizedText();
	
	public void readFromNBT(NBTTagCompound nbt);
	public NBTTagCompound writeToNBT(NBTTagCompound nbt);
}
