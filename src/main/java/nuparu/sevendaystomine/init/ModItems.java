package nuparu.sevendaystomine.init;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.item.*;
import nuparu.sevendaystomine.world.item.BookItem;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SevenDaysToMine.MODID);
    public static final RegistryObject<Item> IRON_SCRAP = ITEMS.register("scrap_iron", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRASS_SCRAP = ITEMS.register("scrap_brass", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LEAD_SCRAP = ITEMS.register("scrap_lead", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_SCRAP = ITEMS.register("scrap_copper", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TIN_SCRAP = ITEMS.register("scrap_tin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_SCRAP = ITEMS.register("scrap_gold", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STEEL_SCRAP = ITEMS.register("scrap_steel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ZINC_SCRAP = ITEMS.register("scrap_zinc", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_SCRAP = ITEMS.register("scrap_bronze", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GLASS_SCRAP = ITEMS.register("scrap_glass", () -> new Item(new Item.Properties()));

    //public static final RegistryObject<Item> PLANK_WOOD = ITEMS.register("plank_wood", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SMALL_STONE = ITEMS.register("small_stone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PLANT_FIBER = ITEMS.register("plant_fiber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EMPTY_CAN = ITEMS.register("empty_can", () -> new EmptyCanItem(new Item.Properties()));
    public static final RegistryObject<Item> CLOTH = ITEMS.register("cloth", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_PIPE = ITEMS.register("iron_pipe", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MURKY_WATER_BOTTLE = ITEMS.register("murky_water_bottle", () -> new MurkyWaterBottleItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BEER_BOTTLE = ITEMS.register("beer_bottle", () -> new DrinkItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STONE_SPEAR = ITEMS.register("stone_spear", () -> new StoneSpearItem(new Item.Properties().durability(10), -3F, 6.0));
    public static final RegistryObject<Item> BONE_SPEAR = ITEMS.register("bone_spear", () -> new BoneSpearItem(new Item.Properties().durability(10), -2.8F, 7.0));
    public static final RegistryObject<Item> CRUDE_CLUB = ITEMS.register("crude_club", () -> new SwordItem(Tiers.WOOD, 3, -3.14f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> WOODEN_CLUB = ITEMS.register("wooden_club", () -> new SwordItem(Tiers.WOOD, 4, -3.14f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IRON_REINFORCED_CLUB = ITEMS.register("iron_reinforced_club", () -> new SwordItem(Tiers.IRON, 5, -3.2f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BARBED_CLUB = ITEMS.register("barbed_club", () -> new SwordItem(Tiers.IRON, 7, -3.22f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SPIKED_CLUB = ITEMS.register("spiked_club", () -> new SwordItem(Tiers.IRON, 9, -3.22f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CRUDE_BOW = ITEMS.register("crude_bow", () -> new BowItem(new Item.Properties().durability(384)));
    public static final RegistryObject<Item> COMPOUND_BOW = ITEMS.register("compound_bow", () -> new BowItem(new Item.Properties().durability(384)));

    public static final RegistryObject<Item> TORCH_UNLIT = ITEMS.register("torch_unlit",
            () -> new StandingAndWallBlockItem(ModBlocks.TORCH_UNLIT.get(), ModBlocks.TORCH_UNLIT_WALL.get(), (new Item.Properties()), Direction.DOWN));
    public static final RegistryObject<Item> TORCH = ITEMS.register("torch",
            () -> new StandingAndWallBlockItem(ModBlocks.TORCH_LIT.get(), ModBlocks.TORCH_LIT_WALL.get(), (new Item.Properties()), Direction.DOWN));
    public static final RegistryObject<Item> SOUL_TORCH = ITEMS.register("soul_torch",
            () -> new StandingAndWallBlockItem(ModBlocks.SOUL_TORCH_LIT.get(), ModBlocks.SOUL_TORCH_LIT_WALL.get(), (new Item.Properties()), Direction.DOWN));
    public static final RegistryObject<Item> SOUL_TORCH_UNLIT = ITEMS.register("soul_torch_unlit",
            () -> new StandingAndWallBlockItem(ModBlocks.SOUL_TORCH_UNLIT.get(), ModBlocks.SOUL_TORCH_UNLIT_WALL.get(), (new Item.Properties()), Direction.DOWN));

    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRASS_INGOT = ITEMS.register("brass_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TIN_INGOT = ITEMS.register("tin_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ZINC_INGOT = ITEMS.register("zinc_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> REANIMATED_CORPSE_SPAWN_EGG = ITEMS.register("reanimated_corpse_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.REANIMATED_CORPSE, 0x403A34, 0x1D2637, (new Item.Properties())));
    public static final RegistryObject<Item> PLAGUED_NURSE_SPAWN_EGG = ITEMS.register("plagued_nurse_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.PLAGUED_NURSE, 0x5B1C1C, 0x242A3C, (new Item.Properties())));
    public static final RegistryObject<Item> FRIGID_HUNTER_SPAWN_EGG = ITEMS.register("frigid_hunter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.FRIGID_HUNTER, 0x607A88, 0x593616, (new Item.Properties())));
    public static final RegistryObject<Item> FROSTBITTEN_WORKER_SPAWN_EGG = ITEMS.register("frostbitten_worker_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.FROSTBITTEN_WORKER, 0x2C5B50, 0x1D2637, (new Item.Properties())));
    public static final RegistryObject<Item> MINER_ZOMBIE_SPAWN_EGG = ITEMS.register("miner_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.MINER_ZOMBIE, 0x1A1325, 0x211A17, (new Item.Properties())));
    public static final RegistryObject<Item> BURNT_ZOMBIE_SPAWN_EGG = ITEMS.register("burnt_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BURNT_ZOMBIE, 0x000000, 0xffc400, (new Item.Properties())));
    public static final RegistryObject<Item> SOUL_BURNT_ZOMBIE_SPAWN_EGG = ITEMS.register("soul_burnt_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOUL_BURNT_ZOMBIE, 0x000000, 0x00F4FF, (new Item.Properties())));
    public static final RegistryObject<Item> BLOATED_ZOMBIE_SPAWN_EGG = ITEMS.register("bloated_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BLOATED_ZOMBIE, 0x4F4134, 0x211B17, (new Item.Properties())));
    public static final RegistryObject<Item> ZOMBIE_SOLDIER_SPAWN_EGG = ITEMS.register("zombie_soldier_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SOLDIER_ZOMBIE, 0x313B63, 0x544A38, (new Item.Properties())));
    public static final RegistryObject<Item> INFECTED_SURVIVOR_SPAWN_EGG = ITEMS.register("infected_survivor_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.INFECTED_SURVIVOR, 0x6B5D65, 0x6C3B38, (new Item.Properties())));
    public static final RegistryObject<Item> FROZEN_LUMBERJACK_SPAWN_EGG = ITEMS.register("frozen_lumberjack_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.FROZEN_LUMBERJACK, 0x151F36, 0x760504, (new Item.Properties())));
    public static final RegistryObject<Item> FERAL_ZOMBIE_SPAWN_EGG = ITEMS.register("feral_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.FERAL_ZOMBIE, 0x4B5435, 0x453A2D, (new Item.Properties())));
    public static final RegistryObject<Item> TWISTED_ZOMBIE_SPAWN_EGG = ITEMS.register("twisted_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.TWISTED_ZOMBIE, 0x11ccbb, 0x555555, (new Item.Properties())));
    public static final RegistryObject<Item> CRAWLER_ZOMBIE_SPAWN_EGG = ITEMS.register("crawler_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.CRAWLER_ZOMBIE, 0x4D463C, 0x375268, (new Item.Properties())));
    public static final RegistryObject<Item> ZOMBIE_WOLF_SPAWN_EGG = ITEMS.register("zombie_wolf_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.ZOMBIE_WOLF, 0x827E7E, 0x851919, (new Item.Properties())));
    public static final RegistryObject<Item> ZOMBIE_PIG_SPAWN_EGG = ITEMS.register("zombie_pig_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.ZOMBIE_PIG, 0xD39294, 0x852122, (new Item.Properties())));
    public static final RegistryObject<Item> SPIDER_ZOMBIE_SPAWN_EGG = ITEMS.register("spider_zombie_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SPIDER_ZOMBIE, 0x2E2E34, 0x474239, (new Item.Properties())));

    public static final RegistryObject<Item> KITCHEN_KNIFE = ITEMS.register("kitchen_knife", () -> new SwordItem(Tiers.IRON, 4, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ARMY_KNIFE = ITEMS.register("army_knife", () -> new SwordItem(ModTiers.STEEL, 6, -2.6f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MACHETE = ITEMS.register("machete", () -> new SwordItem(ModTiers.STEEL, 9, -3f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS.register("sledgehammer", () -> new SwordItem(ModTiers.STEEL, 15, -3.5f, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SCRAP_SHOVEL = ITEMS.register("scrap_shovel", () -> new ShovelItem(ModTiers.SCRAP, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCRAP_PICKAXE = ITEMS.register("scrap_pickaxe", () -> new PickaxeItem(ModTiers.SCRAP, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCRAP_AXE = ITEMS.register("scrap_axe", () -> new AxeItem(ModTiers.SCRAP, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCRAP_HOE = ITEMS.register("scrap_hoe", () -> new HoeItem(ModTiers.SCRAP, 0, -2.8f, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel", () -> new ShovelItem(ModTiers.COPPER, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new PickaxeItem(ModTiers.COPPER, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COPPER_AXE = ITEMS.register("copper_axe", () -> new AxeItem(ModTiers.COPPER, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COPPER_HOE = ITEMS.register("copper_hoe", () -> new HoeItem(ModTiers.COPPER, 0, -2.8f, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BRONZE_SHOVEL = ITEMS.register("bronze_shovel", () -> new ShovelItem(ModTiers.BRONZE, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BRONZE_PICKAXE = ITEMS.register("bronze_pickaxe", () -> new PickaxeItem(ModTiers.BRONZE, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BRONZE_AXE = ITEMS.register("bronze_axe", () -> new AxeItem(ModTiers.BRONZE, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BRONZE_HOE = ITEMS.register("bronze_hoe", () -> new HoeItem(ModTiers.BRONZE, 0, -2.8f, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> STEEL_SHOVEL = ITEMS.register("steel_shovel", () -> new ShovelItem(ModTiers.STEEL, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STEEL_PICKAXE = ITEMS.register("steel_pickaxe", () -> new PickaxeItem(ModTiers.STEEL, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STEEL_AXE = ITEMS.register("steel_axe", () -> new AxeItem(ModTiers.STEEL, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> STEEL_HOE = ITEMS.register("steel_hoe", () -> new HoeItem(ModTiers.STEEL, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> AUGER = ITEMS.register("auger", () -> new AugerItem(ModTiers.AUGER, 0, -2.8f, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CHAINSAW = ITEMS.register("chainsaw", () -> new ChainsawItem(ModTiers.AUGER, 0, -2.8f, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BANDAGE = ITEMS.register("bandage", () -> new BandageItem(new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> ADVANCED_BANDAGE = ITEMS.register("advanced_bandage", () -> new AdvancedBandageItem(new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> FIRST_AID_KIT = ITEMS.register("first_aid_kit", () -> new FirstAidKitItem(new Item.Properties().stacksTo(8)));
    public static final RegistryObject<Item> BLOOD_BAG = ITEMS.register("blood_bag", () -> new BloodBagItem(new Item.Properties().stacksTo(1).durability(0)));
    public static final RegistryObject<Item> BLOOD_DRAW_KIT = ITEMS.register("blood_draw_kit", () -> new BloodDrawKitItem(new Item.Properties().stacksTo(1).durability(16)));
    public static final RegistryObject<Item> ANTIBIOTICS = ITEMS.register("antibiotics", () -> new AntibioticsItem(new Item.Properties().stacksTo(16).food(ModFood.ANTIBIOTICS)));

    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", () -> new SwordItem(Tiers.IRON,0,0,new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CORN = ITEMS.register("corn", () -> new ItemNameBlockItem(ModBlocks.CORN_PLANT.get(), new Item.Properties().stacksTo(64).food(ModFood.CORN)));
    public static final RegistryObject<Item> COFFEE_BERRY = ITEMS.register("coffee_berry", () -> new ItemNameBlockItem(ModBlocks.COFFEE_PLANT.get(), new Item.Properties().stacksTo(64).food(ModFood.COFFEE_BERRY)));
    public static final RegistryObject<Item> COFFEE_BEANS = ITEMS.register("coffee_beans", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLUEBERRY = ITEMS.register("blueberry", () -> new Item(new Item.Properties().stacksTo(64).food(ModFood.BLUEBERRY)));
    public static final RegistryObject<Item> BANEBERRY = ITEMS.register("baneberry", () -> new Item(new Item.Properties().stacksTo(64).food(ModFood.BANEBERRY)));

    public static final RegistryObject<Item> INGOT_MOLD = ITEMS.register("ingot_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));

    public static final RegistryObject<Item> CANNED_CAT_FOOD = ITEMS.register("canned_cat_food", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_ANIMAL_FOOD).durability(2)));
    public static final RegistryObject<Item> CANNED_DOG_FOOD = ITEMS.register("canned_dog_food", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_ANIMAL_FOOD).durability(2)));
    public static final RegistryObject<Item> CANNED_HAM = ITEMS.register("canned_ham", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_HAM).durability(3)));
    public static final RegistryObject<Item> CANNED_CHICKEN = ITEMS.register("canned_chicken", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_CHICKEN).durability(4)));
    public static final RegistryObject<Item> CANNED_CHILI = ITEMS.register("canned_chili", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_CHILI).durability(2)));
    public static final RegistryObject<Item> CANNED_MISO = ITEMS.register("canned_miso", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_MISO).durability(2), true));
    public static final RegistryObject<Item> CANNED_PASTA = ITEMS.register("canned_pasta", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_PASTA).durability(2)));
    public static final RegistryObject<Item> CANNED_PEARS = ITEMS.register("canned_pears", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_FRUIT_AND_VEGETABLES).durability(2)));
    public static final RegistryObject<Item> CANNED_PEAS = ITEMS.register("canned_peas", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_FRUIT_AND_VEGETABLES).durability(2)));
    public static final RegistryObject<Item> CANNED_SALMON = ITEMS.register("canned_salmon", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_CHICKEN).durability(3)));
    public static final RegistryObject<Item> CANNED_SOUP = ITEMS.register("canned_soup", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_SOUP).durability(2), true));
    public static final RegistryObject<Item> CANNED_STOCK = ITEMS.register("canned_stock", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_STOCK).durability(2), true));
    public static final RegistryObject<Item> CANNED_TUNA = ITEMS.register("canned_tuna", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_CHICKEN).durability(3)));
    public static final RegistryObject<Item> CANNED_BEEF = ITEMS.register("canned_beef", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_HUGE_MEAT).durability(4)));
    public static final RegistryObject<Item> CANNED_LAMB = ITEMS.register("canned_lamb", () -> new CannedFoodItem(new Item.Properties().stacksTo(1).food(ModFood.CANNED_HUGE_MEAT).durability(4)));
    public static final RegistryObject<Item> MRE = ITEMS.register("mre", () -> new Item(new Item.Properties().stacksTo(1).food(ModFood.MRE).durability(5)));
    public static final RegistryObject<Item> MOLDY_BREAD = ITEMS.register("moldy_bread", () -> new Item(new Item.Properties().stacksTo(64).food(ModFood.MOLDY_BREAD)));
    public static final RegistryObject<Item> SODA = ITEMS.register("soda", () -> new CannedDrinkItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> COFFEE_BOTTLE = ITEMS.register("coffee_bottle", () -> new DrinkItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GOLDENROD_TEA = ITEMS.register("goldenrod_tea_bottle", () -> new DrinkItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CENT = ITEMS.register("cent", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SAND_DUST = ITEMS.register("sand_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POTASSIUM = ITEMS.register("potassium", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CEMENT = ITEMS.register("cement", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CONCRETE_MIX = ITEMS.register("concrete_mix", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GAS_CANISTER = ITEMS.register("gas_canister", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GEAR = ITEMS.register("gear", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BULLET_TIP = ITEMS.register("bullet_tip", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BULLET_CASING = ITEMS.register("bullet_casing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SURVIVAL_GUIDE = ITEMS.register("survival_guide", () -> new BookItem(new Item.Properties().stacksTo(1), false));
    public static final RegistryObject<Item> BOOK_FORGING = ITEMS.register("book_forging", () -> new BookItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> BOOK_AMMO = ITEMS.register("book_ammo", () -> new BookItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> BOOK_COMPUTERS = ITEMS.register("book_computers", () -> new BookItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> BOOK_CONCRETE = ITEMS.register("book_concrete", () -> new BookItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> BOOK_ELECTRICITY = ITEMS.register("book_electricity", () -> new BookItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> BOOK_CHEMISTRY = ITEMS.register("book_chemistry", () -> new BookItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> BOOK_METALWORKING = ITEMS.register("book_metalworking", () -> new BookItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> BOOK_PISTOL = ITEMS.register("book_pistol", () -> new BookItem(new Item.Properties().stacksTo(1), true));
    public static final RegistryObject<Item> BOOK_MINIBIKE = ITEMS.register("book_minibike", () -> new BookItem(new Item.Properties().stacksTo(1), true));

    public static final RegistryObject<Item> HUNTING_RIFLE_BARREL_MOLD = ITEMS.register("hunting_rifle_barrel_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> HUNTING_RIFLE_BOLT_MOLD = ITEMS.register("hunting_rifle_bolt_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> MP5_BARREL_MOLD = ITEMS.register("mp5_barrel_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> MP5_STOCK_MOLD = ITEMS.register("mp5_stock_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> MP5_TRIGGER_MOLD = ITEMS.register("mp5_trigger_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> PISTOL_BARREL_MOLD = ITEMS.register("pistol_barrel_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> PISTOL_TRIGGER_MOLD = ITEMS.register("pistol_trigger_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> SNIPER_RIFLE_STOCK_MOLD = ITEMS.register("sniper_rifle_stock_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> SNIPER_RIFLE_TRIGGER_MOLD = ITEMS.register("sniper_rifle_trigger_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> SHOTGUN_BARREL_MOLD = ITEMS.register("shotgun_barrel_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> SHOTGUN_RECEIVER_MOLD = ITEMS.register("shotgun_receiver_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> SHOTGUN_SHORT_BARREL_MOLD = ITEMS.register("shotgun_short_barrel_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> IRON_PIPE_MOLD = ITEMS.register("iron_pipe_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));
    public static final RegistryObject<Item> CEMENT_MOLD = ITEMS.register("cement_mold", () -> new Item(new Item.Properties().stacksTo(1).durability(64)));

    public static final RegistryObject<Item> PISTOL_SLIDE = ITEMS.register("pistol_slide", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PISTOL_TRIGGER = ITEMS.register("pistol_trigger", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> PISTOL_GRIP = ITEMS.register("pistol_grip", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SNIPER_RIFLE_BARREL = ITEMS.register("sniper_rifle_barrel", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SNIPER_RIFLE_TRIGGER = ITEMS.register("sniper_rifle_trigger", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SNIPER_RIFLE_SCOPE = ITEMS.register("sniper_rifle_scope", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SNIPER_RIFLE_STOCK = ITEMS.register("sniper_rifle_stock", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SHOTGUN_RECEIVER = ITEMS.register("shotgun_receiver", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHOTGUN_STOCK = ITEMS.register("shotgun_stock", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHOTGUN_STOCK_SHORT = ITEMS.register("shotgun_stock_short", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHOTGUN_PARTS = ITEMS.register("shotgun_parts", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHOTGUN_BARREL = ITEMS.register("shotgun_barrel", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHOTGUN_BARREL_SHORT = ITEMS.register("shotgun_barrel_short", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MAGNUM_FRAME = ITEMS.register("magnum_frame", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MAGNUM_CYLINDER = ITEMS.register("magnum_cylinder", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MAGNUM_GRIP = ITEMS.register("magnum_grip", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> MP5_BARREL = ITEMS.register("mp5_barrel", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MP5_TRIGGER = ITEMS.register("mp5_trigger", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MP5_STOCK = ITEMS.register("mp5_stock", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> HUNTING_RIFLE_BARREL = ITEMS.register("hunting_rifle_barrel", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HUNTING_RIFLE_STOCK = ITEMS.register("hunting_rifle_stock", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RIFLE_PARTS = ITEMS.register("rifle_parts", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HUNTING_RIFLE_BOLT = ITEMS.register("hunting_rifle_bolt", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CAR_BATTERY = ITEMS.register("car_battery", () -> new BatteryItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SMALL_ENGINE = ITEMS.register("small_engine", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MINIBIKE_SEAT = ITEMS.register("minibike_seat", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MINIBIKE_HANDLES = ITEMS.register("minibike_handles", () -> new Item(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> FIBER_HAT = ITEMS.register("fiber_hat", () -> new ArmorItem(ModArmorMaterials.FIBER, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> FIBER_CHESTPLATE = ITEMS.register("fiber_chestplate", () -> new ArmorItem(ModArmorMaterials.FIBER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> FIBER_LEGGINGS = ITEMS.register("fiber_leggings", () -> new ArmorItem(ModArmorMaterials.FIBER, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> FIBER_BOOTS = ITEMS.register("fiber_boots", () -> new ArmorItem(ModArmorMaterials.FIBER, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> LEATHER_IRON_HELMET = ITEMS.register("leather_iron_helmet", () -> new ArmorItem(ModArmorMaterials.LEATHER_IRON, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> LEATHER_IRON_CHESTPLATE = ITEMS.register("leather_iron_chestplate", () -> new ArmorItem(ModArmorMaterials.LEATHER_IRON, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> LEATHER_IRON_LEGGINGS = ITEMS.register("leather_iron_leggings", () -> new ArmorItem(ModArmorMaterials.LEATHER_IRON, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> LEATHER_IRON_BOOTS = ITEMS.register("leather_iron_boots", () -> new ArmorItem(ModArmorMaterials.LEATHER_IRON, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> STEEL_HELMET = ITEMS.register("steel_helmet", () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_CHESTPLATE = ITEMS.register("steel_chestplate", () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_LEGGINGS = ITEMS.register("steel_leggings", () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_BOOTS = ITEMS.register("steel_boots", () -> new ArmorItem(ModArmorMaterials.STEEL, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> MILITARY_HELMET = ITEMS.register("military_helmet", () -> new ArmorItem(ModArmorMaterials.MILITARY, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> MILITARY_CHESTPLATE = ITEMS.register("military_chestplate", () -> new ArmorItem(ModArmorMaterials.MILITARY, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MILITARY_LEGGINGS = ITEMS.register("military_leggings", () -> new ArmorItem(ModArmorMaterials.MILITARY, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MILITARY_BOOTS = ITEMS.register("military_boots", () -> new ArmorItem(ModArmorMaterials.MILITARY, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> SCRAP_HELMET = ITEMS.register("scrap_helmet", () -> new ArmorItem(ModArmorMaterials.SCRAP, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SCRAP_CHESTPLATE = ITEMS.register("scrap_chestplate", () -> new ArmorItem(ModArmorMaterials.SCRAP, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> SCRAP_LEGGINGS = ITEMS.register("scrap_leggings", () -> new ArmorItem(ModArmorMaterials.SCRAP, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> SCRAP_BOOTS = ITEMS.register("scrap_boots", () -> new ArmorItem(ModArmorMaterials.SCRAP, ArmorItem.Type.BOOTS, new Item.Properties()));


    public static final RegistryObject<Item> SHIRT = ITEMS.register("shirt", () -> new ClothingItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), "shirt"));
    public static final RegistryObject<Item> SHORT_SLEEVED_SHIRT = ITEMS.register("short_sleeved_shirt", () -> new ClothingItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), "short_sleeved_shirt"));
    public static final RegistryObject<Item> T_SHIRT_0 = ITEMS.register("t_shirt_0", () -> new ClothingItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), "t_shirt_0"));
    public static final RegistryObject<Item> T_SHIRT_1 = ITEMS.register("t_shirt_1", () -> new ClothingItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), "t_shirt_1"));
    public static final RegistryObject<Item> JACKET = ITEMS.register("jacket", () -> new ClothingItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), "jacket"));
    public static final RegistryObject<Item> JUMPER = ITEMS.register("jumper", () -> new ClothingItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), "jumper"));
    public static final RegistryObject<Item> COAT = ITEMS.register("coat", () -> new ClothingItem(ArmorItem.Type.CHESTPLATE, new Item.Properties(), "coat"));
    public static final RegistryObject<Item> SHORTS = ITEMS.register("shorts", () -> new ClothingItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), "shorts"));
    public static final RegistryObject<Item> SKIRT = ITEMS.register("skirt", () -> new ClothingItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), "skirt"));
    public static final RegistryObject<Item> JEANS = ITEMS.register("jeans", () -> new ClothingItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), "jeans"));
    public static final RegistryObject<Item> SHORTS_LONG = ITEMS.register("shorts_long", () -> new ClothingItem(ArmorItem.Type.LEGGINGS, new Item.Properties(), "shorts_longer"));
    public static final RegistryObject<Item> ANALOG_CAMERA = ITEMS.register("analog_camera", () -> new AnalogCameraItem( new Item.Properties()));

    public static final RegistryObject<Item> PHOTO = ITEMS.register("photo", () -> new PhotoItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BELLOWS = ITEMS.register("bellows", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> REALITY_WAND = ITEMS.register("reality_wand", RealityWandItem::new);
    public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PISTOL_BULLET = ITEMS.register("pistol_bullet", () -> new BulletItem(new Item.Properties()));
    public static final RegistryObject<Item> RIFLE_BULLET = ITEMS.register("rifle_bullet", () -> new BulletItem(new Item.Properties()));
    public static final RegistryObject<Item> SMG_BULLET = ITEMS.register("smg_bullet", () -> new BulletItem(new Item.Properties()));
    public static final RegistryObject<Item> MAGNUM_BULLET = ITEMS.register("magnum_bullet", () -> new BulletItem(new Item.Properties()));
    public static final RegistryObject<Item> SHOTGUN_SHELL = ITEMS.register("shotgun_shell", () -> new BulletItem(new Item.Properties()));
    public static final RegistryObject<Item> ROCKET = ITEMS.register("rocket", () -> new BulletItem(new Item.Properties()));

    public static final RegistryObject<Item> PISTOL = ITEMS.register("pistol", () -> new GunItem(new Item.Properties(), GunItem.Wield.ONE_HAND, 15));



}
