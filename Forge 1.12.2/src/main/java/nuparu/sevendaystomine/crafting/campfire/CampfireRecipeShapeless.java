package nuparu.sevendaystomine.crafting.campfire;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.crafting.ItemStackWrapper;
import nuparu.sevendaystomine.tileentity.TileEntityCampfire;

public class CampfireRecipeShapeless implements ICampfireRecipe {

	private ItemStack result;
	private ItemStack pot;
	private ArrayList<ItemStack> ingredients;
	
	private int xp = 5;

	public CampfireRecipeShapeless(ItemStack result, ItemStack pot, ArrayList<ItemStack> ingredients) {
		this(result,pot,ingredients,5);
	}
	
	public CampfireRecipeShapeless(ItemStack result, ItemStack pot, ArrayList<ItemStack> ingredients, int xp) {
		this.result = result;
		this.pot = pot;
		this.ingredients = ingredients;
		this.xp = xp;
	}

	@Override
	public boolean matches(TileEntityCampfire inv, World worldIn) {
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
	public ItemStack getPot() {
		return pot;
	}

	@Override
	public ItemStack getOutput(TileEntityCampfire tileEntity) {
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
	public void consumeInput(TileEntityCampfire inv) {
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
