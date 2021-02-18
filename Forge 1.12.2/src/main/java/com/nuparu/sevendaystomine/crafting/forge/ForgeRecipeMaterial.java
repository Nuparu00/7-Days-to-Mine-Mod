package com.nuparu.sevendaystomine.crafting.forge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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
	private HashMap<EnumMaterial, Integer> ingredients;

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
	public boolean matches(TileEntityForge inv, World worldIn) {
		HashMap<EnumMaterial, Integer> invMats = getMaterials(inv);
		if (invMats == null)
			return false;
		List<MaterialStackWrapper> listInv = MaterialStackWrapper.wrapList(invMats, false);
		List<MaterialStackWrapper> listIng = MaterialStackWrapper.wrapList(ingredients, false);
		if (listInv.size() != listIng.size())
			return false;

		Iterator<MaterialStackWrapper> itInv = listInv.iterator();
		Iterator<MaterialStackWrapper> itIng = listIng.iterator();

		HashMap<EnumMaterial, Integer> weightsInv = new HashMap<EnumMaterial, Integer>();
		HashMap<EnumMaterial, Integer> weightsIng = new HashMap<EnumMaterial, Integer>();

		double lastRatio = -1;
		while (itInv.hasNext()) {
			MaterialStackWrapper invWrapper = itInv.next();
			while (itIng.hasNext()) {
				MaterialStackWrapper ingWrapper = itIng.next();
				if (invWrapper.equals(ingWrapper)) {
					if (ingWrapper.getWeight() > invWrapper.getWeight()) {
						return false;
					} /*
						 * weightsInv.put(invWrapper.mat,invWrapper.weight);
						 * weightsIng.put(ingWrapper.mat,ingWrapper.weight);
						 */

					double ratio = (double) invWrapper.weight / (double) ingWrapper.weight;
					if (lastRatio != -1 && lastRatio != ratio)
						return false;
					lastRatio = ratio;
					itIng.remove();
					itInv.remove();
					break;
				}
			}
		}
		if (listInv.size() != 0 || listIng.size() != 0) {
			return false;
		}

		return true;
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
			ingredients.add(new ItemStack(scrap, (int)weight));	
		}

		return ingredients;
	}

	@Override
	public int intGetXP(EntityPlayer player) {
		return xp;
	}

	public int getOutputMultiplier(TileEntityForge tileEntity) {
		List<MaterialStackWrapper> listIng = MaterialStackWrapper.wrapList(ingredients, false);
		List<ItemStack> listInv = tileEntity.getActiveInventory();
		Iterator<MaterialStackWrapper> itIng = listIng.iterator();
		Iterator<ItemStack> itInv = listInv.iterator();

		int multiplier = 0;

		while (itIng.hasNext()) {
			MaterialStackWrapper ingWrapper = itIng.next();
			EnumMaterial enumMat = ingWrapper.getMaterial();
			int weightLeft = ingWrapper.getWeight();
			int totalWeight = 0;

			while (itInv.hasNext() && weightLeft > 0) {
				ItemStack stack = itInv.next();
				if (stack == null || stack.isEmpty())
					continue;
				Item item = stack.getItem();
				if (item instanceof IScrapable) {
					IScrapable scrapable = (IScrapable) item;
					if (enumMat != scrapable.getMaterial())
						continue;

					int fakeStackSize = stack.getCount();
					while (fakeStackSize > 0 && weightLeft > 0) {
						--fakeStackSize;
						weightLeft -= scrapable.getWeight();
						if (fakeStackSize <= 0) {
							break;
						}
					}
					totalWeight += scrapable.getWeight() * (stack.getCount() - fakeStackSize);
					if (weightLeft <= 0) {
						break;
					}
				} else if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() != null
						&& ((ItemBlock) item).getBlock() instanceof IScrapable) {
					IScrapable scrapable = (IScrapable) ((ItemBlock) item).getBlock();
					if (enumMat != scrapable.getMaterial())
						continue;

					int fakeStackSize = stack.getCount();
					while (fakeStackSize > 0 && weightLeft > 0) {
						--fakeStackSize;
						weightLeft -= scrapable.getWeight();
						if (fakeStackSize <= 0) {
							break;
						}
					}
					totalWeight += scrapable.getWeight() * (stack.getCount() - fakeStackSize);
					if (weightLeft <= 0) {
						break;
					}
				} else if (VanillaManager.getVanillaScrapable(item) != null) {
					VanillaManager.VanillaScrapableItem scrapable = VanillaManager.getVanillaScrapable(item);
					if (enumMat != scrapable.getMaterial())
						continue;

					int fakeStackSize = stack.getCount();
					while (fakeStackSize > 0 && weightLeft > 0) {
						--fakeStackSize;
						weightLeft -= scrapable.getWeight();
						if (fakeStackSize <= 0) {
							break;
						}
					}
					totalWeight += scrapable.getWeight() * (stack.getCount() - fakeStackSize);
					if (weightLeft <= 0) {
						break;
					}
				} else {
					break;
				}
			}
			itInv = listInv.iterator();
			int i = (int) Math.floor(totalWeight / ingWrapper.getWeight());
			if (i > multiplier) {
				multiplier = i;
			}
		}
		if (multiplier < 1) {
			System.out.println("Multiplier less than 1, setting back to 1");
			multiplier = 1;
		}
		return multiplier;
	}

	@Override
	public List<ItemStack> consumeInput(TileEntityForge tileEntity) {
		int multiplier = getOutputMultiplier(tileEntity);
		List<MaterialStackWrapper> listIng = MaterialStackWrapper.wrapList(ingredients, false);
		List<ItemStack> listInv = tileEntity.getActiveInventory();
		Iterator<MaterialStackWrapper> itIng = listIng.iterator();
		Iterator<ItemStack> itInv = listInv.iterator();
		
		List<ItemStack> leftovers = new ArrayList<ItemStack>();

		while (itIng.hasNext()) {
			MaterialStackWrapper ingWrapper = itIng.next();
			EnumMaterial enumMat = ingWrapper.getMaterial();
			int weightLeft = ingWrapper.getWeight() * multiplier;
			System.out.println(enumMat + " " + weightLeft + " " + ingWrapper.getWeight());
			while (itInv.hasNext() && weightLeft > 0) {
				ItemStack stack = itInv.next();
				if (stack == null || stack.isEmpty())
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
					if (enumMat != scrapable.getMaterial())
						continue;

					while (stack.getCount() > 0 && weightLeft > 0) {
						stack.shrink(1);
						weightLeft -= scrapable.getWeight();
						if (stack.getCount() <= 0) {
							itInv.remove();
							break;
						}
					}
					if(weightLeft == 0) break;
				    if (weightLeft < 0) {
						Item scrap = ItemUtils.INSTANCE.getScrapResult(enumMat);
						if(scrap != null) {
							leftovers.add(new ItemStack(scrap,-weightLeft));
						}
						break;
					}
				} else if (VanillaManager.getVanillaScrapable(item) != null) {
					VanillaManager.VanillaScrapableItem scrapableItem = VanillaManager.getVanillaScrapable(item);
					if (enumMat != scrapableItem.getMaterial())
						continue;

					while (stack.getCount() > 0 && weightLeft > 0) {
						stack.shrink(1);
						weightLeft -= scrapableItem.getWeight();
						if (stack.getCount() <= 0) {
							itInv.remove();
							break;
						}
					}
					if(weightLeft == 0) break;
				    if (weightLeft < 0) {
						Item scrap = ItemUtils.INSTANCE.getScrapResult(enumMat);
						if(scrap != null) {
							leftovers.add(new ItemStack(scrap,-weightLeft));
						}
						break;
					}
				} else {
					break;
				}
			}
			itInv = listInv.iterator();
		}
		System.out.println(leftovers);
		return leftovers;
	}

}
