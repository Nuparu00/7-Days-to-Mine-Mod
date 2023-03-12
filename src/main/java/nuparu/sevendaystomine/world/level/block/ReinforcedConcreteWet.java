package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.level.LevelUtils;
import org.jetbrains.annotations.NotNull;

public class ReinforcedConcreteWet extends BlockBase {

    public ReinforcedConcreteWet(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, RandomSource rand) {
        if (rand.nextInt(10) == 0 && world.isDay() && !world.isRainingAt(pos) && !LevelUtils.isThunderingAt(world, pos)) {
            world.setBlock(pos, ModBlocks.REINFORCED_CONCRETE.get().defaultBlockState(), 3);
        }
    }
}
