package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import nuparu.sevendaystomine.world.level.block.IBlockBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HorizontalBlockBase extends HorizontalDirectionalBlock implements IBlockBase {

    private final boolean invertedRotation;
    protected HorizontalBlockBase(Properties p_54120_) {
        this(p_54120_, false);
    }

    protected HorizontalBlockBase(Properties p_54120_, boolean invertedRotation) {
        super(p_54120_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
        this.invertedRotation = invertedRotation;
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_53304_) {
        return super.getStateForPlacement(p_53304_).setValue(FACING, invertedRotation ? p_53304_.getHorizontalDirection().getOpposite() : p_53304_.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_53334_) {
        p_53334_.add(FACING);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }
}
