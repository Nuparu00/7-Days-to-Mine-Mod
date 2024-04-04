package nuparu.sevendaystomine.integration.jei.chemistry;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ChemistryRecipeCategory extends AbstractChemistryRecipeCategory<ChemistryRecipe> {

	private final IDrawable background;
	private final String name;
	
	public ChemistryRecipeCategory(IGuiHelper helper) {
		super(helper);
		background = helper.createDrawable(TEXTURE, 4, 4, 169, 79);
		name = "Chemistry Station";
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
	public void setRecipe(IRecipeLayout recipeLayout, ChemistryRecipe recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(1, true, 73, 6);
		stacks.init(2, true, 92, 6);
		stacks.init(3, true, 73, 24);
		stacks.init(4, true, 92, 24);
		stacks.init(0, false, 143, 37);
		stacks.set(ingredients);
		
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		animatedFlame.draw(minecraft,84,42);
		animatedArrow.draw(minecraft,115,39);
	}

}
