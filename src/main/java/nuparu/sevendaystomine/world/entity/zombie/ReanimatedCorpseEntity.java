package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import org.jetbrains.annotations.NotNull;

public class ReanimatedCorpseEntity<T extends ReanimatedCorpseEntity> extends ZombieBipedEntity {

    public ReanimatedCorpseEntity(EntityType<ReanimatedCorpseEntity> type, Level world) {
        super(type, world);
    }

    public ReanimatedCorpseEntity(Level world) {
        this(ModEntities.REANIMATED_CORPSE.get(), world);
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

    public static class Factory implements EntityType.EntityFactory<ReanimatedCorpseEntity> {
        @Override
        public @NotNull ReanimatedCorpseEntity create(@NotNull EntityType<ReanimatedCorpseEntity> type, @NotNull Level world) {
            return new ReanimatedCorpseEntity(type, world);
        }
    }
}
