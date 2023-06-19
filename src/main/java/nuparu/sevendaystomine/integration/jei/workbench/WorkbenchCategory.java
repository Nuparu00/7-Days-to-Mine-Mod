package nuparu.sevendaystomine.integration.jei.workbench;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.integration.jei.CraftingGridHelper;
import nuparu.sevendaystomine.world.item.crafting.ILockedRecipe;

import java.util.List;

public class WorkbenchCategory implements IRecipeCategory<ILockedRecipe> {
    public static final RecipeType<ILockedRecipe> WORKBENCH_RECIPE_TYPE =
            RecipeType.create(SevenDaysToMine.MODID, "workbench", ILockedRecipe.class);

    protected static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
            "textures/gui/container/workbench.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final ICraftingGridHelper craftingGridHelper;

    public WorkbenchCategory(IGuiHelper helper){
        background = helper.createDrawable(TEXTURE, 7, 6, 154, 95);
        icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.WORKBENCH.get()));
        title = Component.translatable("container.workbench");
        craftingGridHelper = new CraftingGridHelper(5,5);
    }

    @Override
    public RecipeType<ILockedRecipe> getRecipeType() {
        return WORKBENCH_RECIPE_TYPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, ILockedRecipe recipe, IFocusGroup focuses) {
        List<List<ItemStack>> inputs = recipe.getIngredients().stream()
                .map(ingredient -> List.of(ingredient.getItems()))
                .toList();
        ItemStack resultItem = recipe.getResultItem();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 38).addItemStack(resultItem);
        craftingGridHelper.createAndSetInputs(builder, inputs, 5, 5);
    }
}
