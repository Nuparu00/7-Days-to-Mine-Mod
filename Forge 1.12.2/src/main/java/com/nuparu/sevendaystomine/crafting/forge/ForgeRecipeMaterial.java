package com.nuparu.sevendaystomine.crafting.forge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.nuparu.sevendaystomine.crafting.ItemStackWrapper;
import com.nuparu.sevendaystomine.item.EnumMaterial;
import com.nuparu.sevendaystomine.item.IScrapable;
import com.nuparu.sevendaystomine.tileentity.TileEntityForge;
import com.nuparu.sevendaystomine.util.ItemUtils;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.VanillaManager;
import com.nuparu.sevendaystomine.util.VanillaManager.VanillaScrapableItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ForgeRecipeMaterial implements IForgeRecipe {

	private ItemStack result;
	private ItemStack mold;
	HashMap<EnumMaterial, Integer> ingredients;

	private int xp = 5;

	public ForgeRecipeMaterial(ItemStack result, ItemStack mold, HashMap<EnumMaterial, Integer> ingredients) {
		this(result, mold, ingredients, 5);
	}

	public ForgeRecipeMaterial(ItemStack result, ItemStack mold, HashMap<EnumMaterial, Integer> ingredients, int xp) {
		this.result = result;
		this.mold = mold;
		this.ingredients = ingredients;
		this.xp = xp;
		if (ingredients.size() > 4) {
			throw new IllegalArgumentException(
					"Number of ingredients of a forge recipe for ItemStack " + result.getItem().getUnlocalizedName()
							+ " must be less or equal to 4! Report this to the mod author.");

		}
	}

	@Override
	public ForgeResult matches(TileEntityForge inv, World worldIn) {
		HashMap<EnumMaterial, Integer> invMats = getMaterials(inv);
		if (invMats == null)
			return new ForgeResult(false, null);
		List<MaterialStackWrapper> listInv = MaterialStackWrapper.wrapList(invMats, false);
		List<MaterialStackWrapper> listIng = MaterialStackWrapper.wrapList(ingredients, false);
		if (listInv.size() != listIng.size())
			return new ForgeResult(false, null);

		Iterator<MaterialStackWrapper> itInv = listInv.iterator();
		Iterator<MaterialStackWrapper> itIng = listIng.iterator();

		HashMap<EnumMaterial, Integer> items = new HashMap<EnumMaterial, Integer>();

		double lastRatio = -1;
		while (itInv.hasNext()) {
			MaterialStackWrapper invWrapper = itInv.next();
			while (itIng.hasNext()) {
				MaterialStackWrapper ingWrapper = itIng.next();
				if (invWrapper.equals(ingWrapper)) {
					if (ingWrapper.getWeight() > invWrapper.getWeight()) {
						return new ForgeResult(false, null);
					} /*
						 * weightsInv.put(invWrapper.mat,invWrapper.weight);
						 * weightsIng.put(ingWrapper.mat,ingWrapper.weight);
						 */

					double ratio = (double) invWrapper.weight / (double) ingWrapper.weight;
					if (lastRatio != -1 && lastRatio != ratio) {
						//System.out.println("FdFF " + lastRatio + " " + ratio);
						return new ForgeResult(false, null);
					}
					items.put(invWrapper.mat, (int) Math.ceil(ingWrapper.weight * ratio));
					lastRatio = ratio;
					itIng.remove();
					itInv.remove();
					break;
				}
			}
		}
		if (listInv.size() != 0 || listIng.size() != 0) {
			return new ForgeResult(false, null);
		}
		ForgeResult result = new ForgeResult(true, items, (int) Math.ceil(lastRatio));
		result.simplify(this);
		return result;
	}

	public HashMap<EnumMaterial, Integer> getMaterials(TileEntityForge inv) {
		HashMap<EnumMaterial, Integer> map = new HashMap<EnumMaterial, Integer>();
		List<ItemStack> items = inv.getActiveInventory();
		for (ItemStack itemStack : items) {
			if (itemStack == null | itemStack.isEmpty())
				continue;
			Item item = itemStack.getItem();
			if (item instanceof IScrapable) {
				IScrapable scrapable = (IScrapable) item;
				EnumMaterial mat = scrapable.getMaterial();
				int weight = scrapable.getWeight() * itemStack.getCount();
				for (Map.Entry<EnumMaterial, Integer> entry : map.entrySet()) {
					EnumMaterial enumMat = entry.getKey();
					if (enumMat == mat) {
						weight += entry.getValue();
					}
				}
				map.put(mat, weight);
			} else if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() != null
					&& ((ItemBlock) item).getBlock() instanceof IScrapable) {

				IScrapable scrapable = (IScrapable) ((ItemBlock) item).getBlock();
				EnumMaterial mat = scrapable.getMaterial();
				int weight = scrapable.getWeight() * itemStack.getCount();
				for (Map.Entry<EnumMaterial, Integer> entry : map.entrySet()) {
					EnumMaterial enumMat = entry.getKey();
					if (enumMat == mat) {
						weight += entry.getValue();
					}
				}
				map.put(mat, weight);
			} else if (VanillaManager.getVanillaScrapable(item) != null) {
				VanillaManager.VanillaScrapableItem scrapable = VanillaManager.getVanillaScrapable(item);
				EnumMaterial mat = scrapable.getMaterial();
				int weight = scrapable.getWeight() * itemStack.getCount();
				for (Map.Entry<EnumMaterial, Integer> entry : map.entrySet()) {
					EnumMaterial enumMat = entry.getKey();
					if (enumMat == mat) {
						weight += entry.getValue();
					}
				}
				map.put(mat, weight);
			} else {
				return null;
			}
		}
		return map;
	}

	@Override
	public ItemStack getResult() {
		return result.copy();
	}

	@Override
	public ItemStack getMold() {
		return mold;
	}

	@Override
	public ItemStack getOutput(TileEntityForge tileEntity) {
		ItemStack result = getResult();
		result.setCount(result.getCount() * getOutputMultiplier(tileEntity));
		return result;
	}

	@Override
	public List<ItemStack> getIngredients() {
		List<ItemStack> ingredients = new ArrayList<ItemStack>();

		List<MaterialStackWrapper> listIng = MaterialStackWrapper.wrapList(this.ingredients, false);
		Iterator<MaterialStackWrapper> itIng = listIng.iterator();

		HashMap<Item, Integer> weights = new HashMap<Item, Integer>();
		float biggestRatio = -1;
		while (itIng.hasNext()) {
			MaterialStackWrapper ingWrapper = itIng.next();
			EnumMaterial mat = ingWrapper.mat;
			int weight = ingWrapper.weight;
			Item scrap = ItemUtils.INSTANCE.getSmallestBit(mat);

			if (scrap == null)
				continue;
			int scrapWeight = 1;
			if (scrap instanceof IScrapable) {
				scrapWeight = ((IScrapable) scrap).getWeight();
			} else {
				VanillaScrapableItem vsi = VanillaManager.getVanillaScrapable(scrap);
				if (vsi != null) {
					scrapWeight = vsi.getWeight();
				}
			}
			float ratio = (float) weight / (float) scrapWeight;
			weights.put(scrap, weight);
			ingredients.add(new ItemStack(scrap, (int) weight));
		}

		return ingredients;
	}

	@Override
	public int intGetXP(EntityPlayer player) {
		return xp;
	}

	public int getOutputMultiplier(TileEntityForge tileEntity) {
		if (tileEntity == null || tileEntity.getCurrentResult() == null)
			return 1;
		return tileEntity.getCurrentResult().outputAmount;
	}

	@Override
	public List<ItemStack> consumeInput(TileEntityForge tileEntity) {
		List<ItemStack> leftovers = new ArrayList<ItemStack>();
		if (tileEntity.getCurrentResult() == null || tileEntity.getCurrentResult().usedItems == null)
			return leftovers;

		List<ItemStack> inventory = tileEntity.getActiveInventory();

		Iterator<Entry<EnumMaterial, Integer>> it = tileEntity.getCurrentResult().usedItems.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<EnumMaterial, Integer> pair = it.next();
			EnumMaterial mat = pair.getKey();
			int weight = pair.getValue();

			for (ItemStack stack : inventory) {
				if (weight <= 0)
					break;

				if (stack.isEmpty())
					continue;
				Item item = stack.getItem();

				IScrapable scrapable = null;
				if (item instanceof IScrapable) {
					scrapable = (IScrapable) item;
				} else if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() != null
						&& ((ItemBlock) item).getBlock() instanceof IScrapable) {
					scrapable = (IScrapable) ((ItemBlock) item).getBlock();
				}

				if (scrapable != null) {
					if (scrapable.getMaterial() != mat) continue; 
					int count = Math.min(stack.getCount(),(int) Math.ceil((double)weight/scrapable.getWeight()));

						stack.shrink(count);
						weight-=count*scrapable.getWeight();
					
				} else if (VanillaManager.getVanillaScrapable(item) != null) {
					VanillaManager.VanillaScrapableItem vanllaScrapable = VanillaManager.getVanillaScrapable(item);
					if(vanllaScrapable.getMaterial() != mat) continue;
					int count = Math.min(stack.getCount(),(int) Math.ceil((double)weight/vanllaScrapable.getWeight()));

					stack.shrink(count);
					weight-=count*vanllaScrapable.getWeight();
				}

			}
			if(weight < 0) {
				Item scrap = ItemUtils.INSTANCE.getScrapResult(mat);
				if(scrap != null) {
					leftovers.add(new ItemStack(scrap,-weight));
				}
			}

		}

		return leftovers;
	}

}
