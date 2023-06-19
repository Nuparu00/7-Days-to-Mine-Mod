package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.world.level.block.entity.CalendarBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CalendarBlock extends WallAttachedBlockBase implements EntityBlock {
    private static final VoxelShape NORTH = Block.box(5, 7, 15, 11, 16, 16);
    private static final VoxelShape SOUTH = Block.box(5, 7, 0.0F, 11, 16, 1);
    private static final VoxelShape WEST = Block.box(15, 7, 5, 16, 16, 11);
    private static final VoxelShape EAST = Block.box(0.0F, 7, 5, 1, 16, 11);

    public CalendarBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CalendarBlockEntity(pos,state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH;
            case SOUTH, UP, DOWN -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }
}
