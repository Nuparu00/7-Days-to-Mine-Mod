package nuparu.sevendaystomine.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import nuparu.sevendaystomine.entity.INoiseListener;

public class EntityAIMoveTowardsNoise extends EntityAIBase {

	private EntityLiving entity;
	private double speed;
	public int memory = 200;
	
	public EntityAIMoveTowardsNoise(EntityLiving entity, double speed) {
		this.entity = entity;
		this.speed = speed;
		this.setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
		INoiseListener listener = (INoiseListener)entity;
		return listener.getCurrentNoise() != null && ((listener.getCurrentNoise().getPos().distanceSq(entity.posX, entity.posY, entity.posZ)) > 0) && memory > 0;
	}

	public void resetTask() {
		((INoiseListener)entity).reset();
		memory = 200;
	}

	public void startExecuting() {
		BlockPos pos = ((INoiseListener)entity).getCurrentNoise().getPos();
		entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(),
				pos.getZ(), this.speed);
	}
	public void updateTask() {
		memory--;
		INoiseListener listener = (INoiseListener)entity;
		if (listener.getCurrentNoise() != null) {
			BlockPos pos = listener.getCurrentNoise().getPos();
			this.entity.getLookHelper().setLookPosition(pos.getX(), pos.getY(), pos.getZ(), 30.0F, 30.0F);
		}
	}
}
