package nuparu.sevendaystomine.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncIconMessage implements IMessage {

	private BlockPos pos;
	private NBTTagCompound nbt;
	
	public SyncIconMessage() {
		
	}
	
	public SyncIconMessage(BlockPos pos, NBTTagCompound nbt) {
		this.pos = pos;
		this.nbt = nbt;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		ByteBufUtils.writeTag(buf, this.nbt);
		
	}
	
	public BlockPos getPos() {
		return this.pos;
	}
	
	public NBTTagCompound getNBT() {
		return this.nbt;
	}

}
