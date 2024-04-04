package nuparu.sevendaystomine.integration.jei.chemistry;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeManager;
import nuparu.sevendaystomine.crafting.chemistry.IChemistryRecipe;

public class ChemistryRecipeMaker {

	public static List<ChemistryRecipe> getRecipes(IJeiHelpers helpers){
		IStackHelper stackHelper = helpers.getStackHelper();
		ArrayList<IChemistryRecipe> recipes = ChemistryRecipeManager.getInstance().getRecipes();
		List<ChemistryRecipe> jeiRecipes = Lists.newArrayList();
		
		for(IChemistryRecipe recipe : recipes) {
			ChemistryRecipe jeiRecipe = new ChemistryRecipe(recipe.getIngredients(), recipe.getResult());
			jeiRecipes.add(jeiRecipe);
		}
		
		return jeiRecipes;
	}
}
