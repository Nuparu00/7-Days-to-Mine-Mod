package nuparu.sevendaystomine.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import nuparu.sevendaystomine.world.item.ClothingItem;

public class RandomColorFunction extends LootItemConditionalFunction {

    protected RandomColorFunction(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Item item = stack.getItem();
        if (!(item instanceof ClothingItem clothing))
            return stack;
        if(!clothing.isDyeable())
            return stack;
        clothing.setColor(stack, context.getRandom().nextInt(0xffffff + 1));

        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return ModLootFunctionManager.RANDOM_COLOR_TYPE;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomColorFunction> {
        public void serialize(JsonObject jsonObject, RandomColorFunction function, JsonSerializationContext context) {
            super.serialize(jsonObject, function, context);
        }

        public RandomColorFunction deserialize(JsonObject jsonObject, JsonDeserializationContext context, LootItemCondition[] lootConditions) {
            return new RandomColorFunction(lootConditions);
        }
    }
}
