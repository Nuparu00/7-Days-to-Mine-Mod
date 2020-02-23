package com.nuparu.sevendaystomine.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nuparu.sevendaystomine.item.ItemQuality;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings("deprecation")
public class RecipeQualityShapeless extends ShapelessRecipes {

	public RecipeQualityShapeless(String group, ItemStack output, NonNullList<Ingredient> ingredients) {
		super(group, output, ingredients);
	}

	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack stack = super.getCraftingResult(inv);
		if (stack != null) {
			Container c = ReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, "eventHandler",
					"field_70466_a");
			if (c instanceof ContainerWorkbench) {
				ContainerWorkbench container = (ContainerWorkbench) (c);
				SlotCrafting slot = (SlotCrafting) container.getSlot(0);
				EntityPlayer player = (EntityPlayer) (ReflectionHelper.getPrivateValue(SlotCrafting.class, slot,
						"player", "field_75229_a"));
				if (stack.getTagCompound() == null) {
					stack.setTagCompound(new NBTTagCompound());
				}
				stack.getTagCompound().setInteger("Quality", (int) (Math.max(Math.floor(player.getScore()/ItemQuality.XP_PER_QUALITY_POINT), 1)));
			} else if (c instanceof ContainerPlayer) {
				ContainerPlayer container = (ContainerPlayer) (c);
				EntityPlayer player = (EntityPlayer) (ReflectionHelper.getPrivateValue(ContainerPlayer.class, container,
						"player", "field_82855_n"));
				if (stack.getTagCompound() == null) {
					stack.setTagCompound(new NBTTagCompound());
				}
				stack.getTagCompound().setInteger("Quality", (int) Math.min(Math.max(Math.floor(player.getScore()/ItemQuality.XP_PER_QUALITY_POINT), 1),600));
			}
		}
		return stack;

	}
	
	public static class Factory implements IRecipeFactory {
		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			String group = JsonUtils.getString(json, "group", "");

            NonNullList<Ingredient> ings = NonNullList.create();
            for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients"))
                ings.add(CraftingHelper.getIngredient(ele, context));

            if (ings.isEmpty())
                throw new JsonParseException("No ingredients for shapeless recipe");
            if (ings.size() > 9)
                throw new JsonParseException("Too many ingredients for shapeless recipe");

            ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
			return new RecipeQualityShapeless(group, itemstack, ings);
		}
	}

}
