package nuparu.sevendaystomine.computer;

import net.minecraft.nbt.NBTTagCompound;

public class File {

	String name;
	String path;
	String code;

	public File(String name, String path, String code) {
		this.name = name;
		this.path = path;
		this.code = code;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		this.name = nbt.getString("name");
		this.path = nbt.getString("path");
		this.code = nbt.getString("code");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setString("name", name);
		nbt.setString("path", path);
		nbt.setString("code", code);
		return nbt;

	}

}
