package nuparu.sevendaystomine.event;

import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.init.ModDamageSources;
import nuparu.sevendaystomine.init.ModEffects;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.PlayerUtils;
import nuparu.sevendaystomine.world.entity.EntityUtils;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = SevenDaysToMine.MODID)
public class PlayerTickEventHandler {
    @SubscribeEvent
    public static void onPlayerTickPost(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) return;
        Player player = event.player;
        Level level = player.level();
        IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
        if(extendedPlayer == null) return;

        if(!level.isClientSide()){

            if(ItemUtils.getUpgradeAmount(player.getMainHandItem()) > 0){
                updateUpgrader(player.getMainHandItem(),player);
            }

            if(ItemUtils.getUpgradeAmount(player.getOffhandItem()) > 0){
                updateUpgrader(player.getOffhandItem(),player);
            }

            extendedPlayer.tick(player);

            if (extendedPlayer.isInfected()) {
                int time = extendedPlayer.getInfectionTime();
                extendedPlayer.setInfectionTime(time + 1);
                MobEffectInstance effect = player.getEffect(ModEffects.INFECTION.get());

                int amplifier = PlayerUtils.getInfectionStage(time);
                int timeLeft = PlayerUtils.getCurrentInectionStageRemainingTime(time);

                if (effect == null || effect.getAmplifier() != amplifier) {
                    MobEffectInstance newEffect = new MobEffectInstance(ModEffects.INFECTION.get(), Math.min(24000, timeLeft), amplifier,false,false,true);
                    newEffect.setCurativeItems(new ArrayList<>());
                    player.addEffect(newEffect);
                }

                if (amplifier == PlayerUtils.getNumberOfstages() - 1) {
                    player.hurt(ModDamageSources.INFECTION.apply(level), 1);
                }
            }
        }
    }

    private static void updateUpgrader(ItemStack stack, Player player){
        if(stack.getOrCreateTag().contains("7D2M_UpgradePos", Tag.TAG_LONG)){
            long l = stack.getOrCreateTag().getLong("7D2M_UpgradePos");
            BlockHitResult result = EntityUtils.rayTraceServer(player, player.getAttribute(net.minecraftforge.common.ForgeMod.BLOCK_REACH.get()).getValue(), 1,
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
            if(result.getBlockPos().asLong() != l){
                ItemUtils.eraseUpgraderData(stack);
            }
        }
    }
}
