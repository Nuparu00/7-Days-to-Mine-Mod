package com.nuparu.sevendaystomine.crafting;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.ItemQuality;
import com.nuparu.sevendaystomine.item.ItemRecipeBook;

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
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@SuppressWarnings("deprecation")
public class RecipeLockedShaped extends ShapedRecipes {

	public String recipe;
	boolean quality;

	public RecipeLockedShaped(String group, int width, int height, NonNullList<Ingredient> ingredients,
			ItemStack result, String recipe, boolean quality) {
		super(group, width, height, ingredients, result);
		this.recipe = recipe;
		this.quality = quality;
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
			if (c instanceof com.nuparu.sevendaystomine.inventory.ContainerWorkbench) {
				com.nuparu.sevendaystomine.inventory.ContainerWorkbench container = (com.nuparu.sevendaystomine.inventory.ContainerWorkbench) c;
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
		EntityPlayer player = null;
		Container c = ObfuscationReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, "field_70465_c");
		if (c instanceof com.nuparu.sevendaystomine.inventory.ContainerWorkbench) {
			com.nuparu.sevendaystomine.inventory.ContainerWorkbench container = (com.nuparu.sevendaystomine.inventory.ContainerWorkbench) c;
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
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {

		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		// WATER BUCKETS CAUSE ISSUES DUE TO RETURNING THE EMPTY BUCKET (like setting
		// stacksize to 128, for example), THERE DOES NOT SEEM TO BE ANY WAY TO DISABLE
		// THIS NATURALLY
		if (getRecipeOutput().isEmpty() || getRecipeOutput().getItem() != ModItems.CONCRETE_MIX) {
			for (int i = 0; i < nonnulllist.size(); ++i) {
				ItemStack itemstack = inv.getStackInSlot(i);

				nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
			}
		}

		return nonnulllist;
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
			if (patternJ.size() > 3)
				throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");

			String[] pattern = new String[patternJ.size()];
			for (int x = 0; x < pattern.length; ++x) {
				String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
				if (line.length() > 3)
					throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
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

			String recipe = JsonUtils.getString(json, "recipe");
			if (recipe.isEmpty())
				throw new JsonSyntaxException("Property recipe not specified");

			ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

			boolean quality = false;

			if (JsonUtils.hasField(json, "quality")) {
				quality = JsonUtils.getBoolean(json, "quality");
			}

			return new RecipeLockedShaped(group, pattern[0].length(), pattern.length, input, result, recipe, quality);
		}
	}
}
