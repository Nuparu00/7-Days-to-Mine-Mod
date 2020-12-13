package com.nuparu.sevendaystomine.entity;

import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.init.ModLootTables;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityZombieSoldier extends EntityBipedalZombie {

	public EntityZombieSoldier(World worldIn) {
		super(worldIn);
		this.lootTable = ModLootTables.ZOMBIE_POLICEMAN;
		this.experienceValue = 20;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120F);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6D);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (rand.nextFloat() < 0.005F) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ModItems.NIGHT_VISION_DEVICE));
			 this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
		}
	}

}
