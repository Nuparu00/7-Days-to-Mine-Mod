package nuparu.sevendaystomine.integration.jei.campfire;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public abstract class AbstractCampfireRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/campfire.png");
	
	protected static final int input1 = 3;
	protected static final int input2 = 4;
	protected static final int input3 = 5;
	protected static final int input4 = 6;
	protected static final int fuel = 1;
	protected static final int pot = 0;
	protected static final int output = 2;
	
	
	protected final IDrawableStatic staticFlame;
	protected final IDrawableAnimated animatedFlame;
	protected final IDrawableAnimated animatedArrow;
	
	public AbstractCampfireRecipeCategory(IGuiHelper helper) {
		staticFlame = helper.createDrawable(TEXTURE, 176, 0, 14, 14);
		animatedFlame = helper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
		
		IDrawableStatic staticArrow = helper.createDrawable(TEXTURE, 176, 14, 24, 17);
		animatedArrow = helper.createAnimatedDrawable(staticArrow, 200, IDrawableAnimated.StartDirection.LEFT, false);
	}
}
