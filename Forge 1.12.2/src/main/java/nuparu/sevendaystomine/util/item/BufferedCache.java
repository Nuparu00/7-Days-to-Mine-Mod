package nuparu.sevendaystomine.util.item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class BufferedCache implements Serializable {
	byte[] longItem = null;
	byte[] shortItem_L = null;
	byte[] shortItem_R = null;
	byte[] backpack = null;

	public byte[] writeItemStack(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		NBTTagCompound nbt = new NBTTagCompound();
		stack.writeToNBT(nbt);
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			CompressedStreamTools.writeCompressed(nbt, dos);
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dos != null)
					dos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (baos != null)
					baos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	public ItemStack readBytes(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		ItemStack stack = null;
		NBTTagCompound nbt = null;
		ByteArrayInputStream bis = null;
		DataInputStream dis = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			dis = new DataInputStream(bis);
			nbt = CompressedStreamTools.readCompressed(dis);
			stack = new ItemStack(nbt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (dis != null)
					dis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (bis != null)
					bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stack;
	}

	public void writeLongItem(ItemStack stack) {
		longItem = writeItemStack(stack);
	}

	public void writeShortItem_L(ItemStack stack) {
		shortItem_L = writeItemStack(stack);
	}

	public void writeShortItem_R(ItemStack stack) {
		shortItem_R = writeItemStack(stack);
	}

	public void writeBackpack(ItemStack stack) {
		backpack = writeItemStack(stack);
	}

	public ItemStack readLongItem() {
		return readBytes(longItem);
	}

	public ItemStack readShortItem_L() {
		return readBytes(shortItem_L);
	}

	public ItemStack readShortItem_R() {
		return readBytes(shortItem_R);
	}

	public ItemStack readBackpack() {
		return readBytes(backpack);
	}

	public ItemCache deserialize() {
		ItemCache cache = new ItemCache();
		cache.longItem = readLongItem();
		cache.shortItem_L = readShortItem_L();
		cache.shortItem_R = readShortItem_R();
		cache.backpack = readBackpack();
		return cache;
	}

}