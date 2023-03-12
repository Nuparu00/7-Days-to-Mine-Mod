package nuparu.sevendaystomine.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class FeatureRoads<T extends FeatureConfiguration> extends Feature<T> {
    public FeatureRoads(Codec<T> p_65786_) {
        super(p_65786_);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<T> context) {
        return false;
    }
}
