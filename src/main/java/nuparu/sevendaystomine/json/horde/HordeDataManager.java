package nuparu.sevendaystomine.json.horde;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.json.horde.number.Range;
import nuparu.sevendaystomine.json.horde.number.VariableNumber;
import nuparu.sevendaystomine.json.horde.pool.HordePool;
import nuparu.sevendaystomine.json.horde.time.BloodmoonTime;
import nuparu.sevendaystomine.json.horde.time.DailyTime;
import nuparu.sevendaystomine.json.horde.time.RandomTime;
import nuparu.sevendaystomine.json.horde.time.Time;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HordeDataManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final HordeDataManager INSTANCE = new HordeDataManager();

    private final HashMap<ResourceLocation, HordeEntry> hordes = new HashMap<>();

    private final List<HordeEntry> randomHordes = new ArrayList<>();


    public HordeDataManager() {
        super(GSON, "hordes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, @NotNull ResourceManager resourceManagerIn, @NotNull ProfilerFiller profilerIn) {
        hordes.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            JsonObject jo = je.getAsJsonObject();
            try {
                if(!jo.has("src") || !jo.get("src").getAsString().equals(SevenDaysToMine.MODID)) continue;
                boolean checkForActiveHorde = GsonHelper.getAsBoolean(jo,"checkForActiveHorde", true);
                Range<Integer> waves;
                int wavesDelay = 200;
                if(jo.has("waves")){
                    JsonElement wavesElement = jo.get("waves");
                    if(wavesElement.isJsonPrimitive()){
                        int wavesNumber = wavesElement.getAsInt();
                        waves = Range.of(wavesNumber);
                    }
                    else{
                        JsonObject wavesObject = wavesElement.getAsJsonObject();
                        if(wavesObject.has("value")){
                            waves = Range.of(wavesObject.get("value").getAsInt());
                        }
                        else {
                            if (!wavesObject.has("min") || !wavesObject.has("max")){
                                throw new RuntimeException("The horde entry defines \"waves\" as a object, but the object does not contain the \"min\" or \"max\" values or the \"value\" value");
                            }/*
                            JsonObject minObject = wavesObject.getAsJsonObject("min");
                            JsonObject maxObject = wavesObject.getAsJsonObject("max");
                            waves = new Range<>(VariableNumber.parseInt(minObject), VariableNumber.parseInt(maxObject));*/
                            JsonElement min = wavesObject.get("min");
                            JsonElement max = wavesObject.get("max");
                            waves = new Range<>(min.isJsonPrimitive() ? VariableNumber.of(min.getAsInt()) : VariableNumber.parseInt(min.getAsJsonObject()),
                                    max.isJsonPrimitive() ? VariableNumber.of(max.getAsInt()) : VariableNumber.parseInt(max.getAsJsonObject()));
                        }
                        wavesDelay = GsonHelper.getAsInt(wavesObject,"delay", 200);
                    }
                }
                else{
                    throw new RuntimeException("The horde entry is missing the \"waves\" value");
                }

                HordeEntry.Trigger trigger = null;
                if(jo.has("trigger")){
                    JsonObject triggerJson = jo.get("trigger").getAsJsonObject();
                    boolean global = GsonHelper.getAsBoolean(triggerJson,"global", false);
                    int lifespan = GsonHelper.getAsInt(triggerJson,"lifespan", Integer.MAX_VALUE);
                    boolean despawnEntities = GsonHelper.getAsBoolean(triggerJson,"despawnEntities", false);
                    SoundEvent soundEvent = null;
                    Time time = null;

                    if(triggerJson.has("sound")){
                        String sound = triggerJson.get("sound").getAsString();
                        soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(sound));
                        if(soundEvent == null){
                            SevenDaysToMine.LOGGER.warn("The trigger sound " + sound + " of horde (" + key.toString() + ") does not exist.");
                        }
                    }
                    if(triggerJson.has("time")){
                        JsonObject timeJson = triggerJson.get("time").getAsJsonObject();
                        String type = GsonHelper.getAsString(timeJson,"type", "daily");
                        time = parseTime(type,timeJson);
                    }
                    trigger = new HordeEntry.Trigger(time,global,soundEvent,lifespan,despawnEntities);
                }

                List<HordePool> pools = parsePools(jo.getAsJsonArray("pools"));

                List<HordePool> startPools = pools.stream().filter(hordePool -> hordePool.group().equals("start")).toList();
                List<HordePool> endPools = pools.stream().filter(hordePool -> hordePool.group().equals("end")).toList();
                List<HordePool> normalPools = pools.stream().filter(hordePool -> !startPools.contains(hordePool) && !endPools.contains(hordePool)).toList();

                AdvancementRewards rewards = null;

                if(jo.has("rewards")){
                    rewards = AdvancementRewards.deserialize(jo.getAsJsonObject("rewards"));
                }

                HordeEntry hordeEntry = new HordeEntry(key,checkForActiveHorde,trigger,waves, wavesDelay,startPools, normalPools, endPools, rewards);
                hordes.put(key,hordeEntry);
                if(trigger.time() instanceof RandomTime){
                    randomHordes.add(hordeEntry);
                }
            }
            catch (Exception e){
                SevenDaysToMine.LOGGER.error("An error occurred while trying to load horde (" + key.toString() + ") :" + e.getMessage());
            }
        }
    }

    public Set<ResourceLocation> getHordeNames(){
        return hordes.keySet();
    }

    public Optional<HordeEntry> getByRes(ResourceLocation resourceLocation){
        return Optional.of(hordes.get(resourceLocation));
    }

    public static List<HordePool> parsePools(JsonArray poolObjects) throws CommandSyntaxException {
        List<HordePool> pools = new ArrayList<>();
        for(JsonElement je : poolObjects){
            JsonObject jsonObject = je.getAsJsonObject();
            String group = GsonHelper.getAsString(jsonObject,"group","normal");
            String sound = GsonHelper.getAsString(jsonObject,"sound","");
            Range<Integer> rolls = jsonObject.has("rolls") ? getRangedInt(jsonObject.get("rolls")) : Range.of(1);
            Range<Integer> weight = jsonObject.has("weight") ? getRangedInt(jsonObject.get("weight")) : Range.of(1);
            JsonArray entriesArray = jsonObject.getAsJsonArray("entries");
            List<HordePool.Entry> entries = new ArrayList<>();

            for(JsonElement entryElement : entriesArray){
                JsonObject entryObject = entryElement.getAsJsonObject();
                int entryWeight = GsonHelper.getAsInt(entryObject,"weight",1);
                String name = GsonHelper.getAsString(entryObject,"name","");
                String nbtString = GsonHelper.getAsString(entryObject,"nbt","");
                EntityType<?> type = name.isEmpty() ? null : ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(name));
                CompoundTag nbt = nbtString.isEmpty() ? null : TagParser.parseTag(nbtString);
                entries.add(new HordePool.Entry(entryWeight,type,nbt));
            }
            pools.add(new HordePool(group,sound.isEmpty() ? null : ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(sound)),rolls,weight,entries));

        }

        return pools;
    }

    public static Range<Integer> getRangedInt(JsonElement element){
        if(element.isJsonPrimitive()){
            return Range.of(element.getAsInt());
        }
        JsonObject object = element.getAsJsonObject();
        JsonElement min = object.get("min");
        JsonElement max = object.get("max");
        return new Range<>(min.isJsonPrimitive() ? VariableNumber.of(min.getAsInt()) : VariableNumber.parseInt(min.getAsJsonObject()),
                max.isJsonPrimitive() ? VariableNumber.of(max.getAsInt()) : VariableNumber.parseInt(max.getAsJsonObject()));
    }

    /*private static Range<Integer> parseIntRange(JsonElement rangeElement){
        if(rangeElement.isJsonPrimitive()){
            int wavesNumber = rangeElement.getAsInt();
            return Range.of(wavesNumber);
        }
        else{
            JsonObject rangeObject = rangeElement.getAsJsonObject();
            if (!rangeObject.has("min") || !rangeObject.has("max")){
                throw new RuntimeException("The horde entry defines a range object, but the object does not contain the \"min\" or \"max\" value");
            }
            JsonObject minObject = rangeObject.getAsJsonObject("min");
        }
        return null;
    }

    private static VariableNumber<Integer> parseVariableInt(JsonElement variableIntElement){
        if(variableIntElement.isJsonPrimitive()){
            int wavesNumber = variableIntElement.getAsInt();
            return VariableNumber.of(wavesNumber);
        }
        else{
            JsonObject variableIntObject = variableIntElement.getAsJsonObject();
            if (!variableIntObject.has("base")){
                throw new RuntimeException("The horde entry defines a variable number object, but the object does not contain the \"base\" value");
            }
            int base = variableIntObject.get("base").getAsInt();
            String operation = GsonHelper.getAsString(variableIntObject,"operation", "add").toLowerCase();

        }
    }*/

    private static Time parseTime(String type, JsonObject timeJson){
        return switch (type){
            case "bloodmoon" -> new BloodmoonTime(GsonHelper.getAsInt(timeJson,"start", 16000));
            case "daily" -> new DailyTime(GsonHelper.getAsInt(timeJson,"day", 5), GsonHelper.getAsInt(timeJson,"start", 16000));
            case "random" -> new RandomTime(GsonHelper.getAsFloat(timeJson,"chance", 0.00004166666f));
            default -> null;
        };
    }

    public List<HordeEntry> getRandomHordes() {
        return randomHordes;
    }
}
