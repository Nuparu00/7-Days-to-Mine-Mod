package nuparu.sevendaystomine.integration.jei.workbench;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import nuparu.sevendaystomine.SevenDaysToMine;

public class WorkbenchRecipeCategory extends AbstractWorkbenchRecipeCategory<WorkbenchRecipe> {

	private final IDrawable background;
	private final String name;
	
	public WorkbenchRecipeCategory(IGuiHelper helper) {
		super(helper);
		background = helper.createDrawable(TEXTURE, 4, 4, 156, 95);
		name = "Workbench";
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
	public void setRecipe(IRecipeLayout recipeLayout, WorkbenchRecipe recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		int id = 0;
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				stacks.init(id++, true, 8 + j * 18 - 5, 7 + i * 18 - 5);
			}
		}
		stacks.init(id++, false, 129, 39);
		stacks.set(ingredients);
		
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
	}

}
