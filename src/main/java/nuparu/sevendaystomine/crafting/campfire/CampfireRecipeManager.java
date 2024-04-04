package nuparu.sevendaystomine.crafting.campfire;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

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
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.BOTTLED_WATER),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.BOTTLED_WATER,2),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_MURKY_WATER),new ItemStack(ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.BOTTLED_WATER,3),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_MURKY_WATER),new ItemStack(ModItems.BOTTLED_MURKY_WATER),new ItemStack(ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.BOTTLED_WATER,4),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_MURKY_WATER),new ItemStack(ModItems.BOTTLED_MURKY_WATER),new ItemStack(ModItems.BOTTLED_MURKY_WATER),new ItemStack(ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.CANNED_WATER),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.CANNED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.CANNED_WATER,2),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.CANNED_MURKY_WATER),new ItemStack(ModItems.CANNED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.CANNED_WATER,3),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.CANNED_MURKY_WATER),new ItemStack(ModItems.CANNED_MURKY_WATER),new ItemStack(ModItems.CANNED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.CANNED_WATER,4),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.CANNED_MURKY_WATER),new ItemStack(ModItems.CANNED_MURKY_WATER),new ItemStack(ModItems.CANNED_MURKY_WATER),new ItemStack(ModItems.CANNED_MURKY_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.BOTTLED_COFFEE),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER),new ItemStack(ModItems.COFFEE_BEANS)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.BOTTLED_COFFEE,2),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER),new ItemStack(ModItems.COFFEE_BEANS),new ItemStack(ModItems.BOTTLED_WATER),new ItemStack(ModItems.COFFEE_BEANS)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.GOLDENROD_TEA),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER),new ItemStack(ModBlocks.GOLDENROD)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.GOLDENROD_TEA),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER),new ItemStack(ModBlocks.GOLDENROD),new ItemStack(ModItems.BOTTLED_WATER),new ItemStack(ModBlocks.GOLDENROD)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_CHICKEN),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.CHICKEN)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_CHICKEN,2),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_CHICKEN,3),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_CHICKEN,4),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN),new ItemStack(Items.CHICKEN)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_PORKCHOP),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.PORKCHOP)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_PORKCHOP,2),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.PORKCHOP),new ItemStack(Items.PORKCHOP)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_PORKCHOP,3),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.PORKCHOP),new ItemStack(Items.PORKCHOP),new ItemStack(Items.PORKCHOP)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_PORKCHOP,4),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.PORKCHOP),new ItemStack(Items.PORKCHOP),new ItemStack(Items.PORKCHOP),new ItemStack(Items.PORKCHOP)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_BEEF),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.BEEF)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_BEEF,2),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.BEEF),new ItemStack(Items.BEEF)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_BEEF,3),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.BEEF),new ItemStack(Items.BEEF),new ItemStack(Items.BEEF)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_BEEF,4),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.BEEF),new ItemStack(Items.BEEF),new ItemStack(Items.BEEF),new ItemStack(Items.BEEF)))));
	    addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_MUTTON),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.MUTTON)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_MUTTON,2),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.MUTTON),new ItemStack(Items.MUTTON)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_MUTTON,3),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.MUTTON),new ItemStack(Items.MUTTON),new ItemStack(Items.MUTTON)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_MUTTON,4),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.MUTTON),new ItemStack(Items.MUTTON),new ItemStack(Items.MUTTON),new ItemStack(Items.MUTTON)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_RABBIT),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.RABBIT)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_RABBIT,2),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.RABBIT),new ItemStack(Items.RABBIT)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_RABBIT,3),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.RABBIT),new ItemStack(Items.RABBIT),new ItemStack(Items.RABBIT)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_RABBIT,4),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.RABBIT),new ItemStack(Items.RABBIT),new ItemStack(Items.RABBIT),new ItemStack(Items.RABBIT)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.BAKED_POTATO),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.POTATO)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.BAKED_POTATO,2),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.POTATO),new ItemStack(Items.POTATO)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.BAKED_POTATO,3),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.POTATO),new ItemStack(Items.POTATO),new ItemStack(Items.POTATO)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.BAKED_POTATO,4),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.POTATO),new ItemStack(Items.POTATO),new ItemStack(Items.POTATO),new ItemStack(Items.POTATO)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_FISH),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.FISH)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_FISH,2),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.FISH,1,0),new ItemStack(Items.FISH,1,0)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_FISH,3),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.FISH,1,0),new ItemStack(Items.FISH,1,0),new ItemStack(Items.FISH,1,0)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_FISH,4),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.FISH,1,0),new ItemStack(Items.FISH,1,0),new ItemStack(Items.FISH,1,0),new ItemStack(Items.FISH,1,0)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_FISH,1,1),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.FISH,1,1)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_FISH,2,1),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.FISH,1,1),new ItemStack(Items.FISH,1,1)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_FISH,3,1),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.FISH,1,1),new ItemStack(Items.FISH,1,1),new ItemStack(Items.FISH,1,1)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.COOKED_FISH,4,1),new ItemStack(ModItems.COOKING_GRILL),new ArrayList(Arrays.asList(new ItemStack(Items.FISH,1,1),new ItemStack(Items.FISH,1,1),new ItemStack(Items.FISH,1,1),new ItemStack(Items.FISH,1,1)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.ANTIBIOTICS),new ItemStack(ModBlocks.BEAKER),new ArrayList(Arrays.asList(new ItemStack(ModItems.MOLDY_BREAD),new ItemStack(ModItems.POTASSIUM,2),new ItemStack(ModItems.BOTTLED_WATER)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.COFFEE_BEANS),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.COFFEE_BERRY)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.COFFEE_BEANS,2),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.COFFEE_BERRY),new ItemStack(ModItems.COFFEE_BERRY)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.COFFEE_BEANS,3),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.COFFEE_BERRY),new ItemStack(ModItems.COFFEE_BERRY),new ItemStack(ModItems.COFFEE_BERRY)))));
		addRecipe(new CampfireRecipeShapeless(new ItemStack(ModItems.COFFEE_BEANS,4),new ItemStack(ModBlocks.COOKING_POT),new ArrayList(Arrays.asList(new ItemStack(ModItems.COFFEE_BERRY),new ItemStack(ModItems.COFFEE_BERRY),new ItemStack(ModItems.COFFEE_BERRY),new ItemStack(ModItems.COFFEE_BERRY)))));
		//addRecipe(new CampfireRecipeShapeless(new ItemStack(Items.STICK),new ItemStack(Items.ARROW),new ArrayList(Arrays.asList(new ItemStack(Items.COAL),new ItemStack(Items.PAPER)))));
	    //addRecipe(new CampfireRecipeShaped(new ItemStack(Items.CAKE),new ItemStack(Items.ARROW),new ItemStack[][]{{new ItemStack(Items.BONE),new ItemStack(Items.ARROW)},{new ItemStack(Items.APPLE),new ItemStack(Items.COOKIE)}}));
	}
	
	public void addRecipe(ICampfireRecipe recipe) {
		recipes.add(recipe);
	}
}
