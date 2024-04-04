package nuparu.sevendaystomine.integration.crafttweaker.forge;

import crafttweaker.annotations.ZenRegister;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.forge.MaterialStack;
import nuparu.sevendaystomine.item.EnumMaterial;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenProperty;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenClass("mods." + SevenDaysToMine.MODID + ".recipe.MaterialStack")
@ZenRegister
public class CraftTweakerMaterialStack {

    private final MaterialStack materialStack;

    public CraftTweakerMaterialStack(EnumMaterial material, Number weight){
        this.materialStack = new MaterialStack(material, weight);
    }

    @ZenGetter("material")
    public String material(){
        return materialStack.material().name();
    }

    @ZenGetter("weight")
    public Number weight(){
        return materialStack.weight().asNumber();
    }

    public MaterialStack materialStack(){
        return materialStack;
    }

}
