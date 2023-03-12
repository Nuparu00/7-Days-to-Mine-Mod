package nuparu.sevendaystomine.loot.modifier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class QualityModifier extends LootModifier {
    public static final Supplier<Codec<QualityModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, QualityModifier::new)));
    public QualityModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }
    @Nonnull
    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for(int i = 0; i < generatedLoot.size(); i++){
            ItemStack stack = generatedLoot.get(i);
            if((Object)stack instanceof IQualityStack qualityStack){
                if(qualityStack.canHaveQuality() && !qualityStack.hasQuality()){
                    qualityStack.setQuality(context.getRandom().nextIntBetweenInclusive(1,QualityManager.getMaxLevel()));
                }
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
