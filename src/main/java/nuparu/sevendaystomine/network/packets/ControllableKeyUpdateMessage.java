package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ControllableKeyUpdateMessage implements IMessage {

	int key; //Key code from KeyEventHandler.class
	byte state; //0 == unpressed; 1 == pressed
	
	public ControllableKeyUpdateMessage() {
	}
	
	public ControllableKeyUpdateMessage(int key, byte state) {
		this.key = key;
		this.state = state;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.key = buf.readInt();
		this.state = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(key);
		buf.writeByte(state);
	}
	
	public int getKey(){
		return this.key;
	}
	
	public byte getState() {
		return this.state;
	}

}
