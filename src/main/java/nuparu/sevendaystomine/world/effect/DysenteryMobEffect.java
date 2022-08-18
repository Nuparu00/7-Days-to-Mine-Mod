package nuparu.sevendaystomine.world.effect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;

public class DysenteryMobEffect extends BaseMobEffect{

    public DysenteryMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "9c44164e-bf44-11e7-abc4-cec278b6b50a", -0.1D,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(!ServerConfig.thirst.get()) return;

        if(livingEntity instanceof Player) {
            IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer((Player)livingEntity);
            extendedPlayer.causeExhaustion(0.005F * (float) (amplifier + 1));
        }
    }
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
