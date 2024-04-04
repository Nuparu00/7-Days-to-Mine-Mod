package nuparu.sevendaystomine.integration.jei.workbench;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import nuparu.sevendaystomine.crafting.workbench.RecipeWorkbenchShaped;

public class WorkbenchRecipeMaker {

	public static List<WorkbenchRecipe> getRecipes(IJeiHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();
		List<RecipeWorkbenchShaped> recipes = RecipeWorkbenchShaped.RECIPES;
		List<WorkbenchRecipe> jeiRecipes = Lists.newArrayList();

		for (RecipeWorkbenchShaped recipe : recipes) {
			List<ItemStack> stacks = new ArrayList<ItemStack>();
			for (Ingredient ing : recipe.getIngredients()) {
				if (ing.getMatchingStacks() == null || ing.getMatchingStacks().length < 1) {
					stacks.add(ItemStack.EMPTY);
					continue;
				}
				stacks.add(ing.getMatchingStacks()[0]);
			}
			WorkbenchRecipe jeiRecipe = new WorkbenchRecipe(stacks, recipe.getCraftingResult(null));
			jeiRecipes.add(jeiRecipe);
		}

		return jeiRecipes;
	}
}
