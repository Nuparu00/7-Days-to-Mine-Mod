package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import org.jetbrains.annotations.NotNull;

public class BurntZombieEntity<T extends BurntZombieEntity> extends ZombieBipedEntity {

    private static final EntityDataAccessor<Boolean> DATA_SOUL_CONVERSION_ID = SynchedEntityData
            .defineId(BurntZombieEntity.class, EntityDataSerializers.BOOLEAN);

    private int inSoulFireTime;
    private int conversionTime;
    public BurntZombieEntity(EntityType<BurntZombieEntity> type, Level world) {
        super(type, world);
    }

    public BurntZombieEntity(Level world) {
        this(ModEntities.BURNT_ZOMBIE.get(), world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_SOUL_CONVERSION_ID, false);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 54.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.225F)
                .add(ForgeMod.SWIM_SPEED.get(), 1F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MAX_HEALTH, 65)
                .add(Attributes.ARMOR, 0.5);
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1;
    }

    @Override
    public void tick() {

        double height = this.getBbHeight();
        double width = this.getBbWidth();
        if (!this.level.isClientSide && this.isAlive() && !this.isNoAi()) {
            if (this.isSoulFireConverting()) {
                --this.conversionTime;

                if (this.conversionTime < 0 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this,
                        ModEntities.SOUL_BURNT_ZOMBIE.get(), (timer) -> this.conversionTime = timer)) {
                    this.doSoulFireConversion();
                }
            }
            if (level.getBlockState(new BlockPos(getX() + width / 2, getY() + height / 5, getZ() + width / 2))
                    .getBlock() == Blocks.SOUL_FIRE) {
                ++this.inSoulFireTime;
                if (this.inSoulFireTime >= 100 && !isSoulFireConverting()) {
                    this.startSoulFireConversion(150);
                }
            } else {
                this.inSoulFireTime = -1;
            }

        }

        super.tick();

        for (int x = 0; x < 1 + level.random.nextInt(3); x++) {
            level.addParticle(ParticleTypes.LARGE_SMOKE, getX() + level.random.nextDouble() * 0.3 - 0.15,
                    getY() + height / 2, getZ() + level.random.nextDouble() * 0.3 - 0.15, 0.0D, 0.0D, 0.0D);
        }
        if (random.nextDouble() < 0.1D) {
            /*
             * level.playLocalSound(getX() + width / 2, getY() + height / 2, getZ() + width
             * / 2, SoundEvents.BLAZE_BURN, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
             */

        }
        if (!this.level.isClientSide()) {

            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }

            for (int l = 0; l < 4; ++l) {
                int i = (int) Math.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                int j = (int) Math.floor(this.getY());
                int k = (int) Math.floor(this.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);

                BlockState bottom = level.getBlockState(blockpos.below());

                Block fireBlock = Blocks.FIRE;

                if (bottom.getBlock() == Blocks.SOUL_SAND || bottom.getBlock() == Blocks.SOUL_SOIL) {
                    fireBlock = Blocks.SOUL_FIRE;
                }

                if (this.level.getBlockState(blockpos).getMaterial() == Material.AIR
                        && fireBlock.canSurvive(Blocks.FIRE.defaultBlockState(), this.level, blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, fireBlock.defaultBlockState());
                }
            }
        }
    }

    public boolean isSoulFireConverting() {
        return this.getEntityData().get(DATA_SOUL_CONVERSION_ID);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_213281_1_) {
        super.addAdditionalSaveData(p_213281_1_);
        p_213281_1_.putInt("InSoulFireTime", this.isInWater() ? this.inSoulFireTime : -1);
        p_213281_1_.putInt("SoulConversionTime", this.isSoulFireConverting() ? this.conversionTime : -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_70037_1_) {
        super.readAdditionalSaveData(p_70037_1_);
        this.inSoulFireTime = p_70037_1_.getInt("InSoulFireTime");
        if (p_70037_1_.contains("SoulConversionTime", 99) && p_70037_1_.getInt("SoulConversionTime") > -1) {
            this.startSoulFireConversion(p_70037_1_.getInt("SoulConversionTime"));
        }

    }

    private void startSoulFireConversion(int p_204704_1_) {
        this.conversionTime = p_204704_1_;
        this.getEntityData().set(DATA_SOUL_CONVERSION_ID, true);
    }

    protected void doSoulFireConversion() {
        this.convertTo(ModEntities.SOUL_BURNT_ZOMBIE.get(),true);
        if (!this.isSilent()) {
            this.level.levelEvent(null, 1040, this.blockPosition(), 0);
            double height = this.getBbHeight();
            double width = this.getBbWidth();
            level.playSound(null, getX() + width / 2, getY() + height / 2, getZ() + width / 2,
                    SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.HOSTILE, 1F, 0.8F);
        }

    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    public static class Factory implements EntityType.EntityFactory<BurntZombieEntity> {
        @Override
        public @NotNull BurntZombieEntity create(@NotNull EntityType<BurntZombieEntity> type, @NotNull Level world) {
            return new BurntZombieEntity(type, world);
        }
    }
}
