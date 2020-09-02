package com.nuparu.sevendaystomine.entity;

import net.minecraft.entity.SharedMonsterAttributes;
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
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		range.setBaseValue(56.0D);
		speed.setBaseValue(0.125D);
		attack.setBaseValue(3.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120D);
		armor.setBaseValue(0.0D);
	}
}
