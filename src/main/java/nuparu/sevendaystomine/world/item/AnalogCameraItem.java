package nuparu.sevendaystomine.world.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModCreativeModeTabs;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.SchedulePhotoMessage;
import nuparu.sevendaystomine.world.inventory.ItenMenuProvider;
import nuparu.sevendaystomine.world.inventory.item.ContainerCamera;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class AnalogCameraItem extends ItemBase {
    public AnalogCameraItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        playerIn.startUsingItem(handIn);
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack) {
        return 82000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int dur = this.getUseDuration(stack) - timeLeft;
            if (dur > 10) {
                IItemHandlerExtended inv = stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP,
                        Direction.UP).orElseGet(null);

                ItemStack paper = inv.getStackInSlot(0);
                if (paper.isEmpty() || paper.getItem() != Items.PAPER) {
                    worldIn.playSound(null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.PLAYERS,
                            worldIn.random.nextFloat() * 0.2f + 0.9f, worldIn.random.nextFloat() * 0.2f + 1.9f);
                    return;
                }
                if (ServerConfig.allowPhotos.get()) {
                    if (!worldIn.isClientSide()) {
                        PacketManager.sendTo(PacketManager.schedulePhoto, new SchedulePhotoMessage(), (ServerPlayer) player);
                    }
                }
                paper.shrink(1);
            } else if (player.isCrouching() && player instanceof ServerPlayer serverPlayer) {
                MenuProvider namedContainerProvider = new ItenMenuProvider(stack,stack.getHoverName()){
                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
                        return ContainerCamera.createContainerServerSide(windowID, playerInventory, this.stack);
                    }
                };
                NetworkHooks.openScreen(serverPlayer, namedContainerProvider, (packetBuffer) -> packetBuffer.writeItem(stack));

            }
        }
    }

    public static ItemStack setupDimensions(ItemStack stack, @Nullable Player player) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putDouble("width", 0.75);
        nbt.putDouble("height", 0.75);
        nbt.putDouble("zoom", 1);
        return stack;
    }

    public static double getWidth(ItemStack stack, @Nullable Player player) {
        stack.getOrCreateTag();
        if (!stack.getOrCreateTag().contains("width",Tag.TAG_DOUBLE)) {
            setupDimensions(stack, player);
        }
        return stack.getOrCreateTag().getDouble("width");
    }

    public static double getHeight(ItemStack stack, @Nullable Player player) {
        stack.getOrCreateTag();
        if (!stack.getOrCreateTag().contains("height",Tag.TAG_DOUBLE)) {
            setupDimensions(stack, player);
        }
        return stack.getOrCreateTag().getDouble("height");
    }

    public static double getZoom(ItemStack stack, @Nullable Player player) {
        stack.getOrCreateTag();
        if (!stack.getOrCreateTag().contains("zoom", Tag.TAG_DOUBLE)) {
            setupDimensions(stack, player);
        }
        return stack.getOrCreateTag().getDouble("zoom");
    }

    public static void setWidth(double width, ItemStack stack, @Nullable Player player) {
        stack.getOrCreateTag();
        stack.getOrCreateTag().putDouble("width", width);
    }

    public static void setHeight(double height, ItemStack stack, @Nullable Player player) {
        stack.getOrCreateTag();
        stack.getOrCreateTag().putDouble("height", height);
    }

    public static void setZoom(double zoom, ItemStack stack, @Nullable Player player) {
        stack.getOrCreateTag();
        stack.getOrCreateTag().putDouble("zoom", zoom);
    }

    @Override
    public ResourceLocation creativeModeTab(){
        return ModCreativeModeTabs.TOOLS.getId();
    }
}
