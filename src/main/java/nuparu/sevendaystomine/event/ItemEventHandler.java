package nuparu.sevendaystomine.event;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.world.item.quality.IQualityItem;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
public class ItemEventHandler {
    @SubscribeEvent
    public static void onItemAttributeModifierEvent(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        IQualityItem qualityItem = (IQualityItem) item;
        if(qualityItem.canHaveQuality()) {
            IQualityStack qualityStack = (IQualityStack) (Object)stack;
            int quality = qualityStack.getQuality();
            if(event.getSlotType() == EquipmentSlot.MAINHAND) {
                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ItemUtils.ATTACK_DAMAGE_BOOST, "Tool modifier", qualityStack.getRelativeQualityScaled(), AttributeModifier.Operation.MULTIPLY_BASE));
                if (!(item instanceof ProjectileWeaponItem)) {
                    event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ItemUtils.ATTACK_SPEED_BOOST, "Tool modifier", qualityStack.getRelativeQualityScaled(), AttributeModifier.Operation.MULTIPLY_BASE));
                }
            }
        }
    }
}
