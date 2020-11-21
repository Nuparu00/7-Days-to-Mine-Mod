package com.nuparu.sevendaystomine.entity;

import com.nuparu.sevendaystomine.init.ModLootTables;

import net.minecraft.world.World;

public class EntityPlaguedNurse extends EntityBipedalZombie {

	public EntityPlaguedNurse(World worldIn) {
		super(worldIn);
		this.lootTable = ModLootTables.ZOMBIE_NURSE;
	}
	
	

}
