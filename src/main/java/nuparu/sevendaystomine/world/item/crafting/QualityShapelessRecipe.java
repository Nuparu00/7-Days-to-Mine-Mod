package nuparu.sevendaystomine.world.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModRecipeSerializers;
import nuparu.sevendaystomine.world.inventory.block.ContainerWorkbench;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class QualityShapelessRecipe extends ShapelessRecipe implements ILockedRecipe{

	@SuppressWarnings("CanBeFinal")
	String recipe;
	@SuppressWarnings("CanBeFinal")
	boolean quality;

	public QualityShapelessRecipe(ResourceLocation p_i48161_1_, String p_i48161_2_, ItemStack p_i48161_3_,
								 NonNullList<Ingredient> p_i48161_4_, String recipe, boolean quality) {
		super(p_i48161_1_, p_i48161_2_, p_i48161_3_, p_i48161_4_);
		this.recipe = recipe;
		this.quality = quality;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull CraftingContainer inv) {
		ItemStack stack = super.assemble(inv);
		if (!quality)
			return stack;

		Player player = getPlayerFromContainer(inv);
        if (player != null) {
			((IQualityStack)(Object)stack).setQuality((int) Math
					.min(Math.max(Math.floor(player.totalExperience / ServerConfig.xpPerQuality.get()), 1), QualityManager.getMaxLevel()));
        }
        return stack;

	}

	@Override
	public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
		if(!hasRecipe()) return super.matches(container, level);
		Player player = getPlayerFromContainer(container);

		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		return (!ServerConfig.recipeBooksRequired.get() || player == null || (iep != null && iep.hasRecipe(recipe))) && super.matches(container, level);
	}

	@Nullable
	public Player getPlayerFromContainer(CraftingContainer craftingContainer){
		AbstractContainerMenu container = craftingContainer.menu;
		if(container instanceof CraftingMenu craftingMenu){
			return craftingMenu.player;
		}
		else if (container instanceof InventoryMenu inventoryMenu){
			return inventoryMenu.owner;
		}
		else if (container instanceof ContainerWorkbench workbench) {
			return workbench.player;
		}
		return null;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.QUALITY_SHAPELESS_SERIALIZER.get();
	}

	@Override
	public String getRecipe() {
		return this.recipe;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	public static class Serializer implements RecipeSerializer<QualityShapelessRecipe> {
		private static final ResourceLocation NAME = new ResourceLocation(SevenDaysToMine.MODID, "quality_shapeless");
		public @NotNull QualityShapelessRecipe fromJson(@NotNull ResourceLocation location, @NotNull JsonObject jsonObject) {
			ShapedRecipe.setCraftingSize(5,5);
			String s = GsonHelper.getAsString(jsonObject, "group", "");
			NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else {
				ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
				String recipe = GsonHelper.getAsString(jsonObject, "recipe", "");
				boolean quality = GsonHelper.getAsBoolean(jsonObject, "quality", false);
				return new QualityShapelessRecipe(location, s, itemstack, nonnulllist, recipe, quality);
			}
		}

		private static NonNullList<Ingredient> itemsFromJson(JsonArray p_44276_) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();

			for(int i = 0; i < p_44276_.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(p_44276_.get(i));
				if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
					nonnulllist.add(ingredient);
				}
			}

			return nonnulllist;
		}

		public QualityShapelessRecipe fromNetwork(@NotNull ResourceLocation p_44239_, FriendlyByteBuf p_44240_) {
			ShapedRecipe.setCraftingSize(5,5);
			String s = p_44240_.readUtf();
			int i = p_44240_.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for(int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.fromNetwork(p_44240_));
			}

			ItemStack itemstack = p_44240_.readItem();
			String recipe = p_44240_.readUtf();
			boolean quality = p_44240_.readBoolean();
			return new QualityShapelessRecipe(p_44239_, s, itemstack, nonnulllist,recipe,quality);
		}

		public void toNetwork(FriendlyByteBuf p_44227_, QualityShapelessRecipe p_44228_) {
			p_44227_.writeUtf(p_44228_.getGroup());
			p_44227_.writeVarInt(p_44228_.getIngredients().size());

			for(Ingredient ingredient : p_44228_.getIngredients()) {
				ingredient.toNetwork(p_44227_);
			}

			p_44227_.writeUtf(p_44228_.recipe);
			p_44227_.writeBoolean(p_44228_.quality);
		}
	}

}
