package nuparu.sevendaystomine.json.scrap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrapDataManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final ScrapDataManager INSTANCE = new ScrapDataManager();

    private final List<ScrapEntry> scraps = new ArrayList<>();
    private final HashMap<EnumMaterial,ScrapEntry> scrapBitsMap = new HashMap<>();

    public ScrapDataManager() {
        super(GSON, "scraps");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        scraps.clear();
        scrapBitsMap.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            JsonObject jo = je.getAsJsonObject();
            try {
                if(!jo.has("src") || !jo.get("src").getAsString().equals(SevenDaysToMine.MODID)) continue;
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jo.get("item").getAsString()));
                EnumMaterial material = EnumMaterial.byName(jo.get("material").getAsString());
                if(material == null){
                    throw new NullPointerException("Invalid or null material: " + jo.get("material"));
                }
                String weight = jo.get("weight").getAsString();
                Number weightValue;
                if(weight.contains("/")){
                    weightValue = Fraction.getFraction(weight);
                }
                else{
                    weightValue = jo.get("weight").getAsDouble();
                }

                boolean canBeScrapped = jo.get("canBeScrapped").getAsBoolean();
                boolean isScrapBit = jo.get("isScrapBit").getAsBoolean();
                boolean excludeFromMin = false;
                if(jo.has("excludeFromMin")) {
                    excludeFromMin = jo.get("excludeFromMin").getAsBoolean();
                }
                ScrapEntry scrapEntry = new ScrapEntry(key,item,material,new WeightWrapper(weightValue),canBeScrapped,isScrapBit,excludeFromMin);
                scraps.add(scrapEntry);
                if(isScrapBit){
                    scrapBitsMap.put(material,scrapEntry);
                }
            }
            catch (NullPointerException e){
                SevenDaysToMine.LOGGER.error("An error occurred while trying to load scrap (" + key.toString() + ") :" + e.getMessage());
            }
        }
    }
    public boolean hasEntry(Item item){
        for(ScrapEntry entry : scraps){
            if(entry.item == item){
                return true;
            }
        }
        return false;
    }

    public ScrapEntry getEntry(Item item){
        for(ScrapEntry entry : scraps){
            if(entry.item == item){
                return entry;
            }
        }
        return null;
    }

    public boolean hasEntry(ItemStack stack){
        return hasEntry(stack.getItem());
    }

    public ScrapEntry getEntry(ItemStack stack){
        return getEntry(stack.getItem());
    }

    public boolean hasScrapResult(EnumMaterial material){
        return scrapBitsMap.containsKey(material);
    }

    public ScrapEntry getScrapResult(EnumMaterial material){
        if(hasScrapResult(material)){
            return scrapBitsMap.get(material);
        }
        return null;
    }

}
