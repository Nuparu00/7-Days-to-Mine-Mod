package nuparu.sevendaystomine.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.json.upgrade.UpgradeDataManager;
import nuparu.sevendaystomine.json.upgrade.UpgradeEntry;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.BreakSyncTrackingMessage;
import nuparu.sevendaystomine.util.Utils;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
public class WorldEventHandler {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.getPlayer() != null && event.getPlayer().isCreative()) return;

        BlockState state = event.getState();

        if(Utils.downgradeBlock(event.getLevel(),event.getPos(),state)){
            event.setCanceled(true);
            ItemStack stack = event.getPlayer().getMainHandItem();
            stack.hurtAndBreak(1, event.getPlayer(), (p_41007_) -> {
                p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
    }

    @SubscribeEvent
    public static void onChunkWatchEvent(ChunkWatchEvent event) {
        ServerLevel world = event.getLevel();
        BlockPos pos = event.getPos().getWorldPosition();
        LevelChunk chunk = world.getChunkAt(pos);
        //Sync damage blocks to the client
        IChunkData data = CapabilityHelper.getChunkData(chunk);
        if (data != null && !data.isEmpty()) {
            PacketManager.sendTo(PacketManager.blockBreakSyncTracking,
                    new BreakSyncTrackingMessage(data.writeToNBT(new CompoundTag()), pos), event.getPlayer());
        }
    }


    @SubscribeEvent
    public static void onDetonate(ExplosionEvent.Detonate event) {
        Level level = event.getLevel();
        for(int i = 0; i < event.getAffectedBlocks().size(); i++){
            BlockPos pos = event.getAffectedBlocks().get(i);
            BlockState blockState = level.getBlockState(pos);
            UpgradeEntry upgradeEntry = UpgradeDataManager.INSTANCE.getDowngradeFromEntry(blockState);
            if(upgradeEntry != null){
                Utils.downgradeBlock(level,pos,blockState);
                event.getAffectedBlocks().remove(pos);
            }
        }
    }

}



