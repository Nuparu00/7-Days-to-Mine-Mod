package nuparu.sevendaystomine.integration.jei.campfire;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.crafting.campfire.CampfireRecipeManager;
import nuparu.sevendaystomine.crafting.campfire.ICampfireRecipe;

public class CampfireRecipeMaker {

	public static List<CampfireRecipe> getRecipes(IJeiHelpers helpers){
		IStackHelper stackHelper = helpers.getStackHelper();
		ArrayList<ICampfireRecipe> recipes = CampfireRecipeManager.getInstance().getRecipes();
		List<CampfireRecipe> jeiRecipes = Lists.newArrayList();
		
		for(ICampfireRecipe recipe : recipes) {
			ItemStack pot = recipe.getPot();
			List<ItemStack> ingredients = new ArrayList<ItemStack>();
			ingredients.add(pot);
			ingredients.addAll(recipe.getIngredients());
			CampfireRecipe jeiRecipe = new CampfireRecipe(ingredients, recipe.getResult());
			jeiRecipes.add(jeiRecipe);
		}
		
		return jeiRecipes;
	}
}
