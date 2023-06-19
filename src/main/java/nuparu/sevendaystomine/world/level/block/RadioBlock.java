package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.world.level.block.entity.RadioBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadioBlock extends WaterloggableHorizontalBlockBase implements EntityBlock{

    private static final VoxelShape NORTH = Block.box(2, 0.0F, 5, 14, 7.5F, 11);
    private static final VoxelShape SOUTH = Block.box(2, 0.0F, 5F, 14, 7.5F, 11);
    private static final VoxelShape WEST = Block.box(5F, 0.0F, 2, 11, 7.5F, 14);
    private static final VoxelShape EAST = Block.box(5, 0.0F, 2, 11, 7.5F, 14);

    public RadioBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> NORTH;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new RadioBlockEntity(pos,state);
    }
}
