package com.nuparu.sevendaystomine.entity;

import net.minecraft.world.World;

public class EntityZombieCrawler extends EntityZombieBase {

	public EntityZombieCrawler(World worldIn) {
		super(worldIn);
		setSize(0.75F, 0.33F);
	}
	@Override
	public float getEyeHeight() {
		return this.height;
	}
}
