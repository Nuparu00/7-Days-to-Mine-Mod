package nuparu.sevendaystomine.integration.jei.workbench;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public abstract class AbstractWorkbenchRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/workbench.png");

	public AbstractWorkbenchRecipeCategory(IGuiHelper helper) {
		
	}
}
