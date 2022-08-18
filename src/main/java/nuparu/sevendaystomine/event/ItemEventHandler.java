package nuparu.sevendaystomine.event;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModEffects;
import nuparu.sevendaystomine.json.drink.DrinkDataManager;
import nuparu.sevendaystomine.json.drink.DrinkEntry;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.item.quality.IQualityItem;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
public class ItemEventHandler {
    @SubscribeEvent
    public static void onItemAttributeModifierEvent(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        IQualityItem qualityItem = (IQualityItem) (Object) item;
        if(qualityItem.canHaveQuality()) {
            IQualityStack qualityStack = (IQualityStack) (Object)stack;
            int quality = qualityStack.getQuality();
            if(event.getSlotType() == EquipmentSlot.MAINHAND) {
                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(ItemUtils.ATTACK_DAMAGE_BOOST, "Tool modifier", 1 + (quality / (double) QualityManager.maxLevel) * (QualityManager.levels.size() - 1), AttributeModifier.Operation.MULTIPLY_BASE));
                event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ItemUtils.ATTACK_SPEED_BOOST, "Tool modifier", 1 + (quality / (double) QualityManager.maxLevel) * (QualityManager.levels.size() - 1), AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
    }
}
