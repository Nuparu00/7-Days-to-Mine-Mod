package nuparu.sevendaystomine.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.client.util.CameraHelper;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModBlocksTags;
import nuparu.sevendaystomine.init.ModFluidTags;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.json.scrap.ScrapDataManager;
import nuparu.sevendaystomine.json.scrap.ScrapEntry;
import nuparu.sevendaystomine.json.scrap.WeightWrapper;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.CameraDimensionsMessage;
import nuparu.sevendaystomine.network.messages.HonkMessage;
import nuparu.sevendaystomine.network.messages.PlayerDrinkMessage;
import nuparu.sevendaystomine.network.messages.ReloadMessage;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.entity.EntityUtils;
import nuparu.sevendaystomine.world.entity.item.CarEntity;
import nuparu.sevendaystomine.world.entity.item.MinibikeEntity;
import nuparu.sevendaystomine.world.entity.item.VehicleEntity;
import nuparu.sevendaystomine.world.item.AnalogCameraItem;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import nuparu.sevendaystomine.world.item.IReloadableItem;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityTier;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

import java.util.HashMap;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {

    //Cached BreakData... duh
    public static HashMap<BlockPos, CompoundTag> cachedBreakData = new HashMap<>();
    public static boolean takingPhoto;

    @SubscribeEvent
    public static void onRenderCrosshairPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {
            Player player = mc.player;
            if (player == null) return;
            ItemStack stack = player.getMainHandItem();
            if (stack.hasTag() && stack.getOrCreateTag().contains("7D2M_UpgradeProgress", Tag.TAG_DOUBLE)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty())
            return;
        Item item = stack.getItem();

        IQualityStack qualityStack = (IQualityStack) (Object) stack;

        if (ServerConfig.quality.get() && qualityStack.canHaveQuality()) {
            int quality = qualityStack.getQuality();
            QualityTier qualityTier = qualityStack.getQualityLevel();

            MutableComponent qualityTitle = Component.translatable("tooltip.sevendaystomine.quality." + qualityTier.getUnlocalizedName().toLowerCase());
            MutableComponent qualityValue = Component.translatable("tooltip.sevendaystomine.quality", quality);


            Style style = qualityTitle.getStyle().withColor(qualityTier.textColor);
            qualityTitle.setStyle(style);
            qualityValue.setStyle(style);
            event.getToolTip().add(1, qualityTitle);
            event.getToolTip().add(2, qualityValue);
        }

        EnumMaterial mat = EnumMaterial.NONE;
        WeightWrapper weight = WeightWrapper.ZERO;
        if (ScrapDataManager.INSTANCE.hasEntry(item)) {
            ScrapEntry entry = ScrapDataManager.INSTANCE.getEntry(item);

            mat = entry.material;
            weight = entry.weight;
        }

        if (mat != null && mat != EnumMaterial.NONE) {
            event.getToolTip().add(MutableComponent.create(new TranslatableContents("tooltip.sevendaystomine.material", weight, mat.getLocalizedName())).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    @SubscribeEvent
    public static void onLoggedInEvent(ClientPlayerNetworkEvent.LoggingIn event) {
        QualityManager.reload();
    }

    @SubscribeEvent
    public static void onClickInputEvent(InputEvent.InteractionKeyMappingTriggered event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && Minecraft.getInstance().getCameraEntity() == player) {
            if (player.isSpectator() || player.isCreative()) return;
            if (!player.getMainHandItem().isEmpty()) return;
            BlockHitResult result = EntityUtils.rayTraceServer(player, player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue(), 1,
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
            if (result == null) return;

            BlockState blockState = player.level.getBlockState(result.getBlockPos());
            if (blockState.is(ModBlocksTags.MURKY_WATER_SOURCE) || blockState.getFluidState().is(ModFluidTags.MURKY_WATER_SOURCE)) {
                PacketManager.sendToServer(PacketManager.playerDrink, new PlayerDrinkMessage(result.getBlockPos()));
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        LevelAccessor world = event.getLevel();
        if (world.isClientSide()) {
            ChunkAccess ichunk = event.getChunk();

            if (ichunk instanceof LevelChunk chunk) {
                IChunkData data = CapabilityHelper.getChunkData(chunk);
                if (data != null) {
                    CompoundTag nbt = cachedBreakData.get(chunk.getPos().getWorldPosition());
                    if (nbt == null) return;
                    data.readFromNBT(nbt);
                    cachedBreakData.remove(chunk.getPos().getWorldPosition());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {

        if (!ClientConfig.minibikeCameraRoll.get()) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        Entity riding = player.getVehicle();

        if (riding instanceof MinibikeEntity minibike) {
            Vec3 forward = minibike.getForward();
            Vec3 right = forward.yRot(90);

            float turning = MathUtils.lerp((float) event.getPartialTick(), minibike.getTurningPrev(),
                    minibike.getTurning());
            event.setRoll(event.getRoll() - turning / 8f);
            //event.getInfo().move(right.z*0.1,0,-right.x*0.1);

        }

    }

    @SubscribeEvent
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;


        PoseStack matrixStack = event.getPoseStack();
        float partialTicks = event.getPartialTick();
        Entity riding = player.getVehicle();

        if (riding instanceof MinibikeEntity minibike) {
            Vec3 forward = minibike.getForward();
            matrixStack.mulPose(Vector3f.XN.rotationDegrees((float) (forward.x * MathUtils.lerp(partialTicks, minibike.getTurningPrev(), minibike.getTurning()))));
            matrixStack.mulPose(Vector3f.ZN.rotationDegrees((float) (forward.z * MathUtils.lerp(partialTicks, minibike.getTurningPrev(), minibike.getTurning()))));
        }

    }


    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onKeyPressed(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
            if (ModKeyMappings.HONK.isDown()) {
                Entity riding = player.getVehicle();
                if (riding instanceof CarEntity) {
                    if (riding.getPassengers().indexOf(player) == 0) {
                        System.out.println("Beep beep");
                        System.out.println("Remember to make me attract Wardens");
                        System.out.println("Beep beep");
                        PacketManager.honk.sendToServer(new HonkMessage());
                    }
                }
            }
            if (ModKeyMappings.RELOAD.isDown()) {
                tryToReload(player);
            }
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty() && stack.getItem() instanceof AnalogCameraItem) {
                if (ModKeyMappings.CAMERA_WIDTH_INCREASE.isDown()) {
                    PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0.1,0,0));
                } else if (ModKeyMappings.CAMERA_WIDTH_DECREASE.isDown()) {
                    PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(-0.1,0,0));
                } else if (ModKeyMappings.CAMERA_HEIGHT_INCREASE.isDown()) {
                    PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0.1,0));
                } else if (ModKeyMappings.CAMERA_HEIGHT_DECREASE.isDown()) {
                    PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,-0.1,0));
                } else if (ModKeyMappings.CAMERA_ZOOM.isDown()) {
                    PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0,0.1));
                } else if (ModKeyMappings.CAMERA_UNZOOM.isDown()) {
                    PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0,-0.1));
                }
            }
        }
    }

    private static void tryToReload(Player player) {
        if (player == null) return;

        ItemStack mainStack = player.getMainHandItem();
        ItemStack secStack = player.getOffhandItem();
        if (mainStack.isEmpty() && secStack.isEmpty()) return;

        Item mainItem = mainStack.getItem();
        Item secItem = secStack.getItem();
        if (mainItem instanceof IReloadableItem || secItem instanceof IReloadableItem) {
            PacketManager.gunReload.sendToServer(new ReloadMessage());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {

        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null)
            return;
        if (ClientEventHandler.takingPhoto) {
            CameraHelper.INSTANCE.saveScreenshot(mc.getWindow().getScreenWidth(), mc.getWindow().getScreenHeight(), mc.getMainRenderTarget(), player);
            ClientEventHandler.takingPhoto = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderGameOverlayEvent(RenderGuiOverlayEvent.Pre event) {
        if (event.isCancelable()) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null)
                return;

            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty() && stack.getItem() == ModItems.ANALOG_CAMERA.get()) {
                if (player.getUseItemRemainingTicks() > 0 || takingPhoto) {
                    event.setCanceled(true);
                }
            }
            if (event.getOverlay() == VanillaGuiOverlay.MOUNT_HEALTH.type()) {
                if (player.getVehicle() != null && player.getVehicle() instanceof VehicleEntity) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void updateFOVEvent(ComputeFovModifierEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null)
            return;
        ItemStack stack = player.getMainHandItem();
        if (!stack.isEmpty() && stack.getItem() instanceof AnalogCameraItem) {
            event.setNewFovModifier((float) (event.getNewFovModifier() / AnalogCameraItem.getZoom(stack, player)));
        }
    }
}
