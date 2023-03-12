package nuparu.sevendaystomine.json.upgrade;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.json.IngredientStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradeDataManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final UpgradeDataManager INSTANCE = new UpgradeDataManager();

    private final List<UpgradeEntry> entries = new ArrayList<>();
    public UpgradeDataManager() {
        super(GSON, "upgrades");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, @NotNull ResourceManager resourceManagerIn, @NotNull ProfilerFiller profilerIn) {
        entries.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            JsonObject jo = je.getAsJsonObject();
            try {
                if(!jo.has("src") || !jo.get("src").getAsString().equals(SevenDaysToMine.MODID)) continue;
                Block fromBlock;
                HashMap<String, String> fromPropeties = new HashMap<>();

                if(jo.get("from").isJsonPrimitive()){
                    fromBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(jo.get("from").getAsString()));
                }
                else{
                    JsonObject from = jo.get("from").getAsJsonObject();
                    fromBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(from.get("block").getAsString()));
                    if(from.has("properties")){
                        JsonArray properties = from.get("properties").getAsJsonArray();
                        StateDefinition<Block, BlockState> stateDefinition = fromBlock.getStateDefinition();
                        for(JsonElement propertyElement : properties){
                            JsonObject property = propertyElement.getAsJsonObject();
                            String name = property.get("name").getAsString();
                            String value = property.get("value").getAsString();

                            fromPropeties.put(name,value);
                        }
                    }
                }
                BlockUpgradeData from = new BlockUpgradeData(fromBlock,fromPropeties);


                Block toBlock;
                HashMap<String, String> toProperties = new HashMap<>();

                if(jo.get("to").isJsonPrimitive()){
                    toBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(jo.get("to").getAsString()));
                }
                else{
                    JsonObject to = jo.get("to").getAsJsonObject();
                    toBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(to.get("block").getAsString()));
                    if(to.has("properties")){
                        JsonArray properties = to.get("properties").getAsJsonArray();
                        StateDefinition<Block, BlockState> stateDefinition = toBlock.getStateDefinition();
                        for(JsonElement propertyElement : properties){
                            JsonObject property = propertyElement.getAsJsonObject();
                            String name = property.get("name").getAsString();
                            String value = property.get("value").getAsString();

                            toProperties.put(name,value);
                        }
                    }
                }
                BlockUpgradeData to = new BlockUpgradeData(toBlock,toProperties);

                ArrayList<String> copy = new ArrayList<>();

                if(jo.has("copy")){
                    for(JsonElement element : jo.get("copy").getAsJsonArray()){
                        copy.add(element.getAsString());
                    }
                }


                EnumUpgradeDirection direction = EnumUpgradeDirection.UPGRADE_ONLY;
                if(jo.has("direction")){
                    direction = EnumUpgradeDirection.valueOf(jo.get("direction").getAsString().toUpperCase());
                }
                SoundEvent upgradeSound = null;
                if(jo.has("sound")) {
                    upgradeSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(jo.get("sound").getAsString()));
                }

                ArrayList<IngredientStack> ingredients = new ArrayList<>();
                if(jo.has("ingredients")){
                    JsonArray ingredientsArr = jo.getAsJsonArray("ingredients");
                    for(JsonElement element : ingredientsArr){
                        JsonObject object = element.getAsJsonObject();

                        Ingredient ingredient = Ingredient.fromJson(object.get("stack"));
                        int count = object.get("count").getAsInt();
                        ingredients.add(new IngredientStack(ingredient,count));
                    }
                }

                entries.add(new UpgradeEntry(from,to,copy,ingredients,direction,upgradeSound));

            }
            catch (NullPointerException e){
                SevenDaysToMine.LOGGER.error("An error occurred while trying to load upgrade (" + key.toString() + ") :" + e.getMessage());
            }
        }
    }

    @Nullable
    public UpgradeEntry getUpgradeFromEntry(BlockState state){
        for(UpgradeEntry entry : entries){
            if(entry.getUpgradeFrom() != null && entry.getUpgradeFrom().test(state)) return entry;
        }
        return null;
    }

    @Nullable
    public UpgradeEntry getDowngradeFromEntry(BlockState state){
        for(UpgradeEntry entry : entries){
            if(entry.getDowngradeFrom() != null && entry.getDowngradeFrom().test(state)) return entry;
        }
        return null;
    }

    @Nullable
    public UpgradeEntry getUpgradeToEntry(BlockState state){
        for(UpgradeEntry entry : entries){
            if(entry.getUpgradeTo() != null && entry.getUpgradeTo().test(state)) return entry;
        }
        return null;
    }

    @Nullable
    public UpgradeEntry getDowngradeToEntry(BlockState state){
        for(UpgradeEntry entry : entries){
            if(entry.getDowngradeTo() != null && entry.getDowngradeTo().test(state)) return entry;
        }
        return null;
    }

}
