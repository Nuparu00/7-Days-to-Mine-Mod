package nuparu.sevendaystomine.init;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import nuparu.sevendaystomine.util.damage.EntityDamageVehicle;

public class ModDamageSources {
    public static final DamageSource thirst = new DamageSource("thirst").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();
    public static final DamageSource bleeding = new DamageSource("bleeding").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();
    public static final DamageSource alcoholPoison = new DamageSource("alcoholPoison").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();
    public static final DamageSource fungalInfection = new DamageSource("fungal_infection").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();

    public static final DamageSource infection = new DamageSource("infection").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();

    public static EntityDamageVehicle causeVehicleDamage(Entity source, Entity transmitter) {
        return new EntityDamageVehicle("vehicle", transmitter, source);
    }

}
