package nuparu.sevendaystomine.world.level.horde;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import nuparu.sevendaystomine.json.horde.HordeDataManager;
import nuparu.sevendaystomine.json.horde.HordeEntry;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HordeManager extends SavedData {
    private static final String HORDES_FILE_ID = "hordes";
    private static final int HORDE_POSITION_ATTEMPTS = 128;
    private final List<Horde> activeHordes = new ArrayList<>();
    private final ServerLevel level;

    private int tick;
    private int nextAvailableID;

    public HordeManager(ServerLevel level) {
        this.level = level;
        this.nextAvailableID = 1;
        this.tick = 0;
        this.setDirty();
    }

    private int getUniqueId() {
        return ++this.nextAvailableID;
    }

    public static HordeManager getOrCreate(MinecraftServer server) {
        HordeManager manager = server.overworld().getDataStorage().get(tag -> load(server.overworld(), tag), HORDES_FILE_ID);
        if (manager == null) {
            manager = new HordeManager(server.overworld());
            server.overworld().getDataStorage().set(HORDES_FILE_ID, manager);
        }
        return manager;
    }

    public static HordeManager load(ServerLevel level, CompoundTag tag) {
        HordeManager hordeManager = new HordeManager(level);
        hordeManager.nextAvailableID = tag.getInt("NextAvailableID");
        ListTag listtag = tag.getList("Hordes", Tag.TAG_COMPOUND);

        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            Horde horde = new Horde(level, compoundtag);
            hordeManager.activeHordes.add(horde);
        }
        return hordeManager;
    }

    @NotNull
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("NextAvailableID", this.nextAvailableID);
        ListTag listtag = new ListTag();

        for (Horde horde : this.activeHordes) {
            listtag.add(horde.serializeNBT());
        }

        tag.put("Hordes", listtag);
        return tag;
    }

    public Optional<Horde> startHorde(ResourceLocation hordeKey, ServerLevel level, BlockPos center, ServerPlayer player) {
        Optional<HordeEntry> entry = HordeDataManager.INSTANCE.getByRes(hordeKey);
        if (entry.isPresent()) {
            return startHorde(entry.get(), level, center, player);
        }
        return Optional.empty();
    }

    public Optional<Horde> startHorde(HordeEntry entry, ServerLevel level, BlockPos center, ServerPlayer player) {
        Horde horde = new Horde(entry, getUniqueId(), level, center, 1);
        activeHordes.add(horde);
        horde.start();
        setDirty();
        return Optional.of(horde);
    }

    public void tick() {
        for (Horde horde : getActiveHordes()) {
            horde.update();
        }
        if (this.tick++ % 200 == 0) {
            this.setDirty();
        }
        if (this.tick++ % 40 == 0) {
            for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                for (HordeEntry entry : HordeDataManager.INSTANCE.getRandomHordes()) {
                    if (entry.trigger().time().test(player, player.level())) {
                        Optional<BlockPos> pos = chooseHordePosition(player, entry);
                        if(pos.isPresent()) {
                            startHorde(entry, level, pos.get(), player);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void endHorde(Horde horde) {
        horde.end();
    }

    public boolean removeHorde(Horde horde) {
        return activeHordes.remove(horde);
    }

    public void clearHordes() {
        for (Horde horde : getActiveHordes()) {
            horde.end();
        }
        activeHordes.clear();
    }

    public List<Horde> getActiveHordes() {
        return new ArrayList<>(activeHordes);
    }

    public Optional<Horde> getHorde(int id) {
        return getActiveHordes().stream().filter(horde -> horde.getId() == id).findFirst();
    }

    private Optional<BlockPos> chooseHordePosition(ServerPlayer player, HordeEntry entry){
        double desiredDistance = 64;
        BlockPos playerPos = player.blockPosition();
        for(int i = 0; i < HORDE_POSITION_ATTEMPTS; i++){
            double angle = 2.0 * Math.PI * level.random.nextDouble();
            double x = player.getX() + desiredDistance * Math.cos(angle);
            double z = player.getZ() + desiredDistance * Math.sin(angle);

            BlockPos pos = playerPos.offset((int) x,0, (int) z);
            pos = player.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);

            if(player.level().getBlockState(pos.above()).isAir() && player.level().getBlockState(pos.above(2)).isAir()){
                return Optional.of(pos);
            }
        }
        return Optional.empty();
    }
}
