package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncTileEntityMessage implements IMessage {

	NBTTagCompound data;
	BlockPos pos;
	
	public SyncTileEntityMessage() {

	}

	public SyncTileEntityMessage(NBTTagCompound data, BlockPos pos) {
		this.data = data;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeTag(buf, data);
	}

}
