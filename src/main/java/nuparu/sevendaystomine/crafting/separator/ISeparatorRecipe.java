package nuparu.sevendaystomine.crafting.separator;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntitySeparator;

public interface ISeparatorRecipe {
	 boolean matches(TileEntitySeparator inv, World worldIn);
	 public List<ItemStack> getResult();
	 public List<ItemStack> getOutputs(TileEntitySeparator tileEntity);
	 public ItemStack getIngredient();
	 public void consumeInput(TileEntitySeparator tileEntity);
	 public int intGetXP(EntityPlayer player);
}
