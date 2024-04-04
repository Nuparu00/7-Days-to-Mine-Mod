package nuparu.sevendaystomine.crafting.chemistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import nuparu.sevendaystomine.init.ModItems;

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

	public ArrayList<IChemistryRecipe> getRecipes() {
		return this.recipes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addRecipes() {
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.BOTTLED_WATER),
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_MURKY_WATER)))));
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.BOTTLED_BEER),
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER), new ItemStack(ModItems.CORN),
						new ItemStack(Items.SUGAR)))));
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.BOTTLED_BEER),
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER), new ItemStack(Items.WHEAT),
						new ItemStack(Items.SUGAR)))));
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(Items.GUNPOWDER),
				new ArrayList(Arrays.asList(new ItemStack(Items.COAL), new ItemStack(ModItems.POTASSIUM, 2)))));
		addRecipe(new ChemistryRecipeShapeless(new ItemStack(ModItems.SALT),
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BOTTLED_WATER), new ItemStack(ModItems.NATRIUM_TANK),
						new ItemStack(ModItems.CHLORINE_TANK)))));

		ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
		List<PotionEffect> list = Lists.<PotionEffect>newArrayList();
		list.add(new PotionEffect(MobEffects.POISON, 100, 0));
		PotionUtils.appendEffects(lingering, list);
		addRecipe(new ChemistryRecipeShapeless(lingering,
				new ArrayList(Arrays.asList(new ItemStack(ModItems.BANEBERRY), new ItemStack(ModItems.BANEBERRY),new ItemStack(ModItems.BOTTLED_BEER), new ItemStack(ModItems.BOTTLED_BEER)))));
	}

	public void addRecipe(IChemistryRecipe recipe) {
		recipes.add(recipe);
	}
}
