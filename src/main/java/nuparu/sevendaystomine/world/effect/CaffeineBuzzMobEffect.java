package nuparu.sevendaystomine.world.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CaffeineBuzzMobEffect extends BaseMobEffect {

	public CaffeineBuzzMobEffect(MobEffectCategory type, int color) {
		super(type, color);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
				"275defd8-3fc1-11ec-9356-0242ac130003", 0.125D, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.ATTACK_SPEED,
				"2ad83146-3fc1-11ec-9356-0242ac130003", 0.225D, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.FLYING_SPEED,
				"2e3d8142-3fc1-11ec-9356-0242ac130003", 0.125D, AttributeModifier.Operation.MULTIPLY_BASE);
	}
}