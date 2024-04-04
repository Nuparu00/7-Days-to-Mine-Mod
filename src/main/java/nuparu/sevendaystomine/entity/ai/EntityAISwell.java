package nuparu.sevendaystomine.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import nuparu.sevendaystomine.entity.EntityZombiePoliceman;

public class EntityAISwell extends EntityAIBase {
	/** The creeper that is swelling. */
	EntityZombiePoliceman swellingCreeper;
	/**
	 * The creeper's attack target. This is used for the changing of the creeper's
	 * state.
	 */
	EntityLivingBase creeperAttackTarget;

	public EntityAISwell(EntityZombiePoliceman entitycreeperIn) {
		this.swellingCreeper = entitycreeperIn;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();

		return (float) ((float) this.swellingCreeper.getHealth())
				/ (float) this.swellingCreeper.getMaxHealth() <= 0.25f;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.swellingCreeper.getNavigator().clearPath();
		this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void resetTask() {
		this.creeperAttackTarget = null;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask() {
		this.swellingCreeper.setState(1);
	}
}