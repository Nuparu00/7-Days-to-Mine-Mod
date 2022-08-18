package nuparu.sevendaystomine.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedPlayerProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<IExtendedPlayer> EXTENDED_PLAYER_CAPABILITY = CapabilityManager.get(new CapabilityToken<IExtendedPlayer>() {
    });

    private final IExtendedPlayer instance = new ExtendedPlayer();
    protected final LazyOptional<IExtendedPlayer> lazyInstance = LazyOptional.of(() -> this.instance);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == EXTENDED_PLAYER_CAPABILITY ? lazyInstance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.writeNBT(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.readNBT(nbt);
    }

    public ExtendedPlayerProvider setOwner(Player player) {
        lazyInstance.orElse(null).setOwner(player);
        return this;
    }
}
