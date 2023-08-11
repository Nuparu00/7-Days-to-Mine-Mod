package nuparu.sevendaystomine.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.block.entity.ItemHandlerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(RandomizableContainerBlockEntity.class)
public class MixinRandomizableContainerBlockEntity {

    @Inject(at = @At("HEAD"), method = "setLootTable(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceLocation;)V")
    private static void setLootTable(BlockGetter p_222767_, RandomSource p_222768_, BlockPos p_222769_, ResourceLocation p_222770_, CallbackInfo ci) {
        BlockEntity blockentity = p_222767_.getBlockEntity(p_222769_);
        SevenDaysToMine.LOGGER.error(blockentity.getBlockState().toString() + " " + p_222769_.toShortString() +  " /./ " + p_222770_.toString());
       if (blockentity instanceof ItemHandlerBlockEntity<?> itemHandlerBlockEntity) {
            itemHandlerBlockEntity.setLootTable(p_222770_, p_222768_.nextLong());
            SevenDaysToMine.LOGGER.error(p_222769_.toShortString() +  "!!!!!!!!!!!!!!!!!!!!! " + p_222770_.toString());
        }

    }
}
