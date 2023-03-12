package nuparu.sevendaystomine.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;

import java.util.Set;

public class RandomQualityFunction extends LootItemConditionalFunction {
    private final NumberProvider value;

    protected RandomQualityFunction(LootItemCondition[] conditionsIn, NumberProvider value) {
        super(conditionsIn);
        this.value = value;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Item item = stack.getItem();
        if((Object)stack instanceof IQualityStack qualityStack && qualityStack.canHaveQuality()){
           qualityStack.setQuality(value.getInt(context));
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootFunctionManager.RANDOM_QUALITY_TYPE;
    }

    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.value.getReferencedContextParams();
    }

    public static LootItemConditionalFunction.Builder<?> setQuality(NumberProvider p_165413_) {
        return simpleBuilder((p_165423_) -> new RandomQualityFunction(p_165423_, p_165413_));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomQualityFunction> {
        public void serialize(JsonObject jsonObject, RandomQualityFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
            jsonObject.add("quality", context.serialize(function.value));
        }

        public RandomQualityFunction deserialize(JsonObject jsonObject, JsonDeserializationContext context, LootItemCondition[] lootConditions) {
            NumberProvider numberprovider = GsonHelper.getAsObject(jsonObject, "quality", context, NumberProvider.class);
            return new RandomQualityFunction(lootConditions,numberprovider);
        }
    }
}
