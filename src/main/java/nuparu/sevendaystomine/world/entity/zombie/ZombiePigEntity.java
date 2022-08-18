package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;

public class ZombiePigEntity<T extends ZombiePigEntity> extends ZombieAnimalEntity {

    public ZombiePigEntity(EntityType<ZombiePigEntity> type, Level world) {
        super(type, world);
    }

    public ZombiePigEntity(Level world) {
        this(ModEntities.ZOMBIE_PIG.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.19f)
                .add(ForgeMod.SWIM_SPEED.get(), 1.3F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ARMOR, 0);
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
        this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }


    public static class Factory implements EntityType.EntityFactory<ZombiePigEntity> {
        @Override
        public ZombiePigEntity create(EntityType<ZombiePigEntity> type, Level world) {
            return new ZombiePigEntity(type, world);
        }
    }
}
