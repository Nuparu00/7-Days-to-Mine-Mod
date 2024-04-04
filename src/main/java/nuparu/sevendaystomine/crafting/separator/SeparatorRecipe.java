package nuparu.sevendaystomine.crafting.separator;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntitySeparator;

public class SeparatorRecipe implements ISeparatorRecipe{
	
	private ItemStack ingredient;
	private List<ItemStack> results;
	private int xp = 5;

	public SeparatorRecipe(ItemStack ingredient,List<ItemStack> results) {
		this(ingredient,results,5);
	}
	
	public SeparatorRecipe(ItemStack ingredient,List<ItemStack> results, int xp) {
		this.ingredient = ingredient;
		this.results = results;
		this.xp = xp;
	}
	@Override
	public boolean matches(TileEntitySeparator te, World worldIn) {
		ItemStack input = te.getInventory().getStackInSlot(0);
		if (ItemStack.areItemsEqual(input, ingredient) && ItemStack.areItemStackTagsEqual(input, ingredient) && input.getCount() >= ingredient.getCount()) {
			return true;
		}
		return false;
	}

	@Override
	public List<ItemStack> getResult() {
		return results;
	}

	@Override
	public List<ItemStack> getOutputs(TileEntitySeparator tileEntity) {
		return getResult();
	}

	@Override
	public ItemStack getIngredient() {
		return ingredient;
	}

	@Override
	public void consumeInput(TileEntitySeparator te) {
		ItemStack input = te.getInventory().getStackInSlot(0);
		if (ItemStack.areItemsEqual(input, ingredient) && ItemStack.areItemStackTagsEqual(input, ingredient) && input.getCount() >= ingredient.getCount()) {
			input.shrink(ingredient.getCount());
		}
	}

	@Override
	public int intGetXP(EntityPlayer player) {
		return xp;
	}

}
