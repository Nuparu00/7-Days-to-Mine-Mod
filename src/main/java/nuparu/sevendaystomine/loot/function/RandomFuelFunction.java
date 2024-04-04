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

public class RandomFuelFunction extends LootFunction {

	protected RandomFuelFunction(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		Item item = stack.getItem();
		if (!(item instanceof ItemFuelTool))
			return stack;

		ItemFuelTool tool = (ItemFuelTool) item;
		if(stack.getTagCompound() == null) {
			tool.initNBT(stack);
		}

		int maxFuel = stack.getTagCompound().getInteger("FuelMax");
		stack.getTagCompound().setInteger("FuelCurrent", rand.nextInt(maxFuel+1));

		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<RandomFuelFunction> {
		public Serializer() {
			super(new ResourceLocation(SevenDaysToMine.MODID, "random_fuel"), RandomFuelFunction.class);
		}

		public void serialize(JsonObject object, RandomFuelFunction functionClazz,
				JsonSerializationContext serializationContext) {

		}

		public RandomFuelFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			return new RandomFuelFunction(conditionsIn);
		}
	}

}
