package nuparu.sevendaystomine.world.level.horde;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.level.saveddata.SavedData;
import nuparu.sevendaystomine.json.horde.HordeDataManager;
import nuparu.sevendaystomine.json.horde.HordeEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HordeManager extends SavedData {
    private static final String HORDES_FILE_ID = "hordes";
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
        HordeManager manager = server.overworld().getDataStorage().get(tag -> load(server.overworld(),tag), HORDES_FILE_ID);
        if (manager == null) {
            manager = new HordeManager(server.overworld());
            server.overworld().getDataStorage().set(HORDES_FILE_ID,manager);
        }
        return manager;
    }

    public static HordeManager load(ServerLevel level, CompoundTag tag) {
        HordeManager hordeManager = new HordeManager(level);
        hordeManager.nextAvailableID = tag.getInt("NextAvailableID");
        ListTag listtag = tag.getList("Hordes", Tag.TAG_COMPOUND);

        for(int i = 0; i < listtag.size(); ++i) {
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

        for(Horde horde : this.activeHordes) {
            listtag.add(horde.serializeNBT());
        }

        tag.put("Hordes", listtag);
        return tag;
    }

    public Optional<Horde> startHorde(ResourceLocation hordeKey, ServerLevel level, BlockPos center, ServerPlayer player) {
        Optional<HordeEntry> entry = HordeDataManager.INSTANCE.getByRes(hordeKey);
        if (entry.isPresent()) {
            Horde horde = new Horde(entry.get(), getUniqueId(), level, center);
            activeHordes.add(horde);
            horde.start();
            setDirty();
            return Optional.of(horde);
        }
        return Optional.empty();
    }

    public void tick() {
        for (Horde horde : getActiveHordes()) {
            horde.update();
        }
        if (this.tick++ % 200 == 0) {
            this.setDirty();
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

    public Optional<Horde> getHorde(int id){
        return getActiveHordes().stream().filter(horde -> horde.getId() == id).findFirst();
    }
}
