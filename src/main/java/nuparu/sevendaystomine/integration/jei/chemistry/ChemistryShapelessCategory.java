package nuparu.sevendaystomine.integration.jei.chemistry;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.crafting.chemistry.ChemistryRecipeShapeless;

public class ChemistryShapelessCategory extends AbstractChemistryRecipeCategory<ChemistryRecipeShapeless> {
    public static final RecipeType<ChemistryRecipeShapeless> CHEMISTRY_SHAPELESS_RECIPE_TYPE =
            RecipeType.create(SevenDaysToMine.MODID, "chemistry_shapeless", ChemistryRecipeShapeless.class);

    public ChemistryShapelessCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public RecipeType<ChemistryRecipeShapeless> getRecipeType() {
        return CHEMISTRY_SHAPELESS_RECIPE_TYPE;
    }
}
