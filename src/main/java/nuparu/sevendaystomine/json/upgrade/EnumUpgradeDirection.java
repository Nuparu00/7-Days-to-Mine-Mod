package nuparu.sevendaystomine.json.upgrade;

public enum EnumUpgradeDirection {
    UPGRADE_ONLY,
    DOWNGRADE_ONLY,
    UPGRADE_AND_DOWNGRADE;

    public boolean isUpgrade(){
        return this == UPGRADE_ONLY || this == UPGRADE_AND_DOWNGRADE;
    }

    public boolean isDowngrade(){
        return this == DOWNGRADE_ONLY || this == UPGRADE_AND_DOWNGRADE;
    }
}
