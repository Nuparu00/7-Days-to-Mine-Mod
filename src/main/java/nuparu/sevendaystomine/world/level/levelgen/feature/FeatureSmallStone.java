package nuparu.sevendaystomine.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.MathUtils;

public class FeatureSmallStone extends Feature<NoneFeatureConfiguration> {

    public FeatureSmallStone(Codec<NoneFeatureConfiguration> p_i231922_1_) {
        super(p_i231922_1_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        int ci = (pos.getX() >> 4) << 4;
        int ck = (pos.getZ() >> 4) << 4;
        ResourceKey<Level> dimensionType = level.getLevel().dimension();
        if (context.chunkGenerator() instanceof FlatLevelSource || !ServerConfig.smallRockGenerationDimensions.get().contains(dimensionType.location().toString()))
            return false;
        if (ServerConfig.smallRockGenerationChance.get() <= 0) return false;
        if (random.nextInt(ServerConfig.smallRockGenerationChance.get()) == 0) {

            int x = 0;
            for (int a = 0; a < MathUtils.getIntInRange(random, ServerConfig.smallRockGenerationRateMin.get(), ServerConfig.smallRockGenerationRateMax.get() + 1); a++) {
                int i = ci + random.nextInt(16);
                int k = ck + random.nextInt(16);
                int j = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i, k);
                BlockPos pos2 = new BlockPos(i, j, k);

                BlockPos below = pos2.below();
                BlockState belowState = level.getBlockState(below);
                if (!belowState.isFaceSturdy(level, below, Direction.UP)) continue;
                level.setBlock(pos2, getRandomRock(random), 4);
                x++;
            }
            return x > 0;
        }
        return false;

    }

    static Block[] blocks;

    public static BlockState getRandomRock(RandomSource random) {
        if (blocks == null) {
            blocks = new Block[]{ModBlocks.STONE_SMALL_ROCK.get(), ModBlocks.ANDESITE_SMALL_ROCK.get(), ModBlocks.DIORITE_SMALL_ROCK.get(), ModBlocks.GRANITE_SMALL_ROCK.get()};
        }
        return blocks[random.nextInt(blocks.length)].defaultBlockState();
    }
}