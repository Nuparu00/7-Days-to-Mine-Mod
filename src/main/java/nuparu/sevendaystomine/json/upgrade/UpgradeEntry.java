package nuparu.sevendaystomine.json.upgrade;

import net.minecraft.sounds.SoundEvent;
import nuparu.sevendaystomine.json.IngredientStack;

import javax.annotation.Nullable;
import java.util.ArrayList;

public record UpgradeEntry(BlockUpgradeData from, BlockUpgradeData to, ArrayList<String> copy, ArrayList<IngredientStack> ingredients, EnumUpgradeDirection direction, SoundEvent sound){
    //Returns the result of downgrading - assuming there is one
    @Nullable
    public BlockUpgradeData getDowngradeTo(){
        return switch (direction) {
            case UPGRADE_ONLY -> null;
            case DOWNGRADE_ONLY -> to;
            case UPGRADE_AND_DOWNGRADE -> from;
        };
    }

    @Nullable
    public BlockUpgradeData getUpgradeTo(){
        return switch (direction) {
            case DOWNGRADE_ONLY -> null;
            case UPGRADE_ONLY, UPGRADE_AND_DOWNGRADE -> to;
        };
    }

    @Nullable
    public BlockUpgradeData getDowngradeFrom(){
        return switch (direction) {
            case UPGRADE_ONLY -> null;
            case DOWNGRADE_ONLY -> from;
            case UPGRADE_AND_DOWNGRADE -> to;
        };
    }

    @Nullable
    public BlockUpgradeData getUpgradeFrom(){
        return switch (direction) {
            case DOWNGRADE_ONLY -> null;
            case UPGRADE_ONLY, UPGRADE_AND_DOWNGRADE -> from;
        };
    }
}
