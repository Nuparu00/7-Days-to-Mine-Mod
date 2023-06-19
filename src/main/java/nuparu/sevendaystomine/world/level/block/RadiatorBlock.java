package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RadiatorBlock extends WaterloggableHorizontalBlockBase {

    public final VoxelShape NORTH = Block.box(0.0F, 0.0F, 11.2F, 16.0F, 12F, 16.0F);
    public final VoxelShape SOUTH = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 12F, 4.8F);
    public final VoxelShape WEST = Block.box(11.2F, 0.0F, 0F, 16.0F, 12F, 16.0F);
    public final VoxelShape EAST = Block.box(0.0F, 0.0F, 0.0F, 4.8F, 12F, 16.0F);

    public RadiatorBlock(Properties p_49795_) {
        super(p_49795_);
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
}