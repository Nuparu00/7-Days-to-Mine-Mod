package nuparu.sevendaystomine.crafting.scrap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumMaterial;
import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScrapDataManager {

    private static ScrapDataManager instance;

    private final List<ScrapEntry> scraps = new ArrayList<>();
    private final HashMap<EnumMaterial,ScrapEntry> scrapBitsMap = new HashMap<>();

    public ScrapEntry getEntry(Item item){
        for(ScrapEntry entry : scraps){
            if(entry.item() == item){
                return entry;
            }
        }
        return null;
    }

    public boolean hasEntry(Item item){
        return getEntry(item) != null;
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

    public List<ScrapEntry> getScraps(){
        return new ArrayList<>(scraps);
    }

    public static ScrapDataManager getInstance() {
        if(instance == null){
            instance = new ScrapDataManager();
        }
        return instance;
    }

    public void addScrap(Item item, EnumMaterial material, Number weight, boolean canBeScrapped, boolean isScrapBit, boolean excludeFromMin){
        ScrapEntry scrapEntry = new ScrapEntry(null,item,material, new WeightWrapper(weight),canBeScrapped,isScrapBit, excludeFromMin);
        addScrapEntry(scrapEntry);
    }

    public void addScrapResult(Item item, EnumMaterial material){
        addScrap(item,material,1,false,true,false);
    }

    public void addScrapable(Item item, EnumMaterial material, Number weight){
        addScrap(item, material, weight, true, false, false);
    }

    public void addUnscrapable(Item item, EnumMaterial material, Number weight){
        addScrap(item, material, weight, true, false, false);
    }



    public void addScrap(Block block, EnumMaterial material, Number weight, boolean canBeScrapped, boolean isScrapBit, boolean excludeFromMin){
        addScrap(Item.getItemFromBlock(block),material, weight,canBeScrapped,isScrapBit,excludeFromMin);
    }

    public void addScrapResult(Block block, EnumMaterial material){
        addScrapResult(Item.getItemFromBlock(block),material);
    }

    public void addScrapable(Block block, EnumMaterial material, Number weight){
        addScrapable(Item.getItemFromBlock(block),material, weight);
    }

    public void addUnscrapable(Block block, EnumMaterial material, Number weight){
        addUnscrapable(Item.getItemFromBlock(block),material, weight);
    }

    public void addScrapEntry(ScrapEntry scrapEntry) {
        scraps.add(scrapEntry);
        if(scrapEntry.isScrapBit()){
            scrapBitsMap.put(scrapEntry.material(),scrapEntry);
        }
    }

    public void removeScrap(Item item) {
        scraps.removeIf(scrapEntry -> scrapEntry.item() == item);
    }

    private List<Runnable> removals = new ArrayList<>();

    public void removeScrapLater(Item item){
        this.removals.add(() -> removeScrap(item));
    }

    private void removeScraps(){
        removals.forEach(Runnable::run);
    }

    public void registerDefault(){
        addScrapResult(ModItems.IRON_SCRAP, EnumMaterial.IRON);
        addScrapResult(ModItems.LEAD_SCRAP, EnumMaterial.LEAD);
        addScrapResult(ModItems.BRASS_SCRAP, EnumMaterial.BRASS);
        addScrapResult(ModItems.BRONZE_SCRAP, EnumMaterial.BRONZE);
        addScrapResult(ModItems.COPPER_SCRAP, EnumMaterial.COPPER);
        addScrapResult(ModItems.TIN_SCRAP, EnumMaterial.TIN);
        addScrapResult(ModItems.ZINC_SCRAP, EnumMaterial.ZINC);
        addScrapResult(ModItems.GOLD_SCRAP, EnumMaterial.GOLD);
        addScrapResult(ModItems.STEEL_SCRAP, EnumMaterial.STEEL);
        addScrapResult(ModItems.GLASS_SCRAP, EnumMaterial.GLASS);
        addScrapResult(ModItems.POTASSIUM, EnumMaterial.POTASSIUM);
        addScrapResult(ModItems.PLANK_WOOD, EnumMaterial.WOOD);
        addScrapResult(ModItems.SMALL_STONE, EnumMaterial.STONE);
        addScrapResult(ModItems.SAND_DUST, EnumMaterial.SAND);
        addScrapResult(ModItems.CLOTH, EnumMaterial.CLOTH);
        addScrapResult(ModItems.GAS_CANISTER, EnumMaterial.GASOLINE);
        addScrapResult(ModItems.PLANT_FIBER, EnumMaterial.PLANT_FIBER);
        addScrapResult(Items.LEATHER, EnumMaterial.LEATHER);
        addScrapResult(Items.CLAY_BALL, EnumMaterial.CLAY);
        addScrapResult(Items.COAL, EnumMaterial.CARBON);
        addScrapResult(Items.PAPER, EnumMaterial.PAPER);
        addScrapable(Items.FLINT, EnumMaterial.STONE,1);
        addScrapable(ModItems.GEAR, EnumMaterial.STEEL, 1);
        addScrapable(ModItems.IRON_PIPE, EnumMaterial.IRON, 1);
        addScrapable(ModItems.EMPTY_CAN, EnumMaterial.IRON, 1);
        addScrapable(ModItems.EMPTY_JAR, EnumMaterial.GLASS, 1);
        addScrapable(ModItems.CENT, EnumMaterial.BRASS, 1);
        addScrapable(Items.IRON_INGOT, EnumMaterial.IRON, 10);
        addScrapable(Items.GOLD_INGOT, EnumMaterial.GOLD, 10);
        addScrapable(Items.IRON_NUGGET, EnumMaterial.IRON, Fraction.getFraction(10,9));
        addScrapable(Items.GOLD_NUGGET, EnumMaterial.GOLD, Fraction.getFraction(10,9));
        addScrapable(Items.MINECART, EnumMaterial.IRON, 50);
        addScrapable(Blocks.HOPPER, EnumMaterial.IRON, 50);
        addScrapable(Items.HOPPER_MINECART, EnumMaterial.IRON, 100);
        addScrapable(Blocks.CHEST, EnumMaterial.WOOD, 8);
        addScrapable(Blocks.TRAPPED_CHEST, EnumMaterial.WOOD, 8);
        addScrapable(ModBlocks.CHEST_OLD, EnumMaterial.WOOD, 6);
        addScrapable(Items.BUCKET, EnumMaterial.IRON, 30);
        addScrapable(Items.CAULDRON, EnumMaterial.IRON, 70);
        addScrapable(ModBlocks.CODE_SAFE, EnumMaterial.STEEL, 120);
        addScrapable(ModBlocks.TRASH_CAN, EnumMaterial.IRON, 40);
        addScrapable(ModBlocks.TRAFFIC_LIGHT, EnumMaterial.IRON, 12);
        addScrapable(ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN, EnumMaterial.IRON, 8);
        addScrapable(ModBlocks.FILE_CABINET, EnumMaterial.IRON, 20);
        addScrapable(ModItems.INGOT_LEAD, EnumMaterial.LEAD, 10);
        addScrapable(ModItems.INGOT_BRASS, EnumMaterial.BRASS, 10);
        addScrapable(ModItems.INGOT_BRONZE, EnumMaterial.BRONZE, 10);
        addScrapable(ModItems.INGOT_COPPER, EnumMaterial.COPPER, 10);
        addScrapable(ModItems.INGOT_STEEL, EnumMaterial.STEEL, 10);
        addScrapable(ModItems.INGOT_TIN, EnumMaterial.TIN, 10);
        addScrapable(ModItems.INGOT_ZINC, EnumMaterial.ZINC, 10);
        addScrapable(ModItems.BANDAGE, EnumMaterial.CLOTH, 1);
        addScrapable(ModItems.BANDAGE, EnumMaterial.CLOTH, 1);
        addScrapable(ModItems.SHORTS, EnumMaterial.CLOTH, 5);
        addScrapable(ModItems.SKIRT, EnumMaterial.CLOTH, 4);
        addScrapable(ModItems.JEANS, EnumMaterial.CLOTH, 8);
        addScrapable(ModItems.SHORTS_LONG, EnumMaterial.CLOTH, 6);
        addScrapable(ModItems.JACKET, EnumMaterial.CLOTH, 8);
        addScrapable(ModItems.JUMPER, EnumMaterial.CLOTH, 8);
        addScrapable(ModItems.T_SHIRT_0, EnumMaterial.CLOTH, 6);
        addScrapable(ModItems.T_SHIRT_1, EnumMaterial.CLOTH, 6);
        addScrapable(ModItems.SHIRT, EnumMaterial.CLOTH, 7);
        addScrapable(ModItems.SHORT_SLEEVED_SHIRT, EnumMaterial.CLOTH, 6);
        addScrapable(ModItems.COAT, EnumMaterial.CLOTH, 10);
        addScrapable(ModItems.BACKPACK, EnumMaterial.LEATHER, 12);
        addScrapable(ModItems.CHRISTMAS_HAT, EnumMaterial.CLOTH, 2);
        addScrapable(ModItems.BERET, EnumMaterial.CLOTH, 2);


        addScrapable(Blocks.GRAVEL, EnumMaterial.STONE, 4);
        addScrapable(Blocks.STONE, EnumMaterial.STONE, 9);
        addScrapable(Blocks.MOSSY_COBBLESTONE, EnumMaterial.STONE, 9);
        addScrapable(Blocks.COBBLESTONE, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.DEAD_MOSSY_STONE, EnumMaterial.STONE, 9);
        addScrapable(Blocks.IRON_BLOCK, EnumMaterial.IRON, 90);
        addScrapable(Blocks.GOLD_BLOCK, EnumMaterial.GOLD, 90);
        addScrapable(Blocks.COAL_BLOCK, EnumMaterial.CARBON, 9);
        addScrapable(ModBlocks.LEAD_BLOCK, EnumMaterial.LEAD, 90);
        addScrapable(ModBlocks.BRASS_BLOCK, EnumMaterial.BRASS, 90);
        addScrapable(ModBlocks.BRONZE_BLOCK, EnumMaterial.BRONZE, 90);
        addScrapable(ModBlocks.COPPER_BLOCK, EnumMaterial.COPPER, 90);
        addScrapable(ModBlocks.STEEL_BLOCK, EnumMaterial.STEEL, 90);
        addScrapable(Blocks.CLAY, EnumMaterial.CLAY, 4);
        addScrapable(Blocks.HARDENED_CLAY, EnumMaterial.CLAY, 4);
        addScrapable(Blocks.STAINED_HARDENED_CLAY, EnumMaterial.CLAY, 4);
        addScrapable(Blocks.GLASS, EnumMaterial.GLASS,  Fraction.getFraction(32,3));
        addScrapable(Blocks.STAINED_GLASS, EnumMaterial.GLASS,  Fraction.getFraction(32,3));
        addScrapable(Blocks.GLASS_PANE, EnumMaterial.GLASS, 4);
        addScrapable(Blocks.STAINED_GLASS_PANE, EnumMaterial.GLASS, 4);
        addScrapable(Blocks.LOG, EnumMaterial.WOOD, 4);
        addScrapable(Blocks.LOG2, EnumMaterial.WOOD, 4);
        addScrapable(Blocks.CRAFTING_TABLE, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.TABLE_ACACIA, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.TABLE_BIRCH, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.TABLE_BIG_OAK, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.TABLE_JUNGLE, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.TABLE_OAK, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.TABLE_SPRUCE, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.CHAIR_ACACIA, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.CHAIR_BIRCH, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.CHAIR_BIG_OAK, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.CHAIR_JUNGLE, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.CHAIR_OAK, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.CHAIR_SPRUCE, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.BOARDS, EnumMaterial.WOOD, 2);
        addScrapable(ModBlocks.DRESSER, EnumMaterial.WOOD, 6);
        addScrapable(ModBlocks.ACACIA_FRAME, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.BIRCH_FRAME, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.DARKOAK_FRAME, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.JUNGLE_FRAME, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.OAK_FRAME, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.SPRUCE_FRAME, EnumMaterial.WOOD, 4);
        addScrapable(ModBlocks.BURNT_FRAME, EnumMaterial.CARBON, 4);
        addScrapable(ModBlocks.BURNT_PLANKS_FENCE, EnumMaterial.CARBON, 4);
        addScrapable(ModBlocks.BURNT_PLANKS_SLAB, EnumMaterial.CARBON, 4);
        addScrapable(ModBlocks.BURNT_PLANKS_STAIRS, EnumMaterial.CARBON, 6);
        addScrapable(ModBlocks.BURNT_CHAIR, EnumMaterial.CARBON, 4);
        addScrapable(Blocks.IRON_ORE, EnumMaterial.IRON, 10);
        addScrapable(Blocks.IRON_BARS, EnumMaterial.IRON,  Fraction.getFraction(15,4));
        addScrapable(Items.IRON_DOOR, EnumMaterial.IRON, 20);
        addScrapable(Blocks.IRON_TRAPDOOR, EnumMaterial.IRON, 40);
        addScrapable(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, EnumMaterial.IRON, 20);
        addScrapable(Blocks.GOLD_ORE, EnumMaterial.GOLD, 10);
        addScrapable(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, EnumMaterial.GOLD, 20);
        addScrapable(Blocks.COAL_ORE, EnumMaterial.CARBON, 1);
        addScrapable(ModBlocks.ORE_COPPER, EnumMaterial.COPPER, 10);
        addScrapable(ModBlocks.ORE_TIN, EnumMaterial.TIN, 10);
        addScrapable(ModBlocks.ORE_ZINC, EnumMaterial.ZINC, 10);
        addScrapable(ModBlocks.LEAD_BLOCK, EnumMaterial.LEAD, 10);
        addScrapable(ModBlocks.ORE_POTASSIUM, EnumMaterial.POTASSIUM, 10);
        addScrapable(ModBlocks.ORE_CINNABAR, EnumMaterial.MERCURY, 10);
        addScrapable(ModBlocks.REBAR_FRAME, EnumMaterial.IRON, 4);
        addScrapable(ModBlocks.CATWALK, EnumMaterial.IRON, 6);
        addScrapable(ModBlocks.CATWALK_STAIRS, EnumMaterial.IRON, 6);
        addScrapable(ModBlocks.RADIATOR, EnumMaterial.IRON, 10);
        addScrapable(ModBlocks.SINK_FAUCET, EnumMaterial.IRON, 2);
        addScrapable(ModBlocks.SHOWER_HEAD, EnumMaterial.IRON, 2);
        addScrapable(ModBlocks.RAZOR_WIRE, EnumMaterial.IRON, 5);
        addScrapable(ModBlocks.STAND, EnumMaterial.IRON, 6);
        addScrapable(Blocks.ANVIL, EnumMaterial.IRON, 310);
        addScrapable(ModBlocks.FAKE_ANVIL, EnumMaterial.IRON, 90);
        addScrapable(ModBlocks.TRASH_BIN, EnumMaterial.IRON, 6);
        addScrapable(ModItems.FRIDGE, EnumMaterial.IRON, 45);
        addScrapable(ModItems.STREET_SIGN, EnumMaterial.IRON, 1);
        addScrapable(ModBlocks.BACKPACK_NORMAL, EnumMaterial.CLOTH, 6);
        addScrapable(ModBlocks.BACKPACK_MEDICAL, EnumMaterial.CLOTH, 6);
        addScrapable(ModBlocks.BACKPACK_ARMY, EnumMaterial.CLOTH, 10);


        addScrapable(ModBlocks.ARMCHAIR_BLACK, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_BLUE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_BROWN, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_CYAN, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_GRAY, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_GREEN, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_ORANGE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_YELLOW, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_RED, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_WHITE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_PINK, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_PURPLE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_MAGENTA, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_LIME, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_LIGHT_BLUE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.ARMCHAIR_SILVER, EnumMaterial.CLOTH, 12);

        addScrapable(ModBlocks.SOFA_BLACK, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_BLUE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_BROWN, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_CYAN, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_GRAY, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_GREEN, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_ORANGE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_YELLOW, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_RED, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_WHITE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_PINK, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_PURPLE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_MAGENTA, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_LIME, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_LIGHT_BLUE, EnumMaterial.CLOTH, 12);
        addScrapable(ModBlocks.SOFA_SILVER, EnumMaterial.CLOTH, 12);

        addScrapable(ModItems.BULLET_TIP, EnumMaterial.LEAD, 1);
        addScrapable(ModItems.BULLET_CASING, EnumMaterial.BRASS, 1);
        addScrapable(ModItems.WIRE, EnumMaterial.COPPER, Fraction.getFraction(1,4));
        addScrapable(ModItems.CAR_BATTERY, EnumMaterial.LEAD, 60);
        addScrapable(ModItems.CAR_CHASSIS, EnumMaterial.STEEL, 60);
        addScrapable(ModItems.MINIBIKE_CHASSIS, EnumMaterial.IRON, 14);
        addScrapable(ModItems.PISTOL_SLIDE, EnumMaterial.STEEL, 4);
        addScrapable(ModItems.PISTOL_TRIGGER, EnumMaterial.STEEL, 6);
        addScrapable(ModItems.PISTOL_GRIP, EnumMaterial.STEEL, 4);
        addScrapable(ModItems.SNIPER_RIFLE_SCOPE, EnumMaterial.STEEL, 3);
        addScrapable(ModItems.SNIPER_RIFLE_BARREL, EnumMaterial.STEEL, 8);
        addScrapable(ModItems.SNIPER_RIFLE_STOCK, EnumMaterial.STEEL, 8);
        addScrapable(ModItems.SNIPER_RIFLE_TRIGGER, EnumMaterial.STEEL, 14);
        addScrapable(ModItems.SHOTGUN_BARREL, EnumMaterial.STEEL, 5);
        addScrapable(ModItems.SHOTGUN_BARREL_SHORT, EnumMaterial.STEEL, 3);
        addScrapable(ModItems.SHOTGUN_PARTS, EnumMaterial.WOOD, 4);
        addScrapable(ModItems.SHOTGUN_STOCK, EnumMaterial.WOOD, 6);
        addScrapable(ModItems.SHOTGUN_STOCK_SHORT, EnumMaterial.WOOD, 4);
        addScrapable(ModItems.SHOTGUN_RECEIVER, EnumMaterial.STEEL, 8);
        addScrapable(ModItems.MAGNUM_CYLINDER, EnumMaterial.STEEL, 2);
        addScrapable(ModItems.MAGNUM_FRAME, EnumMaterial.STEEL, 4);
        addScrapable(ModItems.MAGNUM_GRIP, EnumMaterial.STEEL, 3);
        addScrapable(ModItems.MP5_BARREL, EnumMaterial.STEEL, 6);
        addScrapable(ModItems.MP5_TRIGGER, EnumMaterial.STEEL, 10);
        addScrapable(ModItems.MP5_STOCK, EnumMaterial.STEEL, 4);
        addScrapable(ModItems.HUNTING_RIFLE_BARREL, EnumMaterial.STEEL, 6);
        addScrapable(ModItems.HUNTING_RIFLE_BOLT, EnumMaterial.STEEL, 8);
        addScrapable(ModItems.HUNTING_RIFLE_PARTS, EnumMaterial.STEEL, 3);
        addScrapable(ModItems.HUNTING_RIFLE_STOCK, EnumMaterial.WOOD, 6);
        addScrapable(ModItems.AUGER_BLADE, EnumMaterial.STEEL, 10);
        addScrapable(ModItems.AUGER_HANDLES, EnumMaterial.STEEL, 4);

        addScrapable(ModItems.MOTHERBOARD, EnumMaterial.GOLD, 1);
        addScrapable(ModItems.GPU, EnumMaterial.GOLD, 1);
        addScrapable(ModItems.HDD, EnumMaterial.GOLD, 1);
        addScrapable(ModItems.CPU, EnumMaterial.GOLD, 1);
        addScrapable(ModItems.POWER_SUPPLY, EnumMaterial.COPPER, 1);
        addScrapable(ModItems.LINK_TOOL, EnumMaterial.COPPER, 1);
        addScrapable(Items.BOOK, EnumMaterial.PAPER, 12);
        addScrapable(ModBlocks.CARDBOARD_BOX, EnumMaterial.PAPER, 8);
        addScrapable(Items.ENCHANTED_BOOK, EnumMaterial.PAPER, 16);
        addScrapable(ModItems.SURVIVAL_GUIDE, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_AMMO, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_CHEMISTRY, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_COMPUTERS, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_CONCRETE, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_FORGING, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_ELECTRICITY, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_METALWORKING, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_MINIBIKE, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.BOOK_PISTOL, EnumMaterial.PAPER, 14);
        addScrapable(ModItems.AUGER_SCHEMATICS, EnumMaterial.PAPER, 2);
        addScrapable(ModItems.SHOTGUN_SCHEMATICS, EnumMaterial.PAPER, 2);
        addScrapable(ModItems.SNIPER_SCHEMATICS, EnumMaterial.PAPER, 2);
        addScrapable(ModItems.MAGNUM_SCHEMATICS, EnumMaterial.PAPER, 2);
        addScrapable(ModItems.MP5_SCHEMATICS, EnumMaterial.PAPER, 2);
        addScrapable(ModItems.HUNTING_RIFLE_SCHEMATICS, EnumMaterial.PAPER, 2);
        addScrapable(ModItems.ROCKET_SCHEMATICS, EnumMaterial.PAPER, 2);
        addScrapResult(ModItems.CEMENT, EnumMaterial.CONCRETE);
        addScrapable(ModBlocks.CINDER, EnumMaterial.CONCRETE,6);
        addScrapable(ModItems.CONCRETE_MIX, EnumMaterial.CONCRETE,1);

        addScrapable(Blocks.CONCRETE, EnumMaterial.CONCRETE,1);
        addScrapable(Blocks.CONCRETE_POWDER, EnumMaterial.CONCRETE,1);
        addScrapable(Blocks.STONE_SLAB, EnumMaterial.STONE, Fraction.getFraction(9,2));
        addScrapable(Blocks.STONE_SLAB, EnumMaterial.STONE, Fraction.getFraction(9,2));
        addScrapable(Blocks.STONE_SLAB2, EnumMaterial.STONE, Fraction.getFraction(9,2));
        addScrapable(Blocks.STONEBRICK, EnumMaterial.STONE, 9);
        addScrapable(Blocks.STONE_PRESSURE_PLATE, EnumMaterial.STONE, 2);
        addScrapable(Blocks.STONE_BUTTON, EnumMaterial.STONE, 1);

        addScrapable(Blocks.WOODEN_PRESSURE_PLATE, EnumMaterial.WOOD, 2);
        addScrapable(Blocks.WOODEN_BUTTON, EnumMaterial.WOOD, 1);
        addScrapable(Blocks.WOODEN_SLAB, EnumMaterial.WOOD,  Fraction.getFraction(9,2));

        addScrapable(ModBlocks.MARBLE, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.MARBLE_COBBLESTONE, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.MARBLE_POLISHED, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.MARBLE_BRICKS, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.MARBLE_BRICKS_CRACKED, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.MARBLE_BRICKS_MOSSY, EnumMaterial.STONE, 9);

        addScrapable(ModBlocks.BASALT, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.BASALT_COBBLESTONE, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.BASALT_POLISHED, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.BASALT_BRICKS, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.BASALT_BRICKS_CRACKED, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.BASALT_BRICKS_MOSSY, EnumMaterial.STONE, 9);

        addScrapable(ModBlocks.RHYOLITE, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.RHYOLITE_COBBLESTONE, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.RHYOLITE_POLISHED, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.RHYOLITE_BRICKS, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.RHYOLITE_BRICKS_CRACKED, EnumMaterial.STONE, 9);
        addScrapable(ModBlocks.RHYOLITE_BRICKS_MOSSY, EnumMaterial.STONE, 9);

        addScrapable(ModBlocks.STONE_BRICK_STAIRS_CRACKED, EnumMaterial.STONE, Fraction.getFraction(27,2));
        addScrapable(Blocks.STONE_STAIRS, EnumMaterial.STONE, Fraction.getFraction(27,2));
        addScrapable(Blocks.STONE_BRICK_STAIRS, EnumMaterial.STONE, Fraction.getFraction(27,2));
        addScrapable(Blocks.SAND, EnumMaterial.SAND, 9);
        addScrapable(ModBlocks.SANDBAGS, EnumMaterial.SAND, 8);

        removeScraps();
    }
}
