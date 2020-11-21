package com.nuparu.sevendaystomine.crafting;

import java.util.Iterator;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;
import com.nuparu.sevendaystomine.util.ItemUtils;
import com.nuparu.sevendaystomine.util.VanillaManager;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class RecipesScraps extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private ItemStack resultItem = ItemStack.EMPTY;

	protected String resourceDomain;
	protected ResourceLocation resourceLocation;

	public RecipesScraps() {
		this(null);
	}

	public RecipesScraps(String resourceDomain) {
		this.resourceDomain = resourceDomain != null ? resourceDomain.toLowerCase() : resourceDomain;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	public boolean matches(InventoryCrafting inv, World worldIn) {
		if (matchesAnyOtherRecipe(inv, worldIn)) {
			return false;
		}

		this.resultItem = ItemStack.EMPTY;
		EnumMaterial mat = EnumMaterial.NONE;
		int weight = 0;
		for (int k1 = 0; k1 < inv.getSizeInventory(); ++k1) {
			ItemStack itemstack = inv.getStackInSlot(k1);

			if (!itemstack.isEmpty()) {
				Item item = itemstack.getItem();
				if (item instanceof IScrapable) {

					IScrapable scrap = (IScrapable) item;

					if (!scrap.canBeScraped())
						return false;
					if (scrap.getMaterial() != mat && mat != EnumMaterial.NONE)
						return false;
					weight += scrap.getWeight();
					mat = scrap.getMaterial();
				} else if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() != null
						&& ((ItemBlock) item).getBlock() instanceof IScrapable) {

					IScrapable scrap = (IScrapable) ((ItemBlock) item).getBlock();

					if (!scrap.canBeScraped())
						return false;
					if (scrap.getMaterial() != mat && mat != EnumMaterial.NONE)
						return false;
					weight += scrap.getWeight();
					mat = scrap.getMaterial();
				} else if (VanillaManager.getVanillaScrapable(item) != null) {
					VanillaManager.VanillaScrapableItem scrapable = VanillaManager.getVanillaScrapable(item);
					if (!scrapable.canBeScraped())
						return false;
					if (scrapable.getMaterial() != mat && mat != EnumMaterial.NONE)
						return false;
					weight += scrapable.getWeight();
					mat = scrapable.getMaterial();
				} else {
					return false;
				}
			}
		}
		if (weight == 0)
			return false;
		resultItem = new ItemStack(ItemUtils.INSTANCE.getScrapResult(mat), (int) Math.ceil(weight / 2f));
		if (!this.resultItem.isEmpty()) {

			return true;
		}
		return false;

	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Nullable
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return this.resultItem.copy();
	}

	/**
	 * Returns the size of the recipe area
	 */
	public int getRecipeSize() {
		return 10;
	}

	@Nullable
	public ItemStack getRecipeOutput() {
		return this.resultItem;
	}

	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for (int i = 0; i < nonnulllist.size(); ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			nonnulllist.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
		}
		return nonnulllist;
	}

	public boolean matchesAnyOtherRecipe(InventoryCrafting inv, World worldIn) {
		Iterator<IRecipe> recipes = CraftingManager.REGISTRY.iterator();
		while (recipes.hasNext()) {
			IRecipe recipe = recipes.next();
			if (!(recipe instanceof RecipesScraps)) {
				if (recipe.matches(inv, worldIn)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height > 1;
	}

	public static class Factory implements IRecipeFactory {
		@Override
		public IRecipe parse(JsonContext context, JsonObject json) {
			String domain = null;
			if (JsonUtils.hasField(json, "domain")) {
				domain = JsonUtils.getString(json, "domain", "");
			}
			return new RecipesScraps(domain);
		}
	}
}
