package nuparu.sevendaystomine.world.item.crafting.cooking;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.init.ModRecipeTypes;
import nuparu.sevendaystomine.world.level.block.entity.GrillBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CookingShapeless implements ICookingRecipe<GrillBlockEntity> {
    private final ResourceLocation id;
    private final String group;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;
    protected final float experience;
    protected final int cookingTime;

    private final ResourceLocation station;

    public CookingShapeless(ResourceLocation resourceLocation, String group, ItemStack result, NonNullList<Ingredient> ingredients, float experience, int cookingTime, ResourceLocation station) {
        this.id = resourceLocation;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.experience = experience;
        this.cookingTime = cookingTime;
        this.station = station;
    }

    @Override
    public boolean matches(GrillBlockEntity grillInventory, @NotNull Level world) {
        if (!ForgeRegistries.BLOCKS.getKey(grillInventory.getBlockState().getBlock()).equals(getRequiredStation())) return false;
        StackedContents recipeitemhelper = new StackedContents();
        List<ItemStack> inputs = new ArrayList();
        int i = 0;

        for (int j = 0; j < grillInventory.getContainerSize(); ++j) {
            ItemStack itemstack = grillInventory.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (this.isSimple) {
                    recipeitemhelper.accountStack(itemstack, 1);
                } else {
                    inputs.add(itemstack);
                }
            }
        }

        label43:
        {
            if (i == this.ingredients.size()) {
                if (this.isSimple) {
                    if (recipeitemhelper.canCraft(this, null)) {
                        break label43;
                    }
                } else if (RecipeMatcher.findMatches(inputs, this.ingredients) != null) {
                    break label43;
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull GrillBlockEntity grillInventory) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return i * i1 >= this.ingredients.size();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull String getGroup() {
        return this.group;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return this.result.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FORGE_SHAPELESS.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.COOKING_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getRequiredStation() {
        return station;
    }

    @Override
    public float getExperience() {
        return this.experience;
    }

    @Override
    public int getCookingTime() {
        return this.cookingTime;
    }

    public static class Factory implements RecipeSerializer<CookingShapeless> {
        final int defaultCookingTime = 600;

        @Override
        public @NotNull CookingShapeless fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            String s = GsonHelper.getAsString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 2 * 2) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is " + 2 * 2);
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
                int cookingTime = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);
                ResourceLocation station = new ResourceLocation(GsonHelper.getAsString(json, "station", "sevendaystomine:cooking_grill"));
                return new CookingShapeless(recipeId, s, itemstack, nonnulllist, experience, cookingTime, station);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_199568_0_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < p_199568_0_.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(p_199568_0_.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Nullable
        @Override
        public CookingShapeless fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String s = buffer.readUtf(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            nonnulllist.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack itemstack = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();
            ResourceLocation station = new ResourceLocation(buffer.readUtf());
            return new CookingShapeless(recipeId, s, itemstack, nonnulllist, experience, cookingTime, station);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CookingShapeless recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());
            Iterator var3 = recipe.ingredients.iterator();

            while (var3.hasNext()) {
                Ingredient ingredient = (Ingredient) var3.next();
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
            buffer.writeUtf(recipe.station.toString());
        }
    }
}
