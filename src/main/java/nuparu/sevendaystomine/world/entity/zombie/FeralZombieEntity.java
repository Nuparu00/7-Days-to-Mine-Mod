package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import org.jetbrains.annotations.NotNull;

public class FeralZombieEntity<T extends FeralZombieEntity> extends ZombieBipedEntity {

    public FeralZombieEntity(EntityType<FeralZombieEntity> type, Level world) {
        super(type, world);
    }

    public FeralZombieEntity(Level world) {
        this(ModEntities.FERAL_ZOMBIE.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(ForgeMod.SWIM_SPEED.get(), 1.75F)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.ARMOR, 20);
    }


    @Override
    public boolean hurt(@NotNull DamageSource source, float amount)
    {
        return super.hurt(source, this.isOnFire() ? amount*4.25f : amount/3);
    }

    public static class Factory implements EntityType.EntityFactory<FeralZombieEntity> {
        @Override
        public @NotNull FeralZombieEntity create(@NotNull EntityType<FeralZombieEntity> type, @NotNull Level world) {
            return new FeralZombieEntity(type, world);
        }
    }
}
