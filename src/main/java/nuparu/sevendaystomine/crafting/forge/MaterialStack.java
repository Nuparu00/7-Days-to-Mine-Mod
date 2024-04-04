package nuparu.sevendaystomine.crafting.forge;

import nuparu.sevendaystomine.crafting.scrap.WeightWrapper;
import nuparu.sevendaystomine.item.EnumMaterial;

public class MaterialStack{
    public final EnumMaterial material;
    public final WeightWrapper weight;
    public static final MaterialStack EMPTY = new MaterialStack(EnumMaterial.NONE,0);


    public MaterialStack(EnumMaterial material, WeightWrapper weight) {
        this.material = material;
        this.weight = weight;
    }
    public MaterialStack(EnumMaterial material) {
        this(material,1);
    }
    public MaterialStack(EnumMaterial material, Number weight) {
        this(material,new WeightWrapper(weight));
    }

    public EnumMaterial getMaterial(){
        return material;
    }

    public WeightWrapper getWeight(){
        return weight;
    }

    public EnumMaterial material() {
        return this.material;
    }

    public WeightWrapper weight() {
        return this.weight;
    }
}
