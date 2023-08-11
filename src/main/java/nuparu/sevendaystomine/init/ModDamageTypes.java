package nuparu.sevendaystomine.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> THIRST = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SevenDaysToMine.MODID, "thirst"));
    public static final ResourceKey<DamageType> BLEEDING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SevenDaysToMine.MODID, "bleeding"));
    public static final ResourceKey<DamageType> VEHICLE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SevenDaysToMine.MODID, "vehicle"));
    public static final ResourceKey<DamageType> ALCOHOL_POISONING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SevenDaysToMine.MODID, "alcohol_poisoning"));
    public static final ResourceKey<DamageType> INFECTION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SevenDaysToMine.MODID, "infection"));
    public static final ResourceKey<DamageType> FUNGAL_INFECTION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SevenDaysToMine.MODID, "fungal_infection"));
}
