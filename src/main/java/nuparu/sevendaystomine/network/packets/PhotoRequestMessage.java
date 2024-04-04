package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PhotoRequestMessage implements IMessage {

	private String path;

	public PhotoRequestMessage() {

	}

	public PhotoRequestMessage(String path) {
		this.path = path;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.path = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.path);
	}

	public String getPath() {
		return this.path;
	}

}
