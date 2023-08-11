package nuparu.sevendaystomine.integration.jei.locked;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import nuparu.sevendaystomine.integration.jei.CraftingGridHelper;
import nuparu.sevendaystomine.world.item.crafting.ILockedRecipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LockedExtension<T extends ILockedRecipe> implements ICraftingCategoryExtension {

    private final T recipe;
    private final LockedIcon lockedIcon;
    int recipeWidth;
    int recipeHeight;
    private final ICraftingGridHelper craftingGridHelper;

    public LockedExtension(T recipe) {
        this.recipe = recipe;
        lockedIcon = new LockedIcon();
        craftingGridHelper = new CraftingGridHelper(5,5);
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if(!recipe.hasRecipe())
            return;

        this.recipeWidth = recipeWidth;
        this.recipeHeight = recipeHeight;
        lockedIcon.draw(guiGraphics.pose(), recipeWidth);
    }

    @Override
    public List<Component> getTooltipStrings(double mouseX, double mouseY)
    {
        if(!recipe.hasRecipe())
            return Collections.emptyList();

        if(lockedIcon != null){
            List<Component> list = lockedIcon.getTooltipStrings((int)mouseX,(int)mouseY,recipeWidth,recipeHeight, recipe.getRecipe());
            if(list != null) return list;
        }
        return Collections.emptyList();
    }
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        if(recipe.getWidth() > 3 || recipe.getHeight() > 3) return;
        craftingGridHelper.createAndSetInputs(builder,VanillaTypes.ITEM_STACK, recipe.getIngredients().stream().map(i -> Arrays.asList(i.getItems())).toList(),recipe.getWidth(), recipe.getHeight());
        craftingGridHelper.createAndSetOutputs(builder,VanillaTypes.ITEM_STACK, Lists.newArrayList(recipe.getResultItem(Minecraft.getInstance().level.registryAccess())));
    }
}