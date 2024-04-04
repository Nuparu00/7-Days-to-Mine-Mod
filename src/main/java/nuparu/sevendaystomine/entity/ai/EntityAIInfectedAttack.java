package nuparu.sevendaystomine.entity.ai;

import net.minecraft.entity.ai.EntityAIAttackMelee;
import nuparu.sevendaystomine.entity.EntityZombieBase;

public class EntityAIInfectedAttack extends EntityAIAttackMelee
{
    private final EntityZombieBase zombie;

    public EntityAIInfectedAttack(EntityZombieBase zombieIn, double speedIn, boolean longMemoryIn)
    {
        super(zombieIn, speedIn, longMemoryIn);
        this.zombie = zombieIn;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        super.resetTask();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        super.updateTask();
    }
}