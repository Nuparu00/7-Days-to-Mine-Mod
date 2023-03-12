package nuparu.sevendaystomine.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedEntityProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<IExtendedEntity> EXTENDED_ENTITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<IExtendedEntity>() {
    });

    private final IExtendedEntity instance = new ExtendedEntity();
    protected final LazyOptional<IExtendedEntity> lazyInstance = LazyOptional.of(() -> this.instance);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == EXTENDED_ENTITY_CAPABILITY ? lazyInstance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.writeNBT(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.readNBT(nbt);
    }

    public ExtendedEntityProvider setOwner(Entity owner) {
        lazyInstance.orElse(null).setOwner(owner);
        return this;
    }
}
