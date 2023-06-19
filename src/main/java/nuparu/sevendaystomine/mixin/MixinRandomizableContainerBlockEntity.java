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

    @Shadow protected long lootTableSeed;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootContext;)V", ordinal = 0), method = "unpackLootTable(Lnet/minecraft/world/entity/player/Player;)V")
    private void fill(Player p_59641_, CallbackInfo ci) {
        RandomizableContainerBlockEntity blockEntity = (RandomizableContainerBlockEntity)(Object)this;
        SevenDaysToMine.LOGGER.error(blockEntity.getBlockState().toString() + " " + this.getClass().getName() + " " + blockEntity.getBlockPos().toShortString() +  " // " + lootTableSeed);

    }


    @Inject(at = @At("HEAD"), method = "setLootTable(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceLocation;)V")
    private static void setLootTable(BlockGetter p_222767_, RandomSource p_222768_, BlockPos p_222769_, ResourceLocation p_222770_, CallbackInfo ci) {
        BlockEntity blockentity = p_222767_.getBlockEntity(p_222769_);
        SevenDaysToMine.LOGGER.error(blockentity.getBlockState().toString() + " " + p_222769_.toShortString() +  " /./ " + p_222770_.toString());
       if (blockentity instanceof ItemHandlerBlockEntity<?> itemHandlerBlockEntity) {
            itemHandlerBlockEntity.setLootTable(p_222770_, p_222768_.nextLong());
            SevenDaysToMine.LOGGER.error(p_222769_.toShortString() +  "!!!!!!!!!!!!!!!!!!!!! " + p_222770_.toString());
        }

    }



    @Inject(at = @At("HEAD"), method = "setLootTable(Lnet/minecraft/resources/ResourceLocation;J)V")
    private void setLootTable(ResourceLocation p_59627_, long p_59628_, CallbackInfo ci) {
        RandomizableContainerBlockEntity blockEntity = (RandomizableContainerBlockEntity)(Object)this;
        SevenDaysToMine.LOGGER.error(blockEntity.getBlockState().toString() + " " + blockEntity.getBlockPos().toShortString() +  " >> " + p_59628_);
        //System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()).replace( ',', '\n' ));

    }
    @Inject(at = @At("HEAD"), method = "tryLoadLootTable(Lnet/minecraft/nbt/CompoundTag;)Z")
    private void tryLoadLootTable(CompoundTag p_59632_, CallbackInfoReturnable<Boolean> cir) {
        RandomizableContainerBlockEntity blockEntity = (RandomizableContainerBlockEntity)(Object)this;
        if(blockEntity.getBlockState().getBlock() == Blocks.HOPPER) {
            SevenDaysToMine.LOGGER.error(blockEntity.getBlockState().toString() + " " + blockEntity.getBlockPos().toShortString() + " >> " + p_59632_.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()).replace( ',', '\n' ));
        }
    }
}
