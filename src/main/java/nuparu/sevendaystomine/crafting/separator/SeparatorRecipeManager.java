package nuparu.sevendaystomine.crafting.separator;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.init.ModItems;

public class SeparatorRecipeManager {
	private static SeparatorRecipeManager INSTANCE;

	private ArrayList<ISeparatorRecipe> recipes = new ArrayList<ISeparatorRecipe>();

	public SeparatorRecipeManager() {
		INSTANCE = this;
		addRecipes();
	}

	public static SeparatorRecipeManager getInstance() {
		return INSTANCE;
	}

	public ArrayList<ISeparatorRecipe> getRecipes() {
		return this.recipes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addRecipes() {
		addRecipe(new SeparatorRecipe(new ItemStack(ModItems.SALT), Arrays.asList(new ItemStack(ModItems.CHLORINE_TANK),new ItemStack(ModItems.NATRIUM_TANK))));
	}

	public void addRecipe(ISeparatorRecipe recipe) {
		recipes.add(recipe);
	}
}
