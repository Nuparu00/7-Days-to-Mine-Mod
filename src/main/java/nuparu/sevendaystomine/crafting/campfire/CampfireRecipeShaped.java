package nuparu.sevendaystomine.crafting.campfire;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityCampfire;
import nuparu.sevendaystomine.util.Utils;
import scala.actors.threadpool.Arrays;

public class CampfireRecipeShaped implements ICampfireRecipe {

	private ItemStack result;
	private ItemStack pot;
	private ItemStack[][] matrix;

	private int width;
	private int height;
	
	private int xp = 5;

	public CampfireRecipeShaped(ItemStack result, ItemStack pot, ItemStack[][] matrix) {
		this(result, pot, matrix, matrix[0].length, matrix.length, 5);
	}

	public CampfireRecipeShaped(ItemStack result, ItemStack pot, ItemStack[][] matrix, int width, int height) {
		this(result,pot,matrix,width,height,5);
	}
	
	
	public CampfireRecipeShaped(ItemStack result, ItemStack pot, ItemStack[][] matrix, int xp) {
		this(result, pot, matrix, matrix[0].length, matrix.length, xp);
	}

	public CampfireRecipeShaped(ItemStack result, ItemStack pot, ItemStack[][] matrix, int width, int height, int xp) {
		this.result = result;
		this.pot = pot;
		this.matrix = matrix;
		if (width < 0 || height < 0 || width > 2 || height > 2) {
			throw new IllegalArgumentException(
					"Dimensions of a campfire recipe for ItemStack " + result.getItem().getUnlocalizedName()
							+ " must be between 0 and 2 (both inclusive)! Report this to the mod author.");
		}
		this.width = width;
		this.height = height;
		this.xp = xp;
	}

	@Override
	public boolean matches(TileEntityCampfire inv, World worldIn) {
		ItemStack[][] invArray = inv.getActiveInventoryAsArray().clone();
		for (int i = 0; i <= 2 - width; i++) {
			for (int j = 0; j <= 2 - height; j++) {
				if(checkMatch(invArray,i,j)) {
					return true;
				}

			}
		}

		return false;
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
	public ItemStack getOutput(TileEntityCampfire tileEntity) {
		return getResult();
	}

	@Override
	public ItemStack getPot() {
		return pot;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	@Override
	public int intGetXP(EntityPlayer player) {
		return xp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemStack> getIngredients() {
		List<ItemStack> asList = Arrays.asList(matrix);
		return asList;
	}

	@Override
	public void consumeInput(TileEntityCampfire inv) {
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
	}

}
