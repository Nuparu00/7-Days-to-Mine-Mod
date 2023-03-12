package nuparu.sevendaystomine.integration.jei.cooking;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.item.crafting.cooking.CookingShapeless;

public class CookingGrillShapelessCategory extends AbstractCookingRecipeCategory<CookingShapeless> {
    public static final RecipeType<CookingShapeless> COOKING_GRILL_SHAPELESS_RECIPE_TYPE =
            RecipeType.create(SevenDaysToMine.MODID, "cooking_grill_shapeless", CookingShapeless.class);

    public CookingGrillShapelessCategory(IGuiHelper helper) {
        super(helper,new ItemStack(ModBlocks.COOKING_GRILL.get()),"container.cooking_grill");
    }

    @Override
    public RecipeType<CookingShapeless> getRecipeType() {
        return COOKING_GRILL_SHAPELESS_RECIPE_TYPE;
    }
}
