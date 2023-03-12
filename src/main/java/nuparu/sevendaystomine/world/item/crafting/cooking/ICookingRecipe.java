package nuparu.sevendaystomine.world.item.crafting.cooking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import nuparu.sevendaystomine.world.level.block.entity.GrillBlockEntity;

public interface ICookingRecipe<T extends GrillBlockEntity> extends Recipe<T> {
	ResourceLocation getRequiredStation();
	float getExperience();
	int getCookingTime();

	default boolean consume(GrillBlockEntity grill){
		return false;
	}
}
