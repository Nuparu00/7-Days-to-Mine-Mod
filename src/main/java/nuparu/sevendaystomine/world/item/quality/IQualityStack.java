package nuparu.sevendaystomine.world.item.quality;

public interface IQualityStack {
    /**
     * Sets the quality of the stack
     * @param quality quality value
     */
    void setQuality(int quality);

    /**
     * @return the quality value of the stack (the number, not the tier)
     */
    int getQuality();

    /**
     * @return how "close" the quality value of this stack is towards the maximal possible quality value
     */
    default double getRelativeQuality(){
        return ((getQuality() - 1) / (double) (QualityManager.getMaxLevel() - 1));
    }

    /**
     * @return {@link #getRelativeQuality()} scaled by the number of {@link QualityTier}s in {@link QualityManager}
     */
    default double getRelativeQualityScaled(){
        return getRelativeQuality() * (QualityManager.tierCount() - 1);
    }


    /**
     * @return the quality level of the stack (the tier, not the number)
     */
    default QualityTier getQualityLevel() {
        return QualityManager.getQualityTier(getQuality());
    }

    /**
     * @return Can this stack have the quality "attribute"?
     */
    boolean canHaveQuality();

    /**
     * @return Does this stack have the quality "attribute"?
     */
    boolean hasQuality();
}
