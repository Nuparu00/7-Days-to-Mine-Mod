package nuparu.sevendaystomine.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.HashMap;

public interface IChunkData {

    BreakData getBreakData(BlockPos pos);

    boolean hasBreakData(BlockPos pos);

    BreakData setBreakData(BlockPos pos, BreakData data);

    BreakData setBreakData(BlockPos pos, float data);

    BreakData addBreakData(BlockPos pos, float delta);

    void removeBreakData(BlockPos pos);

    void removeBreakData(BreakData data);

    void update(ServerLevel world);

    void syncTo(ServerPlayer player);

    void readFromNBT(CompoundTag nbt);

    boolean isEmpty();

    CompoundTag writeToNBT(CompoundTag nbt);

    HashMap<BlockPos, BreakData> getData();

    void markDirty();

    void setOwner(LevelChunk chunk);

}
