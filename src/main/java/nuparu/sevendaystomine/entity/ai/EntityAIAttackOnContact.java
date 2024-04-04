package nuparu.sevendaystomine.entity.ai;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumHand;

public class EntityAIAttackOnContact extends EntityAIBase {

	private EntityLivingBase entity;
	private EntityLivingBase target;
	private Class <? extends EntityLivingBase > targetClass;
	public EntityAIAttackOnContact(EntityLivingBase entity, Class <? extends EntityLivingBase > target) {
		this.entity = entity;
		this.targetClass = target;
	}
	
	@Override
	public boolean shouldExecute() {
		List<EntityLivingBase> list = this.entity.world.<EntityLivingBase>getEntitiesWithinAABB(targetClass, entity.getEntityBoundingBox().grow(0.3, 0.3, 0.3));
        for(EntityLivingBase living : list) {
        	if(living.getIsInvulnerable()) {
        		continue;
        	}
        	target = living;
        	return true;
        }
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting()
    {
		return target != null && target.getDistanceSq(entity) <= 1;
    }

	@Override
	public void updateTask() {
		if(target == null) {
			return;
		}
		
		entity.attackEntityAsMob(target);
		entity.swingArm(EnumHand.MAIN_HAND);
	}
}
