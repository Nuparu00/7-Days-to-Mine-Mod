package com.nuparu.sevendaystomine.crafting.forge;

import java.util.List;

import com.nuparu.sevendaystomine.tileentity.TileEntityCampfire;
import com.nuparu.sevendaystomine.tileentity.TileEntityForge;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

public class ForgeRecipeShaped implements IForgeRecipe {

	private ItemStack result;
	private ItemStack mold;
	private ItemStack[][] matrix;

	private int width;
	private int height;
	
	private int xp = 5;
	
	public ForgeRecipeShaped(ItemStack result, ItemStack mold, ItemStack[][] matrix) {
		this(result, mold, matrix, matrix[0].length, matrix.length,5);
	}

	public ForgeRecipeShaped(ItemStack result, ItemStack mold, ItemStack[][] matrix, int width, int height) {
		this(result,mold,matrix,width,height,5);
	}

	public ForgeRecipeShaped(ItemStack result, ItemStack mold, ItemStack[][] matrix, int xp) {
		this(result, mold, matrix, matrix[0].length, matrix.length, xp);
	}

	public ForgeRecipeShaped(ItemStack result, ItemStack mold, ItemStack[][] matrix, int width, int height, int xp) {
		this.result = result;
		this.mold = mold;
		this.matrix = matrix;
		this.xp = xp;
		if (width < 0 || height < 0 || width > 2 || height > 2) {
			throw new IllegalArgumentException(
					"Dimensions of a forge recipe for ItemStack " + result.getItem().getUnlocalizedName()
							+ " must be between 0 and 2 (both inclusive)! Report this to the mod author.");
		}
		this.width = width;
		this.height = height;
	}

	@Override
	public ForgeResult matches(TileEntityForge inv, World worldIn) {
		ItemStack[][] invArray = inv.getActiveInventoryAsArray().clone();
		for (int i = 0; i <= 2 - width; i++) {
			for (int j = 0; j <= 2 - height; j++) {
				if(checkMatch(invArray,i,j)) {
					return new ForgeResult(true,null);
				}

			}
		}

		return new ForgeResult(false,null);
	}
	
	private boolean checkMatch(ItemStack[][] invArray, int x, int y) {
		for (int i = 0; i < 2; ++i)
        {
            for (int j = 0; j < 2; ++j)
            {
                int k = i - x;
                int l = j - y;
                
                ItemStack itemStack = ItemStack.EMPTY;
                if (k >= 0 && l >= 0 && k < width && l < height)
                {
                	itemStack = matrix[k][l];
                }
                if(!ItemStack.areItemsEqual(itemStack, invArray[k][l]) || itemStack.getCount() < invArray[k][l].getCount()) {
                	Utils.getLogger().info("Are not equal " + itemStack.toString() + " " + invArray[k][l].toString());
                	return false;
                }
                
            }
        }
		return true;
	}

	@Override
	public ItemStack getResult() {
		return result.copy();
	}

	@Override
	public ItemStack getOutput(TileEntityForge tileEntity) {
		return getResult();
	}

	@Override
	public ItemStack getMold() {
		return mold;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemStack> getIngredients() {
		List<ItemStack> asList = Arrays.asList(matrix);
		return asList;
	}
	
	@Override
	public int intGetXP(EntityPlayer player) {
		return xp;
	}

	@Override
	public List<ItemStack> consumeInput(TileEntityForge inv) {
		ItemStack[][] invArray = inv.getActiveInventoryAsArray().clone();
		for (int m = 0; m <= 2 - width; m++) {
			for (int n = 0; n <= 2 - height; n++) {
				for (int i = 0; i < 2; ++i)
		        {
		            for (int j = 0; j < 2; ++j)
		            {
		                int k = i - m;
		                int l = j - n;
		                
		                ItemStack stack = ItemStack.EMPTY;
		                ItemStack itemStack = invArray[k][l];
		                if (k >= 0 && l >= 0 && k < width && l < height)
		                {
		                	stack = matrix[k][l];
		                }
		                if(ItemStack.areItemsEqual(itemStack, stack)) {
		                	itemStack.shrink(stack.getCount());
		                }
		                
		            }
		        }
			}
		}
		return null;
	}

}
