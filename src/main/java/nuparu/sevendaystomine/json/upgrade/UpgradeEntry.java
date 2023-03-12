package nuparu.sevendaystomine.json.upgrade;

import net.minecraft.sounds.SoundEvent;
import nuparu.sevendaystomine.json.IngredientStack;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class UpgradeEntry {
    public BlockUpgradeData from;
    public BlockUpgradeData to;

    public ArrayList<String> copy;
    public ArrayList<IngredientStack> ingredients;

    public EnumUpgradeDirection direction;
    public SoundEvent sound;

    public UpgradeEntry(BlockUpgradeData from, BlockUpgradeData to, ArrayList<String> copy, ArrayList<IngredientStack> ingredients, EnumUpgradeDirection direction, SoundEvent sound){
        this.from = from;
        this.to = to;
        this.copy = copy;
        this.ingredients = ingredients;
        this.direction = direction;
        this.sound = sound;
    }

    //Returns the result of downgrading - assuming there is one
    @Nullable
    public BlockUpgradeData getDowngradeTo(){
        switch (direction){
            default :
            case UPGRADE_ONLY: return null;
            case DOWNGRADE_ONLY: return to;
            case UPGRADE_AND_DOWNGRADE: return from;
        }
    }

    @Nullable
    public BlockUpgradeData getUpgradeTo(){
        switch (direction){
            default :
            case DOWNGRADE_ONLY: return null;
            case UPGRADE_ONLY:
            case UPGRADE_AND_DOWNGRADE: return to;
        }
    }

    @Nullable
    public BlockUpgradeData getDowngradeFrom(){
        switch (direction){
            default :
            case UPGRADE_ONLY: return null;
            case DOWNGRADE_ONLY: return from;
            case UPGRADE_AND_DOWNGRADE: return to;
        }
    }

    @Nullable
    public BlockUpgradeData getUpgradeFrom(){
        switch (direction){
            default :
            case DOWNGRADE_ONLY: return null;
            case UPGRADE_ONLY:
            case UPGRADE_AND_DOWNGRADE: return from;
        }
    }
}
