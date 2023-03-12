package nuparu.sevendaystomine.integration.jei.cooking;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.item.crafting.cooking.CookingShapeless;

public class BeakerShapelessCategory extends AbstractCookingRecipeCategory<CookingShapeless> {
    public static final RecipeType<CookingShapeless> BEAKER_SHAPELESS_RECIPE_TYPE =
            RecipeType.create(SevenDaysToMine.MODID, "beaker_shapeless", CookingShapeless.class);

    public BeakerShapelessCategory(IGuiHelper helper) {
        super(helper,new ItemStack(ModBlocks.BEAKER.get()),"container.beaker");
    }

    @Override
    public RecipeType<CookingShapeless> getRecipeType() {
        return BEAKER_SHAPELESS_RECIPE_TYPE;
    }
}
