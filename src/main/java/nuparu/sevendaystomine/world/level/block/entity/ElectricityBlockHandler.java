package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import nuparu.sevendaystomine.world.inventory.block.BlockEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ElectricityBlockHandler extends BlockEntity {

    protected final BlockEnergyStorage energyStorage;
    private LazyOptional<BlockEnergyStorage> energy;
    public ElectricityBlockHandler(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
        this.energyStorage = createEnergyStorage();
        this.energy = LazyOptional.of(() -> energyStorage);
    }

    protected BlockEnergyStorage createEnergyStorage(){
        return new BlockEnergyStorage(getCapacity(),getMaxReceive(),getMaxExtract(),this);
    }

    public abstract int getCapacity();
    public abstract int getMaxReceive();
    public abstract int getMaxExtract();

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
           return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps(){
        super.invalidateCaps();
        this.energy.invalidate();
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.energyStorage.setEnergy(compound.getInt("Energy"));
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("Energy",getEnergy());
    }

    public int getEnergy(){
        return this.energyStorage.getEnergyStored();
    }

}
