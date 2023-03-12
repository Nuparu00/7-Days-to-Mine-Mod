package nuparu.sevendaystomine.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.world.level.block.HorizontalBlockBase;
import nuparu.sevendaystomine.world.level.block.entity.PhotoBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class PhotoItem extends Item {
    public PhotoItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip,
                                @NotNull TooltipFlag flagIn) {
        Optional<String> path = getPath(stack);
        if (path.isPresent()) {
            MutableComponent text = Component.translatable(path.get());
            text.setStyle(text.getStyle().withItalic(true));
            tooltip.add(text);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        if (!playerIn.isCrouching()) {
            Optional<String> path = getPath(stack);
            if (path.isPresent()) {
                SevenDaysToMine.proxy.openClientOnlyGui(0, stack);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {

        Direction facing = context.getClickedFace();
        Player player = context.getPlayer();
        Level worldIn = context.getLevel();
        InteractionHand hand = context.getHand();
        BlockPos pos = context.getClickedPos();
        if (facing == Direction.UP) {
            return InteractionResult.PASS;
        } else {
            ItemStack stack = player.getItemInHand(hand);

            BlockState state = worldIn.getBlockState(pos);
            Block block = state.getBlock();

            BlockState stateToPlace = ModBlocks.PHOTO.get().defaultBlockState().setValue(HorizontalBlockBase.FACING, facing);

            if (!state.isFaceSturdy(worldIn, pos, facing)) {
                return InteractionResult.PASS;
            } else {
                worldIn.setBlockAndUpdate(pos.relative(facing), stateToPlace);
                if (worldIn.getBlockEntity(pos.relative(facing)) instanceof PhotoBlockEntity photoBlockEntity) {
                    photoBlockEntity.setPath(stack.getOrCreateTag().getString("path"));
                }
                stack.shrink(1);
                return InteractionResult.SUCCESS;

            }
        }
    }

    public static Optional<String> getPath(ItemStack stack){
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains("path", Tag.TAG_STRING)) {
            return Optional.of(nbt.getString("path"));
        }
        return Optional.empty();
    }

    public static ItemStack setPath(ItemStack stack, String path){
       stack.getOrCreateTag().putString("path",path);
       return stack;
    }
}
