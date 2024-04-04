package nuparu.sevendaystomine.util.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class EntityDamageVehicle extends EntityDamageSourceIndirect {
	public EntityDamageVehicle(String name, Entity transmitter, Entity indirectSource) {
		super(name, transmitter, indirectSource);
		this.setDamageBypassesArmor();
	}

	@Override
	public ITextComponent getDeathMessage(EntityLivingBase target) {

		Entity entity = (Entity) getTrueSource();
		EntityLivingBase entitylivingbase1 = null;
		if (entity instanceof EntityLivingBase) {
			entitylivingbase1 = (EntityLivingBase) entity;
		}
		String s = "death.attack." + this.damageType;
		if (entitylivingbase1 != null) {
			if (getImmediateSource() != null && getImmediateSource() instanceof EntityLivingBase && ((EntityLivingBase)getImmediateSource()).hasCustomName()) {
				String s1 = s + ".vehicle";
				return new TextComponentTranslation(s1, target.getDisplayName(), entitylivingbase1.getDisplayName(),
						getImmediateSource().getDisplayName());
			}
			else {
				return new TextComponentTranslation(s, target.getDisplayName(), entitylivingbase1.getDisplayName());
			}
		} else {
			String s1 = s + ".unknown";
			return new TextComponentTranslation(s1, target.getDisplayName());

		}

	}

}