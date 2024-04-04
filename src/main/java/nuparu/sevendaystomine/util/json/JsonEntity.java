package nuparu.sevendaystomine.util.json;

import com.google.gson.annotations.SerializedName;

public class JsonEntity {

	@SerializedName("entity")
    protected String name;
	protected double x;
	protected double y;
	protected double z;
	protected float yaw;
	protected float pitch;
	@SerializedName("NBTData")
	protected String nbt;

	public JsonEntity(String name, double x, double y, double z, float yaw, float pitch, String nbt) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.nbt = nbt;
	}

}
