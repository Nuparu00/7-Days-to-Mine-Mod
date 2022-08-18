package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;

public class FrostbittenWorkerEntity<T extends FrostbittenWorkerEntity> extends FrozenZombieEntity {

    public FrostbittenWorkerEntity(EntityType<FrostbittenWorkerEntity> type, Level world) {
        super(type, world);
    }

    public FrostbittenWorkerEntity(Level world) {
        this(ModEntities.FROSTBITTEN_WORKER.get(), world);
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

    public static class Factory implements EntityType.EntityFactory<FrostbittenWorkerEntity> {
        @Override
        public FrostbittenWorkerEntity create(EntityType<FrostbittenWorkerEntity> type, Level world) {
            return new FrostbittenWorkerEntity(type, world);
        }
    }
}
