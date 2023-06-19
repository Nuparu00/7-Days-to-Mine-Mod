package nuparu.sevendaystomine.world.item.quality;

public class QualityTier {

    private final String unlocalizedName;
    public final int textColor;
    private final int startLevel;
    private final int endLevel;

    public QualityTier(String unlocalizedName, int startLevel, int endLevel, int color){
        this.unlocalizedName = unlocalizedName;
        this.startLevel = startLevel;
        this.endLevel = endLevel;
        this.textColor = color;
    }

    public String getUnlocalizedName(){
        return unlocalizedName;
    }


    public int getStartLevel() {
        return startLevel;
    }

    public int getEndLevel() {
        return endLevel;
    }

}
