package nuparu.sevendaystomine.crafting.forge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.crafting.ItemStackWrapper;
import nuparu.sevendaystomine.tileentity.TileEntityForge;

public class ForgeRecipeShapeless implements IForgeRecipe {

	private ItemStack result;
	private ItemStack mold;
	private ArrayList<ItemStack> ingredients;

	public ForgeRecipeShapeless(ItemStack result, ItemStack mold, ArrayList<ItemStack> ingredients) {
		this.result = result;
		this.mold = mold;
		this.ingredients = ingredients;
	}

	@Override
	public ForgeResult matches(TileEntityForge inv, World worldIn) {
		List<ItemStackWrapper> listInv = ItemStackWrapper.wrapList(inv.getActiveInventory(), false);
		List<ItemStackWrapper> listIng = ItemStackWrapper.wrapList(ingredients, false);

		if(listInv.size() != listIng.size()) 
			return new ForgeResult(false,null);
		
		Iterator<ItemStackWrapper> itInv = listInv.iterator();
		Iterator<ItemStackWrapper> itIng = listIng.iterator();
		
		while(itInv.hasNext()) {
			ItemStackWrapper invWrapper = itInv.next();
			while(itIng.hasNext()) {
				ItemStackWrapper ingWrapper = itIng.next();
				if(invWrapper.equals(ingWrapper)) {
					if(invWrapper.getStackSize() >= ingWrapper.getStackSize()) {
						itIng.remove();
						itInv.remove();
						break;
					}
				}
			}
		}
		if(listInv.size() != 0 || listIng.size() != 0) {
			return new ForgeResult(false,null);
		}
		

		return new ForgeResult(true,null);
		
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
		return getResult();
	}

	@Override
	public List<ItemStack> getIngredients() {
		return ingredients;
	}
	
	@Override
	public int intGetXP(EntityPlayer player) {
		return 5;
	}

	@Override
	public List<ItemStack> consumeInput(TileEntityForge tileEntity) {
		List<ItemStackWrapper> listInv = ItemStackWrapper.wrapList(tileEntity.getActiveInventory(), false);
		List<ItemStackWrapper> listIng = ItemStackWrapper.wrapList(ingredients, false);
		ListIterator<ItemStackWrapper> iteratorInv = listInv.listIterator();
		ListIterator<ItemStackWrapper> iteratorIng = listIng.listIterator();
		while (iteratorInv.hasNext()) {
			while (iteratorIng.hasNext()) {
				ItemStack itemStack = iteratorInv.next().getItemStack();
				ItemStack stack = iteratorIng.next().getItemStack();
				if (ItemStack.areItemsEqual(stack, itemStack) && ItemStack.areItemStackTagsEqual(itemStack, stack)) {
					itemStack.shrink(stack.getCount());
					iteratorInv.remove();
					iteratorIng.remove();
				}
			}

		}
		return null;
	}

}
