package nuparu.sevendaystomine.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.function.Supplier;

public class SyncTileEntityMessage {

	CompoundTag data;
	BlockPos pos;

	public SyncTileEntityMessage() {

	}

	public SyncTileEntityMessage(CompoundTag data, BlockPos pos) {
		this.data = data;
		this.pos = pos;
	}

	public static void encode(SyncTileEntityMessage msg, FriendlyByteBuf buf) {
		buf.writeNbt(msg.data);
		buf.writeBlockPos(msg.pos);
	}

	public static SyncTileEntityMessage decode(FriendlyByteBuf buf) {
		return new SyncTileEntityMessage(buf.readNbt(), buf.readBlockPos());
	}

	public static class Handler {

		public static void handle(SyncTileEntityMessage msg, Supplier<NetworkEvent.Context> ctx) {

			ctx.get().enqueueWork(() -> {
				BlockPos pos = msg.pos;
				CompoundTag nbt = msg.data;
				Level world = SevenDaysToMine.proxy.getLevel();
				if (world == null) {
					return;
				}
				BlockEntity te = world.getBlockEntity(pos);
				if (te == null) {
					return;
				}
				te.deserializeNBT(nbt);
			});
			ctx.get().setPacketHandled(true);
		}
	}

}
