package nuparu.sevendaystomine.util.damage;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class EntityDamageVehicle extends IndirectEntityDamageSource {
	public EntityDamageVehicle(String name, Entity transmitter, Entity indirectSource) {
		super(name, transmitter, indirectSource);
		this.bypassArmor();
	}

	@Override
	public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity target) {

		Entity entity = getEntity();
		LivingEntity LivingEntity1 = null;
		if (entity instanceof LivingEntity) {
			LivingEntity1 = (LivingEntity) entity;
		}
		String s = "death.attack." + this.msgId;
		if (LivingEntity1 != null) {
			if (this.getDirectEntity() != null && getDirectEntity() instanceof LivingEntity
					&& getDirectEntity().hasCustomName()) {
				String s1 = s + ".vehicle";
				return Component.translatable(s1, target.getDisplayName(), LivingEntity1.getDisplayName(),
						getDirectEntity().getDisplayName());
			} else {
				return Component.translatable(s, target.getDisplayName(), LivingEntity1.getDisplayName());
			}
		} else {
			String s1 = s + ".unknown";
			return Component.translatable(s1, target.getDisplayName());

		}

	}

}