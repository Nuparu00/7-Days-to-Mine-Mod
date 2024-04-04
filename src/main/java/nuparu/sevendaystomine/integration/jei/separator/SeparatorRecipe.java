package nuparu.sevendaystomine.integration.jei.separator;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class SeparatorRecipe implements IRecipeWrapper {

	private final List<ItemStack> outputs;
	private final ItemStack input;

	public SeparatorRecipe(ItemStack input, List<ItemStack> outputs) {
		super();
		this.input = input;
		this.outputs = outputs;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, input);
		ingredients.setOutputs(VanillaTypes.ITEM, outputs);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}

}
