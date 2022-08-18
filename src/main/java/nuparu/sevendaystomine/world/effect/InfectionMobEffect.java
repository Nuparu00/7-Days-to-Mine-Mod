package nuparu.sevendaystomine.world.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import nuparu.sevendaystomine.init.ModDamageSources;
import nuparu.sevendaystomine.util.MathUtils;

public class InfectionMobEffect extends BaseMobEffect{

    public InfectionMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MAX_HEALTH, "96a8df9e-bf44-11e7-abc4-cec278b6b50a",
                -0.1D,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
