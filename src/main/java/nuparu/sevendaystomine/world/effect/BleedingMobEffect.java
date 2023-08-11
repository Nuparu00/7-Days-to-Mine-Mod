package nuparu.sevendaystomine.world.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import nuparu.sevendaystomine.init.ModDamageSources;
import nuparu.sevendaystomine.util.MathUtils;

public class BleedingMobEffect extends BaseMobEffect{

    public BleedingMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide() && (livingEntity instanceof Mob || livingEntity instanceof Player)) {
            if (livingEntity.level().random.nextInt(12) == 0) {
                livingEntity.hurt(ModDamageSources.BLEEDING.apply(livingEntity.level()), 1);
                for (int i = 0; i < MathUtils.getIntInRange(livingEntity.level().random, 20, 35); i++) {
                    //PacketManager.sendToTrackingEntity(PacketManager.spawnBlood, new SpawnBloodMessage(entity.getX(0.5), entity.getY() + entity.getBbHeight() * MathUtils.getFloatInRange(0.4f, 0.75f), entity.getZ(0.5), MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.1f, 0.22f), MathUtils.getFloatInRange(-0.1f, 0.1f)), () -> entity);
                }
            }
        }
    }
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
