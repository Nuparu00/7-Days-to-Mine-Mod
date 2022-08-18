package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.world.level.block.entity.SleepingBagBlockEntity;
import org.jetbrains.annotations.Nullable;

public class SleepingBagBlock extends BedBlock implements IBlockBase {
    protected static final VoxelShape BOTTOM_SOUTH = Block.box(1.0D, 0.0D, 0.0D, 15.0D, 2.0D, 15);
    protected static final VoxelShape BOTTOM_EAST= Block.box(0.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15);
    protected static final VoxelShape BOTTOM_NORTH = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 16);
    protected static final VoxelShape BOTTOM_WEST = Block.box(1.0D, 0.0D, 1.0D, 16.0D, 2.0D, 15);

    public SleepingBagBlock(DyeColor p_49454_, Properties p_49455_) {
        super(p_49454_, p_49455_);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        Direction direction = getConnectedDirection(p_220053_1_).getOpposite();
        switch (direction) {
            case NORTH:
                return BOTTOM_NORTH;
            case SOUTH:
                return BOTTOM_SOUTH;
            case WEST:
                return BOTTOM_WEST;
            default:
                return BOTTOM_EAST;
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SleepingBagBlockEntity(pos, state, this.getColor());
    }
}
