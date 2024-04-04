package nuparu.sevendaystomine.integration.jei.chemistry;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ChemistryRecipe implements IRecipeWrapper {

	private final List<ItemStack> inputs;
	private final ItemStack output;

	public ChemistryRecipe(List<ItemStack> inputs, ItemStack output) {
		super();
		this.inputs = inputs;
		this.output = output;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}

}
