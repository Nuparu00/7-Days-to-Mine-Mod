package nuparu.sevendaystomine.client.event;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModBlocksTags;
import nuparu.sevendaystomine.init.ModFluidTags;
import nuparu.sevendaystomine.json.scrap.ScrapDataManager;
import nuparu.sevendaystomine.json.scrap.ScrapEntry;
import nuparu.sevendaystomine.json.scrap.WeightWrapper;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.PlayerDrinkMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityLevel;
import nuparu.sevendaystomine.world.item.quality.QualityManager;

import java.util.HashMap;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {

    //Cached BreakData... duh
    public static HashMap<BlockPos, CompoundTag> cachedBreakData = new HashMap<>();

    @SubscribeEvent
    public static void onRenderCrosshairPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {
            Player player = mc.player;
            if(player == null) return;
            ItemStack stack = player.getMainHandItem();
            if(stack.hasTag() && stack.getOrCreateTag().contains("7D2M_UpgradeProgress", Tag.TAG_DOUBLE)){
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
            QualityLevel qualityLevel = qualityStack.getQualityLevel();

            MutableComponent qualityTitle = Component.translatable( "tooltip.sevendaystomine.quality." + qualityLevel.getUnlocalizedName().toLowerCase());
            MutableComponent qualityValue = Component.translatable("tooltip.sevendaystomine.quality", quality);


            Style style = qualityTitle.getStyle().withColor(qualityLevel.textColor);
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
        if (Minecraft.getInstance().getCameraEntity() == Minecraft.getInstance().player) {
            Player player = Minecraft.getInstance().player;
            if(player.isSpectator() || player.isCreative()) return;
            if(!player.getMainHandItem().isEmpty()) return;
            BlockHitResult result = Utils.rayTraceServer(player, player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue(), 1,
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
            if(result == null) return;

            BlockState blockState = player.level.getBlockState(result.getBlockPos());
            if(blockState.is(ModBlocksTags.MURKY_WATER_SOURCE) || blockState.getFluidState().is(ModFluidTags.MURKY_WATER_SOURCE)){
                PacketManager.sendToServer(PacketManager.playerDrink,new PlayerDrinkMessage(result.getBlockPos()));
            }
        }
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        LevelAccessor world = event.getLevel();
        if (world.isClientSide()) {
            ChunkAccess ichunk = event.getChunk();

            if (ichunk instanceof LevelChunk) {
                LevelChunk chunk = (LevelChunk) ichunk;
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


}
