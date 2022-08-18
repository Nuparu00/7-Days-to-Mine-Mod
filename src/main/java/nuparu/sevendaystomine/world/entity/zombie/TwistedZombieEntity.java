package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;

public class TwistedZombieEntity<T extends TwistedZombieEntity> extends ZombieBipedEntity {

    public TwistedZombieEntity(EntityType<TwistedZombieEntity> type, Level world) {
        super(type, world);
    }

    public TwistedZombieEntity(Level world) {
        this(ModEntities.TWISTED_ZOMBIE.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.125F)
                .add(ForgeMod.SWIM_SPEED.get(), 0.9F)
                .add(Attributes.ATTACK_DAMAGE, 6D)
                .add(Attributes.MAX_HEALTH, 150)
                .add(Attributes.ARMOR, 25);
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        return super.hurt(source, this.isOnFire() ? amount*4.25f : amount/3);
    }

    @Override
    public void aiStep() {

        if (this.level.isClientSide) {
            for(int i = 0; i < random.nextInt(2); ++i) {
                this.level.addParticle(ParticleTypes.WARPED_SPORE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }

        super.aiStep();
    }

    public static class Factory implements EntityType.EntityFactory<TwistedZombieEntity> {
        @Override
        public TwistedZombieEntity create(EntityType<TwistedZombieEntity> type, Level world) {
            return new TwistedZombieEntity(type, world);
        }
    }
}
