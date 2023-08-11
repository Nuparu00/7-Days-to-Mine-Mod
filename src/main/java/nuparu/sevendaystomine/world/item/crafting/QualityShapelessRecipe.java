package nuparu.sevendaystomine.world.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
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
import java.lang.reflect.Field;

public class QualityShapelessRecipe extends ShapelessRecipe implements ILockedRecipe{

	@SuppressWarnings("CanBeFinal")
	String recipe;
	@SuppressWarnings("CanBeFinal")
	boolean quality;

	public QualityShapelessRecipe(ResourceLocation p_i48161_1_, String p_i48161_2_, CraftingBookCategory craftingBookCategory, ItemStack p_i48161_3_,
								  NonNullList<Ingredient> p_i48161_4_, String recipe, boolean quality) {
		super(p_i48161_1_, p_i48161_2_, craftingBookCategory, p_i48161_3_, p_i48161_4_);
		this.recipe = recipe;
		this.quality = quality;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull CraftingContainer inv, RegistryAccess registryAccess) {
		ItemStack stack = super.assemble(inv, registryAccess);
		if (!quality)
			return stack;

		try {
			Player player = getPlayerFromContainer(inv);
			if (player != null) {
				((IQualityStack)(Object)stack).setQuality((int) Math
						.min(Math.max(Math.floor(player.totalExperience / ServerConfig.xpPerQuality.get()), 1), QualityManager.getMaxLevel()));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return stack;
		}
        return stack;

	}

	@Override
	public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
		if(!hasRecipe()) return super.matches(container, level);
		try {
			Player player = getPlayerFromContainer(container);
			if(player == null) return false;
			IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
			return (!ServerConfig.recipeBooksRequired.get() || (iep != null && iep.hasRecipe(recipe))) && super.matches(container, level);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Nullable
	public Player getPlayerFromContainer(CraftingContainer craftingContainer) throws IllegalAccessException {
		Class<?> clazz = craftingContainer.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (AbstractContainerMenu.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				AbstractContainerMenu container = (AbstractContainerMenu) field.get(craftingContainer);
				if(container instanceof CraftingMenu craftingMenu){
					return craftingMenu.player;
				}
				else if (container instanceof InventoryMenu inventoryMenu){
					return inventoryMenu.owner;
				}
				else if (container instanceof ContainerWorkbench workbench) {
					return workbench.player;
				}
			}
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
			CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", (String)null), CraftingBookCategory.MISC);
			String s = GsonHelper.getAsString(jsonObject, "group", "");
			NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
			if (nonnulllist.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else {
				ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
				String recipe = GsonHelper.getAsString(jsonObject, "recipe", "");
				boolean quality = GsonHelper.getAsBoolean(jsonObject, "quality", false);
				return new QualityShapelessRecipe(location, s,craftingbookcategory, itemstack, nonnulllist, recipe, quality);
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
			CraftingBookCategory craftingbookcategory = p_44240_.readEnum(CraftingBookCategory.class);
			int i = p_44240_.readVarInt();
			NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

			for(int j = 0; j < nonnulllist.size(); ++j) {
				nonnulllist.set(j, Ingredient.fromNetwork(p_44240_));
			}

			ItemStack itemstack = p_44240_.readItem();
			String recipe = p_44240_.readUtf();
			boolean quality = p_44240_.readBoolean();
			return new QualityShapelessRecipe(p_44239_, s,craftingbookcategory, itemstack, nonnulllist,recipe,quality);
		}

		public void toNetwork(FriendlyByteBuf p_44227_, QualityShapelessRecipe p_44228_) {
			p_44227_.writeUtf(p_44228_.getGroup());
			p_44227_.writeEnum(p_44228_.category());
			p_44227_.writeVarInt(p_44228_.getIngredients().size());

			for(Ingredient ingredient : p_44228_.getIngredients()) {
				ingredient.toNetwork(p_44227_);
			}
			p_44227_.writeItem(p_44228_.result);
			p_44227_.writeUtf(p_44228_.recipe);
			p_44227_.writeBoolean(p_44228_.quality);
		}
	}

}
