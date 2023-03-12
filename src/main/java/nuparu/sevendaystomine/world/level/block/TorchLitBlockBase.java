package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.level.block.entity.TorchBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TorchLitBlockBase extends TorchBlockBase implements EntityBlock {
    public TorchLitBlockBase(BlockBehaviour.Properties properties, ParticleOptions particleOptions) {
        super(properties, particleOptions);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state){
        return ModBlockEntities.TORCH.get().create(pos,state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : (level1, blockPos, blockState, t) -> {
            if (t instanceof TorchBlockEntity tile) {
                tile.tickServer();
            }
        };
    }

    public static void extinguish(ServerLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof TorchBlock))
            return;
        boolean soul = state.getBlock() == ModBlocks.SOUL_TORCH_LIT.get();
        world.setBlockAndUpdate(pos, (soul ? ModBlocks.SOUL_TORCH_UNLIT : ModBlocks.TORCH_UNLIT).get().defaultBlockState());
        world.playSound(null,(float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F,
                (float) pos.getZ() + 0.5F, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F,
                2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
        for (int i = 0; i < 5; ++i) {
            double d0 = (double) pos.getX() + world.random.nextDouble() * 0.6D + 0.2D;
            double d1 = (double) pos.getY() + world.random.nextDouble() * 0.6D + 0.2D;
            double d2 = (double) pos.getZ() + world.random.nextDouble() * 0.6D + 0.2D;
            world.sendParticles(ParticleTypes.SMOKE,d0, d1, d2,0,0,0,0,0);
        }
    }
}
