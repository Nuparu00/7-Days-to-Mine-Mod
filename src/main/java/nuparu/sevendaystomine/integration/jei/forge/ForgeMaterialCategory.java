package nuparu.sevendaystomine.integration.jei.forge;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.json.scrap.ScrapDataManager;
import nuparu.sevendaystomine.json.scrap.ScrapEntry;
import nuparu.sevendaystomine.json.scrap.WeightWrapper;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import nuparu.sevendaystomine.world.item.crafting.forge.ForgeRecipeMaterial;
import nuparu.sevendaystomine.world.item.crafting.forge.MaterialStack;

import java.util.ArrayList;

public class ForgeMaterialCategory extends AbstractForgeRecipeCategory<ForgeRecipeMaterial>{
    public static final RecipeType<ForgeRecipeMaterial> FORGE_MATERIAL_RECIPE_TYPE =
            RecipeType.create(SevenDaysToMine.MODID, "forge_material", ForgeRecipeMaterial.class);

    public ForgeMaterialCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public RecipeType<ForgeRecipeMaterial> getRecipeType() {
        return FORGE_MATERIAL_RECIPE_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ForgeRecipeMaterial recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,3,38).addItemStack(recipe.getMoldItem());
        ArrayList<ItemStack> list = new ArrayList<>();

        for(MaterialStack materialStack : recipe.getMaterialIngredients()){
            EnumMaterial material = materialStack.getMaterial();
            WeightWrapper weight = materialStack.getWeight();

            ScrapEntry entry = ScrapDataManager.INSTANCE.getScrapResult(material);
            if(entry != null){
                ItemStack stack = new ItemStack(entry.item, (int)Math.ceil((double)weight.divide(entry.weight).doubleValue()));
                if(stack.isEmpty()) continue;
                list.add(stack);
            }

        }

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                int index = i*2+j;
                builder.addSlot(RecipeIngredientRole.INPUT, 41 + j * 18 - 5, 12 + i * 18 - 5).addItemStack(index < list.size() ? list.get(index) : ItemStack.EMPTY);
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 106, 38).addItemStack(recipe.getResultItem());
    }
}
