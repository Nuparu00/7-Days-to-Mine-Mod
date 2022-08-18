package nuparu.sevendaystomine.json;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientStack {

    public Ingredient ingredient;
    public int count;
    public double chance = 1;
    public IngredientStack(Ingredient ingredient, int count){
        this.ingredient = ingredient;
        this.count = count;
    }

    public IngredientStack withChance(double chance){
        this.chance = chance;
        return this;
    }

    public Ingredient ingredient(){
        return ingredient;
    }

    public int count(){
        return count;
    }
    public double chance(){
        return chance;
    }
    public boolean test(ItemStack stack){
        return ingredient().test(stack);
    }

    public IngredientStack clone(){
        return new IngredientStack(ingredient(),count());
    }
}