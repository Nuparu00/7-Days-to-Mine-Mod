package nuparu.sevendaystomine.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;

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
}
