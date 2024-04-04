package nuparu.sevendaystomine.crafting.workbench;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;

@Deprecated
public class WorkbenchCraftingManager {

	private static ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();

	public ArrayList<IRecipe> getRecipes() {
		return recipes;
	}

	public static void addRecipes() {
		/*
		addShapedRecipe(new ResourceLocation("minecraft:recipe"), new ResourceLocation("group"),
				new ItemStack(ModBlocks.REBAR_FRAME), "ppp", "pbp", "ppp", 'b', Items.BOOK, 'p', Items.PAPER);*/
	}

	public static void addShapedRecipe(ResourceLocation name, ResourceLocation group, @Nonnull ItemStack output,
			Object... params) {
		ShapedPrimer primer = CraftingHelper.parseShaped(params);
		addRecipe(new ShapedRecipes(group == null ? "" : group.toString(), primer.width, primer.height, primer.input,
				output).setRegistryName(name));
	}

	public static void addRecipe(IRecipe recipe) {
		recipes.add(recipe);
	}

	public static ItemStack findMatchingResult(InventoryCrafting craftMatrix, World worldIn) {
		for (IRecipe irecipe : recipes) {
			if (irecipe.matches(craftMatrix, worldIn)) {
				return irecipe.getCraftingResult(craftMatrix);
			}
		}

		return ItemStack.EMPTY;
	}

	@Nullable
	public static IRecipe findMatchingRecipe(InventoryCrafting craftMatrix, World worldIn) {
		for (IRecipe irecipe : recipes) {
			if (irecipe.matches(craftMatrix, worldIn)) {
				return irecipe;
			}
		}

		return null;
	}

	public static NonNullList<ItemStack> getRemainingItems(InventoryCrafting craftMatrix, World worldIn) {
		for (IRecipe irecipe : recipes) {
			if (irecipe.matches(craftMatrix, worldIn)) {
				return irecipe.getRemainingItems(craftMatrix);
			}
		}

		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(craftMatrix.getSizeInventory(),
				ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); ++i) {
			nonnulllist.set(i, craftMatrix.getStackInSlot(i));
		}

		return nonnulllist;
	}

}
