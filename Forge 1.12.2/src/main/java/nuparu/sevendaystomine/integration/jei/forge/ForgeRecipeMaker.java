package nuparu.sevendaystomine.integration.jei.forge;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import nuparu.sevendaystomine.crafting.forge.IForgeRecipe;

public class ForgeRecipeMaker {

	public static List<ForgeRecipe> getRecipes(IJeiHelpers helpers){
		IStackHelper stackHelper = helpers.getStackHelper();
		ArrayList<IForgeRecipe> recipes = ForgeRecipeManager.getInstance().getRecipes();
		List<ForgeRecipe> jeiRecipes = Lists.newArrayList();
		
		for(IForgeRecipe recipe : recipes) {
			ItemStack mold = recipe.getMold();
			List<ItemStack> ingredients = new ArrayList<ItemStack>();
			ingredients.add(mold);
			ingredients.addAll(recipe.getIngredients());
			ForgeRecipe jeiRecipe = new ForgeRecipe(ingredients, recipe.getResult());
			jeiRecipes.add(jeiRecipe);
		}
		
		return jeiRecipes;
	}
}
