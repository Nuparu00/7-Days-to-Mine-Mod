package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.world.level.block.ISpikeBlock;

public class SpikesBlockEntity extends BlockEntity {
    public int health;

    public SpikesBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPIKES.get(), pos, state);
        this.health = ((ISpikeBlock)state.getBlock()).getMaxHealth(pos,state,level);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if(compound.contains("Health", Tag.TAG_INT)){
            this.health = compound.getInt("Health");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("Health", this.health);
    }

    public void damage(int amount){
        if (level.isClientSide())
            return;
        health -= amount;
        if (health <= 0) {
            ((ISpikeBlock)getBlockState().getBlock()).degradeBlock(worldPosition, level);
        }
    }
}