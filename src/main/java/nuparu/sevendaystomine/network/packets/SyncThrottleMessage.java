package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncThrottleMessage implements IMessage {

	int id;
	float throttle;
	public SyncThrottleMessage() {
		
	}
	
	public SyncThrottleMessage(int id, float throttle) {
		this.id = id;
		this.throttle = throttle;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.id = buf.readInt();
		this.throttle = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		buf.writeFloat(throttle);
	}

}
