package nuparu.sevendaystomine.world.item.crafting.forge;

import net.minecraft.network.FriendlyByteBuf;
import nuparu.sevendaystomine.json.scrap.WeightWrapper;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import org.apache.commons.lang3.math.Fraction;

public record MaterialStack(EnumMaterial material, WeightWrapper weight){

    public static final MaterialStack EMPTY = new MaterialStack(EnumMaterial.NONE,0);


    public MaterialStack(EnumMaterial material) {
        this(material,1);
    }
    public MaterialStack(EnumMaterial material, Number weight) {
        this(material,new WeightWrapper(weight));
    }

    public static MaterialStack fromNetwork(FriendlyByteBuf buffer) {
        String matName = buffer.readUtf();
        EnumMaterial material = EnumMaterial.byName(matName);
        if(material == EnumMaterial.NONE) return EMPTY;
        String weight = buffer.readUtf();
        Number weightValue;
        if(weight.contains("/")){
            weightValue = Fraction.getFraction(weight);
        }
        else{
            weightValue = Double.valueOf(weight);
        }
        return new MaterialStack(material,new WeightWrapper(weightValue));
    }

    public final void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeUtf(material.getRegistryName());
        buffer.writeUtf(weight.toString());
    }

    public EnumMaterial getMaterial(){
        return material;
    }

    public WeightWrapper getWeight(){
        return weight;
    }
}
