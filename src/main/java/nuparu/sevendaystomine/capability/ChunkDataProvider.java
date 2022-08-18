package nuparu.sevendaystomine.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChunkDataProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<IChunkData> CHUNK_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<IChunkData>() {
    });

    private final IChunkData instance = new ChunkData();
    protected final LazyOptional<IChunkData> lazyInstance = LazyOptional.of(() -> this.instance);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == CHUNK_DATA_CAPABILITY ? lazyInstance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.writeToNBT(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.readFromNBT(nbt);
    }


    public ChunkDataProvider setOwner(LevelChunk chunk) {
        lazyInstance.orElse(null).setOwner(chunk);
        return this;
    }

}