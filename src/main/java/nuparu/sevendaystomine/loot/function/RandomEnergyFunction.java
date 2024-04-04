package nuparu.sevendaystomine.loot.function;

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
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.item.ItemFuelTool;

import java.util.Random;

public class RandomEnergyFunction extends LootFunction {

	protected RandomEnergyFunction(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		Item item = stack.getItem();
		if (!(item instanceof IBattery))
			return stack;

		IBattery battery = (IBattery) item;
		battery.setVoltage(stack,null,rand.nextInt((int) (1 + battery.getCapacity(stack, null))));

		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<RandomEnergyFunction> {
		public Serializer() {
			super(new ResourceLocation(SevenDaysToMine.MODID, "random_energy"), RandomEnergyFunction.class);
		}

		public void serialize(JsonObject object, RandomEnergyFunction functionClazz,
				JsonSerializationContext serializationContext) {

		}

		public RandomEnergyFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
                                                LootCondition[] conditionsIn) {
			return new RandomEnergyFunction(conditionsIn);
		}
	}

}
