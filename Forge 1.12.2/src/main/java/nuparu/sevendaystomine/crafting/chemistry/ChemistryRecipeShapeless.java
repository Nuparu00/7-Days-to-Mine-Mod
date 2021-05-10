package nuparu.sevendaystomine.crafting.chemistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.crafting.ItemStackWrapper;
import nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;

public class ChemistryRecipeShapeless implements IChemistryRecipe{
	private ItemStack result;
	private ArrayList<ItemStack> ingredients;
	private int xp = 5;

	public ChemistryRecipeShapeless(ItemStack result,ArrayList<ItemStack> ingredients) {
		this(result,ingredients,5);
	}
	
	public ChemistryRecipeShapeless(ItemStack result,ArrayList<ItemStack> ingredients, int xp) {
		this.result = result;
		this.ingredients = ingredients;
		this.xp = xp;
	}

	@Override
	public boolean matches(TileEntityChemistryStation inv, World worldIn) {
		List<ItemStackWrapper> listInv = ItemStackWrapper.wrapList(inv.getActiveInventory(), false);
		List<ItemStackWrapper> listIng = ItemStackWrapper.wrapList(ingredients, false);

		if(listInv.size() != listIng.size()) return false;
		
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
			return false;
		}
		
		
		return true;
		
	}

	@Override
	public ItemStack getResult() {
		return result.copy();
	}

	@Override
	public ItemStack getOutput(TileEntityChemistryStation tileEntity) {
		return getResult();
	}

	@Override
	public List<ItemStack> getIngredients() {
		return ingredients;
	}
	
	@Override
	public int intGetXP(EntityPlayer player) {
		return xp;
	}

	@Override
	public void consumeInput(TileEntityChemistryStation inv) {
		List<ItemStackWrapper> listInv = ItemStackWrapper.wrapList(inv.getActiveInventory(),false);
        List<ItemStackWrapper> listIng = ItemStackWrapper.wrapList(ingredients,false);
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
	}

}
