package nuparu.sevendaystomine.json.scrap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import nuparu.sevendaystomine.world.item.crafting.forge.MaterialStack;
import org.apache.commons.lang3.math.Fraction;

public class ScrapEntry {
    public ResourceLocation name;
    public Item item;
    public EnumMaterial material;
    public WeightWrapper weight;
    public boolean canBeScrapped;
    //Scrap bit is the smallest unit of the material and the result of scrapping of other items
    public boolean isScrapBit;
    public boolean excludeFromMin;

    public ScrapEntry(ResourceLocation name, Item item, EnumMaterial material, WeightWrapper weight, boolean canBeScrapped, boolean isScrapBit, boolean excludeFromMin){
        this.name = name;
        this.item = item;
        this.material = material;
        this.weight = weight;
        this.canBeScrapped = canBeScrapped;
        this.isScrapBit = isScrapBit;
        this.excludeFromMin = excludeFromMin;
    }

    public CompoundTag save(CompoundTag nbt){
        if(material == null) {
            return null;
        }
        nbt.putString("name",name.toString());
        nbt.putString("item",ForgeRegistries.ITEMS.getKey(item).toString());
        nbt.putString("material", material.getRegistryName());
        if(weight.isFraction()){
            Fraction fraction = weight.asFraction();
            nbt.putString("weight", fraction.toProperString());
        }
        else {
            nbt.putDouble("weight", weight.asDouble());
        }
        nbt.putBoolean("canBeScrapped", canBeScrapped);
        nbt.putBoolean("isScrapBit", isScrapBit);
        nbt.putBoolean("excludeFromMin", excludeFromMin);
        return nbt;
    }

    public static ScrapEntry of(CompoundTag nbt){
        if(!nbt.contains("name", Tag.TAG_STRING)) return null;
        if(!nbt.contains("item", Tag.TAG_STRING)) return null;
        if(!nbt.contains("material", Tag.TAG_STRING)) return null;
        if(!nbt.contains("weight",Tag.TAG_STRING) && !nbt.contains("weight",Tag.TAG_DOUBLE)) return null;
        if(!nbt.contains("canBeScrapped", Tag.TAG_BYTE)) return null;
        if(!nbt.contains("isScrapBit", Tag.TAG_BYTE)) return null;
        if(!nbt.contains("excludeFromMin", Tag.TAG_BYTE)) return null;

        ResourceLocation name = new ResourceLocation(nbt.getString("name"));
        Item item =  ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("item")));
        EnumMaterial material =  EnumMaterial.byName(nbt.getString("material"));
        Number weight;
        if(nbt.contains("weight",Tag.TAG_STRING)){
            weight = Fraction.getFraction(nbt.getString("weight"));
        }
        else{
            weight = nbt.getDouble("weight");
        }
        boolean canBeScrapped = nbt.getBoolean("canBeScrapped");
        boolean isScrapBit = nbt.getBoolean("isScrapBit");
        boolean excludeFromMin = nbt.getBoolean("excludeFromMin");

        return new ScrapEntry(name,item,material,new WeightWrapper(weight),canBeScrapped,isScrapBit,excludeFromMin);
    }

    public MaterialStack toMaterialStack(){
        return new MaterialStack(material,weight);
    }
}