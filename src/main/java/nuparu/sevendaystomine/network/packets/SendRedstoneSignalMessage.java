package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SendRedstoneSignalMessage implements IMessage {

	byte strength;
	BlockPos pos;
	EnumFacing facing;

	public SendRedstoneSignalMessage() {

	}

	public SendRedstoneSignalMessage(byte strength, BlockPos pos, EnumFacing facing) {
		this.strength = strength;
		this.pos = pos;
		this.facing = facing;
	}
	

	public SendRedstoneSignalMessage(int strength, BlockPos pos, EnumFacing facing) {
		this((byte)strength,pos,facing);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		strength = buf.readByte();
		facing = EnumFacing.getHorizontal(buf.readByte());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeByte(strength);
		buf.writeByte(facing.getHorizontalIndex());
	}

}
