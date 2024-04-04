package nuparu.sevendaystomine.loot.function;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.item.IQuality;
import nuparu.sevendaystomine.item.ItemFuelTool;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.util.MathUtils;

public class RandomQualityFunction extends LootFunction {

	protected RandomQualityFunction(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		Item item = stack.getItem();
		if (!(item instanceof IQuality))
			return stack;

		IQuality quality = (IQuality) item;
		quality.setQuality(stack, 1+(int)(MathUtils.bias(rand.nextDouble(),0.35)*(ModConfig.players.maxQuality-1)));

		if (item instanceof ItemGun) {
			((ItemGun) item).initNBT(stack);
		} else if (item instanceof ItemFuelTool) {
			((ItemFuelTool) item).initNBT(stack);
		}

		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<RandomQualityFunction> {
		public Serializer() {
			super(new ResourceLocation(SevenDaysToMine.MODID, "random_quality"), RandomQualityFunction.class);
		}

		public void serialize(JsonObject object, RandomQualityFunction functionClazz,
				JsonSerializationContext serializationContext) {

		}

		public RandomQualityFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			return new RandomQualityFunction(conditionsIn);
		}
	}

}
