package nuparu.sevendaystomine.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.world.item.GunItem;

import java.util.Optional;

public class PlayerUtils {
    public static int getInfectionStageStart(int stage) {
        if(stage >= ServerConfig.infectionStagesDuration.get().size()) return Integer.MIN_VALUE;

        int start = 0;

        if(stage == 0) return start;

        for(int i = 0; i < stage; i++) {
            start+= ServerConfig.infectionStagesDuration.get().get(i);
        }
        return start;
    }

    // Returns the time until the next infection stage, takes the infection time
    // from IExtendedPlayer
    public static int getCurrentInectionStageRemainingTime(int time) {
        int stage = getInfectionStage(time);
        if(stage >= ServerConfig.infectionStagesDuration.get().size()) return 24000;

        int nextStageStart = getInfectionStageStart(stage+1);
        return nextStageStart-time;

    }

    // Reutns the current infection staged based on the infection time from
    // IExtendedPlayer
    public static int getInfectionStage(int time) {
        if (time < 0)
            return -1;
        for(int i = 0; i < getNumberOfstages()-1;i++) {
            int start = getInfectionStageStart(i);
            int end = getInfectionStageStart(i+1);
            if(start <= time && time < end) {

                return i;
            }

        }
        return ServerConfig.infectionStagesDuration.get().size()-1;
    }

    public static int getNumberOfstages() {
        return ServerConfig.infectionStagesDuration.get().size();
    }

    public static void infectPlayer(Player player, int time) {
        IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
        if (iep != null && !iep.isInfected()) {
            iep.setInfectionTime(time);
        }
    }

    public static BiOptional<ItemStack, ItemStack> gunsInHands(Player player){
        Optional<ItemStack> mainHand = gunInHand(player, InteractionHand.MAIN_HAND);
        Optional<ItemStack> offHand = gunInHand(player, InteractionHand.OFF_HAND);
        if(mainHand.isPresent() && offHand.isPresent()){
            Optional<GunItem.Wield> mainHandWield = GunItem.getWield(mainHand.get());
            Optional<GunItem.Wield> offHandWield = GunItem.getWield(offHand.get());

            if((mainHandWield.isPresent() && mainHandWield.get() == GunItem.Wield.TWO_HAND) ||
            (offHandWield.isPresent() && offHandWield.get() == GunItem.Wield.TWO_HAND)){
                return BiOptional.of(Optional.empty(), Optional.empty());
            }
        }
        return BiOptional.of(mainHand, offHand);
    }

    public static Optional<ItemStack> gunInHand(Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        if(stack.getItem() instanceof GunItem){
            return Optional.of(stack);
        }
        return Optional.empty();
    }
}
