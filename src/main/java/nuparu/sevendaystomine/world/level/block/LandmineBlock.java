package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.world.level.block.entity.ItemHandlerBlockEntity;
import nuparu.sevendaystomine.world.level.block.entity.SmallContainerBlockEntity;
import org.jetbrains.annotations.Nullable;

public class LandmineBlock extends BlockBase{


    private static final VoxelShape SHAPE = Block.box(4, 0.0D, 4, 12, 3, 12);
    private float power;
    
    public LandmineBlock(Properties p_49795_, float power) {
        super(p_49795_);
        this.power = power;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if(worldIn.isClientSide()) return;
        if (!(entityIn instanceof LivingEntity)) {
            return;
        }
        if (entityIn instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return;
            }
        }
        worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        worldIn.explode(null, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, power, true, Explosion.BlockInteraction.DESTROY);
    }

    @Override
    public void onBlockExploded(BlockState state, Level worldIn, BlockPos pos, Explosion explosionIn) {
        if(worldIn.isClientSide()) return;
        worldIn.explode(null, pos.getX() + 0.5, pos.getY() + 0.25 , pos.getZ() + 0.5, power, true,Explosion.BlockInteraction.DESTROY);
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        BlockPos blockpos = p_196260_3_.below();
        BlockState blockstate = p_196260_2_.getBlockState(blockpos);
        return this.canSurviveOn(p_196260_2_, blockpos, blockstate);
    }

    private boolean canSurviveOn(BlockGetter p_235552_1_, BlockPos p_235552_2_, BlockState p_235552_3_) {
        return p_235552_3_.isFaceSturdy(p_235552_1_, p_235552_2_, Direction.UP) || p_235552_3_.is(Blocks.HOPPER);
    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, Level p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_,
                                BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!p_220069_2_.isClientSide) {
            if (!p_220069_1_.canSurvive(p_220069_2_, p_220069_3_)) {
                dropResources(p_220069_1_, p_220069_2_, p_220069_3_);
                p_220069_2_.removeBlock(p_220069_3_, false);
            }

        }
    }

}
