package nuparu.sevendaystomine.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.capability.IExtendedEntity;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.json.upgrade.UpgradeDataManager;
import nuparu.sevendaystomine.json.upgrade.UpgradeEntry;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.BreakSyncTrackingMessage;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.level.LevelUtils;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
public class WorldEventHandler {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.getPlayer() != null && event.getPlayer().isCreative()) return;

        BlockState state = event.getState();

        if(LevelUtils.downgradeBlock(event.getLevel(),event.getPos(),state)){
            event.setCanceled(true);
            ItemStack stack = event.getPlayer().getMainHandItem();
            stack.hurtAndBreak(1, event.getPlayer(), (p_41007_) -> p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
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
        //Make explosions damage blocks
        Level level = event.getLevel();
        for(int i = 0; i < event.getAffectedBlocks().size(); i++){
            BlockPos pos = event.getAffectedBlocks().get(i);
            BlockState blockState = level.getBlockState(pos);
            UpgradeEntry upgradeEntry = UpgradeDataManager.INSTANCE.getDowngradeFromEntry(blockState);
            if(upgradeEntry != null){
                LevelUtils.downgradeBlock(level,pos,blockState);
                event.getAffectedBlocks().remove(pos);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if(event.getEntity().level().isClientSide()) return;
        //System.out.println(event.getEntity() + " " + event.getAmount());
        if(event.getAmount() <= 0) return;
        IExtendedEntity iee = CapabilityHelper.getExtendedEntity(event.getEntity());
        if(iee != null && iee.hasHorde()){
            iee.getHorde().updateBossbar();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        if(event.getEntity().level().isClientSide()) return;
        IExtendedEntity iee = CapabilityHelper.getExtendedEntity(event.getEntity());
        if(iee != null && iee.hasHorde()){
            iee.getHorde().removeEntityFromHorde(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        /*
        Changes the base damage of arrows fired bows and crossbows, so they are affected by quality
        (+ also sets the base damage of the modded bows)
        */
        if(entity instanceof AbstractArrow abstractArrow && abstractArrow.life < 20
                && abstractArrow.getOwner() instanceof LivingEntity livingEntity){
            ItemStack heldStack = livingEntity.getMainHandItem();
             if(livingEntity.getMainHandItem().getItem() instanceof BowItem || livingEntity.getMainHandItem().getItem() instanceof CrossbowItem){
                Item item = heldStack.getItem();
                double baseDamage = abstractArrow.getBaseDamage();
                if(item == ModItems.CRUDE_BOW.get()){
                    baseDamage = Math.max(1,baseDamage/2d);
                }
                else if (item == ModItems.COMPOUND_BOW.get()){
                    baseDamage *= 2f;
                }

                if((Object)heldStack instanceof IQualityStack qualityStack && qualityStack.canHaveQuality()){
                    baseDamage *= 1 + qualityStack.getRelativeQualityScaled();
                }

                abstractArrow.setBaseDamage(baseDamage);
            }
        }

        IExtendedEntity iee = CapabilityHelper.getExtendedEntity(entity);
        if(iee != null){

        }
    }

}



