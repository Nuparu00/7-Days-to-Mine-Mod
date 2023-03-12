package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.world.entity.item.MountableBlockEntity;
import org.jetbrains.annotations.NotNull;

public class ArmchairBlock extends WaterloggableHorizontalBlockBase{

    private static final VoxelShape SOUTH = Block.box(0F, 0.0F, 0F, 1F*16, 0.375F*16, 0.9375F*16);
    private static final VoxelShape NORTH = Block.box(0F, 0.0F, 0.0625F*16, 1F*16, 0.375F*16, 1F*16);
    private static final VoxelShape WEST = Block.box(0.0625F*16, 0.0F, 0F, 1F*16, 0.375F*16, 1F*16);
    private static final VoxelShape EAST = Block.box(0F, 0.0F, 0F, 0.9375F*16, 0.375F*16, 1F*16);


    public ArmchairBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, Boolean.FALSE));
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

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos,
                                          Player playerIn, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (!playerIn.isCrouching()) {
            MountableBlockEntity.mountBlock(worldIn, pos, playerIn,0.15f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
