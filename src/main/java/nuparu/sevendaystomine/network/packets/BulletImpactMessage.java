package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BulletImpactMessage implements IMessage {


	protected double posX;
	protected double posY;
	protected double posZ;
	protected double motionX;
	protected double motionY;
	protected double motionZ;
	protected BlockPos pos;
	
	
	public BulletImpactMessage() {

	}

	public BulletImpactMessage(double posX, double posY, double posZ, double motionX, double motionY, double motionZ, BlockPos pos) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
		motionX = buf.readDouble();
		motionY = buf.readDouble();
		motionZ = buf.readDouble();
		pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
		buf.writeDouble(motionX);
		buf.writeDouble(motionY);
		buf.writeDouble(motionZ);
		buf.writeLong(pos.toLong());
	}
}
