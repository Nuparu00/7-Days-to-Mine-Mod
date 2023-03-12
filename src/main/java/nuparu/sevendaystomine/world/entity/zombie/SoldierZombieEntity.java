package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import org.jetbrains.annotations.NotNull;

public class SoldierZombieEntity<T extends SoldierZombieEntity> extends ZombieBipedEntity {

    public SoldierZombieEntity(EntityType<SoldierZombieEntity> type, Level world) {
        super(type, world);
    }

    public SoldierZombieEntity(Level world) {
        this(ModEntities.SOLDIER_ZOMBIE.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18F)
                .add(ForgeMod.SWIM_SPEED.get(), 1.3F)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ARMOR, 1);
    }

    public static class Factory implements EntityType.EntityFactory<SoldierZombieEntity> {
        @Override
        public @NotNull SoldierZombieEntity create(@NotNull EntityType<SoldierZombieEntity> type, @NotNull Level world) {
            return new SoldierZombieEntity(type, world);
        }
    }
}
