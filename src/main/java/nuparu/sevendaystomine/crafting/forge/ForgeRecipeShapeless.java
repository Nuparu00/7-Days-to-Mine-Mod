package nuparu.sevendaystomine.crafting.forge;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityForge;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ForgeRecipeShapeless implements IForgeRecipe {
    private final ResourceLocation id;
    private final String group;
    private final ItemStack result;
    private final ItemStack mold;
    private final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;
    protected final float experience;
    protected final int cookingTime;

    public ForgeRecipeShapeless(ResourceLocation resourceLocation, String group, ItemStack result, ItemStack mold, NonNullList<Ingredient> ingredients, float experience, int cookingTime) {
        this.id = resourceLocation;
        this.group = group;
        this.result = result;
        this.mold = mold;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(TileEntityForge forge, @Nonnull World world) {
        if(!ItemStack.areItemsEqualIgnoreDurability(forge.getMoldSlot(),mold)) return false;

        int ingredientCount = 0;
        net.minecraft.client.util.RecipeItemHelper recipeItemHelper = new net.minecraft.client.util.RecipeItemHelper();
        List<ItemStack> inputs = Lists.newArrayList();

        for(int j = 0; j < 4; ++j) {
            ItemStack itemstack = forge.getItem(j);
            if (!itemstack.isEmpty())
            {
                ++ingredientCount;
                inputs.add(itemstack);
            }
        }

        if (ingredientCount != this.ingredients.size())
            return false;

        return net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.ingredients) != null;
    }

    @Override
    public @Nonnull ItemStack assemble(@Nonnull TileEntityForge grillInventory) {
        return this.result.copy();
    }

    @Override
    public ResourceLocation name() {
        return null;
    }

    public ItemStack getMoldItem() {
        return this.mold.copy();
    }

    @Override
    public List<ItemStack> getJeiIngredients() {
        return null;
    }

    @Override
    public ItemStack getJeiOutput() {
        return null;
    }


    public @Nonnull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }


    @Override
    public float getExperience() {
        return this.experience;
    }

    @Override
    public int getCookingTime() {
        return this.cookingTime;
    }

    @Override
    @Nullable
    public Fraction getRatio(TileEntityForge forge){
        return Fraction.ONE;
    }
}
