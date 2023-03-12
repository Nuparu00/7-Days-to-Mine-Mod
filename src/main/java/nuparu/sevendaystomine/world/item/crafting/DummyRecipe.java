package nuparu.sevendaystomine.world.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import org.jetbrains.annotations.NotNull;

public class DummyRecipe extends CustomRecipe {
    public DummyRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer inv, @NotNull Level worldIn) {
      return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer p_44001_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.DUMMY_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<DummyRecipe> {

        public @NotNull DummyRecipe fromJson(@NotNull ResourceLocation p_199425_1_, @NotNull JsonObject json) {
            return new DummyRecipe(p_199425_1_);
        }

        public DummyRecipe fromNetwork(@NotNull ResourceLocation p_199426_1_, @NotNull FriendlyByteBuf p_199426_2_) {

            return new DummyRecipe(p_199426_1_);
        }

        public void toNetwork(@NotNull FriendlyByteBuf p_199427_1_, @NotNull DummyRecipe p_199427_2_) {
        }
    }
}
