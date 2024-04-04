package nuparu.sevendaystomine.util.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class EntityDamageShot extends EntityDamageSourceIndirect {
	public EntityDamageShot(String name, Entity transmitter, Entity indirectSource) {
		super(name, transmitter, indirectSource);
		this.setProjectile();
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
			ItemStack stack = entitylivingbase1.getHeldItemMainhand();
			if (stack != null && !stack.isEmpty()) {
				String s1 = s + ".item";
				return new TextComponentTranslation(s1, target.getDisplayName(), entitylivingbase1.getDisplayName(),
						stack.getItem().getItemStackDisplayName(stack));
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