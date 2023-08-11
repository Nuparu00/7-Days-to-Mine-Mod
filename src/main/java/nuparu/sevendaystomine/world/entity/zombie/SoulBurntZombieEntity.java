package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import org.jetbrains.annotations.NotNull;

public class SoulBurntZombieEntity<T extends SoulBurntZombieEntity> extends ZombieBipedEntity {

    public SoulBurntZombieEntity(EntityType<SoulBurntZombieEntity> type, Level world) {
        super(type, world);
    }

    public SoulBurntZombieEntity(Level world) {
        this(ModEntities.SOUL_BURNT_ZOMBIE.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 54.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(ForgeMod.SWIM_SPEED.get(), 0.8F)
                .add(Attributes.ATTACK_DAMAGE, 4D)
                .add(Attributes.MAX_HEALTH, 75)
                .add(Attributes.ARMOR, 1);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1;
    }

    @Override
    public void tick() {

        double height = this.getBbHeight();

        super.tick();

        for (int x = 0; x < 1 + level().random.nextInt(3); x++) {
            level().addParticle(ParticleTypes.LARGE_SMOKE, getX() + level().random.nextDouble() * 0.3 - 0.15,
                    getY() + height / 2, getZ() + level().random.nextDouble() * 0.3 - 0.15, 0.0D, 0.0D, 0.0D);
        }
        if (!this.level().isClientSide()) {

            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                return;
            }

            for (int l = 0; l < 4; ++l) {
                int i = (int) Math.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                int j = (int) Math.floor(this.getY());
                int k = (int) Math.floor(this.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockpos = new BlockPos(i, j, k);

                BlockState bottom = level().getBlockState(blockpos.below());

                Block fireBlock = Blocks.FIRE;

                if (bottom.getBlock() == Blocks.SOUL_SAND || bottom.getBlock() == Blocks.SOUL_SOIL) {
                    fireBlock = Blocks.SOUL_FIRE;
                }

                if (this.level().getBlockState(blockpos).isAir()
                        && fireBlock.canSurvive(Blocks.FIRE.defaultBlockState(), this.level(), blockpos)) {
                    this.level().setBlockAndUpdate(blockpos, fireBlock.defaultBlockState());
                }
            }
        }
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    public static class Factory implements EntityType.EntityFactory<SoulBurntZombieEntity> {
        @Override
        public @NotNull SoulBurntZombieEntity create(@NotNull EntityType<SoulBurntZombieEntity> type, @NotNull Level world) {
            return new SoulBurntZombieEntity(type, world);
        }
    }
}
