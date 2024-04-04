package nuparu.sevendaystomine.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.ai.EntityAIBreakBlock;
import nuparu.sevendaystomine.entity.ai.EntityAIMoveTowardsNoise;

public class EntityBlindZombie extends EntityBipedalZombie implements INoiseListener{

	private Noise noise;
	
	public EntityBlindZombie(World worldIn) {
		super(worldIn);
		this.experienceValue = 20;
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
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.5D* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70D* ModConfig.players.balanceModifier);
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
