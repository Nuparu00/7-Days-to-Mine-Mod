package nuparu.sevendaystomine.json.salvage;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import nuparu.sevendaystomine.json.IngredientStack;

import java.util.ArrayList;

public record SalvageEntry (Block block, ArrayList<IngredientStack> ingredients, SoundEvent sound){

}
