package nuparu.sevendaystomine.world.item.quality;

public interface IQualityStack {
    void setQuality(int quality);
    int getQuality();
    QualityLevel getQualityLevel();

    boolean canHaveQuality();
}
