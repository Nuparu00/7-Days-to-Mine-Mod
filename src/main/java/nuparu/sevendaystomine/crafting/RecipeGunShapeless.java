package nuparu.sevendaystomine.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import nuparu.sevendaystomine.item.IQuality;

@SuppressWarnings("deprecation")
public class RecipeGunShapeless extends RecipeLockedShapeless {

	
	public ItemStack output = ItemStack.EMPTY;
	
	public RecipeGunShapeless(String group, ItemStack output, NonNullList<Ingredient> ingredients, String recipe) {
		super(group, output, ingredients, recipe, false);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack stack = super.getCraftingResult(inv);
		
		int count = 0;
		int qualitySum = 0;
		
		for (int k = 0; k < inv.getSizeInventory(); ++k) {
			ItemStack itemstack = inv.getStackInSlot(k);
			if(!itemstack.isEmpty() && itemstack.getItem() instanceof IQuality) {
				count++;
				qualitySum+=((IQuality)itemstack.getItem()).getQuality(itemstack);
			}
			
		}
		((IQuality)stack.getItem()).setQuality(stack, (int)((float)qualitySum/count));
		
		output = stack;
		
		return stack;

	}
	
	@Override
	public ItemStack getRecipeOutput()
    {
        return output.isEmpty() ? super.getRecipeOutput() : output;
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
            if (ings.size() > 25)
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            String recipe = JsonUtils.getString(json, "recipe");
			if (recipe.isEmpty())
				throw new JsonSyntaxException("Property recipe not specified");
			
            ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
			return new RecipeGunShapeless(group, itemstack, ings, recipe);
		}
	}

}
