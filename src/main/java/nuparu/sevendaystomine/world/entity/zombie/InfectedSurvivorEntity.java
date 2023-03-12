package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import org.jetbrains.annotations.NotNull;

public class InfectedSurvivorEntity<T extends InfectedSurvivorEntity> extends ZombieBipedEntity {

    public InfectedSurvivorEntity(EntityType<InfectedSurvivorEntity> type, Level world) {
        super(type, world);
    }

    public InfectedSurvivorEntity(Level world) {
        this(ModEntities.INFECTED_SURVIVOR.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.175F)
                .add(ForgeMod.SWIM_SPEED.get(), 1.5F)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ARMOR, 0);
    }

    public static class Factory implements EntityType.EntityFactory<InfectedSurvivorEntity> {
        @Override
        public @NotNull InfectedSurvivorEntity create(@NotNull EntityType<InfectedSurvivorEntity> type, @NotNull Level world) {
            return new InfectedSurvivorEntity(type, world);
        }
    }
}
