package nuparu.sevendaystomine.integration.jei.cooking;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.item.crafting.cooking.CookingShapeless;

public class CookingPotShapelessCategory extends AbstractCookingRecipeCategory<CookingShapeless> {
    public static final RecipeType<CookingShapeless> COOKING_POT_SHAPELESS_RECIPE_TYPE =
            RecipeType.create(SevenDaysToMine.MODID, "cooking_pot_shapeless", CookingShapeless.class);

    public CookingPotShapelessCategory(IGuiHelper helper) {
        super(helper,new ItemStack(ModBlocks.COOKING_POT.get()),"container.cooking_pot");
    }

    @Override
    public RecipeType<CookingShapeless> getRecipeType() {
        return COOKING_POT_SHAPELESS_RECIPE_TYPE;
    }
}
