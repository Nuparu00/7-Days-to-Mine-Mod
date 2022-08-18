package nuparu.sevendaystomine.world.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SplintedMobLegEffect extends BaseMobEffect{

    public SplintedMobLegEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "7a5c093e-7a5d-11ea-bc55-0242ac130003", -0.1D,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
