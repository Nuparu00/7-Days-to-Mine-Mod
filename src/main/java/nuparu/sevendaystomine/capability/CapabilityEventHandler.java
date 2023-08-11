package nuparu.sevendaystomine.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.AnalogCameraItem;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEventHandler {
    public static final ResourceLocation EXTENDED_PLAYER_KEY = new ResourceLocation(SevenDaysToMine.MODID,
            "extended_player");

    public static final ResourceLocation CHUNK_DATA_KEY = new ResourceLocation(SevenDaysToMine.MODID,
            "chunk_data");
    public static final ResourceLocation EXTENDED_INV_KEY = new ResourceLocation(SevenDaysToMine.MODID, "extended_inv");
    public static final ResourceLocation EXTENDED_ENTITY_KEY = new ResourceLocation(SevenDaysToMine.MODID,
            "extended_entity");

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player){
            ExtendedPlayerProvider provider = new ExtendedPlayerProvider().setOwner(player);
            event.addCapability(EXTENDED_PLAYER_KEY, provider);
            event.addListener(provider.lazyInstance::invalidate);
        }
        else{
            ExtendedEntityProvider provider = new ExtendedEntityProvider().setOwner(event.getObject());
            event.addCapability(EXTENDED_ENTITY_KEY, provider);
            event.addListener(provider.lazyInstance::invalidate);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getOriginal();

        IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
        IExtendedPlayer oldExtendedPlayer = CapabilityHelper.getExtendedPlayer(event.getOriginal());

        if (!event.isWasDeath()) {
            extendedPlayer.copy(oldExtendedPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if(!player.level().isClientSide()) {
            IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
            extendedPlayer.onDataChanged();
        }
    }

    @SubscribeEvent
    public static void playerStartedTracking(PlayerEvent.StartTracking event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (target instanceof Player targetPlayer) {

            IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(targetPlayer);
            if(extendedPlayer == null) return;

            extendedPlayer.onStartedTracking(targetPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
        extendedPlayer.onDataChanged();
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesChunk(AttachCapabilitiesEvent<LevelChunk> event) {
        if (event.getObject() == null) return;
        LevelChunk chunk = event.getObject();

        ChunkDataProvider provider = new ChunkDataProvider().setOwner(chunk);
        event.addCapability(CHUNK_DATA_KEY, provider);
        event.addListener(provider.lazyInstance::invalidate);
    }

    @SubscribeEvent
    public static void attachCapabilityToStack(AttachCapabilitiesEvent<ItemStack> event) {
        if(event.getObject().getItem() instanceof AnalogCameraItem){
            event.addCapability(EXTENDED_INV_KEY, new ExtendedInventoryProvider().setInstance(new ExtendedInventory(){
                @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack){
                    if(slot == 0){
                        return !stack.isEmpty() && stack.getItem() == Items.PAPER;
                    }
                    return super.isItemValid(slot, stack);
                }
            }).setSize(1));
        }
    }

}
