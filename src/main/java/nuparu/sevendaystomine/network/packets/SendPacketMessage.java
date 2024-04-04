package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SendPacketMessage implements IMessage {

	String packet;
	BlockPos to;
	BlockPos from;

	public SendPacketMessage() {

	}

	public SendPacketMessage(BlockPos from,BlockPos to,String packet) {
		this.from = from;
		this.to = to;
		this.packet = packet;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		from = BlockPos.fromLong(buf.readLong());
		to = BlockPos.fromLong(buf.readLong());
		packet = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(from.toLong());
		buf.writeLong(to.toLong());
		ByteBufUtils.writeUTF8String(buf, packet);
	}

}
