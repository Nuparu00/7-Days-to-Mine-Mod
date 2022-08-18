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
import nuparu.sevendaystomine.client.event.ClientEventHandler;

import java.util.function.Supplier;

public class BreakSyncTrackingMessage {
    CompoundTag data;
    BlockPos pos;

    public BreakSyncTrackingMessage() {

    }

    public BreakSyncTrackingMessage(CompoundTag data, BlockPos pos) {
        this.data = data;
        this.pos = pos;
    }

    public static void encode(BreakSyncTrackingMessage msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
        buf.writeBlockPos(msg.pos);
    }

    public static BreakSyncTrackingMessage decode(FriendlyByteBuf buf) {
        return new BreakSyncTrackingMessage(buf.readNbt(),buf.readBlockPos());
    }

    public static class Handler {

        public static void handle(BreakSyncTrackingMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if(msg.data == null) return;
                Level world = SevenDaysToMine.proxy.getLevel();
                ClientEventHandler.cachedBreakData.put(msg.pos,msg.data);
				/*BreakSavedData data = BreakSavedData.get(world);
				if (data != null && msg.data != null) {
					data.readFromNBT(msg.data);
				}*/
            });
            ctx.get().setPacketHandled(true);
        }
    }
}