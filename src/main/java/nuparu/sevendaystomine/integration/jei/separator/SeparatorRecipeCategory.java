package nuparu.sevendaystomine.integration.jei.separator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import nuparu.sevendaystomine.SevenDaysToMine;

public class SeparatorRecipeCategory extends AbstractSeparatorRecipeCategory<SeparatorRecipe> {

	private final IDrawable background;
	private final String name;
	
	public SeparatorRecipeCategory(IGuiHelper helper) {
		super(helper);
		background = helper.createDrawable(TEXTURE, 4, 4, 169, 79);
		name = "Electrolytic Cell";
	}

	@Override
	public String getUid() {
		return name;
	}

	@Override
	public String getTitle() {
		return name;
	}

	@Override
	public String getModName() {
		return SevenDaysToMine.MODID;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, SeparatorRecipe recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(0, true, 75, 37);
		stacks.init(1, false, 7, 37);
		stacks.init(2, false, 143, 37);
		
		stacks.set(ingredients);
		
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		animatedArrowRight.draw(minecraft,103,39);
		animatedArrowLeft.draw(minecraft,41,39);
	}

}
