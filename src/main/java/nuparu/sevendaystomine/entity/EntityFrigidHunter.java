package nuparu.sevendaystomine.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import nuparu.sevendaystomine.config.ModConfig;

public class EntityFrigidHunter extends EntityBipedalZombie {

	public EntityFrigidHunter(World worldIn) {
		super(worldIn);
		this.experienceValue = 20;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(90D* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
	}

}
