package nuparu.sevendaystomine.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ServerConfig {
    public static ForgeConfigSpec.DoubleValue scrapCoefficient;
    public static ForgeConfigSpec.DoubleValue blockToughnessModifier;
    public static ForgeConfigSpec.BooleanValue thirst;
    public static ForgeConfigSpec.BooleanValue quality;
    public static ForgeConfigSpec.BooleanValue recipeBooksRequired;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> infectionStagesDuration;


    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> qualityTierBreakpoints;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> qualityTierNames;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> qualityTierColors;
    public static ForgeConfigSpec.DoubleValue xpPerQuality;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> disabledRecipes;
    public static ForgeConfigSpec.IntValue damageDecayRate;
    public static ForgeConfigSpec.IntValue torchBurnTime;
    public static ForgeConfigSpec.BooleanValue torchRainExtinguish;
    public static ForgeConfigSpec.IntValue bloodmoonFrequency;


    public static ForgeConfigSpec.BooleanValue zombiesBreakBlocks;
    public static ForgeConfigSpec.BooleanValue zombieCorpses;
    public static ForgeConfigSpec.IntValue corpseLifespan;

    public static void init(ForgeConfigSpec.Builder server) {
        scrapCoefficient = server.comment("Controls how much scrap you get from scrapping in inventory")
                .defineInRange("crafting.scrapCoefficient", 0.5, 0, 1);

        thirst = server.comment("Controls whether the thirst mechanic should be enabled")
                .define("player.thirst", true);

        quality = server.comment("Controls whether the quality mechanic should be enabled")
                .define("player.quality", true);

        infectionStagesDuration = server.comment("The duration of the individual infection stages").defineList(
                "player.infection_stages_duration", Arrays.asList(24000, 24000, 24000, 24000, 24000, 24000, 24000 ), it -> it instanceof Integer);

        qualityTierBreakpoints = server.comment("At what quality levels does the tier change").defineList(
                "player.quality_tier_breakpoints", Arrays.asList(100, 200, 300, 400, 500, 600 ), it -> it instanceof Integer);
        qualityTierNames = server.comment("The unlocalized names of the quality tiers. Do not edit this just to change the name - you can do that in the lang file. This is for adding/removing tiers. Should have the same amount of entries as quality_tier_breakpoints.").defineList(
                "player.quality_tier_names", Arrays.asList("faulty", "poor","good","fine","great","flawless"), it -> it instanceof String);
        qualityTierColors = server.comment("The RGB colors in decimal format of the quality tiers").defineList(
                "player.quality_tier_colors", Arrays.asList(0x9C8867,0xCF7F29,0xA2A41B,0x42C234,0x315DCE,0xA42ACC), it -> it instanceof Integer);
        recipeBooksRequired = server.comment("Do recipes have to be unlocked using the recipe books?")
                .define("player.recipeBooksRequired", true);
        xpPerQuality = server.comment("How many XP per 1 Quality point").defineInRange("player.xpPerQuality", 5, 0,
                Double.MAX_VALUE);

        blockToughnessModifier = server.comment("Affects how hard it is to mine blocks. Set to 1 for vanilla speeds.").defineInRange("player.blockToughnessModifier", 10, 0.00000000001,
                Double.MAX_VALUE);

        disabledRecipes = server.comment("The list of disabled crafting recipes. If you experience any problems regarding recipes, empty this list and create a dat pack that overrides the recipes instead.").defineList(
                "player.disabledRecipes", Arrays.asList("minecraft:oak_planks", "minecraft:birch_planks", "minecraft:spruce_planks",
                        "minecraft:jungle_planks", "minecraft:dark_oak_planks", "minecraft:acacia_planks", "minecraft:crimson_planks",
                        "minecraft:warped_planks", "minecraft:furnace", "minecraft:wooden_sword", "minecraft:wooden_shovel", "minecraft:wooden_pickaxe",
                        "minecraft:wooden_axe", "minecraft:wooden_hoe", "minecraft:stone_sword", "minecraft:stone_shovel", "minecraft:stone_pickaxe",
                        "minecraft:stone_axe", "minecraft:stone_hoe", "minecraft:iron_sword", "minecraft:golden_sword" , "minecraft:golden_shovel" , "minecraft:golden_axe" , "minecraft:golden_pickaxe", "minecraft:golden_hoe",  "minecraft:diamond_sword",
                        "minecraft:iron_shovel" , "minecraft:iron_axe" , "minecraft:iron_pickaxe", "minecraft:iron_hoe",
                        "minecraft:diamond_shovel", "minecraft:diamond_pickaxe", "minecraft:diamond_axe", "minecraft:diamond_hoe", "minecraft:diamond_helmet",
                        "minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots", "minecraft:iron_ingot",
                        "minecraft:iron_ingot_from_blasting", "minecraft:gold_ingot", "minecraft:gold_ingot_from_blasting" , "minecraft:piston"), it -> it instanceof String);


        damageDecayRate = server.comment("The rate of damaged blocks decay, how often does the decay update (12000 = every 12000 ticks - twice a day). Non-positive values disable the decay. Can be overridden by the damageDecayRate gamerule").defineInRange("world.damageDecayRate", 12000, -1,
                Integer.MAX_VALUE);

        torchBurnTime = server.comment("How many ticks until a torch burns out (-1 = infinity)").defineInRange("world.torchBurnTime", 22000, 0,
                Integer.MAX_VALUE);
        torchRainExtinguish = server.comment("Does rain extinguish burning torches?")
                .define("world.torchRainExtinguish", true);

        bloodmoonFrequency = server.comment("How many days between individual bloodmoons (0 = disabled)").defineInRange("hordes.bloodmoonFrequency", 7,
                0, Integer.MAX_VALUE);

        zombiesBreakBlocks = server.comment("Can zombies break blocks?")
                .define("mobs.zombiesBreakBlocks", true);

        corpseLifespan = server.comment("How many ticks until a corpse decays").defineInRange("mobs.corpseLifespan", 20000, 0,
                Integer.MAX_VALUE);
        zombieCorpses = server.comment("Do corpses spawn on zombies' death?")
                .define("mobs.zombieCorpses", true);
    }
}
