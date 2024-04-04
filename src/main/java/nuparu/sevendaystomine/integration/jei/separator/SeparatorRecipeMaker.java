package nuparu.sevendaystomine.integration.jei.separator;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import nuparu.sevendaystomine.crafting.separator.ISeparatorRecipe;
import nuparu.sevendaystomine.crafting.separator.SeparatorRecipeManager;

public class SeparatorRecipeMaker {

	public static List<SeparatorRecipe> getRecipes(IJeiHelpers helpers){
		IStackHelper stackHelper = helpers.getStackHelper();
		ArrayList<ISeparatorRecipe> recipes = SeparatorRecipeManager.getInstance().getRecipes();
		List<SeparatorRecipe> jeiRecipes = Lists.newArrayList();
		
		for(ISeparatorRecipe recipe : recipes) {
			SeparatorRecipe jeiRecipe = new SeparatorRecipe(recipe.getIngredient(), recipe.getResult());
			jeiRecipes.add(jeiRecipe);
		}
		
		return jeiRecipes;
	}
}
