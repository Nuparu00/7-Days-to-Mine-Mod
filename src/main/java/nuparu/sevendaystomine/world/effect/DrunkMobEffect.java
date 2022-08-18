package nuparu.sevendaystomine.world.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class DrunkMobEffect extends BaseMobEffect {

	public DrunkMobEffect(MobEffectCategory type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"9c442288-bf44-11e7-abc4-cec278b6b50a", -0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.MAX_HEALTH, "9c442f44-bf44-11e7-abc4-cec278b6b50a",
				-0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
