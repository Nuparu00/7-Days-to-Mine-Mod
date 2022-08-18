package nuparu.sevendaystomine.json.salvage;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import nuparu.sevendaystomine.json.IngredientStack;
import nuparu.sevendaystomine.json.upgrade.EnumUpgradeDirection;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class SalvageEntry {
    public Block block;

    public ArrayList<IngredientStack> ingredients;
    public SoundEvent sound;

    public SalvageEntry(Block block, ArrayList<IngredientStack> ingredients, SoundEvent sound){
        this.block = block;
        this.ingredients = ingredients;
        this.sound = sound;
    }

}
