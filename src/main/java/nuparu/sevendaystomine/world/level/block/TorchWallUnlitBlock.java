package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import org.jetbrains.annotations.NotNull;

public class TorchWallUnlitBlock extends TorchWallBlockBase{
    public TorchWallUnlitBlock() {
        super(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().sound(SoundType.WOOD),null);
    }

    @Override
    public void animateTick(@NotNull BlockState p_57494_, @NotNull Level p_57495_, @NotNull BlockPos p_57496_, @NotNull RandomSource p_57497_) {

    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult result) {
        if (player.isCrouching()) return super.use(state, level, pos, player, hand, result);
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        if (item == Items.FLINT_AND_STEEL) {
            lit(level, pos, this == ModBlocks.SOUL_TORCH_UNLIT_WALL.get());
            if (!player.isCreative()) {
                stack.hurtAndBreak(1, player, (p_43388_) -> p_43388_.broadcastBreakEvent(player.getUsedItemHand()));
            }
        } else if (item == Items.TORCH || item == ModItems.TORCH.get()) {
            lit(level, pos,this == ModBlocks.SOUL_TORCH_UNLIT_WALL.get());
        } else if (item == Items.FIRE_CHARGE) {
            lit(level, pos,this == ModBlocks.SOUL_TORCH_UNLIT_WALL.get());
            if (!player.isCreative()) {
                stack.shrink(1);
                if (stack.getCount() <= 0) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static void lit(Level world, BlockPos pos, boolean soul) {
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof TorchBlock))
            return;
        world.setBlockAndUpdate(pos, (soul ? ModBlocks.SOUL_TORCH_LIT_WALL : ModBlocks.TORCH_LIT_WALL).get().defaultBlockState().setValue(TorchWallLitBlockBase.FACING,
                state.getValue(TorchWallUnlitBlock.FACING)));
        world.playLocalSound((float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F,
                (float) pos.getZ() + 0.5F, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 0.5F,
                2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F, false);
    }
}
