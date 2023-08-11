package nuparu.sevendaystomine.world.effect;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import nuparu.sevendaystomine.init.ModDamageSources;

public class AlcoholPoisonMobEffect extends BaseMobEffect {

	public AlcoholPoisonMobEffect(MobEffectCategory type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"0d9a52e1-adc0-4c8b-bdad-3fe38a5d2657", -0.1D, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.MAX_HEALTH,
				"1acf3189-a3be-48d6-a8cb-9a3181c5ad36", -0.1, AttributeModifier.Operation.MULTIPLY_BASE);

	}

	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
		if (livingEntity.level().getDifficulty() == Difficulty.PEACEFUL)
			return;
		if (livingEntity.level().random.nextInt(25) == 0) {
			livingEntity.hurt(ModDamageSources.ALCOHOL_POISONING.apply(livingEntity.level()), 1);

		}
	}
	@Override
	public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
		return true;
	}

}
