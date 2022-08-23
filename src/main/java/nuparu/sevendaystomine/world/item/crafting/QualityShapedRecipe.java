package nuparu.sevendaystomine.world.item.crafting;

import com.google.gson.*;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

import javax.annotation.Nullable;
import java.util.Map;

@SuppressWarnings("deprecation")
public class QualityShapedRecipe extends ShapedRecipe implements ILockedRecipe{

	String recipe;
	boolean quality;
	public QualityShapedRecipe(ResourceLocation p_i48162_1_, String p_i48162_2_, int p_i48162_3_, int p_i48162_4_,
							   NonNullList<Ingredient> p_i48162_5_, ItemStack p_i48162_6_, String recipe, boolean quality) {
		super(p_i48162_1_, p_i48162_2_, p_i48162_3_, p_i48162_4_, p_i48162_5_, p_i48162_6_);
		this.recipe = recipe;
		this.quality = quality;
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		ItemStack stack = super.assemble(inv);
		if (!quality)
			return stack;

		Player player = getPlayerFromContainer(inv);
        if (player != null) {
			((IQualityStack)(Object)stack).setQuality((int) Math
					.min(Math.max(Math.floor(player.totalExperience / ServerConfig.xpPerQuality.get()), 1), QualityManager.maxLevel));
        }
        return stack;

	}


	@Override
	public boolean matches(CraftingContainer container, Level level) {
		if(!hasRecipe()) return super.matches(container, level);
		Player player = getPlayerFromContainer(container);

		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		return (!ServerConfig.recipeBooksRequired.get() || player == null || (iep != null && iep.hasRecipe(recipe))) && super.matches(container, level);
	}

	@Nullable
	public Player getPlayerFromContainer(CraftingContainer craftingContainer){
		AbstractContainerMenu container = craftingContainer.menu;
		if(container instanceof CraftingMenu){
			CraftingMenu craftingMenu = (CraftingMenu)container;
			return craftingMenu.player;
		}
		else if (container instanceof InventoryMenu){
			InventoryMenu inventoryMenu = (InventoryMenu) container;
			return inventoryMenu.owner;
		}
		return null;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.QUALITY_SHAPED_SERIALIZER.get();
	}

	@Override
	public String getRecipe() {
		return this.recipe;
	}

	public static class Serializer implements RecipeSerializer<QualityShapedRecipe> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "quality_shaped");
		public QualityShapedRecipe fromJson(ResourceLocation p_44236_, JsonObject p_44237_) {
			ShapedRecipe.setCraftingSize(5,5);
			String s = GsonHelper.getAsString(p_44237_, "group", "");
			Map<String, Ingredient> map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(p_44237_, "key"));
			String[] astring = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(p_44237_, "pattern")));
			int i = astring[0].length();
			int j = astring.length;
			NonNullList<Ingredient> nonnulllist = ShapedRecipe.dissolvePattern(astring, map, i, j);
			ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44237_, "result"));
			String recipe = GsonHelper.getAsString(p_44237_, "recipe", "");
			boolean quality = GsonHelper.getAsBoolean(p_44237_, "quality", false);

			return new QualityShapedRecipe(p_44236_, s, i, j, nonnulllist, itemstack,recipe,quality);
		}

		public QualityShapedRecipe fromNetwork(ResourceLocation p_44239_, FriendlyByteBuf p_44240_) {
			ShapedRecipe.setCraftingSize(5,5);
			int i = p_44240_.readVarInt();
			int j = p_44240_.readVarInt();
			String s = p_44240_.readUtf();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

			for(int k = 0; k < nonnulllist.size(); ++k) {
				nonnulllist.set(k, Ingredient.fromNetwork(p_44240_));
			}

			ItemStack itemstack = p_44240_.readItem();
			String recipe = p_44240_.readUtf();
			boolean quality = p_44240_.readBoolean();
			return new QualityShapedRecipe(p_44239_, s, i, j, nonnulllist, itemstack,recipe,quality);
		}

		public void toNetwork(FriendlyByteBuf p_44227_, QualityShapedRecipe p_44228_) {
			p_44227_.writeVarInt(p_44228_.getWidth());
			p_44227_.writeVarInt(p_44228_.getHeight());
			p_44227_.writeUtf(p_44228_.getGroup());

			for(Ingredient ingredient : p_44228_.getIngredients()) {
				ingredient.toNetwork(p_44227_);
			}

			p_44227_.writeItem(p_44228_.getResultItem());
			p_44227_.writeUtf(p_44228_.recipe);
			p_44227_.writeBoolean(p_44228_.quality);
		}
	}

}
