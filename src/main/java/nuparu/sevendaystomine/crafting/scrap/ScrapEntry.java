package nuparu.sevendaystomine.crafting.scrap;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.crafting.forge.MaterialStack;
import nuparu.sevendaystomine.item.EnumMaterial;

public class ScrapEntry {
    private final ResourceLocation name;
    private final Item item;
    private final EnumMaterial material;
    private final WeightWrapper weight;
    private final boolean canBeScrapped;
    private final boolean isScrapBit;
    private final boolean excludeFromMin;
    public ScrapEntry(ResourceLocation name, Item item, EnumMaterial material, WeightWrapper weight, boolean canBeScrapped, boolean isScrapBit, boolean excludeFromMin){
        this.name = name;
        this.item = item;
        this.material = material;
        this.weight = weight;
        this.canBeScrapped = canBeScrapped;
        this.isScrapBit = isScrapBit;
        this.excludeFromMin = excludeFromMin;
    }

    public ResourceLocation name() {
        return name;
    }

    public Item item() {
        return item;
    }

    public EnumMaterial material() {
        return material;
    }

    public WeightWrapper weight() {
        return weight;
    }

    public boolean canBeScrapped() {
        return canBeScrapped;
    }

    public boolean isScrapBit() {
        return isScrapBit;
    }

    public boolean excludeFromMin() {
        return excludeFromMin;
    }

    public MaterialStack toMaterialStack() {
        return new MaterialStack(material, weight.asNumber());
    }
}
