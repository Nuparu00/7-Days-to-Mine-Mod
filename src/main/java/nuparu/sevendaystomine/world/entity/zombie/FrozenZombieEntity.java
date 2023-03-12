package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class FrozenZombieEntity<T extends FrozenZombieEntity> extends ZombieBipedEntity {

    public FrozenZombieEntity(EntityType<T> type, Level world) {
        super(type, world);
    }

}
