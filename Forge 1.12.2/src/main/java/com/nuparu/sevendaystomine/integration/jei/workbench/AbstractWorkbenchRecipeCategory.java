package com.nuparu.sevendaystomine.integration.jei.workbench;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractWorkbenchRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {

	protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/workbench.png");

	public AbstractWorkbenchRecipeCategory(IGuiHelper helper) {
		
	}
}
