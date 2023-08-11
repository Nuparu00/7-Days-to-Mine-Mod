package nuparu.sevendaystomine.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.world.item.GunItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics {

    @Shadow public abstract void fill(RenderType pRenderType, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor);

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;getCooldownPercent(Lnet/minecraft/world/item/Item;F)F"), method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    private float renderGuiItemDecorations$getCooldownPercent(ItemCooldowns instance, Item item, float partialTicks) {
        return 0;
    }
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getCooldowns()Lnet/minecraft/world/item/ItemCooldowns;", ordinal = 0), method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
    private void renderGuiItemDecorations$renderCooldown(Font pFont, ItemStack stack, int pX, int pY, String pText, CallbackInfo ci) {
        LocalPlayer localplayer = Minecraft.getInstance().player;
        float f = localplayer == null ? 0.0F : (stack.getItem() instanceof GunItem gunItem ? gunItem.getCooldownPercent(stack) : localplayer.getCooldowns().getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime()));
        if (f > 0.0F) {
            RenderSystem.disableDepthTest();
            int i1 = pY + Mth.floor(16.0F * (1.0F - f));
            int j1 = i1 + Mth.ceil(16.0F * f);
            fill(RenderType.guiOverlay(), pX, i1, pX + 16, j1, Integer.MAX_VALUE);
            RenderSystem.enableDepthTest();
        }
    }
}
