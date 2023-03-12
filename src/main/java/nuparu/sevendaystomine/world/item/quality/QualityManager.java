package nuparu.sevendaystomine.world.item.quality;

import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ServerConfig;

import java.util.ArrayList;
import java.util.List;

public class QualityManager {
    private static ArrayList<QualityTier> tiers = new ArrayList<>();
    private static int maxLevel = 0;

    private static final QualityTier NONE = new QualityTier("none", Integer.MIN_VALUE,0,0xffffff);


    public static void reload(){
        tiers.clear();

        List<? extends String> names = ServerConfig.qualityTierNames.get();
        List<? extends Integer> breakpoints = ServerConfig.qualityTierBreakpoints.get();
        List<? extends Integer> colors = ServerConfig.qualityTierColors.get();

        if(names.size() != breakpoints.size()){
            SevenDaysToMine.LOGGER.warn("The length of the server config property qualityTierNames(%d) is not the same as the length of qualityTierBreakpoints(%d)!",names.size(),breakpoints.size());
        }
        int size = Math.min(names.size(),breakpoints.size());

        tiers.add(NONE);
        int prevEnd = 0;
        for(int i = 0; i < size; i++){
            int start = prevEnd+1;
            int end = breakpoints.get(i);
            prevEnd = end;

            tiers.add(new QualityTier(names.get(i),start,end,colors.get(i)));
        }
        maxLevel= prevEnd;
    }

    public static QualityTier getQualityTier(int level){
        for(QualityTier qualityTier : tiers){
            if(qualityTier.getStartLevel() <= level && qualityTier.getEndLevel() >= level){
                return qualityTier;
            }
        }
        return NONE;
    }

    public static int getMaxLevel() {
        return maxLevel;
    }

    public static int tierCount(){
        return tiers.size();
    }
}
