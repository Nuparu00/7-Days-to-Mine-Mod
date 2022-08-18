package nuparu.sevendaystomine.world.item.quality;

import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ServerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QualityManager {
    public static ArrayList<QualityLevel> levels = new ArrayList<>();
    public static int maxLevel = 0;

    private static final QualityLevel NONE = new QualityLevel("none", Integer.MIN_VALUE,0,0xffffff);


    public static void reload(){
        levels.clear();

        List<? extends String> names = ServerConfig.qualityTierNames.get();
        List<? extends Integer> breakpoints = ServerConfig.qualityTierBreakpoints.get();
        List<? extends Integer> colors = ServerConfig.qualityTierColors.get();

        if(names.size() != breakpoints.size()){
            SevenDaysToMine.LOGGER.warn("The length of the server config property qualityTierNames(%d) is not the same as the length of qualityTierBreakpoints(%d)!",names.size(),breakpoints.size());
        }
        int size = Math.min(names.size(),breakpoints.size());

        levels.add(NONE);
        int prevEnd = 0;
        for(int i = 0; i < size; i++){
            int start = prevEnd+1;
            int end = breakpoints.get(i);
            prevEnd = end;

            levels.add(new QualityLevel(names.get(i),start,end,colors.get(i)));
        }
        maxLevel= prevEnd;
    }

    public static QualityLevel getQualityLevel(int level){
        for(QualityLevel qualityLevel : levels){
            if(qualityLevel.getStartLevel() <= level && qualityLevel.getEndLevel() >= level){
                return qualityLevel;
            }
        }
        return NONE;
    }
}
