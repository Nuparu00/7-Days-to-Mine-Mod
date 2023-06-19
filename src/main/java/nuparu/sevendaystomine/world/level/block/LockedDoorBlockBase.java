package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.util.MathUtils;
import org.jetbrains.annotations.NotNull;

public class LockedDoorBlockBase extends DoorBlockBase{

    private static long nextSoundAllowed = 0;

    public LockedDoorBlockBase(Properties p_52737_) {
        super(p_52737_);
    }

    public void unlock(Level world, BlockPos pos, BlockState state) {
        BlockPos bottom = pos;
        if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
            bottom = pos.below();
        }

        Block block = state.getBlock();
        Direction facing = state.getValue(DoorBlock.FACING);
        boolean open = state.getValue(DoorBlock.OPEN);
        boolean powered = state.getValue(DoorBlock.POWERED);
        DoorHingeSide hinge = state.getValue(DoorBlock.HINGE);

        world.setBlockAndUpdate(bottom.above(),
                block.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)
                        .setValue(DoorBlock.FACING, facing).setValue(DoorBlock.OPEN, open)
                        .setValue(DoorBlock.POWERED, powered).setValue(DoorBlock.HINGE, hinge));

        world.setBlockAndUpdate(bottom,
                block.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
                        .setValue(DoorBlock.FACING, facing).setValue(DoorBlock.OPEN, open)
                        .setValue(DoorBlock.POWERED, powered).setValue(DoorBlock.HINGE, hinge));

    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player,
                                          @NotNull InteractionHand hand, @NotNull BlockHitResult rayTraceResult) {
        if (System.currentTimeMillis() >= nextSoundAllowed && !player.isCrouching()) {
            nextSoundAllowed = System.currentTimeMillis() + 500L;
            worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.DOOR_LOCKED.get(), SoundSource.BLOCKS,
                    0.75f + MathUtils.getFloatInRange(0, 0.25f), 0.8f + MathUtils.getFloatInRange(0, 0.2f),false);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, Level p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {

    }
}
