package nuparu.sevendaystomine.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import nuparu.sevendaystomine.client.util.ClientUtils;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.entity.item.AirdropEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldData extends SavedData {
    private static final String FILE_ID = "world";
    private long lastAirdrop;
    private final ServerLevel level;

    public WorldData(ServerLevel level) {
        this.level = level;
        this.lastAirdrop = 0;
        this.setDirty();
    }


    public static WorldData getOrCreate(MinecraftServer server) {
        WorldData manager = server.overworld().getDataStorage().get(tag -> load(server.overworld(),tag), FILE_ID);
        if (manager == null) {
            manager = new WorldData(server.overworld());
            server.overworld().getDataStorage().set(FILE_ID,manager);
        }
        return manager;
    }

    public static WorldData load(ServerLevel level, CompoundTag tag) {
        WorldData hordeManager = new WorldData(level);
        hordeManager.lastAirdrop = tag.getLong("LastAirdrop");
        return hordeManager;
    }

    @NotNull
    public CompoundTag save(CompoundTag tag) {
        tag.putLong("LastAirdrop", lastAirdrop);
        return tag;
    }

    public void tick() {
        if(level.getGameTime() - lastAirdrop >= ServerConfig.airdropPeriod.get() && level.getGameTime() >= ServerConfig.airdropDelay.get()){
            spawnAirdrop();
        }
    }

    private void spawnAirdrop(){
        BlockPos pos = getAirdropPos();
        //Spawns the airdrop at the world spawn as that one is always loaded
        AirdropEntity e = new AirdropEntity(level, level.getSharedSpawnPos().above(level.getMaxBuildHeight()));

        /*LootTable loottable = server.getLootTables().get(ModLootTables.AIRDROP);
        LootContext.Builder lootcontext$builder = (new LootContext.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos));

        ItemUtils.fill(loottable, e.getInventory(), lootcontext$builder.create(LootContextParams.CHEST));
*/
        level.addFreshEntity(e);

        //Move the airdrop to the actual position
        e.setPos(pos.getX(), pos.getY(), pos.getZ());
        System.out.println(pos);
        if(ServerConfig.airdropAnnouncement.get()) {
            int x = pos.getX() + MathUtils.getIntInRange(level.random, ServerConfig.airdropAnnouncementOffsetMin.get(), ServerConfig.airdropAnnouncementOffsetMax.get()) * (level.random.nextBoolean() ? 1 : -1);
            int z = pos.getZ() + MathUtils.getIntInRange(level.random, ServerConfig.airdropAnnouncementOffsetMin.get(), ServerConfig.airdropAnnouncementOffsetMax.get()) * (level.random.nextBoolean() ? 1 : -1);

            level.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("airdrop.message",
                    x,
                    z), true);

            level.getServer().getPlayerList().getPlayers().forEach(player -> player.sendChatMessage(OutgoingChatMessage.create(
                    PlayerChatMessage.system(ClientUtils.localize("airdrop.message", x, z))), true, ChatType.bind(ChatType.SAY_COMMAND, e)));
        }
        lastAirdrop = level.getGameTime();
        this.setDirty();
    }

    private BlockPos getAirdropPos() {
        List<? extends ServerPlayer> players = level.players();
        double xSum = 0;
        double zSum = 0;

        if (players.size() == 1) {
            ServerPlayer player = players.get(0);

            double angle = 2.0 * Math.PI * level.random.nextDouble();
            double dist = MathUtils.getIntInRange(level.random, ServerConfig.airdropDistanceMin.get(), ServerConfig.airdropDistanceMax.get());
            double x = player.getX() + dist * Math.cos(angle);
            double z = player.getZ() + dist * Math.sin(angle);

            return new BlockPos((int) x, 255, (int) z);
        }

        for (ServerPlayer player : players) {
            xSum += player.getX();
            zSum += player.getZ();
        }

        double angle = 2.0 * Math.PI * level.random.nextDouble();
        double dist = MathUtils.getIntInRange(level.random, ServerConfig.airdropDistanceMin.get(), ServerConfig.airdropDistanceMax.get());
        double x = xSum / players.size() + dist * Math.cos(angle);
        double z = zSum / players.size() + dist * Math.sin(angle);

        return new BlockPos((int) x, level.getMaxBuildHeight(), (int) z);
    }

}
