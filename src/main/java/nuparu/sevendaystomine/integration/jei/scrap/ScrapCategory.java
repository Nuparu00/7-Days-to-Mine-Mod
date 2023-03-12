package nuparu.sevendaystomine.integration.jei.scrap;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.crafting.ScrapRecipe;

public class ScrapCategory implements IRecipeCategory<ScrapRecipe> {

    public static final RecipeType<ScrapRecipe> SCRAP_RECIPE_TYPE = RecipeType.create(SevenDaysToMine.MODID, "scrap", ScrapRecipe.class);

    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;

    public static final int width = 116;
    public static final int height = 54;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final ICraftingGridHelper craftingGridHelper;

    public ScrapCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("jei:textures/gui/gui_vanilla.png");
        background = guiHelper.createDrawable(location, 0, 60, width, height);
        icon = guiHelper.createDrawableItemStack(new ItemStack(Blocks.CRAFTING_TABLE));
        title = Component.translatable("gui.jei.category.craftingTable");
        craftingGridHelper = guiHelper.createCraftingGridHelper();
    }

    @Override
    public RecipeType<ScrapRecipe> getRecipeType() {
        return SCRAP_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ScrapRecipe recipe, IFocusGroup focuses) {

    }
}
