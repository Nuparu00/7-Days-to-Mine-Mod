package nuparu.sevendaystomine.crafting.workbench;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import net.minecraft.item.crafting.ShapedRecipes;
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

public class RecipeWorkbenchShaped extends ShapedRecipes {

	public static final List<RecipeWorkbenchShaped> RECIPES = new ArrayList<RecipeWorkbenchShaped>();
	
	String recipe;
	boolean quality;

	public RecipeWorkbenchShaped(String group, int width, int height, NonNullList<Ingredient> ingredients,
			ItemStack result, String recipe, boolean quality) {
		super(group, width, height, ingredients, result);
		this.recipe = recipe;
		this.quality = quality;
		RECIPES.add(this);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack stack = super.getCraftingResult(inv);
		if (stack != null && !stack.isEmpty() && inv != null) {
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

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		if (recipe == null || recipe.isEmpty())
			return super.matches(inv, worldIn);
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

	public static class Factory implements IRecipeFactory {
		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			String group = JsonUtils.getString(json, "group", "");
			// if (!group.isEmpty() && group.indexOf(':') == -1)
			// group = context.getModId() + ":" + group;

			Map<Character, Ingredient> ingMap = Maps.newHashMap();
			for (Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet()) {
				if (entry.getKey().length() != 1)
					throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey()
							+ "' is an invalid symbol (must be 1 character only).");
				if (" ".equals(entry.getKey()))
					throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

				ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
			}
			ingMap.put(' ', Ingredient.EMPTY);

			JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

			if (patternJ.size() == 0)
				throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
			if (patternJ.size() > 5)
				throw new JsonSyntaxException("Invalid pattern: too many rows, 5 is maximum");

			String[] pattern = new String[patternJ.size()];
			for (int x = 0; x < pattern.length; ++x) {
				String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
				if (line.length() > 5)
					throw new JsonSyntaxException("Invalid pattern: too many columns, 5 is maximum");
				if (x > 0 && pattern[0].length() != line.length())
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				pattern[x] = line;
			}

			NonNullList<Ingredient> input = NonNullList.withSize(pattern[0].length() * pattern.length,
					Ingredient.EMPTY);
			Set<Character> keys = Sets.newHashSet(ingMap.keySet());
			keys.remove(' ');

			int x = 0;
			for (String line : pattern) {
				for (char chr : line.toCharArray()) {
					Ingredient ing = ingMap.get(chr);
					if (ing == null)
						throw new JsonSyntaxException(
								"Pattern references symbol '" + chr + "' but it's not defined in the key");
					input.set(x++, ing);
					keys.remove(chr);
				}
			}

			if (!keys.isEmpty())
				throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);

			String recipe = "";
			if (JsonUtils.hasField(json, "recipe")) {
				recipe = JsonUtils.getString(json, "recipe");
			}
			boolean quality = false;

			if (JsonUtils.hasField(json, "quality")) {
				quality = JsonUtils.getBoolean(json, "quality");
			}

			ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
			return new RecipeWorkbenchShaped(group, pattern[0].length(), pattern.length, input, result, recipe,
					quality);
		}
	}
}
