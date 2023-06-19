package nuparu.sevendaystomine.init;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.registries.DeferredRegister;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, SevenDaysToMine.MODID);

    /*public static final Supplier<List<OreConfiguration.TargetBlockState>> TIN_ORES = Suppliers.memoize(
            () -> List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,ModBlocks.TIN_ORE.get().defaultBlockState()))
    );

    public static final RegistryObject<ConfiguredFeature<?, ?>> TIN_ORE = CONFIGURED_FEATURES.register("tin_ore", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(TIN_ORES.get(),7)));
*/}
