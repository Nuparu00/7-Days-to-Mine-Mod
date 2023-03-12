package nuparu.sevendaystomine.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import nuparu.sevendaystomine.SevenDaysToMine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RandomizableContainerBlockEntity.class)
public class MixinRandomizableContainerBlockEntity {

    @Shadow protected long lootTableSeed;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootContext;)V", ordinal = 0), method = "unpackLootTable(Lnet/minecraft/world/entity/player/Player;)V")
    private void fill(Player p_59641_, CallbackInfo ci) {
        RandomizableContainerBlockEntity blockEntity = (RandomizableContainerBlockEntity)(Object)this;
        SevenDaysToMine.LOGGER.error(this.getClass().getName() + " " + blockEntity.getBlockPos().toShortString() +  " // " + lootTableSeed);

    }


    @Inject(at = @At("HEAD"), method = "setLootTable(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/resources/ResourceLocation;)V")
    private static void setLootTable(BlockGetter p_222767_, RandomSource p_222768_, BlockPos p_222769_, ResourceLocation p_222770_, CallbackInfo ci) {
        SevenDaysToMine.LOGGER.error(p_222769_.toShortString() +  " // " + p_222770_.toString());

    }
}
