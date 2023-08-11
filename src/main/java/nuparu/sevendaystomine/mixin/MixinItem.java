package nuparu.sevendaystomine.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.world.item.quality.IQualityItem;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem implements IQualityItem {

    @Shadow public abstract Item asItem();

    //@Shadow protected abstract boolean allowedIn(CreativeModeTab p_220153_);

    @Override
    public boolean canHaveQuality() {
        Item instance =(Item)(Object)this;
        return ItemUtils.canHaveQuality(instance);
    }
/*
    @Inject(method = "fillItemCategory(Lnet/minecraft/world/item/CreativeModeTab;Lnet/minecraft/core/NonNullList;)V", at = @At("HEAD"), cancellable = true)
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list, CallbackInfo ci) {
        if (canHaveQuality() && allowedIn(group)) {
            ItemStack stack = new ItemStack(asItem());
            ((IQualityStack)(Object)stack).setQuality(SevenDaysToMine.proxy.getQualityForCurrentPlayer());
            list.add(stack);
            ci.cancel();
        }
    }*/

    @Inject(method = "onCraftedBy(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;)V", at = @At("HEAD"))
    public void onCraftedBy(ItemStack stack, Level world, Player player, CallbackInfo ci) {
        if (canHaveQuality() && ((IQualityStack)(Object)stack).getQuality()<= 0) {
            ((IQualityStack)(Object)stack).setQuality((int) Math.min(Math.floor(player.totalExperience / ServerConfig.xpPerQuality.get()),
                    QualityManager.getMaxLevel()));
        }
    }

    @Inject(method = "getBarColor(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"),cancellable = true)
    public void getBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (ServerConfig.quality.get() && canHaveQuality()) {
            int color = ((IQualityStack)(Object)stack).getQualityLevel().textColor;
            cir.setReturnValue(color);
        }
    }

    @Inject(method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    public void getName(ItemStack stack, CallbackInfoReturnable<Component> cir) {
        if (ServerConfig.quality.get() && canHaveQuality()) {
            Component component = cir.getReturnValue();

            int color = ((IQualityStack)(Object)stack).getQualityLevel().textColor;
            MutableComponent mutableComponent = Component.empty().append(component);
            mutableComponent.setStyle(mutableComponent.getStyle().withColor(color));
            cir.setReturnValue(mutableComponent);
        }
    }/*
    @Inject(method = "getItemCategory()Lnet/minecraft/world/item/CreativeModeTab;", at = @At("RETURN"), cancellable = true)
    public void getItemCategory(CallbackInfoReturnable<CreativeModeTab> cir) {
        Item instance =(Item)(Object)this;
        if(instance == Items.TORCH || instance == Items.SOUL_TORCH){
            cir.setReturnValue(null);
        }
    }*/
    @Inject(method = "inventoryTick(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;IZ)V", at = @At("RETURN"))
    public void inventoryTick(ItemStack stack, Level p_41405_, Entity entity, int slot, boolean p_41408_, CallbackInfo ci) {
        Item instance =(Item)(Object)this;
        if(entity instanceof Player) {
            if (instance == Items.TORCH) {
                ItemStack newStack = new ItemStack(ModItems.TORCH.get(), stack.getCount());
                if(stack.hasCustomHoverName()) {
                    newStack.setHoverName(stack.getDisplayName());
                }
                ((Player)entity).getInventory().setItem(slot,newStack);
            }
            else if (instance == Items.SOUL_TORCH) {
                ItemStack newStack = new ItemStack(ModItems.SOUL_TORCH.get(), stack.getCount());
                if(stack.hasCustomHoverName()) {
                    newStack.setHoverName(stack.getDisplayName());
                }
                ((Player)entity).getInventory().setItem(slot,newStack);
            }
        }
    }
}
