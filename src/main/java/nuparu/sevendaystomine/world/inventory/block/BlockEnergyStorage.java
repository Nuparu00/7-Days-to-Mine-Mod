package nuparu.sevendaystomine.world.inventory.block;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;
import nuparu.sevendaystomine.util.MathUtils;

public class BlockEnergyStorage extends EnergyStorage {

    private final BlockEntity blockEntity;

    public BlockEnergyStorage(int capacity, BlockEntity blockEntity) {
        super(capacity);
        this.blockEntity = blockEntity;
    }

    public BlockEnergyStorage(int capacity, int maxTransfer, BlockEntity blockEntity) {
        super(capacity, maxTransfer);
        this.blockEntity = blockEntity;
    }

    public BlockEnergyStorage(int capacity, int maxReceive, int maxExtract, BlockEntity blockEntity) {
        super(capacity, maxReceive, maxExtract);
        this.blockEntity = blockEntity;
    }

    public BlockEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, BlockEntity blockEntity) {
        super(capacity, maxReceive, maxExtract, energy);
        this.blockEntity = blockEntity;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        this.blockEntity.setChanged();
        return super.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        this.blockEntity.setChanged();
        return super.extractEnergy(maxExtract, simulate);
    }

    public void setEnergy(int energy){
        this.energy = MathUtils.clamp(energy,0,capacity);
    }

    public BlockEntity getBlockEntity(){
        return this.blockEntity;
    }
}
