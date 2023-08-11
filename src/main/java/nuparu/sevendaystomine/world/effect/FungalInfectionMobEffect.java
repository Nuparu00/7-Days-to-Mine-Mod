package nuparu.sevendaystomine.world.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import nuparu.sevendaystomine.init.ModDamageSources;

public class FungalInfectionMobEffect extends BaseMobEffect{

    public FungalInfectionMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().random.nextInt(12) == 0) {
            livingEntity.hurt(ModDamageSources.FUNGAL_INFECTION.apply(livingEntity.level()), 1);
        }
        if (livingEntity.level().isClientSide() && livingEntity.level().getDifficulty() != Difficulty.PEACEFUL) {
            if (livingEntity.level().random.nextInt(2) == 0) {
                livingEntity.level().addParticle(ParticleTypes.WARPED_SPORE, livingEntity.getRandomX(0.5D), livingEntity.getRandomY(), livingEntity.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }

        }
    }
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
