package nuparu.sevendaystomine.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

public interface ILockedRecipe extends CraftingRecipe {
    String getRecipe();
    default boolean hasRecipe() {
        return getRecipe() != null && !getRecipe().isEmpty();
    }
    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
    int getWidth();
    int getHeight();
    ItemStack getResultItem();
}
