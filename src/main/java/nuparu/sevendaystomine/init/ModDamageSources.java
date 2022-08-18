package nuparu.sevendaystomine.init;

import net.minecraft.world.damagesource.DamageSource;

public class ModDamageSources {
    public static DamageSource thirst = new DamageSource("thirst").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();
    public static DamageSource bleeding = new DamageSource("bleeding").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();
    public static DamageSource alcoholPoison = new DamageSource("alcoholPoison").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();
    public static DamageSource fungalInfection = new DamageSource("fungal_infection").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();

    public static DamageSource infection = new DamageSource("infection").setScalesWithDifficulty().bypassMagic()
            .bypassArmor();

}
