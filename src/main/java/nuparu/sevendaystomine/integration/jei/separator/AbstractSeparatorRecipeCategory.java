package nuparu.sevendaystomine.integration.jei.separator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public abstract class AbstractSeparatorRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/separator.png");
	
	protected final IDrawableAnimated animatedArrowRight;
	protected final IDrawableAnimated animatedArrowLeft;
	
	public AbstractSeparatorRecipeCategory(IGuiHelper helper) {
	
		IDrawableStatic staticArrowRight = helper.createDrawable(TEXTURE, 176, 14, 24, 17);
		animatedArrowRight = helper.createAnimatedDrawable(staticArrowRight, 200, IDrawableAnimated.StartDirection.LEFT, false);
		IDrawableStatic staticArrowLeft = helper.createDrawable(TEXTURE, 176, 31, 24, 17);
		animatedArrowLeft = helper.createAnimatedDrawable(staticArrowLeft, 200, IDrawableAnimated.StartDirection.RIGHT, false);
	}
}
