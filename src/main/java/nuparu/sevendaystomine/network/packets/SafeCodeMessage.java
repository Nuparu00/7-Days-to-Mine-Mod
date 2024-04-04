package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SafeCodeMessage implements IMessage {

	BlockPos pos;
	int toAdd;
	
	public SafeCodeMessage() {

	}

	public SafeCodeMessage(BlockPos pos, int toAdd) {
		this.pos = pos;
		this.toAdd = toAdd;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		toAdd = buf.readInt();
		pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(toAdd);
		buf.writeLong(pos.toLong());
	}

}
