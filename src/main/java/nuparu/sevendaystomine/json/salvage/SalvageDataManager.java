package nuparu.sevendaystomine.json.salvage;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.json.IngredientStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalvageDataManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final SalvageDataManager INSTANCE = new SalvageDataManager();

    private final List<SalvageEntry> entries = new ArrayList<>();
    public SalvageDataManager() {
        super(GSON, "salvage");
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
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(jo.get("block").getAsString()));
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

                        IngredientStack ingredientStack = new IngredientStack(ingredient,count);

                        if(object.has("chance")){
                            ingredientStack.withChance(object.get("chance").getAsDouble());
                        }
                        ingredients.add(ingredientStack);
                    }
                }

                entries.add(new SalvageEntry(block,ingredients,upgradeSound));

            }
            catch (NullPointerException e){
                SevenDaysToMine.LOGGER.error("An error occurred while trying to load salvage (" + key.toString() + ") :" + e.getMessage());
            }
        }
    }

    @Nullable
    public SalvageEntry getSalvageForBlock(Block block){
        for(SalvageEntry entry : entries){
            if(entry.block == block){
                return entry;
            }
        }
        return null;
    }
}
