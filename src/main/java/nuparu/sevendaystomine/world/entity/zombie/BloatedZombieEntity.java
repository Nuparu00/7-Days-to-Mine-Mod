package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;

public class BloatedZombieEntity<T extends BloatedZombieEntity> extends ZombieBipedEntity {

    public BloatedZombieEntity(EntityType<BloatedZombieEntity> type, Level world) {
        super(type, world);
    }

    public BloatedZombieEntity(Level world) {
        this(ModEntities.BLOATED_ZOMBIE.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15F)
                .add(ForgeMod.SWIM_SPEED.get(), 1.2F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.ARMOR, 3);
    }

    public static class Factory implements EntityType.EntityFactory<BloatedZombieEntity> {
        @Override
        public BloatedZombieEntity create(EntityType<BloatedZombieEntity> type, Level world) {
            return new BloatedZombieEntity(type, world);
        }
    }
}
