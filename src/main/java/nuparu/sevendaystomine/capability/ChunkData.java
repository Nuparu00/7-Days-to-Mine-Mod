package nuparu.sevendaystomine.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModGameRules;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.BreakSyncMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChunkData implements IChunkData {

    private HashMap<BlockPos, BreakData> map = new HashMap<>();
    private long lastUpdate = 0;
    private LevelChunk owner;

    @Override
    public BreakData getBreakData(BlockPos pos) {
        return map.get(pos);
    }

    @Override
    public boolean hasBreakData(BlockPos pos) {
        return getBreakData(pos) != null;
    }

    @Override
    public BreakData setBreakData(BlockPos pos, BreakData data) {
        if (data.getState() <= 0) {
            removeBreakData(pos);
            return null;
        }
        map.put(pos, data);
        markDirty();
        return data;
    }

    @Override
    public BreakData setBreakData(BlockPos pos, float data) {
        BreakData breakData = new BreakData(owner.getWorldForge().getGameTime(), data);
        return setBreakData(pos, breakData);
    }
    @Override
    public BreakData addBreakData(BlockPos pos, float delta){
            BreakData breakData = this.getBreakData(pos);
            if (breakData == null) {
                breakData = new BreakData(owner.getWorldForge().getGameTime(), delta);
            } else {
                breakData.addState(delta).setLastChange(owner.getWorldForge().getGameTime());
            }
            return setBreakData(pos, breakData);
        }

    @Override
    public void removeBreakData(BlockPos pos) {
        map.remove(pos);
        markDirty();
    }

    @Override
    public void removeBreakData(BreakData data) {
        map.values().remove(data);
        markDirty();
    }

    @Override
    public boolean isEmpty(){
        return map == null || map.isEmpty();
    }

    @Override
    public void update(ServerLevel world) {
        int decayRate = world.getGameRules().getInt(ModGameRules.damageDecayRate);
        if (decayRate <= 0) {
            decayRate = ServerConfig.damageDecayRate.get();
        }
        if (decayRate <= 0) {
            return;
        }
        if (Math.abs(world.getGameTime() - lastUpdate) <= decayRate)
            return;


        boolean dirty = false;

        @SuppressWarnings("unchecked")
        HashMap<BlockPos, BreakData> localMap = (HashMap<BlockPos, BreakData>)map.clone();
        ArrayList<BlockPos> toRemove = new ArrayList<>();

        Iterator<Map.Entry<BlockPos, BreakData>> it = localMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BlockPos, BreakData> pair = it.next();
            BreakData data = pair.getValue();
            if (Math.abs(world.getGameTime() - data.getLastChange()) >= decayRate) {
                if (data.getState() > 0.4) {
                    data.addState(0.1f);
                    dirty = true;
                    if (data.getState() >= 1) {
                        world.destroyBlock(pair.getKey(), false);
                        toRemove.add(pair.getKey());
                        continue;
                    }
                } else {
                    data.addState(-0.1f);
                    dirty = true;
                    if (data.getState() <= 0) {
                        toRemove.add(pair.getKey());
                        continue;
                    }
                }
                data.setLastChange(world.getGameTime());
            }

        }

        toRemove.forEach(map.keySet()::remove);
        lastUpdate = world.getGameTime();
        markDirty();
    }

    @Override
    public void syncTo(ServerPlayer player) {
        PacketManager.sendTo(PacketManager.blockBreakSync, new BreakSyncMessage(this.writeToNBT(new CompoundTag()),owner.getPos().getWorldPosition()),player);
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        lastUpdate = nbt.getLong("lastUpdate");
        map = BreakHelper.of(nbt.getCompound("map"));
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt.putLong("lastUpdate", lastUpdate);
        nbt.put("map", BreakHelper.save(map));

        return nbt;
    }

    @Override
    public HashMap<BlockPos, BreakData> getData() {
        return (HashMap<BlockPos, BreakData>) this.map.clone();
    }

    @Override
    public void markDirty() {
        owner.setUnsaved(true);
        PacketManager.sendToChunk(PacketManager.blockBreakSync,  new BreakSyncMessage(this.writeToNBT(new CompoundTag()),owner.getPos().getWorldPosition()), () -> owner);
    }

    @Override
    public void setOwner(LevelChunk chunk) {
        owner = chunk;
    }
}
