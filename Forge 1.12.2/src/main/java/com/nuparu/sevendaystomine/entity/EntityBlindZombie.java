package com.nuparu.sevendaystomine.entity;

import com.nuparu.sevendaystomine.entity.ai.EntityAIAttackOnContact;
import com.nuparu.sevendaystomine.entity.ai.EntityAIBreakBlock;
import com.nuparu.sevendaystomine.entity.ai.EntityAIMoveTowardsNoise;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityBlindZombie extends EntityBipedalZombie implements INoiseListener{

	private Noise noise;
	
	public EntityBlindZombie(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		//this.tasks.addTask(2, new EntityAIInfectedAttack(this, 1.0D, false));
		this.tasks.addTask(1, new EntityAIBreakBlock(this));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.tasks.addTask(0, new EntityAIMoveTowardsNoise(this, 1D));
		//this.tasks.addTask(0, new EntityAIAttackOnContact(this,EntityPlayer.class));
		
		//this.applyEntityAI();
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.16D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.5D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60D);
	}

	@Override
	public void addNoise(Noise noise) {
		this.noise = noise;
	}

	@Override
	public Noise getCurrentNoise() {
		return noise;
	}

	@Override
	public void reset() {
		noise = null;
	}

}
