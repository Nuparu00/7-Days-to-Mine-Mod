package nuparu.sevendaystomine.util.json;

import com.google.gson.annotations.SerializedName;

public class JsonBlock {

	protected int x;
	protected int y;
	protected int z;
	@SerializedName("metadata")
	protected int meta;
	@SerializedName("tile")
	protected String block;
	@SerializedName("NBTData")
	protected String nbt;
	@SerializedName("loottable")
	protected String lootTable;

	public JsonBlock(int x, int y, int z, String block, int meta) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.meta = meta;
		this.block = block;
		this.nbt = null;
		this.lootTable = null;
	}

	public JsonBlock(int x, int y, int z, String block, int meta, String nbt) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.meta = meta;
		this.block = block;
		this.nbt = nbt;
		this.lootTable = null;
	}

	public JsonBlock(int x, int y, int z, String block, int meta, String nbt, String lootTable) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.meta = meta;
		this.block = block;
		this.nbt = nbt;
		this.lootTable = lootTable;
	}
}
