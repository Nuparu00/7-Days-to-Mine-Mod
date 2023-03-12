package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SmallRockBlock extends PickableBlock{
    private static final VoxelShape SHAPE = Block.box(3.4, 0.0F,5, 11, 3, 11);

    public SmallRockBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter p_220053_2_, @NotNull BlockPos p_220053_3_,
                                        @NotNull CollisionContext p_220053_4_) {
        Vec3 vector3d = state.getOffset(p_220053_2_, p_220053_3_);
        return SHAPE.move(vector3d.x, vector3d.y, vector3d.z);
    }
}
