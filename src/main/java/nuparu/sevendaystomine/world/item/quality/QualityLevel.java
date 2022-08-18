package nuparu.sevendaystomine.world.item.quality;

import net.minecraft.ChatFormatting;

public class QualityLevel {

    private String unlocalizedName;
    public int textColor = 0xffffff;
    private int startLevel;
    private int endLevel;

    public QualityLevel(String unlocalizedName, int startLevel, int endLevel, int color){
        this.unlocalizedName = unlocalizedName;
        this.startLevel = startLevel;
        this.endLevel = endLevel;
        this.textColor = color;
    }

    public String getUnlocalizedName(){
        return unlocalizedName;
    };


    public int getStartLevel() {
        return startLevel;
    }

    public int getEndLevel() {
        return endLevel;
    }

}
