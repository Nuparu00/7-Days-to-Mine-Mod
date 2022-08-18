package nuparu.sevendaystomine.network.messages;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.ExtendedPlayer;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

import java.util.function.Supplier;

public class ExtendedPlayerSyncMessage {
    IExtendedPlayer extendedPlayer;
    int playerID;

    public ExtendedPlayerSyncMessage() {
        this.extendedPlayer = null;
        this.playerID = 0;
    }

    public ExtendedPlayerSyncMessage(IExtendedPlayer extendedPlayer, Player player) {
        this.extendedPlayer = extendedPlayer;
        this.playerID = player.getId();
    }

    public ExtendedPlayerSyncMessage(IExtendedPlayer extendedPlayer, int playerID) {
        this.extendedPlayer = extendedPlayer;
        this.playerID = playerID;
    }

    public static void encode(ExtendedPlayerSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.playerID);
        CompoundTag nbt = new CompoundTag();
        if (msg.extendedPlayer != null) {
            msg.extendedPlayer.writeNBT(nbt);
        }
        buf.writeNbt(nbt);
    }

    public static ExtendedPlayerSyncMessage decode(FriendlyByteBuf buf) {
        int playerID = buf.readInt();
        CompoundTag nbt = buf.readNbt();
        if (nbt == null)
            return null;
        IExtendedPlayer extendedPlayer = new ExtendedPlayer();
        extendedPlayer.readNBT(nbt);
        return new ExtendedPlayerSyncMessage(extendedPlayer,playerID);
    }

    public static class Handler {

        public static void handle(ExtendedPlayerSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Player player = SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx);
                IExtendedPlayer extendedPlayer = msg.extendedPlayer;
                int playerID = msg.playerID;
                if(player != null && player.level != null){
                    Entity entity = player.level.getEntity(playerID);
                    if(entity instanceof Player){
                        Player player2 = (Player)entity;
                        IExtendedPlayer localExtendedPlayer = CapabilityHelper.getExtendedPlayer(player2);
                        if(localExtendedPlayer == null) return;
                        localExtendedPlayer.copy(extendedPlayer);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
