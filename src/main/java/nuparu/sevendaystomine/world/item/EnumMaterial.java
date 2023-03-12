package nuparu.sevendaystomine.world.item;

import net.minecraft.client.resources.language.I18n;

public enum EnumMaterial {
    NONE("none"), CARBON("carbon"), IRON("iron"), BRASS("brass"),
    LEAD("lead"), STEEL("steel"), COPPER("copper"),
    BRONZE("bronze"), TIN("tin"), ZINC("zinc"), GOLD("gold"),
    WOLFRAM("wolfram"), URANIUM("uranium"), WOOD("wood"),
    STONE("stone"), GLASS("glass"), CLOTH("cloth"), PLANT_FIBER("plant_fiber"),
    PLASTIC("plastic"), CLAY("clay"), MERCURY("mercury"), POTASSIUM("potassium"),
    CONCRETE("concrete"), LEATHER("leather"), GASOLINE("gasoline"),
    SAND("sand"), PAPER("paper"), STRING("string"), BONE("bone"), NETHERITE("netherite");

    final String name;

    EnumMaterial(String name) {
        this.name = name;
    }

    public String getRegistryName() {
        return this.name;
    }

    public String getUnlocalizedName() {
        return "material." + this.name;
    }

    public String getLocalizedName() {
        return I18n.get(getUnlocalizedName());
    }

    public static EnumMaterial byName(String name){
        for(EnumMaterial material : values()){
            if(material.name.equals(name)){
                return material;
            }
        }
        return null;
    }

}