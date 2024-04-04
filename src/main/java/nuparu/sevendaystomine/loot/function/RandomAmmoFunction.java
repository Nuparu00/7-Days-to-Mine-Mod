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

public class RandomAmmoFunction extends LootFunction {

	protected RandomAmmoFunction(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		Item item = stack.getItem();
		if (!(item instanceof ItemGun))
			return stack;

		ItemGun gun = (ItemGun) item;
		if(stack.getTagCompound() == null) {
			gun.initNBT(stack);
		}
		if(rand.nextInt(2) == 0) return stack;
		int capacity = stack.getTagCompound().getInteger("Capacity");
		stack.getTagCompound().setInteger("Ammo", rand.nextInt(capacity+1));

		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<RandomAmmoFunction> {
		public Serializer() {
			super(new ResourceLocation(SevenDaysToMine.MODID, "random_ammo"), RandomAmmoFunction.class);
		}

		public void serialize(JsonObject object, RandomAmmoFunction functionClazz,
				JsonSerializationContext serializationContext) {

		}

		public RandomAmmoFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			return new RandomAmmoFunction(conditionsIn);
		}
	}

}
