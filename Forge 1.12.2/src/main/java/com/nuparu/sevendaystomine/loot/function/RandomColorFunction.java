package com.nuparu.sevendaystomine.loot.function;

import java.util.Random;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.item.IQuality;
import com.nuparu.sevendaystomine.item.ItemClothing;
import com.nuparu.sevendaystomine.item.ItemFuelTool;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.item.ItemQuality;
import com.nuparu.sevendaystomine.util.MathUtils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class RandomColorFunction extends LootFunction {

	protected RandomColorFunction(LootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		Item item = stack.getItem();
		if (!(item instanceof ItemClothing))
			return stack;

		ItemClothing clothing = (ItemClothing)item;
		if(!clothing.isDyeable)
			return stack;
		
		clothing.setColor(stack, rand.nextInt(0xffffff + 1));

		return stack;
	}

	public static class Serializer extends LootFunction.Serializer<RandomColorFunction> {
		public Serializer() {
			super(new ResourceLocation(SevenDaysToMine.MODID, "random_color"), RandomColorFunction.class);
		}

		public void serialize(JsonObject object, RandomColorFunction functionClazz,
				JsonSerializationContext serializationContext) {

		}

		public RandomColorFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			return new RandomColorFunction(conditionsIn);
		}
	}

}