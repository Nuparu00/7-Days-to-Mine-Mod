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

import java.util.Iterator;

public class DummyRecipe extends CustomRecipe {
    public DummyRecipe(ResourceLocation location) {
        super(location);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
      return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer p_44001_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.DUMMY_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<DummyRecipe> {

        public DummyRecipe fromJson(ResourceLocation p_199425_1_, JsonObject json) {
            return new DummyRecipe(p_199425_1_);
        }

        public DummyRecipe fromNetwork(ResourceLocation p_199426_1_, FriendlyByteBuf p_199426_2_) {

            return new DummyRecipe(p_199426_1_);
        }

        public void toNetwork(FriendlyByteBuf p_199427_1_, DummyRecipe p_199427_2_) {
        }
    }
}
