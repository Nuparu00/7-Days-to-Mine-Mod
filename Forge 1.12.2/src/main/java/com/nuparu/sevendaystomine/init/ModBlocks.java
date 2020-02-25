package com.nuparu.sevendaystomine.init;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockArmchair;
import com.nuparu.sevendaystomine.block.BlockBackpack;
import com.nuparu.sevendaystomine.block.BlockBaneberry;
import com.nuparu.sevendaystomine.block.BlockBase;
import com.nuparu.sevendaystomine.block.BlockBigSignMaster;
import com.nuparu.sevendaystomine.block.BlockBigSignSlave;
import com.nuparu.sevendaystomine.block.BlockBirdNest;
import com.nuparu.sevendaystomine.block.BlockBlueberry;
import com.nuparu.sevendaystomine.block.BlockBoards;
import com.nuparu.sevendaystomine.block.BlockBookshelfEnhanced;
import com.nuparu.sevendaystomine.block.BlockBush;
import com.nuparu.sevendaystomine.block.BlockCampfire;
import com.nuparu.sevendaystomine.block.BlockCardboardBox;
import com.nuparu.sevendaystomine.block.BlockCatwalkBase;
import com.nuparu.sevendaystomine.block.BlockCatwalkStairs;
import com.nuparu.sevendaystomine.block.BlockChair;
import com.nuparu.sevendaystomine.block.BlockChemistryStation;
import com.nuparu.sevendaystomine.block.BlockChestOld;
import com.nuparu.sevendaystomine.block.BlockCinder;
import com.nuparu.sevendaystomine.block.BlockCodeSafe;
import com.nuparu.sevendaystomine.block.BlockCoffeePlant;
import com.nuparu.sevendaystomine.block.BlockComputer;
import com.nuparu.sevendaystomine.block.BlockCookware;
import com.nuparu.sevendaystomine.block.BlockCornPlant;
import com.nuparu.sevendaystomine.block.BlockCorpse;
import com.nuparu.sevendaystomine.block.BlockCupboard;
import com.nuparu.sevendaystomine.block.BlockDoorLocked;
import com.nuparu.sevendaystomine.block.BlockDresser;
import com.nuparu.sevendaystomine.block.BlockEnergyPole;
import com.nuparu.sevendaystomine.block.BlockFlag;
import com.nuparu.sevendaystomine.block.BlockForge;
import com.nuparu.sevendaystomine.block.BlockGarbage;
import com.nuparu.sevendaystomine.block.BlockGasoline;
import com.nuparu.sevendaystomine.block.BlockGenerator;
import com.nuparu.sevendaystomine.block.BlockGoldenrod;
import com.nuparu.sevendaystomine.block.BlockKeySafe;
import com.nuparu.sevendaystomine.block.BlockLadderMetal;
import com.nuparu.sevendaystomine.block.BlockLamp;
import com.nuparu.sevendaystomine.block.BlockMailBox;
import com.nuparu.sevendaystomine.block.BlockMedicalCabinet;
import com.nuparu.sevendaystomine.block.BlockMicrowave;
import com.nuparu.sevendaystomine.block.BlockMonitor;
import com.nuparu.sevendaystomine.block.BlockOre;
import com.nuparu.sevendaystomine.block.BlockPhoto;
import com.nuparu.sevendaystomine.block.BlockPlanksFrame;
import com.nuparu.sevendaystomine.block.BlockPlanksReinforced;
import com.nuparu.sevendaystomine.block.BlockPlanksReinforcedIron;
import com.nuparu.sevendaystomine.block.BlockRadiator;
import com.nuparu.sevendaystomine.block.BlockRebarFrame;
import com.nuparu.sevendaystomine.block.BlockRebarFrameWood;
import com.nuparu.sevendaystomine.block.BlockRefrigerator;
import com.nuparu.sevendaystomine.block.BlockReinforcedConcrete;
import com.nuparu.sevendaystomine.block.BlockReinforcedConcreteWet;
import com.nuparu.sevendaystomine.block.BlockRock;
import com.nuparu.sevendaystomine.block.BlockScreenProjector;
import com.nuparu.sevendaystomine.block.BlockSedan;
import com.nuparu.sevendaystomine.block.BlockShowerHead;
import com.nuparu.sevendaystomine.block.BlockSinkFaucet;
import com.nuparu.sevendaystomine.block.BlockSkeleton;
import com.nuparu.sevendaystomine.block.BlockSleepingBag;
import com.nuparu.sevendaystomine.block.BlockSmallRock;
import com.nuparu.sevendaystomine.block.BlockSofa;
import com.nuparu.sevendaystomine.block.BlockStick;
import com.nuparu.sevendaystomine.block.BlockStoneBase;
import com.nuparu.sevendaystomine.block.BlockStoneBrickWall;
import com.nuparu.sevendaystomine.block.BlockStreetSign;
import com.nuparu.sevendaystomine.block.BlockTable;
import com.nuparu.sevendaystomine.block.BlockTelevisionBroken;
import com.nuparu.sevendaystomine.block.BlockThrottle;
import com.nuparu.sevendaystomine.block.BlockToilet;
import com.nuparu.sevendaystomine.block.BlockTorchEnhanced;
import com.nuparu.sevendaystomine.block.BlockTrafficLight;
import com.nuparu.sevendaystomine.block.BlockTrafficLightPedestrian;
import com.nuparu.sevendaystomine.block.BlockTrashBin;
import com.nuparu.sevendaystomine.block.BlockTrashCan;
import com.nuparu.sevendaystomine.block.BlockUnlitTorch;
import com.nuparu.sevendaystomine.block.BlockWallClock;
import com.nuparu.sevendaystomine.block.BlockWallStreetSign;
import com.nuparu.sevendaystomine.block.BlockWheels;
import com.nuparu.sevendaystomine.block.BlockWoodenDoor;
import com.nuparu.sevendaystomine.block.BlockWoodenDoorReinforced;
import com.nuparu.sevendaystomine.block.BlockWoodenSpikes;
import com.nuparu.sevendaystomine.block.BlockWorkbench;
import com.nuparu.sevendaystomine.block.BlockWritingTable;
import com.nuparu.sevendaystomine.item.EnumMaterial;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;

public class ModBlocks {
	public static final Block OAK_FRAME = new BlockPlanksFrame(BlockPlanks.EnumType.OAK).setRegistryName("OakPlanksFrame").setUnlocalizedName("OakPlanksFrame");
	public static final Block BIRCH_FRAME = new BlockPlanksFrame(BlockPlanks.EnumType.BIRCH).setRegistryName("BirchPlanksFrame").setUnlocalizedName("BirchPlanksFrame");
	public static final Block SPRUCE_FRAME = new BlockPlanksFrame(BlockPlanks.EnumType.SPRUCE).setRegistryName("SprucePlanksFrame").setUnlocalizedName("SprucePlanksFrame");
	public static final Block JUNGLE_FRAME = new BlockPlanksFrame(BlockPlanks.EnumType.JUNGLE).setRegistryName("JunglePlanksFrame").setUnlocalizedName("JunglePlanksFrame");
	public static final Block ACACIA_FRAME = new BlockPlanksFrame(BlockPlanks.EnumType.ACACIA).setRegistryName("AcaciaPlanksFrame").setUnlocalizedName("AcaciaPlanksFrame");
	public static final Block DARKOAK_FRAME = new BlockPlanksFrame(BlockPlanks.EnumType.DARK_OAK).setRegistryName("DarkOakPlanksFrame").setUnlocalizedName("DarkOakPlanksFrame");
	
	public static final Block OAK_PLANKS_REINFORCED_IRON = new BlockPlanksReinforcedIron().setRegistryName("OakPlanksReinforcedIron").setUnlocalizedName("OakPlanksReinforcedIron");
	public static final Block BIRCH_PLANKS_REINFORCED_IRON = new BlockPlanksReinforcedIron().setRegistryName("BirchPlanksReinforcedIron").setUnlocalizedName("BirchPlanksReinforcedIron");
	public static final Block SPRUCE_PLANKS_REINFORCED_IRON = new BlockPlanksReinforcedIron().setRegistryName("SprucePlanksReinforcedIron").setUnlocalizedName("SprucePlanksReinforcedIron");
	public static final Block JUNGLE_PLANKS_REINFORCED_IRON = new BlockPlanksReinforcedIron().setRegistryName("JunglePlanksReinforcedIron").setUnlocalizedName("JunglePlanksReinforcedIron");
	public static final Block ACACIA_PLANKS_REINFORCED_IRON = new BlockPlanksReinforcedIron().setRegistryName("AcaciaPlanksReinforcedIron").setUnlocalizedName("AcaciaPlanksReinforcedIron");
	public static final Block DARKOAK_PLANKS_REINFORCED_IRON = new BlockPlanksReinforcedIron().setRegistryName("DarkOakPlanksReinforcedIron").setUnlocalizedName("DarkOakPlanksReinforcedIron");
	
	public static final Block OAK_PLANKS_REINFORCED = new BlockPlanksReinforced(BlockPlanks.EnumType.OAK).setRegistryName("OakPlanksReinforced").setUnlocalizedName("OakPlanksReinforced");
	public static final Block BIRCH_PLANKS_REINFORCED = new BlockPlanksReinforced(BlockPlanks.EnumType.BIRCH).setRegistryName("BirchPlanksReinforced").setUnlocalizedName("BirchPlanksReinforced");
	public static final Block SPRUCE_PLANKS_REINFORCED = new BlockPlanksReinforced(BlockPlanks.EnumType.SPRUCE).setRegistryName("SprucePlanksReinforced").setUnlocalizedName("SprucePlanksReinforced");
	public static final Block JUNGLE_PLANKS_REINFORCED = new BlockPlanksReinforced(BlockPlanks.EnumType.JUNGLE).setRegistryName("JunglePlanksReinforced").setUnlocalizedName("JunglePlanksReinforced");
	public static final Block ACACIA_PLANKS_REINFORCED = new BlockPlanksReinforced(BlockPlanks.EnumType.ACACIA).setRegistryName("AcaciaPlanksReinforced").setUnlocalizedName("AcaciaPlanksReinforced");
	public static final Block DARKOAK_PLANKS_REINFORCED = new BlockPlanksReinforced(BlockPlanks.EnumType.DARK_OAK).setRegistryName("DarkOakPlanksReinforced").setUnlocalizedName("DarkOakPlanksReinforced");
	
	public static final Block SMALL_STONE = new BlockSmallRock().setRegistryName("SmallRock").setUnlocalizedName("SmallRock");
	public static final Block STICK = new BlockStick().setRegistryName("Stick").setUnlocalizedName("Stick");
	public static final Block BUSH = new BlockBush().setRegistryName("Bush").setUnlocalizedName("Bush");
	public static final Block GOLDENROD = new BlockGoldenrod().setRegistryName("Goldenrod").setUnlocalizedName("Goldenrod");
	public static final Block COFFEE_PLANT = new BlockCoffeePlant().setRegistryName("CoffeePlant").setUnlocalizedName("CoffeePlant");
	public static final Block CORN_PLANT = new BlockCornPlant().setRegistryName("CornPlant").setUnlocalizedName("CornPlant");
	public static final Block BLUEBERRY_PLANT = new BlockBlueberry().setRegistryName("BlueberryPlant").setUnlocalizedName("BlueberryPlant");
	public static final Block BANEBERRY_PLANT = new BlockBaneberry().setRegistryName("BaneberryPlant").setUnlocalizedName("BaneberryPlant");
	
	
	public static final Block CAMPFIRE = new BlockCampfire(false).setRegistryName("Campfire").setUnlocalizedName("Campfire").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block CAMPFIRE_LIT = new BlockCampfire(true).setRegistryName("Campfire_Lit").setUnlocalizedName("Campfire");
	public static final Block FORGE = new BlockForge(false).setRegistryName("forge").setUnlocalizedName("forge").setCreativeTab(SevenDaysToMine.TAB_FORGING);
	public static final Block FORGE_LIT = new BlockForge(true).setRegistryName("forge_lit").setUnlocalizedName("forge_lit");
	
	public static final Block COOKING_POT = new BlockCookware(Material.IRON,EnumMaterial.IRON,SoundType.METAL,new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D)).setRegistryName("CookingPot").setUnlocalizedName("CookingPot");
	public static final Block BEAKER = new BlockCookware(Material.GLASS,EnumMaterial.GLASS,SoundType.GLASS,new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D)).setRegistryName("Beaker").setUnlocalizedName("Beaker");
	
	public static final Block ORE_COPPER = new BlockOre(EnumMaterial.COPPER).setRegistryName("OreCopper").setUnlocalizedName("OreCopper").setHardness(35F).setResistance(5.0F);
	public static final Block ORE_TIN = new BlockOre(EnumMaterial.TIN).setRegistryName("OreTin").setUnlocalizedName("OreTin").setHardness(35F).setResistance(5.0F);
	public static final Block ORE_ZINC = new BlockOre(EnumMaterial.ZINC).setRegistryName("OreZinc").setUnlocalizedName("OreZinc").setHardness(35F).setResistance(5.0F);
	public static final Block ORE_LEAD = new BlockOre(EnumMaterial.LEAD).setRegistryName("OreLead").setUnlocalizedName("OreLead").setHardness(35F).setResistance(5.0F);
	public static final Block ORE_POTASSIUM = new BlockOre(EnumMaterial.POTASSIUM).setRegistryName("OrePotassium").setUnlocalizedName("OrePotassium").setHardness(35F).setResistance(5.0F);
	public static final Block ORE_CINNABAR = new BlockOre(EnumMaterial.MERCURY).setRegistryName("OreCinnabar").setUnlocalizedName("OreCinnabar").setHardness(35F).setResistance(5.0F);
	
	public static final Block REBAR_FRAME = new BlockRebarFrame().setRegistryName("RebarFrame").setUnlocalizedName("RebarFrame");
	public static final Block REBAR_FRAME_WOOD = new BlockRebarFrameWood().setRegistryName("RebarFrameWood").setUnlocalizedName("RebarFrameWood");
	public static final Block REINFORCED_CONCRETE_WET = new BlockReinforcedConcreteWet().setRegistryName("ReinforcedConcreteWet").setUnlocalizedName("ReinforcedConcreteWet");
	public static final Block REINFORCED_CONCRETE = new BlockReinforcedConcrete().setRegistryName("ReinforcedConcrete").setUnlocalizedName("ReinforcedConcrete");
	public static final Block ASPHALT = new BlockBase(Material.ROCK).setRegistryName("Asphalt").setUnlocalizedName("Asphalt").setHardness(35.0F).setResistance(35.0F).setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	
	
	public static final Block CODE_SAFE = new BlockCodeSafe().setRegistryName("CodeSafe").setUnlocalizedName("CodeSafe");
	public static final Block KEY_SAFE = new BlockKeySafe().setRegistryName("KeySafe").setUnlocalizedName("KeySafe");
	
	public static final Block CARDBOARD_BOX = new BlockCardboardBox().setRegistryName("CardboardBox").setUnlocalizedName("CardboardBox").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block CUPBOARD = new BlockCupboard().setRegistryName("Cupboard").setUnlocalizedName("Cupboard").setCreativeTab(SevenDaysToMine.TAB_BUILDING);	
	public static final Block GARBAGE = new BlockGarbage().setRegistryName("Garbage").setUnlocalizedName("Garbage").setCreativeTab(SevenDaysToMine.TAB_BUILDING);	
	public static final Block BOOKSHELF = new BlockBookshelfEnhanced().setRegistryName("Bookshelf").setUnlocalizedName("Bookshelf").setCreativeTab(SevenDaysToMine.TAB_BUILDING);	
	public static final Block WRITING_TABLE = new BlockWritingTable().setRegistryName("WritingTable").setUnlocalizedName("WritingTable");	
	public static final Block MEDICAL_CABINET = new BlockMedicalCabinet().setRegistryName("MedicalCabinet").setUnlocalizedName("MedicalCabinet").setCreativeTab(SevenDaysToMine.TAB_BUILDING);	
	public static final Block FRIDGE = new BlockRefrigerator().setRegistryName("Fridge").setUnlocalizedName("Fridge");	
	public static final Block MAIL_BOX = new BlockMailBox().setRegistryName("MailBox").setUnlocalizedName("MailBox");	
	public static final Block BIRD_NEST = new BlockBirdNest().setRegistryName("BirdNest").setUnlocalizedName("BirdNest");	
	public static final Block TRASH_CAN = new BlockTrashCan().setRegistryName("Trashcan").setUnlocalizedName("Trashcan");	
	
	public static final Block TORCH_UNLIT = new BlockUnlitTorch().setRegistryName("TorchUnlit").setUnlocalizedName("TorchUnlit");
	public static final Block TORCH_LIT = new BlockTorchEnhanced().setRegistryName("TorchLit").setUnlocalizedName("TorchLit");
	
	public static final Block ARMCHAIR_BLACK = new BlockArmchair().setRegistryName("ArmchairBlack").setUnlocalizedName("ArmchairBlack");
	public static final Block ARMCHAIR_WHITE = new BlockArmchair().setRegistryName("ArmchairWhite").setUnlocalizedName("ArmchairWhite");
	public static final Block ARMCHAIR_RED = new BlockArmchair().setRegistryName("ArmchairRed").setUnlocalizedName("ArmchairRed");
	public static final Block ARMCHAIR_GREEN = new BlockArmchair().setRegistryName("ArmchairGreen").setUnlocalizedName("ArmchairGreen");
	public static final Block ARMCHAIR_BROWN = new BlockArmchair().setRegistryName("ArmchairBrown").setUnlocalizedName("ArmchairBrown");
	public static final Block ARMCHAIR_PINK = new BlockArmchair().setRegistryName("ArmchairPink").setUnlocalizedName("ArmchairPink");
	public static final Block ARMCHAIR_YELLOW = new BlockArmchair().setRegistryName("ArmchairYellow").setUnlocalizedName("ArmchairYellow");
	public static final Block ARMCHAIR_BLUE = new BlockArmchair().setRegistryName("ArmchairBlue").setUnlocalizedName("ArmchairBlue");
	public static final Block SLEEPING_BAG = new BlockSleepingBag().setRegistryName("SleepingBag").setUnlocalizedName("SleepingBag");
	public static final Block THROTTLE = new BlockThrottle().setRegistryName("throttle").setUnlocalizedName("throttle");
	
	public static final Block WOODEN_DOOR = new BlockWoodenDoor().setRegistryName("wooden_door").setUnlocalizedName("wooden_door");
	public static final Block WOODEN_DOOR_REINFORCED = new BlockWoodenDoorReinforced().setRegistryName("wooden_door_reinforced").setUnlocalizedName("wooden_door_reinforced");
	public static final Block LOCKED_DOOR = new BlockDoorLocked().setRegistryName("locked_door").setUnlocalizedName("locked_door");
	public static final Block WOODEN_DOOR_IRON_REINFORCED = new BlockWoodenDoorReinforced().setRegistryName("wooden_door_iron_reinforced").setUnlocalizedName("wooden_door_iron_reinforced");
	
	public static final Block TRAFFIC_LIGHT = new BlockTrafficLight().setRegistryName("TrafficLight").setUnlocalizedName("TrafficLight");
	public static final Block TRAFFIC_LIGHT_PEDESTRIAN = new BlockTrafficLightPedestrian().setRegistryName("TrafficLightPedestrian").setUnlocalizedName("TrafficLightPedestrian");
	
	public static final Block SEDAN_RED = new BlockSedan().setRegistryName("sedan_red").setUnlocalizedName("sedan_red");
	public static final Block SEDAN_GREEN = new BlockSedan().setRegistryName("sedan_green").setUnlocalizedName("sedan_green");
	public static final Block SEDAN_BLUE = new BlockSedan().setRegistryName("sedan_blue").setUnlocalizedName("sedan_blue");
	public static final Block SEDAN_YELLOW = new BlockSedan().setRegistryName("sedan_yellow").setUnlocalizedName("sedan_yellow");
	public static final Block SEDAN_WHITE = new BlockSedan().setRegistryName("sedan_white").setUnlocalizedName("sedan_white");
	public static final Block SEDAN_BLACK = new BlockSedan().setRegistryName("sedan_black").setUnlocalizedName("sedan_black");
	
	
	public static final Block DEAD_MOSSY_STONE = new BlockStoneBase().setRegistryName("DeadMossyStone").setUnlocalizedName("DeadMossyStone");
	public static final Block DEAD_MOSSY_BRICK = new BlockStoneBase().setRegistryName("DeadMossyBrick").setUnlocalizedName("DeadMossyBrick");
	
	
	public static final Block BASALT = new BlockStoneBase().setRegistryName("Basalt").setUnlocalizedName("Basalt");
	public static final Block MARBLE = new BlockStoneBase().setRegistryName("Marble").setUnlocalizedName("Marble");
	public static final Block RHYOLITE = new BlockStoneBase().setRegistryName("Rhyolite").setUnlocalizedName("Rhyolite");
	
	public static final Block BASALT_COBBLESTONE = new BlockStoneBase().setRegistryName("BasaltCobblestone").setUnlocalizedName("BasaltCobblestone");
	public static final Block MARBLE_COBBLESTONE = new BlockStoneBase().setRegistryName("MarbleCobblestone").setUnlocalizedName("MarbleCobblestone");
	public static final Block RHYOLITE_COBBLESTONE = new BlockStoneBase().setRegistryName("RhyoliteCobblestone").setUnlocalizedName("RhyoliteCobblestone");
	
	public static final Block BASALT_BRICKS = new BlockStoneBase().setRegistryName("BasaltBricks").setUnlocalizedName("BasaltBricks");
	public static final Block MARBLE_BRICKS = new BlockStoneBase().setRegistryName("MarbleBricks").setUnlocalizedName("MarbleBricks");
	public static final Block RHYOLITE_BRICKS = new BlockStoneBase().setRegistryName("RhyoliteBricks").setUnlocalizedName("RhyoliteBricks");
	
	public static final Block BASALT_POLISHED = new BlockStoneBase().setRegistryName("BasaltPolished").setUnlocalizedName("BasaltPolished");
	public static final Block MARBLE_POLISHED = new BlockStoneBase().setRegistryName("MarblePolished").setUnlocalizedName("MarblePolished");
	public static final Block RHYOLITE_POLISHED = new BlockStoneBase().setRegistryName("RhyolitePolished").setUnlocalizedName("RhyolitePolished");
	
	public static final Block ANDESITE_BRICKS = new BlockStoneBase().setRegistryName("AndesiteBricks").setUnlocalizedName("AndesiteBricks");
	public static final Block DIORITE_BRICKS = new BlockStoneBase().setRegistryName("DioriteBricks").setUnlocalizedName("DioriteBricks");
	public static final Block GRANITE_BRICKS = new BlockStoneBase().setRegistryName("GraniteBricks").setUnlocalizedName("GraniteBricks");
	
	public static final Block STONEBRICK_WALL = new BlockStoneBrickWall(Blocks.STONEBRICK).setRegistryName("StoneBrick_Wall").setUnlocalizedName("stonebrick_wall");

	public static final Block CATWALK = new BlockCatwalkBase().setRegistryName("Catwalk").setUnlocalizedName("Catwalk");
	public static final Block CATWALK_STAIRS = new BlockCatwalkStairs().setRegistryName("catwalk_stairs").setUnlocalizedName("catwalk_stairs");
	
	public static final Block BASALT_BRICKS_CRACKED = new BlockStoneBase().setRegistryName("basalt_bricks_cracked").setUnlocalizedName("basalt_bricks_cracked");
	public static final Block MARBLE_BRICKS_CRACKED = new BlockStoneBase().setRegistryName("marble_bricks_cracked").setUnlocalizedName("marble_bricks_cracked");
	public static final Block RHYOLITE_BRICKS_CRACKED = new BlockStoneBase().setRegistryName("rhyolite_bricks_cracked").setUnlocalizedName("rhyolite_bricks_cracked");
	
	
	public static final Block ANDESITE_BRICKS_CRACKED = new BlockStoneBase().setRegistryName("andesite_bricks_cracked").setUnlocalizedName("andesite_bricks_cracked");
	public static final Block DIORITE_BRICKS_CRACKED = new BlockStoneBase().setRegistryName("diorite_bricks_cracked").setUnlocalizedName("diorite_bricks_cracked");
	public static final Block GRANITE_BRICKS_CRACKED = new BlockStoneBase().setRegistryName("granite_bricks_cracked").setUnlocalizedName("granite_bricks_cracked");
	
	public static final Block BASALT_BRICKS_MOSSY = new BlockStoneBase().setRegistryName("basalt_bricks_mossy").setUnlocalizedName("basalt_bricks_mossy");
	public static final Block MARBLE_BRICKS_MOSSY = new BlockStoneBase().setRegistryName("marble_bricks_mossy").setUnlocalizedName("marble_bricks_mossy");
	public static final Block RHYOLITE_BRICKS_MOSSY = new BlockStoneBase().setRegistryName("rhyolite_bricks_mossy").setUnlocalizedName("rhyolite_bricks_mossy");
	
	
	public static final Block ANDESITE_BRICKS_MOSSY = new BlockStoneBase().setRegistryName("andesite_bricks_mossy").setUnlocalizedName("andesite_bricks_mossy");
	public static final Block DIORITE_BRICKS_MOSSY = new BlockStoneBase().setRegistryName("diorite_bricks_mossy").setUnlocalizedName("diorite_bricks_mossy");
	public static final Block GRANITE_BRICKS_MOSSY = new BlockStoneBase().setRegistryName("granite_bricks_mossy").setUnlocalizedName("granite_bricks_mossy");
	
	public static final Block COMPUTER = new BlockComputer().setRegistryName("computer").setUnlocalizedName("computer").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Block MONITOR_OFF = new BlockMonitor().setRegistryName("monitor_off").setUnlocalizedName("monitor_off").setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	public static final Block MONITOR_MAC = new BlockMonitor().setRegistryName("monitor_mac").setUnlocalizedName("monitor_mac");
	public static final Block MONITOR_LINUX = new BlockMonitor().setRegistryName("monitor_linux").setUnlocalizedName("monitor_linux");
	public static final Block MONITOR_WIN98 = new BlockMonitor().setRegistryName("monitor_win98").setUnlocalizedName("monitor_win98");
	public static final Block MONITOR_WINXP = new BlockMonitor().setRegistryName("monitor_winxp").setUnlocalizedName("monitor_winxp");
	public static final Block MONITOR_WIN7 = new BlockMonitor().setRegistryName("monitor_win7").setUnlocalizedName("monitor_win7");
	public static final Block MONITOR_WIN8 = new BlockMonitor().setRegistryName("monitor_win8").setUnlocalizedName("monitor_win8");
	public static final Block MONITOR_WIN10 = new BlockMonitor().setRegistryName("monitor_win10").setUnlocalizedName("monitor_win10");
	
	public static final Block WALL_CLOCK = new BlockWallClock().setRegistryName("wall_clock").setUnlocalizedName("wall_clock");
	public static final Block BOARDS = new BlockBoards().setRegistryName("boards").setUnlocalizedName("boards").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block TOILET = new BlockToilet().setRegistryName("toilet").setUnlocalizedName("toilet").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block FLAG = new BlockFlag().setRegistryName("flag").setUnlocalizedName("flag").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block MICROWAVE = new BlockMicrowave().setRegistryName("microwave").setUnlocalizedName("microwave").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block SINK_FAUCET = new BlockSinkFaucet().setRegistryName("sink_faucet").setUnlocalizedName("sink_faucet");
	public static final Block METAL_LADDER = new BlockLadderMetal().setRegistryName("metal_ladder").setUnlocalizedName("metal_ladder");
	public static final Block RADIATOR = new BlockRadiator().setRegistryName("radiator").setUnlocalizedName("radiator");
	
	public static final Block DARK_BRICKS = new BlockStoneBase().setRegistryName("dark_bricks").setUnlocalizedName("dark_bricks");
	public static final Block CINDER = new BlockCinder().setRegistryName("cinder").setUnlocalizedName("cinder");
	
	public static final Block BACKPACK_NORMAL = new BlockBackpack().setRegistryName("backpack_normal").setUnlocalizedName("backpack_normal");
	public static final Block BACKPACK_ARMY = new BlockBackpack().setRegistryName("backpack_army").setUnlocalizedName("backpack_army");
	public static final Block BACKPACK_MEDICAL = new BlockBackpack().setRegistryName("backpack_medical").setUnlocalizedName("backpack_medical");
	
	public static final Block CHEST_OLD = new BlockChestOld().setRegistryName("chest_old").setUnlocalizedName("chest_old");
	public static final Block TV_BROKEN = new BlockTelevisionBroken().setRegistryName("television_broken").setUnlocalizedName("television_broken");
	public static final Block SHOWER_HEAD = new BlockShowerHead().setRegistryName("shower_head").setUnlocalizedName("shower_head");
	
	public static final Block CORPSE_00 = new BlockCorpse().setRegistryName("corpse_00").setUnlocalizedName("corpse_00");
	public static final Block CORPSE_01 = new BlockCorpse().setRegistryName("corpse_01").setUnlocalizedName("corpse_01");
	public static final Block SKELETON = new BlockSkeleton().setRegistryName("skeleton").setUnlocalizedName("skeleton");
	public static final Block SKELETON_SITTING = new BlockSkeleton().setRegistryName("skeleton_sitting").setUnlocalizedName("skeleton_sitting");
	
	public static final Block WORKBENCH = new BlockWorkbench().setRegistryName("workbench").setUnlocalizedName("workbench_real");
	public static final Block CHEMISTRY_STATION = new BlockChemistryStation().setRegistryName("chemistry_station").setUnlocalizedName("chemistry_station");
	
	public static final Block CHAIR_OAK = new BlockChair().setRegistryName("chair_oak").setUnlocalizedName("chair_oak");
	public static final Block CHAIR_BIRCH = new BlockChair().setRegistryName("chair_birch").setUnlocalizedName("chair_birch");
	public static final Block CHAIR_SPRUCE = new BlockChair().setRegistryName("chair_spruce").setUnlocalizedName("chair_spruce");
	public static final Block CHAIR_JUNGLE = new BlockChair().setRegistryName("chair_jungle").setUnlocalizedName("chair_jungle");
	public static final Block CHAIR_ACACIA = new BlockChair().setRegistryName("chair_acacia").setUnlocalizedName("chair_acacia");
	public static final Block CHAIR_BIG_OAK = new BlockChair().setRegistryName("chair_big_oak").setUnlocalizedName("chair_big_oak");
	
	public static final Block TABLE_OAK = new BlockTable(Material.WOOD).setRegistryName("table_oak").setUnlocalizedName("table_oak");
	public static final Block TABLE_BIRCH = new BlockTable(Material.WOOD).setRegistryName("table_birch").setUnlocalizedName("table_birch");
	public static final Block TABLE_SPRUCE = new BlockTable(Material.WOOD).setRegistryName("table_spruce").setUnlocalizedName("table_spruce");
	public static final Block TABLE_JUNGLE = new BlockTable(Material.WOOD).setRegistryName("table_jungle").setUnlocalizedName("table_jungle");
	public static final Block TABLE_ACACIA = new BlockTable(Material.WOOD).setRegistryName("table_acacia").setUnlocalizedName("table_acacia");
	public static final Block TABLE_BIG_OAK = new BlockTable(Material.WOOD).setRegistryName("table_big_oak").setUnlocalizedName("table_big_oak");
	
	public static final Block GENERATOR_GAS = new BlockGenerator(Material.IRON).setRegistryName("generator_gas").setUnlocalizedName("generator_gas");
	public static final Block GASOLINE = new BlockGasoline(ModFluids.GASOLINE,Material.WATER).setRegistryName("gasoline").setUnlocalizedName("gasoline");
	public static final Block ENERGY_POLE = new BlockEnergyPole().setRegistryName("energy_pole").setUnlocalizedName("energy_pole");
	public static final Block LAMP = new BlockLamp().setRegistryName("lamp").setUnlocalizedName("lamp");
	public static final Block STREET_SIGN = new BlockStreetSign().setRegistryName("street_sign").setUnlocalizedName("street_sign");
	public static final Block STREET_SIGN_WALL = new BlockWallStreetSign().setRegistryName("street_sign_wall").setUnlocalizedName("street_sign_wall");
	public static final Block PHOTO = new BlockPhoto().setRegistryName("photo").setUnlocalizedName("photo");
	public static final Block SCREEN_PROJECTOR = new BlockScreenProjector().setRegistryName("screen_projector").setUnlocalizedName("screen_projector");
	public static final Block DRESSER = new BlockDresser().setRegistryName("dresser").setUnlocalizedName("dresser").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block BIG_SIGN_MASTER = new BlockBigSignMaster().setRegistryName("big_sign").setUnlocalizedName("big_sign");
	public static final Block BIG_SIGN_SLAVE = new BlockBigSignSlave().setRegistryName("big_sign_slave").setUnlocalizedName("big_sign");
	
	public static final Block SOFA_BLACK = new BlockSofa().setRegistryName("sofa_black").setUnlocalizedName("sofa_black").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block SOFA_WHITE = new BlockSofa().setRegistryName("sofa_white").setUnlocalizedName("sofa_white").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block SOFA_RED = new BlockSofa().setRegistryName("sofa_red").setUnlocalizedName("sofa_red").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block SOFA_GREEN = new BlockSofa().setRegistryName("sofa_green").setUnlocalizedName("sofa_green").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block SOFA_BLUE = new BlockSofa().setRegistryName("sofa_blue").setUnlocalizedName("sofa_blue").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block SOFA_BROWN = new BlockSofa().setRegistryName("sofa_brown").setUnlocalizedName("sofa_brown").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block SOFA_PINK = new BlockSofa().setRegistryName("sofa_pink").setUnlocalizedName("sofa_pink").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block SOFA_YELLOW = new BlockSofa().setRegistryName("sofa_yellow").setUnlocalizedName("sofa_yellow").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	
	public static final Block TRASH_BIN = new BlockTrashBin().setRegistryName("trash_bin").setUnlocalizedName("trash_bin").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block WHEELS = new BlockWheels().setRegistryName("wheels").setUnlocalizedName("wheels").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block LARGE_ROCK = new BlockRock().setRegistryName("large_rock").setUnlocalizedName("large_rock");
	
	public static final Block WOODEN_SPIKES = new BlockWoodenSpikes().setRegistryName("wooden_spikes").setUnlocalizedName("wooden_spikes").setCreativeTab(SevenDaysToMine.TAB_BUILDING);
	public static final Block WOODEN_SPIKES_BLOODED = new BlockWoodenSpikes().setRegistryName("wooden_spikes_blooded").setUnlocalizedName("wooden_spikes_blooded");
	public static final Block WOODEN_SPIKES_BROKEN = new BlockWoodenSpikes().setRegistryName("wooden_spikes_broken").setUnlocalizedName("wooden_spikes_broken");

}
