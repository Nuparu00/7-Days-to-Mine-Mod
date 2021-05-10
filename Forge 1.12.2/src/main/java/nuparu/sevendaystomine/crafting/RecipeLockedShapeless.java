package nuparu.sevendaystomine.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.item.ItemRecipeBook;

@SuppressWarnings("deprecation")
public class RecipeLockedShapeless extends ShapelessRecipes {

	public String recipe;
	public boolean quality;

	public RecipeLockedShapeless(String group, ItemStack output, NonNullList<Ingredient> ingredients, String recipe, boolean quality) {
		super(group, output, ingredients);
		this.recipe = recipe;
		this.quality = quality;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		EntityPlayer player = null;
		Container c = ObfuscationReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, "field_70465_c");
		if (c instanceof nuparu.sevendaystomine.inventory.ContainerWorkbench) {
			nuparu.sevendaystomine.inventory.ContainerWorkbench container = (nuparu.sevendaystomine.inventory.ContainerWorkbench) c;
			player = container.player;
		} else if (c instanceof ContainerWorkbench) {
			ContainerWorkbench container = (ContainerWorkbench) (c);
			SlotCrafting slot = (SlotCrafting) container.getSlot(0);
			player = (EntityPlayer) (ObfuscationReflectionHelper.getPrivateValue(SlotCrafting.class, slot,
					"field_75238_b"));
		} else if (c instanceof ContainerPlayer) {
			ContainerPlayer container = (ContainerPlayer) (c);
			player = (EntityPlayer) (ObfuscationReflectionHelper.getPrivateValue(ContainerPlayer.class, container,
					"field_82862_h"));

		}
		if (player == null)
			return false;

		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		return (!ModConfig.players.recipeBooksRequired || iep.hasRecipe(recipe)) && super.matches(inv, worldIn);

	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack stack = super.getCraftingResult(inv);
		if (stack != null && !stack.isEmpty()) {
			if (stack.getItem() instanceof ItemRecipeBook
					&& ((ItemRecipeBook) stack.getItem()).getRecipe().equals(recipe)) {
				((ItemRecipeBook) stack.getItem()).setRead(stack, true);
			}

			if (!quality)
				return stack;

			Container c = ObfuscationReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, "field_70465_c");

			EntityPlayer player = null;
			if (c instanceof nuparu.sevendaystomine.inventory.ContainerWorkbench) {
				nuparu.sevendaystomine.inventory.ContainerWorkbench container = (nuparu.sevendaystomine.inventory.ContainerWorkbench) c;
				player = container.player;
			} else if (c instanceof ContainerWorkbench) {
				ContainerWorkbench container = (ContainerWorkbench) (c);
				SlotCrafting slot = (SlotCrafting) container.getSlot(0);
				player = (EntityPlayer) (ObfuscationReflectionHelper.getPrivateValue(SlotCrafting.class, slot,
						"field_75238_b"));
			} else if (c instanceof ContainerPlayer) {
				ContainerPlayer container = (ContainerPlayer) (c);
				player = (EntityPlayer) (ObfuscationReflectionHelper.getPrivateValue(ContainerPlayer.class, container,
						"field_82862_h"));
			}
			if (player != null) {
				if (stack.getTagCompound() == null) {
					stack.setTagCompound(new NBTTagCompound());
				}
				stack.getTagCompound().setInteger("Quality", (int) Math
						.min(Math.max(Math.floor(player.experienceTotal / ModConfig.players.xpPerQuality), 1), 600));
			}
		}
		return stack;
	}

	public static class Factory implements IRecipeFactory {
		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			String group = JsonUtils.getString(json, "group", "");

			NonNullList<Ingredient> ings = NonNullList.create();
			for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients"))
				ings.add(CraftingHelper.getIngredient(ele, context));

			if (ings.isEmpty())
				throw new JsonParseException("No ingredients for shapeless recipe");
			if (ings.size() > 9)
				throw new JsonParseException("Too many ingredients for shapeless recipe");

			String recipe = JsonUtils.getString(json, "recipe");
			if (recipe.isEmpty())
				throw new JsonSyntaxException("Property recipe not specified");

			ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
			
			
			boolean quality = false;

			if (JsonUtils.hasField(json, "quality")) {
				quality = JsonUtils.getBoolean(json, "quality");
			}
			
			return new RecipeLockedShapeless(group, result, ings, recipe, quality);
		}
	}
}
