package nuparu.sevendaystomine.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedInventoryProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<IItemHandlerExtended> EXTENDED_INV_CAP = CapabilityManager.get(new CapabilityToken<IItemHandlerExtended>() {
    });

    private IItemHandlerExtended instance = new ExtendedInventory();
    protected LazyOptional<IItemHandlerExtended> lazyInstance = LazyOptional.of(() -> this.instance);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == EXTENDED_INV_CAP ? lazyInstance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.deserializeNBT(nbt);
    }

    public ExtendedInventoryProvider setSize(int size) {
        instance.setSize(size);
        return this;
    }

    public ExtendedInventoryProvider setInstance(IItemHandlerExtended instance){
        this.instance = instance;
        this.lazyInstance = LazyOptional.of(() -> instance);
        return this;
    }
}