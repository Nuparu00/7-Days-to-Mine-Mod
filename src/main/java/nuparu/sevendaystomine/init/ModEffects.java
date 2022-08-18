package nuparu.sevendaystomine.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.effect.*;

public class ModEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SevenDaysToMine.MODID);

    public static final RegistryObject<MobEffect> DYSENTERY = EFFECTS.register("dysentery",() -> new DysenteryMobEffect(MobEffectCategory.HARMFUL, 5797459));
    public static final RegistryObject<MobEffect> BLEEDING = EFFECTS.register("bleeding",() -> new BleedingMobEffect(MobEffectCategory.HARMFUL, 0x661111));
    public static final RegistryObject<MobEffect> BROKEN_LEG = EFFECTS.register("broken_leg", () ->  new BrokenLegMobEffect(MobEffectCategory.HARMFUL, 0xaaaaaa));
    public static final RegistryObject<MobEffect> SPLINTED_LEG = EFFECTS.register("splinted_leg", () ->  new SplintedMobLegEffect(MobEffectCategory.HARMFUL, 0xaaaaaa));
    public static final RegistryObject<MobEffect> ALCOHOL_BUZZ = EFFECTS.register("alcohol_buzz", () ->  new AlcoholBuzzMobEffect(MobEffectCategory.BENEFICIAL, 20092544));
    public static final RegistryObject<MobEffect> DRUNK = EFFECTS.register("drunk", () ->  new DrunkMobEffect(MobEffectCategory.HARMFUL, 22092544));
    public static final RegistryObject<MobEffect> ALCOHOL_POISON = EFFECTS.register("alcohol_poison", () ->  new AlcoholPoisonMobEffect(MobEffectCategory.HARMFUL, 1092544));
    public static final RegistryObject<MobEffect> INFECTION = EFFECTS.register("infection", () ->  new InfectionMobEffect(MobEffectCategory.HARMFUL, 1930808));
    public static final RegistryObject<MobEffect> FUNGAL_INFECTION = EFFECTS.register("fungal_infection", () ->  new FungalInfectionMobEffect(MobEffectCategory.HARMFUL, 0x11bbaa));
    public static final RegistryObject<MobEffect> CAFFEINE_BUZZ = EFFECTS.register("caffeine_buzz", () ->  new CaffeineBuzzMobEffect(MobEffectCategory.BENEFICIAL, 5092544));

}
