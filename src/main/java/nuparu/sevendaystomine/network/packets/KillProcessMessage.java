package nuparu.sevendaystomine.network.packets;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class KillProcessMessage implements IMessage {

	private BlockPos pos;
	private UUID uuid;
	
	public KillProcessMessage() {
		
	}
	
	public KillProcessMessage(BlockPos pos, UUID uuid) {
		this.pos = pos;
		this.uuid = uuid;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		ByteBufUtils.writeUTF8String(buf, this.uuid.toString());
		
	}
	
	public BlockPos getPos() {
		return this.pos;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}

}
