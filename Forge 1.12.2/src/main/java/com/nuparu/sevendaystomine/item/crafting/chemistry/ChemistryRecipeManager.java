package com.nuparu.sevendaystomine.item.crafting.chemistry;

import java.util.ArrayList;
import java.util.Arrays;

import com.nuparu.sevendaystomine.init.ModItems;

import net.minecraft.item.ItemStack;

public class ChemistryRecipeManager {
private static ChemistryRecipeManager INSTANCE;
	
	private ArrayList<IChemistryRecipe> recipes = new ArrayList<IChemistryRecipe>();
	
	public ChemistryRecipeManager() {
		INSTANCE = this;
		addRecipes();
	}
	
	public static ChemistryRecipeManager getInstance() {
		return INSTANCE;
	}
	
	public ArrayList<IChemistryRecipe> getRecipes(){
		return this.recipes;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addRecipes() {
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.BOTTLED_WATER), new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER)))));
	}
	
	public void addRecipe(IChemistryRecipe recipe) {
		recipes.add(recipe);
	}
}
