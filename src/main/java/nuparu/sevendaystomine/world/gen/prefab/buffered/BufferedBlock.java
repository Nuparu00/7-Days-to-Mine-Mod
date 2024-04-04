package nuparu.sevendaystomine.world.gen.prefab.buffered;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class BufferedBlock {

	private int x;
	private int y;
	private int z;
	private int meta;
	private Block block;
	private NBTTagCompound nbt;
	protected String lootTable;

	public BufferedBlock(int x, int y, int z, Block block, int meta) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setMeta(meta);
		this.setBlock(block);
		this.setNBT(null);
		this.lootTable = null;
	}

	public BufferedBlock(int x, int y, int z, Block block, int meta, NBTTagCompound nbt) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setMeta(meta);
		this.setBlock(block);
		this.setNBT(nbt);
		this.lootTable = null;
	}

	public BufferedBlock(int x, int y, int z, Block block, int meta, NBTTagCompound nbt, String lootTable) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setMeta(meta);
		this.setBlock(block);
		this.setNBT(nbt);
		this.lootTable = lootTable;
	}

	@SuppressWarnings("deprecation")
	public IBlockState getBlockState() {
		if(getBlock() == null) { return null;}
		return getBlock().getStateFromMeta(getMeta());
	}
	public BlockPos getPos(BlockPos origin) {
		return origin.add(getX(), getY(), getZ());
	}

	public NBTTagCompound getNBT() {
		return nbt;
	}

	public BufferedBlock rotate(float angle) {
		if(angle % 360 == 0) return this;
		
		int x1 = (int) Math.round(x * Math.cos(Math.toRadians(angle)) - z * Math.sin(Math.toRadians(angle)));
		int z1 = (int) Math.round(x * Math.sin(Math.toRadians(angle)) + z * Math.cos(Math.toRadians(angle)));
		return new BufferedBlock(x1, y, z1, getBlock(), getMeta(), getNBT(), lootTable);
	}
	
	public BufferedBlock flipX() {
		return new BufferedBlock(-x, y, z, getBlock(), getMeta(), getNBT(), lootTable);
	}
	public BufferedBlock flipZ() {
		return new BufferedBlock(x, y, -z, getBlock(), getMeta(), getNBT(), lootTable);
	}
	public BufferedBlock flipY() {
		return new BufferedBlock(x, -y, z, getBlock(), getMeta(), getNBT(), lootTable);
	}

	public BufferedBlock copy() {
		return new BufferedBlock(getX(), getY(), getZ(), getBlock(), getMeta(), getNBT(), lootTable);
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public int getMeta() {
		return meta;
	}

	public void setMeta(int meta) {
		this.meta = meta;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setNBT(NBTTagCompound nbt) {
		this.nbt = nbt;
	}
}
