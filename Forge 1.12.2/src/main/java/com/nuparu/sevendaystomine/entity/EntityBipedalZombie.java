package com.nuparu.sevendaystomine.entity;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityBipedalZombie extends EntityZombieBase {

	public EntityBipedalZombie(World worldIn) {
		super(worldIn);
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		EntityLootableCorpse lootable = new EntityLootableCorpse(world);
		lootable.setOriginal(this);
		lootable.setPosition(posX, posY, posZ);
		isDead = true;
		if (!world.isRemote) {
			world.spawnEntity(lootable);
		}
	}

}
