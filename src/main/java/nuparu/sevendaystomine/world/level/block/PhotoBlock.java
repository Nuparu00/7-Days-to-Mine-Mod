package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.world.item.PhotoItem;
import nuparu.sevendaystomine.world.level.block.entity.PhotoBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PhotoBlock extends WallAttachedBlockBase implements EntityBlock {
    private static final VoxelShape NORTH = Block.box(0.0F, 0.0F, 15.4, 16.0F, 16, 16);
    private static final VoxelShape SOUTH = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16, 0.6);
    private static final VoxelShape WEST = Block.box(15.4, 0.0F, 0F, 16, 16, 16);
    private static final VoxelShape EAST = Block.box(0.0F, 0.0F, 0.0F, 0.6, 16, 16);

    public PhotoBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PhotoBlockEntity(pos,state);
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

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_149645_1_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player){
        if(world.getBlockEntity(pos) instanceof PhotoBlockEntity photoBlockEntity) {
            return PhotoItem.setPath(new ItemStack(ModItems.PHOTO.get()),photoBlockEntity.getPath());
        }
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                          @NotNull BlockHitResult rayTraceResult) {
        if(world.getBlockEntity(pos) instanceof PhotoBlockEntity photoBlockEntity) {
            SevenDaysToMine.proxy.openPhoto(photoBlockEntity.getPath());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
