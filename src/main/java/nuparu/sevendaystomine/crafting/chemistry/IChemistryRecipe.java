package nuparu.sevendaystomine.crafting.chemistry;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;

public interface IChemistryRecipe {
	 boolean matches(TileEntityChemistryStation inv, World worldIn);
	 ItemStack getResult();
	 public ItemStack getOutput(TileEntityChemistryStation tileEntity);
	 public List<ItemStack> getIngredients();
	 public void consumeInput(TileEntityChemistryStation tileEntity);
	 public int intGetXP(EntityPlayer player);
}
