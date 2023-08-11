package nuparu.sevendaystomine.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.util.damage.EntityDamageVehicle;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.Function;

public class ModDamageSources {/*
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
    }*/

    public static final Function<Level, DamageSource> THIRST = (level) -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.THIRST));
    public static final Function<Level, DamageSource> BLEEDING = (level) -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.THIRST));
    public static final TriFunction<Level, Entity, Entity, DamageSource> VEHICLE = (level, direct, indirect) -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.THIRST), direct, indirect);
    public static final Function<Level, DamageSource> ALCOHOL_POISONING = (level) -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.THIRST));
    public static final Function<Level, DamageSource> INFECTION = (level) -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.THIRST));
    public static final Function<Level, DamageSource> FUNGAL_INFECTION = (level) -> new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.THIRST));

}
