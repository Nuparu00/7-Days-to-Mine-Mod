package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PhotoToClientMessage implements IMessage {

	private byte[] bytes;
	private int index;
	private int parts;
	private String name;

	public PhotoToClientMessage() {

	}

	public PhotoToClientMessage(byte[] bytes, int index, int parts, String name) {
		this.bytes = bytes;
		this.index = index;
		this.parts = parts;
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound nbt = ByteBufUtils.readTag(buf);
		bytes = nbt.getByteArray("image");
		index = nbt.getInteger("index");
		parts = nbt.getInteger("parts");
		name = nbt.getString("name");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByteArray("image", bytes);
		nbt.setInteger("index", index);
		nbt.setInteger("parts", parts);
		nbt.setString("name", name);
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

	public String getName() {
		return name;
	}

}
