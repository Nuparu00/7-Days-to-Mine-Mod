package nuparu.sevendaystomine.item;

import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.Optional;

public enum EnumMaterial {
    NONE("none"), CARBON("carbon"), IRON("iron"), BRASS("brass"),
    LEAD("lead"), STEEL("steel"), COPPER("copper"),
    BRONZE("bronze"), TIN("tin"), ZINC("zinc"), GOLD("gold"),
    WOLFRAM("wolfram"), URANIUM("uranium"), WOOD("wood"),
    STONE("stone"), GLASS("glass"), CLOTH("cloth"), PLANT_FIBER("plant_fiber"),
    PLASTIC("plastic"), CLAY("clay"),
    MERCURY("mercury"), POTASSIUM("potassium"), CONCRETE("concrete"),
    LEATHER("leather"), GASOLINE("gasoline"), SAND("sand"), PAPER("paper");

    String unlocalizedName;

    EnumMaterial(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
    }

    public String getUnlocalizedName() {
        return "material." + this.unlocalizedName;
    }

    public String getLocalizedName() {
        return SevenDaysToMine.proxy.localize(getUnlocalizedName());
    }

    public static Optional<EnumMaterial> byName(String name){
        for (EnumMaterial material : values()){
            if(material.name().equalsIgnoreCase(name)){
                return Optional.of(material);
            }
        }
        return Optional.empty();
    }

}