package com.nuparu.sevendaystomine.item.crafting.campfire;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.item.ItemStack;

public class CampfireRecipeManager {

	private static CampfireRecipeManager INSTANCE;
	
	private ArrayList<ICampfireRecipe> recipes = new ArrayList<ICampfireRecipe>();
	
	public CampfireRecipeManager() {
		INSTANCE = this;
		addRecipes();
	}
	
	public static CampfireRecipeManager getInstance() {
		return INSTANCE;
	}
	
	public ArrayList<ICampfireRecipe> getRecipes(){
		return this.recipes;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addRecipes() {
		addRecipe(new CampfireRecipeShapeless(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_WATER,2),new ItemStack(com.nuparu.sevendaystomine.init.ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_WATER,3),new ItemStack(com.nuparu.sevendaystomine.init.ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_WATER,4),new ItemStack(com.nuparu.sevendaystomine.init.ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_WATER,2),new ItemStack(com.nuparu.sevendaystomine.init.ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_WATER,3),new ItemStack(com.nuparu.sevendaystomine.init.ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_WATER,4),new ItemStack(com.nuparu.sevendaystomine.init.ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER),new ItemStack(com.nuparu.sevendaystomine.init.ModItems.CANNED_MURKY_WATER)))));
		//addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.STICK),new ItemStack(Items.ARROW),new ArrayList(Arrays.asList(new ItemStack(Items.COAL),new ItemStack(Items.PAPER)))));
	    //addRecipe(new CampfireRecipeShaped(new ItemStack(Items.CAKE),new ItemStack(Items.ARROW),new ItemStack[][]{{new ItemStack(Items.BONE),new ItemStack(Items.ARROW)},{new ItemStack(Items.APPLE),new ItemStack(Items.COOKIE)}}));
	}
	
	public void addRecipe(ICampfireRecipe recipe) {
		recipes.add(recipe);
	}
}
