package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CameraDimensionsMessage implements IMessage {

	double deltaWidth;
	double deltaHeight;
	double deltaZoom;

	public CameraDimensionsMessage() {

	}

	public CameraDimensionsMessage(double deltaWidth, double deltaHeight, double deltaZoom) {
		this.deltaWidth = deltaWidth;
		this.deltaHeight = deltaHeight;
		this.deltaZoom = deltaZoom;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		deltaWidth = buf.readDouble();
		deltaHeight = buf.readDouble();
		deltaZoom = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(deltaWidth);
		buf.writeDouble(deltaHeight);
		buf.writeDouble(deltaZoom);
	}
}
