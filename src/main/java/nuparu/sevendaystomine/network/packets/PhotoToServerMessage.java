package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PhotoToServerMessage implements IMessage {

	private byte[] bytes;
	private int index;
	private int parts;
	private String id;

	public PhotoToServerMessage() {

	}

	public PhotoToServerMessage(byte[] bytes, int index, int parts, String id) {
		this.bytes = bytes;
		this.index = index;
		this.parts = parts;
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		bytes = nbt.getByteArray("image");
		index = nbt.getInteger("index");
		parts = nbt.getInteger("parts");
		id = nbt.getString("id");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByteArray("image", bytes);
		nbt.setInteger("index", index);
		nbt.setInteger("parts", parts);
		nbt.setString("id", id);
		ByteBufUtils.writeTag(buf, nbt);

	}

	public byte[] getBytes() {
		return bytes;
	}

	public int getIndex() {
		return index;
	}

	public int getParts() {
		return parts;
	}

	public String getID() {
		return id;
	}

}
