package com.nuparu.sevendaystomine.item.crafting.separator;

import java.util.ArrayList;
import java.util.Arrays;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

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
		addRecipe(new SeparatorRecipe(new ItemStack(Blocks.DIRT), Arrays.asList(new ItemStack(Blocks.STONE),new ItemStack(Blocks.GRASS))));
	}

	public void addRecipe(ISeparatorRecipe recipe) {
		recipes.add(recipe);
	}
}
