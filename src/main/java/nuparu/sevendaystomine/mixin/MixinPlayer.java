package nuparu.sevendaystomine.mixin;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(method = "checkMovementStatistics(DDD)V", at = @At("HEAD"))
    public void checkMovementStatistics(double x, double y, double z, CallbackInfo ci) {
        Player player = ((Player)(Object)this);
        IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);

        if (!player.isPassenger()) {
            if (player.isSwimming()) {
                int i = Math.round((float)Math.sqrt(x * x + y * y + z * z) * 100.0F);
                if (i > 0) {
                    extendedPlayer.causeExhaustion(0.01F * (float)i * 0.01F);
                }
            } else if (player.isEyeInFluid(FluidTags.WATER)) {
                int j = Math.round((float)Math.sqrt(x * x + y * y + z * z) * 100.0F);
                if (j > 0) {
                    extendedPlayer.causeExhaustion(0.01F * (float)j * 0.01F);
                }
            } else if (player.isInWater()) {
                int k = Math.round((float)Math.sqrt(x * x + z * z) * 100.0F);
                if (k > 0) {
                    extendedPlayer.causeExhaustion(0.01F * (float)k * 0.01F);
                }
            } else if (!player.onClimbable() && player.isOnGround()) {
                int l = Math.round((float)Math.sqrt(x * x + z * z) * 100.0F);
                if (l > 0) {
                    if (player.isSprinting()) {
                        extendedPlayer.causeExhaustion(0.1F * (float)l * 0.01F);
                    }
                }
            }

        }
    }
}
