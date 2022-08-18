package nuparu.sevendaystomine.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.item.quality.IQualityItem;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityLevel;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MixinItemStack implements IQualityStack {
    @Override
    public void setQuality(int quality) {
        ItemStack stack = (ItemStack)(Object)this;
        if(((IQualityItem)stack.getItem()).canHaveQuality()){
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("Quality",quality);
        }
    }

    @Override
    public int getQuality() {
        ItemStack stack = (ItemStack)(Object)this;
        if(((IQualityItem)stack.getItem()).canHaveQuality()){
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.contains("Quality", Tag.TAG_INT)) {
                return tag.getInt("Quality");
            }
        }
        return 0;
    }

    @Override
    public QualityLevel getQualityLevel() {
        return QualityManager.getQualityLevel(getQuality());
    }

    @Override
    public boolean canHaveQuality() {
        ItemStack stack = (ItemStack)(Object)this;
        return ((IQualityItem)stack.getItem()).canHaveQuality();
    }
    @Inject(method = "inventoryTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;IZ)V", at = @At("RETURN"))
    public void inventoryTick(Level p_41667_, Entity p_41668_, int p_41669_, boolean p_41670_, CallbackInfo ci) {
        ItemStack stack = (ItemStack)(Object)this;
        if(!p_41670_ && stack.hasTag() && stack.getOrCreateTag().contains("7D2M_UpgradeProgress")){
            ItemUtils.eraseUpgraderData(stack);
        }
    }

}
