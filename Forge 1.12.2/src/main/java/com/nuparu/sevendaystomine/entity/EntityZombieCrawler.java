package com.nuparu.sevendaystomine.entity;

import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.util.ItemUtils;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
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
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4D);
		armor.setBaseValue(0.0D);
	}
	
	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		EntityLootableCorpse lootable = new EntityLootableCorpse(world);
		lootable.setOriginal(this);
		lootable.setPosition(posX, posY, posZ);
		isDead = true;
		if (!world.isRemote) {
			ItemUtils.fillWithLoot(lootable.getInventory(), lootTable, world, rand);
			world.spawnEntity(lootable);
		}
	}
	
	@Override
	public Vec3d corpseRotation() {
		return new Vec3d(1, 64, 1);
	}

	@Override
	public Vec3d corpseTranslation() {
		return new Vec3d(0, -0.25, 0);
	}

	@Override
	public boolean customCoprseTransform() {
		return true;
	}
}
