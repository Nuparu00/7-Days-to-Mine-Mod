package nuparu.sevendaystomine.world.item.crafting.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import nuparu.sevendaystomine.world.level.block.entity.ForgeBlockEntity;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;

public interface IForgeRecipe<T extends ForgeBlockEntity> extends Recipe<T> {
	ItemStack getMoldItem();
	float getExperience();
	int getCookingTime();
	@Nullable
	Fraction getRatio(ForgeBlockEntity forge);

	default boolean consume(ForgeBlockEntity forge){
		return false;
	}
}
