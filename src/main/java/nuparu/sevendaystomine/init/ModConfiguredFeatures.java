package nuparu.sevendaystomine.init;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.List;
import java.util.function.Supplier;

public class ModConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, SevenDaysToMine.MODID);

    /*public static final Supplier<List<OreConfiguration.TargetBlockState>> TIN_ORES = Suppliers.memoize(
            () -> List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES,ModBlocks.TIN_ORE.get().defaultBlockState()))
    );

    public static final RegistryObject<ConfiguredFeature<?, ?>> TIN_ORE = CONFIGURED_FEATURES.register("tin_ore", () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(TIN_ORES.get(),7)));
*/}
