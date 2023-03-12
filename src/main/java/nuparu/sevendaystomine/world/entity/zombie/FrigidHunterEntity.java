package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import org.jetbrains.annotations.NotNull;

public class FrigidHunterEntity<T extends FrigidHunterEntity> extends FrozenZombieEntity {

    public FrigidHunterEntity(EntityType<FrigidHunterEntity> type, Level world) {
        super(type, world);
    }

    public FrigidHunterEntity(Level world) {
        this(ModEntities.FRIGID_HUNTER.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15F)
                .add(ForgeMod.SWIM_SPEED.get(), 1.5F)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.ARMOR, 1);
    }

    public static class Factory implements EntityType.EntityFactory<FrigidHunterEntity> {
        @Override
        public @NotNull FrigidHunterEntity create(@NotNull EntityType<FrigidHunterEntity> type, @NotNull Level world) {
            return new FrigidHunterEntity(type, world);
        }
    }
}
