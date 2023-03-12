package nuparu.sevendaystomine.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.world.level.block.entity.CodeSafeBlockEntity;

import java.util.function.Supplier;

public class SafeCodeMessage {

	BlockPos pos;
	int toAdd;
	
	public SafeCodeMessage() {

	}

	public SafeCodeMessage(BlockPos pos, int toAdd) {
		this.pos = pos;
		this.toAdd = toAdd;
	}

	public static void encode(SafeCodeMessage msg, FriendlyByteBuf buf) {
		buf.writeBlockPos(msg.pos);
		buf.writeInt(msg.toAdd);
	}

	public static SafeCodeMessage decode(FriendlyByteBuf buf) {
		return new SafeCodeMessage(buf.readBlockPos(),buf.readInt());
	}

	public static class Handler {

		public static void handle(SafeCodeMessage msg, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(() -> {
				BlockPos pos = msg.pos;
				int toAdd = msg.toAdd;
				
				if (pos == null || toAdd == 0) {
					return;
				}

				ServerPlayer player = ctx.get().getSender();
					Level world = player.level;
					if (world == null) {
						return;
					}
					BlockEntity te = world.getBlockEntity(pos);
					if (te instanceof CodeSafeBlockEntity safe) {
						//if(!safe.isLooking(player)) return;
						if(!safe.isUsableByPlayer(player)) return;

						int selectedCode = safe.getSelectedCode();

						int h = (selectedCode / 100) % 10;
						int d = (selectedCode / 10) % 10;
						int u = selectedCode % 10;

						int absToAdd = Math.abs(toAdd);
						if (absToAdd <= 10) {
							u += (toAdd / 10);
							if (u < 0) {
								u = 9;
							} else if (u > 9) {
								u = 0;
							}
						} else if (absToAdd <= 100) {
							d += (toAdd / 100);
							if (d < 0) {
								d = 9;
							} else if (d > 9) {
								d = 0;
							}
						} else if (absToAdd <= 1000) {
							h += (toAdd / 1000);
							if (h < 0) {
								h = 9;
							} else if (h > 9) {
								h = 0;
							}
						}

						String codeInString = String.valueOf(h) + d + u;
						int codeInInt = Integer.parseInt(codeInString);
						safe.setSelectedCode(codeInInt, player);
						CompoundTag nbt = safe.saveWithoutMetadata();
						nbt.remove("CorrectCode");
						PacketManager.sendToDimension(PacketManager.syncTileEntity, new SyncTileEntityMessage(nbt, pos), world::dimension);

					}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}
