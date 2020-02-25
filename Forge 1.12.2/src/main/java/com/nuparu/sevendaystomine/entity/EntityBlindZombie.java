package com.nuparu.sevendaystomine.entity;

import net.minecraft.world.World;

public class EntityBlindZombie extends EntityBipedalZombie implements INoiseListener{

	public EntityBlindZombie(World worldIn) {
		super(worldIn);
	}

	@Override
	public void addNoise(Noise noise) {
		
	}

}
