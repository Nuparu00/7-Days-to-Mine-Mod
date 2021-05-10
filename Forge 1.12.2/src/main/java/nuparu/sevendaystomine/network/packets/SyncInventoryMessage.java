package nuparu.sevendaystomine.network.packets;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.item.BufferedCache;
import nuparu.sevendaystomine.util.item.ItemCache;

public class SyncInventoryMessage implements IMessage {

	private String name;
	private BufferedCache items;

	public SyncInventoryMessage() {

	}

	public SyncInventoryMessage(ItemCache items, String name) {
		this.items = items.serialize();
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.name = ByteBufUtils.readUTF8String(buf);
		byte[] bytes = new byte[buf.readableBytes()];
		if (bytes.length > 0) {
			int readerIndex = buf.readerIndex();
			buf.getBytes(readerIndex, bytes);
			Object obj = null;
			try {
				obj = Utils.convertFromBytes(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (obj != null && obj instanceof BufferedCache) {
				items = (BufferedCache) obj;
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, this.name);
		byte[] dataArgs = new byte[0];
		try {
			dataArgs = Utils.convertToBytes(items);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buf.writeBytes(dataArgs);

	}
	
	public BufferedCache getItems() {
		return this.items;
	}
	
	public String getName() {
		return this.name;
	}

}
