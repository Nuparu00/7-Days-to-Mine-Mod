package nuparu.sevendaystomine.entity;

import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModLootTables;

public class EntityPlaguedNurse extends EntityBipedalZombie {

	public EntityPlaguedNurse(World worldIn) {
		super(worldIn);
		this.lootTable = ModLootTables.ZOMBIE_NURSE;
	}
	
	

}
