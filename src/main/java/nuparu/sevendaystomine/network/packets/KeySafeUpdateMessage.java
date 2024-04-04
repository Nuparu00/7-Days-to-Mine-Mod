package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class KeySafeUpdateMessage implements IMessage {

	float angle;
	float force;
	long pos;

	public KeySafeUpdateMessage() {

	}

	public KeySafeUpdateMessage(float angle, float force, BlockPos pos) {
		this.angle = angle;
		this.force = force;
		this.pos = pos.toLong();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		angle = buf.readFloat();
		force = buf.readFloat();
		pos = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(angle);
		buf.writeFloat(force);
		buf.writeLong(pos);
	}

}
