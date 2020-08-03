package com.nuparu.sevendaystomine.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityBurntZombie extends EntityBipedalZombie {

	public EntityBurntZombie(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.09D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300D);
	}

	@Override
	public boolean isBurning() {
		return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (rand.nextDouble() < 0.3) {
			entityIn.setFire(8);
		}
		return super.attackEntityAsMob(entityIn);
	}

}
