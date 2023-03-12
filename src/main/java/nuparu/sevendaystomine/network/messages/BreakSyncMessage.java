package nuparu.sevendaystomine.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;

import java.util.function.Supplier;

public class BreakSyncMessage {
    CompoundTag data;
    BlockPos pos;

    public BreakSyncMessage() {

    }

    public BreakSyncMessage(CompoundTag data, BlockPos pos) {
        this.data = data;
        this.pos = pos;
    }

    public static void encode(BreakSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
        buf.writeBlockPos(msg.pos);
    }

    public static BreakSyncMessage decode(FriendlyByteBuf buf) {
        return new BreakSyncMessage(buf.readNbt(),buf.readBlockPos());
    }

    public static class Handler {

        public static void handle(BreakSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {

                Level world = SevenDaysToMine.proxy.getLevel();
                ChunkAccess ichunk = world.getChunk(msg.pos);
                if(ichunk instanceof LevelChunk chunk) {
                    IChunkData data = CapabilityHelper.getChunkData(chunk);
                    data.readFromNBT(msg.data);
                }
				/*BreakSavedData data = BreakSavedData.get(world);
				if (data != null && msg.data != null) {
					data.readFromNBT(msg.data);
				}*/
            });
            ctx.get().setPacketHandled(true);
        }
    }
}