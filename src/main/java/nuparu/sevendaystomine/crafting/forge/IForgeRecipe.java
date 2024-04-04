package nuparu.sevendaystomine.crafting.forge;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityForge;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;

public interface IForgeRecipe {
	ItemStack getMoldItem();
	List<ItemStack> getJeiIngredients();
	ItemStack getJeiOutput();
	float getExperience();
	int getCookingTime();
	@Nullable
	Fraction getRatio(TileEntityForge forge);

	default boolean consume(TileEntityForge forge){
		return false;
	}

	boolean matches(TileEntityForge forge, World world);

	ItemStack assemble(TileEntityForge tileEntityForge);

	ResourceLocation name();
}
