package nuparu.sevendaystomine.json.upgrader;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.HashMap;
import java.util.Map;

public class UpgraderToolDataManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final UpgraderToolDataManager INSTANCE = new UpgraderToolDataManager();

    public HashMap<Item,UpgraderToolEntry> upgraders = new HashMap<>();
    public UpgraderToolDataManager() {
        super(GSON, "upgraders");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        upgraders.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            JsonObject jo = je.getAsJsonObject();
            try {
                if(!jo.has("src") || !jo.get("src").getAsString().equals(SevenDaysToMine.MODID)) continue;
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jo.get("item").getAsString()));
                double upgradePower = jo.has("upgradePower") ? jo.get("upgradePower").getAsDouble() : -1;
                double salvagePower = jo.has("salvagePower") ? jo.get("salvagePower").getAsDouble() : -1;
                upgraders.put(item,new UpgraderToolEntry(item,upgradePower,salvagePower));
            }
            catch (NullPointerException e){
                SevenDaysToMine.LOGGER.error("An error occurred while trying to load upgrader (" + key.toString() + ") :" + e.getMessage());
            }
        }
    }

    public double getItemUpgradePower(Item item){
        return upgraders.containsKey(item) ? upgraders.get(item).upgradePower() : -1;
    }
    public double getItemSalvagePower(Item item){
        return upgraders.containsKey(item) ? upgraders.get(item).salvagePower() : -1;
    }

}
