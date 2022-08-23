package nuparu.sevendaystomine.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.common.extensions.IForgeItem;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import nuparu.sevendaystomine.util.ItemUtils;

import java.util.Collection;

@Mixin(IForgeItem.class)
public interface MixinIForgeItem {
    @Shadow(remap = false)
    private Item self() {
        return null;
    }

    /**
     * @author Nuparu00
     * @reason Can not inject into interface, I think- or at least that was the case in 1.16.5
     */
    @Overwrite(remap = false)
    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {
        IForgeItem iForgeItem= (IForgeItem) (Object) this;
        Item self = self();


        Multimap<Attribute, AttributeModifier> vanilla = self().getDefaultAttributeModifiers(slot);
        if(!ServerConfig.quality.get())
            return vanilla;
        if(self instanceof DiggerItem){
            DiggerItem diggerItem = (DiggerItem)self;
            IQualityStack qualityStack = (IQualityStack) (Object)stack;
            int quality = qualityStack.getQuality();

            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(vanilla);
            builder.put(Attributes.ATTACK_DAMAGE,new AttributeModifier(ItemUtils.ATTACK_DAMAGE_BOOST, "Tool modifier", 1 + (quality / (double)QualityManager.maxLevel) * (QualityManager.levels.size() - 1), AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ItemUtils.ATTACK_SPEED_BOOST, "Tool modifier", 1 + ((quality / (double)QualityManager.maxLevel) * (QualityManager.levels.size() - 1) * 0.5f), AttributeModifier.Operation.MULTIPLY_BASE));

            return builder.build();
        }
        else if(self instanceof ArmorItem){

        }
        else if (self instanceof SwordItem){

        } else if (self instanceof ProjectileWeaponItem) {

        }

        return vanilla;
    }

}
