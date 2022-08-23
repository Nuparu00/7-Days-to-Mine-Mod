package nuparu.sevendaystomine.json.drink;

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
import nuparu.sevendaystomine.json.scrap.ScrapEntry;
import nuparu.sevendaystomine.json.scrap.WeightWrapper;
import nuparu.sevendaystomine.world.item.EnumMaterial;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrinkDataManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final DrinkDataManager INSTANCE = new DrinkDataManager();

    private final HashMap<Item,DrinkEntry> drinkData = new HashMap<>();

    public DrinkDataManager() {
        super(GSON, "drinks");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        drinkData.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            JsonObject jo = je.getAsJsonObject();
            try {
                if(!jo.has("src") || !jo.get("src").getAsString().equals(SevenDaysToMine.MODID)) continue;
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jo.get("item").getAsString()));
                int amount = jo.get("amount").getAsInt();
                double dirtiness = 0d;
                if(jo.has("dirtiness")){
                    dirtiness = jo.get("dirtiness").getAsDouble();
                }
                boolean alcoholic = false;
                if(jo.has("alcoholic")){
                    alcoholic = jo.get("alcoholic").getAsBoolean();
                }
                int caffeineBuzzDuration = 0;
                int caffeineBuzzAmplifier = 0;

                if(jo.has("caffeineBuzzDuration")){
                    caffeineBuzzDuration = Math.abs(jo.get("caffeineBuzzDuration").getAsInt());
                }

                if(jo.has("caffeineBuzzAmplifier")){
                    caffeineBuzzAmplifier = Math.abs(jo.get("caffeineBuzzAmplifier").getAsInt());
                }

                boolean tea = false;
                if(jo.has("tea")){
                    tea = jo.get("tea").getAsBoolean();
                }
                drinkData.put(item,new DrinkEntry(amount,dirtiness,alcoholic,caffeineBuzzDuration,caffeineBuzzAmplifier,tea));
            }
            catch (NullPointerException e){
                SevenDaysToMine.LOGGER.error("An error occurred while trying to load drink (" + key.toString() + ") :" + e.getMessage());
            }
        }
    }

    public boolean hasDrinkData(Item item){
        return drinkData.containsKey(item);
    }

    public DrinkEntry getDrinkData(Item item){
        return drinkData.get(item);
    }
}
