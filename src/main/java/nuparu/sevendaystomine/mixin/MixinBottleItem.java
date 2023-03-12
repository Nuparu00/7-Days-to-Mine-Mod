package nuparu.sevendaystomine.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import nuparu.sevendaystomine.init.ModFluidTags;
import nuparu.sevendaystomine.init.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BottleItem.class)
public abstract class MixinBottleItem {
    @Shadow protected abstract ItemStack turnBottleIntoItem(ItemStack p_40652_, Player p_40653_, ItemStack p_40654_);

    @Inject(method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;", ordinal = 0), cancellable = true)
    public void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        BottleItem item = (BottleItem) (Object) this;
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult hitresult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hitresult.getType() != HitResult.Type.MISS) {
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = hitresult.getBlockPos();
                if (player.mayInteract(level, blockpos)) {

                    if (level.getFluidState(blockpos).is(ModFluidTags.MURKY_WATER_SOURCE)) {
                        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                        cir.setReturnValue(InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(itemstack, player, new ItemStack(ModItems.MURKY_WATER_BOTTLE.get())), level.isClientSide()));
                        cir.cancel();
                    }
                }
            }
        }
    }
}
