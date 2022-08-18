package nuparu.sevendaystomine.network;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.network.messages.*;

import java.util.Optional;
import java.util.function.Supplier;

public class PacketManager {

    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public static SimpleChannel extendedPlayerSync = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "extended_player_sync"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static SimpleChannel playerDrink = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "player_drink"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static SimpleChannel blockBreakSync = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "block_break_sync"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static SimpleChannel blockBreakSyncTracking = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "block_break_sync_tracking"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static SimpleChannel openGuiClient = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "open_gui_client"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static SimpleChannel safeCodeUpdate = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "safe_code_reload"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();


    public static SimpleChannel syncTileEntity = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SevenDaysToMine.MODID, "sync_tile_entity"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    private static int discriminator = 0;
    public static void setup() {
        PacketManager.extendedPlayerSync.registerMessage(PacketManager.discriminator++, ExtendedPlayerSyncMessage.class, ExtendedPlayerSyncMessage::encode, ExtendedPlayerSyncMessage::decode, ExtendedPlayerSyncMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        PacketManager.playerDrink.registerMessage(PacketManager.discriminator++, PlayerDrinkMessage.class, PlayerDrinkMessage::encode, PlayerDrinkMessage::decode, PlayerDrinkMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        PacketManager.blockBreakSync.registerMessage(PacketManager.discriminator++, BreakSyncMessage.class, BreakSyncMessage::encode, BreakSyncMessage::decode, BreakSyncMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        PacketManager.blockBreakSyncTracking.registerMessage(PacketManager.discriminator++, BreakSyncTrackingMessage.class, BreakSyncTrackingMessage::encode, BreakSyncTrackingMessage::decode, BreakSyncTrackingMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        PacketManager.openGuiClient.registerMessage(PacketManager.discriminator++, OpenGuiClientMessage.class, OpenGuiClientMessage::encode, OpenGuiClientMessage::decode, OpenGuiClientMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        PacketManager.safeCodeUpdate.registerMessage(PacketManager.discriminator++, SafeCodeMessage.class, SafeCodeMessage::encode, SafeCodeMessage::decode, SafeCodeMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        PacketManager.syncTileEntity.registerMessage(PacketManager.discriminator++, SyncTileEntityMessage.class, SyncTileEntityMessage::encode, SyncTileEntityMessage::decode, SyncTileEntityMessage.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendTo(SimpleChannel channel, Object message, ServerPlayer player) {
        channel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToChunk(SimpleChannel channel, Object message, Supplier<LevelChunk> chunk) {
        channel.send(PacketDistributor.TRACKING_CHUNK.with(chunk), message);
    }

    public static void sendToDimension(SimpleChannel channel, Object message, Supplier<ResourceKey<Level>> world) {
        channel.send(PacketDistributor.DIMENSION.with(world), message);
    }

    public static void sendToTrackingEntity(SimpleChannel channel, Object message, Supplier<Entity> entity) {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(entity), message);
    }

    public static void sendToAll(SimpleChannel channel, Object message) {
        channel.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToServer(SimpleChannel channel, Object message) {
        channel.sendToServer(message);
    }
}
