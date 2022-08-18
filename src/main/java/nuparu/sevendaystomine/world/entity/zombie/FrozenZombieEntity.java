package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;

public class FrozenZombieEntity<T extends FrozenZombieEntity> extends ZombieBipedEntity {

    public FrozenZombieEntity(EntityType<T> type, Level world) {
        super(type, world);
    }

}
