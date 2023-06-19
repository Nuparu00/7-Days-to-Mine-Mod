package nuparu.sevendaystomine.world.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.json.scrap.ScrapDataManager;
import nuparu.sevendaystomine.json.scrap.ScrapEntry;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class ScrapRecipe extends CustomRecipe {
    private ItemStack resultItem = ItemStack.EMPTY;

    public ScrapRecipe() {
        super(new ResourceLocation(SevenDaysToMine.MODID, "scrap"));
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level worldIn) {
        if (matchesAnyOtherRecipe(inv, worldIn)) {
            return false;
        }

        this.resultItem = ItemStack.EMPTY;
        EnumMaterial mat = EnumMaterial.NONE;
        Fraction weight = Fraction.ZERO;
        for (int k1 = 0; k1 < inv.getContainerSize(); ++k1) {
            ItemStack itemstack = inv.getItem(k1);

            if (!itemstack.isEmpty()) {
                Item item = itemstack.getItem();
                if (ScrapDataManager.INSTANCE.hasEntry(item)) {
                    ScrapEntry entry = ScrapDataManager.INSTANCE.getEntry(item);
                    if (!entry.canBeScrapped())
                        return false;
                    if (entry.material() != mat && mat != EnumMaterial.NONE)
                        return false;
                    weight = weight.add(entry.weight().isFraction() ? entry.weight().asFraction() : Fraction.getFraction(entry.weight().asDouble()));
                    mat = entry.material();
                } else {
                    return false;
                }
            }
        }
        if (weight.equals(Fraction.ZERO))
            return false;
        resultItem = new ItemStack(ItemUtils.getScrapResult(mat),
                (int) Math.floor(weight.doubleValue() * ServerConfig.scrapCoefficient.get()));
        return !this.resultItem.isEmpty();

    }

    public boolean matchesAnyOtherRecipe(CraftingContainer inv, Level worldIn) {
        if (Utils.getServer() == null) return false;
        Iterator<Recipe<?>> recipes = Utils.getServer().getRecipeManager().getRecipes().iterator();
        while (recipes.hasNext()) {
            Recipe<?> recipe = recipes.next();
            if (!(recipe instanceof CraftingRecipe craftingRecipe))
                continue;

            if (!(craftingRecipe instanceof ScrapRecipe)) {
                if (craftingRecipe.matches(inv, worldIn)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer p_44001_) {
        return this.resultItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return x * y >= 1;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SCRAP_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<ScrapRecipe> {

        public @NotNull ScrapRecipe fromJson(@NotNull ResourceLocation p_199425_1_, @NotNull JsonObject json) {
            return new ScrapRecipe();
        }

        public ScrapRecipe fromNetwork(@NotNull ResourceLocation p_199426_1_, @NotNull FriendlyByteBuf p_199426_2_) {

            return new ScrapRecipe();
        }

        public void toNetwork(@NotNull FriendlyByteBuf p_199427_1_, @NotNull ScrapRecipe p_199427_2_) {
        }
    }
}
