package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.world.level.block.entity.GlobeBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlobeBlock extends WaterloggableHorizontalBlockBase implements EntityBlock {

    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 14, 12);

    public GlobeBlock(Properties p_49795_) {
        super(p_49795_);
    }


    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                          BlockHitResult rayTraceResult) {
        double hitX = rayTraceResult.getLocation().x;
        double hitY = rayTraceResult.getLocation().y;
        double hitZ = rayTraceResult.getLocation().z;
        if (worldIn.isClientSide())
            return InteractionResult.SUCCESS;

        if(worldIn.getBlockEntity(pos) instanceof GlobeBlockEntity globe) {
            double speed = -0.6f;
            if ((hitX == 0 && hitZ < 0.5) || (hitX == 1 && hitZ > 0.5) || (hitX > 0.5 && hitZ == 0) || (hitX < 0.5 && hitZ == 1)) {
                speed = -speed;
            }
            globe.addSpeed(speed * 5);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GlobeBlockEntity(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof GlobeBlockEntity tile) {
                tile.tick();
            }
        };
    }

}
