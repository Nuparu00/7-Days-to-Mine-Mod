package nuparu.sevendaystomine.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import nuparu.sevendaystomine.util.ItemUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AugerItem extends FuelDiggerItem {

    public static final Set<ToolAction> DEFAULT_AUGER_ACTIONS = of(ToolActions.PICKAXE_DIG, ToolActions.SHOVEL_DIG);
    private static Set<ToolAction> of(ToolAction... actions) {
        return Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
    public AugerItem(Tier p_42961_, int p_42962_, float p_42963_, Item.Properties p_42964_) {
        super((float) p_42962_, p_42963_, p_42961_, BlockTags.MINEABLE_WITH_PICKAXE, p_42964_);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return DEFAULT_AUGER_ACTIONS.contains(toolAction);
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot,
                                                                        ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

        int fuel = getAmmo(stack,null);
        if (fuel <= 0 && equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(multimap);
            builder.put(Attributes.ATTACK_DAMAGE,new AttributeModifier(ItemUtils.ATTACK_DAMAGE_BOOST, "Tool modifier", 0 , AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ItemUtils.ATTACK_SPEED_BOOST, "Tool modifier",0, AttributeModifier.Operation.MULTIPLY_TOTAL));

            return builder.build();
        }

        return multimap;
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, BlockState state) {
        return (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL))&& net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, BlockState state) {
        return (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL)) ? this.speed : 1.0F;
    }

}