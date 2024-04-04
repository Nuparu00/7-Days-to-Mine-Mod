package nuparu.sevendaystomine.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

@SuppressWarnings("deprecation")
public class RecipeManager {

	public static RecipeManager INSTANCE;

	public RecipeManager() {
		INSTANCE = this;
	}

	public void init() {

	}

	public static void removeRecipe(Item item) {
		ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
		ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());

		for (IRecipe r : recipes) {
			ItemStack output = r.getRecipeOutput();
			if (output.getItem() == item) {
				recipeRegistry.remove(r.getRegistryName());
				recipeRegistry.register(DummyRecipe.from(r));
			}
		}
	}

	public static void removeRecipe(ItemStack itemStack) {
		ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
		ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());

		for (IRecipe r : recipes) {
			ItemStack output = r.getRecipeOutput();
			if (output.equals(itemStack) && r.getRegistryName().getResourceDomain().equals("minecraft")) {
				recipeRegistry.remove(r.getRegistryName());
				recipeRegistry.register(DummyRecipe.from(r));
			}
		}
	}

	public static void removeRecipe(Block block) {
		removeRecipe(Item.getItemFromBlock(block));
	}

	public static void removeItem(Item item) {
		item.setCreativeTab(null);
		ForgeRegistry<IRecipe> recipeRegistry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
		ArrayList<IRecipe> recipes = Lists.newArrayList(recipeRegistry.getValues());

		for (IRecipe r : recipes) {
			ItemStack output = r.getRecipeOutput();
			if (output.getItem() == item) {
				recipeRegistry.remove(r.getRegistryName());
				recipeRegistry.register(DummyRecipe.from(r));
			}
		}
	}

	public static void removeItem(Block block) {
		removeItem(Item.getItemFromBlock(block));
	}
	
	public static void removeSmelting(ItemStack resultStack, String modID) {
		ItemStack recipeResult = null;
		Map<ItemStack,ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
		Iterator<ItemStack> iterator = recipes.keySet().iterator();
		while(iterator.hasNext()) {
			ItemStack tmpRecipe = iterator.next();
			recipeResult = recipes.get(tmpRecipe);
			if (ItemStack.areItemStacksEqual(resultStack, recipeResult)) {
				iterator.remove();
			 }
		}
	}
}
