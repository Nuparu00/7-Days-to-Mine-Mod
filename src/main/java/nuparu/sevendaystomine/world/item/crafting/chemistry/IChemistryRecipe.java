package nuparu.sevendaystomine.world.item.crafting.chemistry;

import net.minecraft.world.item.crafting.Recipe;
import nuparu.sevendaystomine.world.level.block.entity.ChemistryBlockEntity;

public interface IChemistryRecipe<T extends ChemistryBlockEntity> extends Recipe<T> {
	float getExperience();
	int getCookingTime();
}
