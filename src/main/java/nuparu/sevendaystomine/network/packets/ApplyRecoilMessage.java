package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ApplyRecoilMessage implements IMessage {


	protected float recoil;
	protected boolean main;
	protected boolean flash;
	public ApplyRecoilMessage() {

	}

	public ApplyRecoilMessage(float recoil, boolean main, boolean flash) {
		this.recoil = recoil;
		this.main = main;
		this.flash = flash;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		recoil = buf.readFloat();
		main = buf.readBoolean();
		flash = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(recoil);
		buf.writeBoolean(main);
		buf.writeBoolean(flash);

	}
}
