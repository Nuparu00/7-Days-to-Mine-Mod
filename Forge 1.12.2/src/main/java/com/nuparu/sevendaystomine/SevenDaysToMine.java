package com.nuparu.sevendaystomine;

import java.util.HashMap;
import java.util.List;

import com.nuparu.sevendaystomine.block.repair.BreakSavedData;
import com.nuparu.sevendaystomine.block.repair.RepairManager;
import com.nuparu.sevendaystomine.capability.CapabilityHandler;
import com.nuparu.sevendaystomine.capability.ExtendedChunk;
import com.nuparu.sevendaystomine.capability.ExtendedChunkStorage;
import com.nuparu.sevendaystomine.capability.ExtendedInventory;
import com.nuparu.sevendaystomine.capability.ExtendedInventoryStorage;
import com.nuparu.sevendaystomine.capability.ExtendedPlayer;
import com.nuparu.sevendaystomine.capability.ExtendedPlayerStorage;
import com.nuparu.sevendaystomine.capability.IExtendedChunk;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;
import com.nuparu.sevendaystomine.capability.IItemHandlerExtended;
import com.nuparu.sevendaystomine.capability.ILockedRecipe;
import com.nuparu.sevendaystomine.capability.LockedRecipe;
import com.nuparu.sevendaystomine.capability.LockedRecipeStorage;
import com.nuparu.sevendaystomine.client.renderer.RenderGlobalEnhanced;
import com.nuparu.sevendaystomine.command.CommandAirdrop;
import com.nuparu.sevendaystomine.command.CommandGenerateCity;
import com.nuparu.sevendaystomine.command.CommandGetBlockBreak;
import com.nuparu.sevendaystomine.command.CommandInfect;
import com.nuparu.sevendaystomine.command.CommandPlaceLegacyPrefab;
import com.nuparu.sevendaystomine.command.CommandPlacePrefab;
import com.nuparu.sevendaystomine.command.CommandSavePrefab;
import com.nuparu.sevendaystomine.command.CommandSetBlockBreak;
import com.nuparu.sevendaystomine.events.ClientEventHandler;
import com.nuparu.sevendaystomine.events.LivingEventHandler;
import com.nuparu.sevendaystomine.events.PlayerEventHandler;
import com.nuparu.sevendaystomine.events.TerrainGenEventHandler;
import com.nuparu.sevendaystomine.events.TickHandler;
import com.nuparu.sevendaystomine.events.WorldEventHandler;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModFluids;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.crafting.RecipeManager;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.proxy.CommonProxy;
import com.nuparu.sevendaystomine.util.GuiHandler;
import com.nuparu.sevendaystomine.util.VanillaManager;
import com.nuparu.sevendaystomine.world.gen.BlueberryWorldGenerator;
import com.nuparu.sevendaystomine.world.gen.CityWorldGenerator;
import com.nuparu.sevendaystomine.world.gen.CornWorldGenerator;
import com.nuparu.sevendaystomine.world.gen.GoldenrodWorldGenerator;
import com.nuparu.sevendaystomine.world.gen.OreWorldGenerator;
import com.nuparu.sevendaystomine.world.gen.RoadWorldGenerator;
import com.nuparu.sevendaystomine.world.gen.SmallFeatureWorldGenerator;
import com.nuparu.sevendaystomine.world.gen.StructureGenerator;
import com.nuparu.sevendaystomine.world.gen.city.CityHelper;
import com.nuparu.sevendaystomine.world.gen.structure.MapGenCity;
import com.nuparu.sevendaystomine.world.gen.structure.StructureCityPieces;
import com.nuparu.sevendaystomine.world.horde.HordeSavedData;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = SevenDaysToMine.MODID, version = SevenDaysToMine.VERSION, useMetadata = true)
public class SevenDaysToMine {
	public static final String MODID = "sevendaystomine";
	public static final String VERSION = "Beta 1.0";

	static final String CLIENT_PROXY_CLASS = "com.nuparu.sevendaystomine.proxy.ClientProxy";
	static final String SERVER_PROXY_CLASS = "com.nuparu.sevendaystomine.proxy.CommonProxy";

	@Instance(SevenDaysToMine.MODID)
	public static SevenDaysToMine instance;
	@SidedProxy(clientSide = SevenDaysToMine.CLIENT_PROXY_CLASS, serverSide = SevenDaysToMine.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	@SideOnly(Side.CLIENT)
	public static RenderGlobalEnhanced renderGlobalEnhanced;

	public static BreakSavedData breakSavedData;
	public static HordeSavedData hordeSavedData;

	public static HashMap<ChunkPos, Integer> ticketList;
	public static Ticket chunkLoaderTicket;

	static {
		// DataSerializers.registerSerializer(Utils.DIALOGUES);
		FluidRegistry.enableUniversalBucket();
		if (!FluidRegistry.isFluidRegistered(ModFluids.GASOLINE)) {
			FluidRegistry.registerFluid(ModFluids.GASOLINE);
			FluidRegistry.addBucketForFluid(ModFluids.GASOLINE);
		} else {
			ModFluids.GASOLINE = FluidRegistry.getFluid(ModFluids.GASOLINE.getName());
		}

		if (!FluidRegistry.isFluidRegistered(ModFluids.MERCURY)) {
			FluidRegistry.registerFluid(ModFluids.MERCURY);
			FluidRegistry.addBucketForFluid(ModFluids.MERCURY);
		} else {
			ModFluids.MERCURY = FluidRegistry.getFluid(ModFluids.MERCURY.getName());
		}
	}

	// CREATIVE TABS
	public static CreativeTabs TAB_MATERIALS = new CreativeTabs(MODID + ".materials") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.IRON_SCRAP);
		}
	};

	public static CreativeTabs TAB_BUILDING = new CreativeTabs(MODID + ".building") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.OAK_FRAME);
		}
	};

	public static CreativeTabs TAB_MEDICINE = new CreativeTabs(MODID + ".medicine") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.BANDAGE_ADVANCED);
		}
	};

	public static CreativeTabs TAB_FORGING = new CreativeTabs(MODID + ".forging") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ModBlocks.FORGE);
		}
	};

	public static CreativeTabs TAB_ELECTRICITY = new CreativeTabs(MODID + ".electricity") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.WIRE);
		}
	};

	public static CreativeTabs TAB_CLOTHING = new CreativeTabs(MODID + ".clothing") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.SHORTS);
		}
	};

	public static CreativeTabs TAB_BOOKS = new CreativeTabs(MODID + ".books") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.BOOK_FORGING);
		}
	};

	public static final Item.ToolMaterial STONE_TOOLS = EnumHelper.addToolMaterial("stone_tools", 1, 100, 3.5f, 14, 2);
	public static final Item.ToolMaterial BONE_TOOLS = EnumHelper.addToolMaterial("bone_tools", 1, 100, 2f, 14, 2);
	public static final Item.ToolMaterial CRUDE_TOOLS = EnumHelper.addToolMaterial("crude_tools", 1, 60, 2f, 16, 2);
	public static final Item.ToolMaterial WOODEN_TOOLS = EnumHelper.addToolMaterial("wooden_tools", 1, 180, 2f, 20, 2);
	public static final Item.ToolMaterial WOODEN_REINFORCED_TOOLS = EnumHelper
			.addToolMaterial("wooden_reinforced_tools", 1, 200, 2f, 25, 2);
	public static final Item.ToolMaterial BARBED_TOOLS = EnumHelper.addToolMaterial("barbed_tools", 1, 220, 2f, 30, 2);
	public static final Item.ToolMaterial SPIKED_TOOLS = EnumHelper.addToolMaterial("spiked_tools", 1, 250, 2f, 40, 2);
	public static final Item.ToolMaterial COPPER_TOOLS = EnumHelper.addToolMaterial("copper_tools", 1, 200, 4.8f, 16,
			2);
	public static final Item.ToolMaterial SCRAP_TOOLS = EnumHelper.addToolMaterial("copper_tools", 1, 125, 5.5f, 12, 2);
	public static final Item.ToolMaterial BRONZE_TOOLS = EnumHelper.addToolMaterial("bronze_tools", 1, 250, 6f, 18, 2);
	public static final Item.ToolMaterial IRON_TOOLS = EnumHelper.addToolMaterial("iron_tools", 1, 300, 8.2f, 22, 2);
	public static final Item.ToolMaterial STEEL_TOOLS = EnumHelper.addToolMaterial("steel_tools", 1, 40, 11f, 24, 2);
	public static final Item.ToolMaterial ARMY_TOOLS = EnumHelper.addToolMaterial("army_tools", 1, 350, 2f, 26, 2);
	public static final Item.ToolMaterial MACHETE = EnumHelper.addToolMaterial("machete", 1, 200, 2f, 35, 2);
	public static final Item.ToolMaterial SLEDGEHAMMER = EnumHelper.addToolMaterial("sledgehammer", 1, 40, 11f, 60, 2);
	public static final Item.ToolMaterial AUGER = EnumHelper.addToolMaterial("AUGER", 5, 600, 26F, 80, 0);

	public static final ItemArmor.ArmorMaterial CLOTHING = EnumHelper.addArmorMaterial("clothing",
			"sevendaystomine:clothing", 5, new int[] { 1, 2, 2, 1 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static final ItemArmor.ArmorMaterial FIBER = EnumHelper.addArmorMaterial("fiber", "sevendaystomine:fiber",
			10, new int[] { 1, 2, 2, 1 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
	public static final ItemArmor.ArmorMaterial STEEL_ARMOR = EnumHelper.addArmorMaterial("steel",
			"sevendaystomine:steel", 25, new int[] { 3, 4, 3, 2 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);
	public static final ItemArmor.ArmorMaterial LEATHER_IRON_ARMOR = EnumHelper.addArmorMaterial("leather_iron",
			"sevendaystomine:leather_iron", 12, new int[] { 2, 3, 2, 1 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

	public static final ItemArmor.ArmorMaterial SCRAP_ARMOR = EnumHelper.addArmorMaterial("scrap",
			"sevendaystomine:scrap", 10, new int[] { 2, 3, 1, 1 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

	public static final Block[] BLOCKS = new Block[] { ModBlocks.OAK_FRAME, ModBlocks.BIRCH_FRAME,
			ModBlocks.SPRUCE_FRAME, ModBlocks.JUNGLE_FRAME, ModBlocks.ACACIA_FRAME, ModBlocks.DARKOAK_FRAME,
			ModBlocks.OAK_PLANKS_REINFORCED, ModBlocks.BIRCH_PLANKS_REINFORCED, ModBlocks.SPRUCE_PLANKS_REINFORCED,
			ModBlocks.JUNGLE_PLANKS_REINFORCED, ModBlocks.ACACIA_PLANKS_REINFORCED, ModBlocks.DARKOAK_PLANKS_REINFORCED,
			ModBlocks.OAK_PLANKS_REINFORCED_IRON, ModBlocks.BIRCH_PLANKS_REINFORCED_IRON,
			ModBlocks.SPRUCE_PLANKS_REINFORCED_IRON, ModBlocks.JUNGLE_PLANKS_REINFORCED_IRON,
			ModBlocks.ACACIA_PLANKS_REINFORCED_IRON, ModBlocks.DARKOAK_PLANKS_REINFORCED_IRON, ModBlocks.SMALL_STONE,
			ModBlocks.STICK, ModBlocks.BUSH, ModBlocks.CAMPFIRE, ModBlocks.CAMPFIRE_LIT, ModBlocks.COOKING_POT,
			ModBlocks.BEAKER, ModBlocks.GOLDENROD, ModBlocks.COFFEE_PLANT, ModBlocks.CORN_PLANT,
			ModBlocks.BLUEBERRY_PLANT, ModBlocks.BANEBERRY_PLANT, ModBlocks.FORGE, ModBlocks.FORGE_LIT,
			ModBlocks.ORE_COPPER, ModBlocks.ORE_TIN, ModBlocks.ORE_ZINC, ModBlocks.ORE_LEAD, ModBlocks.ORE_POTASSIUM,
			ModBlocks.ORE_CINNABAR, ModBlocks.REBAR_FRAME, ModBlocks.REBAR_FRAME_WOOD, ModBlocks.REINFORCED_CONCRETE,
			ModBlocks.REINFORCED_CONCRETE_WET, ModBlocks.KEY_SAFE, ModBlocks.CODE_SAFE, ModBlocks.CARDBOARD_BOX,
			ModBlocks.CUPBOARD, ModBlocks.TORCH_UNLIT, ModBlocks.TORCH_LIT, ModBlocks.ASPHALT, ModBlocks.GARBAGE,
			ModBlocks.ARMCHAIR_BLACK, ModBlocks.ARMCHAIR_BLUE, ModBlocks.ARMCHAIR_BROWN, ModBlocks.ARMCHAIR_GREEN,
			ModBlocks.ARMCHAIR_PINK, ModBlocks.ARMCHAIR_RED, ModBlocks.ARMCHAIR_WHITE, ModBlocks.ARMCHAIR_YELLOW,
			ModBlocks.BOOKSHELF, ModBlocks.WRITING_TABLE, ModBlocks.MEDICAL_CABINET, ModBlocks.FRIDGE,
			ModBlocks.MAIL_BOX, ModBlocks.BIRD_NEST, ModBlocks.TRASH_CAN, ModBlocks.SLEEPING_BAG, ModBlocks.WOODEN_DOOR,
			ModBlocks.WOODEN_DOOR_REINFORCED, ModBlocks.TRAFFIC_LIGHT, ModBlocks.TRAFFIC_LIGHT_PEDESTRIAN,
			ModBlocks.SEDAN_RED, ModBlocks.SEDAN_GREEN, ModBlocks.SEDAN_BLUE, ModBlocks.SEDAN_YELLOW,
			ModBlocks.SEDAN_WHITE, ModBlocks.SEDAN_BLACK, ModBlocks.DEAD_MOSSY_STONE, ModBlocks.DEAD_MOSSY_BRICK,
			ModBlocks.BASALT, ModBlocks.MARBLE, ModBlocks.RHYOLITE, ModBlocks.BASALT_COBBLESTONE,
			ModBlocks.MARBLE_COBBLESTONE, ModBlocks.RHYOLITE_COBBLESTONE, ModBlocks.BASALT_BRICKS,
			ModBlocks.MARBLE_BRICKS, ModBlocks.RHYOLITE_BRICKS, ModBlocks.BASALT_POLISHED, ModBlocks.MARBLE_POLISHED,
			ModBlocks.RHYOLITE_POLISHED, ModBlocks.ANDESITE_BRICKS, ModBlocks.DIORITE_BRICKS, ModBlocks.GRANITE_BRICKS,
			ModBlocks.CATWALK, ModBlocks.CATWALK_STAIRS, ModBlocks.STONEBRICK_WALL, ModBlocks.THROTTLE,
			ModBlocks.BASALT_BRICKS_CRACKED, ModBlocks.MARBLE_BRICKS_CRACKED, ModBlocks.RHYOLITE_BRICKS_CRACKED,
			ModBlocks.ANDESITE_BRICKS_CRACKED, ModBlocks.DIORITE_BRICKS_CRACKED, ModBlocks.GRANITE_BRICKS_CRACKED,
			ModBlocks.BASALT_BRICKS_MOSSY, ModBlocks.MARBLE_BRICKS_MOSSY, ModBlocks.RHYOLITE_BRICKS_MOSSY,
			ModBlocks.ANDESITE_BRICKS_MOSSY, ModBlocks.DIORITE_BRICKS_MOSSY, ModBlocks.GRANITE_BRICKS_MOSSY,
			ModBlocks.COMPUTER, ModBlocks.MONITOR_OFF, ModBlocks.MONITOR_MAC, ModBlocks.MONITOR_LINUX,
			ModBlocks.MONITOR_WIN98, ModBlocks.MONITOR_WINXP, ModBlocks.MONITOR_WIN7, ModBlocks.MONITOR_WIN8,
			ModBlocks.MONITOR_WIN10/* , ModBlocks.WALL_CLOCK */, ModBlocks.LOCKED_DOOR, ModBlocks.BOARDS,
			ModBlocks.TOILET, ModBlocks.FLAG, ModBlocks.MICROWAVE, ModBlocks.SINK_FAUCET, ModBlocks.METAL_LADDER,
			ModBlocks.RADIATOR, ModBlocks.DARK_BRICKS, ModBlocks.CINDER, ModBlocks.BACKPACK_NORMAL,
			ModBlocks.BACKPACK_ARMY, ModBlocks.BACKPACK_MEDICAL, ModBlocks.CHEST_OLD, ModBlocks.TV_BROKEN,
			ModBlocks.SHOWER_HEAD, ModBlocks.CORPSE_00, ModBlocks.CORPSE_01, ModBlocks.SKELETON,
			ModBlocks.SKELETON_SITTING, ModBlocks.WORKBENCH, ModBlocks.CHEMISTRY_STATION, ModBlocks.CHAIR_OAK,
			ModBlocks.CHAIR_BIRCH, ModBlocks.CHAIR_SPRUCE, ModBlocks.CHAIR_JUNGLE, ModBlocks.CHAIR_ACACIA,
			ModBlocks.CHAIR_BIG_OAK, ModBlocks.TABLE_OAK, ModBlocks.TABLE_BIRCH, ModBlocks.TABLE_SPRUCE,
			ModBlocks.TABLE_JUNGLE, ModBlocks.TABLE_ACACIA, ModBlocks.TABLE_BIG_OAK, ModBlocks.GENERATOR_GAS,
			ModBlocks.GASOLINE, ModBlocks.ENERGY_POLE, ModBlocks.LAMP, ModBlocks.STREET_SIGN_WALL,
			ModBlocks.STREET_SIGN, ModBlocks.PHOTO, ModBlocks.SCREEN_PROJECTOR, ModBlocks.DRESSER,
			ModBlocks.BIG_SIGN_MASTER, ModBlocks.BIG_SIGN_SLAVE, ModBlocks.SOFA_BLACK, ModBlocks.SOFA_WHITE,
			ModBlocks.SOFA_RED, ModBlocks.SOFA_GREEN, ModBlocks.SOFA_BLUE, ModBlocks.SOFA_BROWN, ModBlocks.SOFA_PINK,
			ModBlocks.SOFA_YELLOW, ModBlocks.TRASH_BIN, ModBlocks.WHEELS, ModBlocks.LARGE_ROCK, ModBlocks.WOODEN_SPIKES,
			ModBlocks.WOODEN_SPIKES_BLOODED, ModBlocks.WOODEN_SPIKES_BROKEN, ModBlocks.AIRPLANE_ROTOR,
			ModBlocks.SOLAR_PANEL, ModBlocks.WIND_TURBINE, ModBlocks.BATTERY_STATION, ModBlocks.GENERATOR_COMBUSTION,
			ModBlocks.ENERGY_SWITCH, ModBlocks.THERMOMETER, ModBlocks.BRICK_MOSSY, ModBlocks.DARK_BRICKS_MOSSY,
			ModBlocks.TURRET_BASE, ModBlocks.OAK_LOG_SPIKE, ModBlocks.BIRCH_LOG_SPIKE, ModBlocks.SPRUCE_LOG_SPIKE,
			ModBlocks.JUNGLE_LOG_SPIKE, ModBlocks.ACACIA_LOG_SPIKE, ModBlocks.DARK_OAK_LOG_SPIKE, ModBlocks.SANDBAGS,
			ModBlocks.FILE_CABINET, ModBlocks.CASH_REGISTER, ModBlocks.CAMERA, ModBlocks.BURNT_LOG,
			ModBlocks.BURNT_PLANKS, ModBlocks.BURNT_FRAME, ModBlocks.DRY_GROUND, ModBlocks.BURNT_PLANKS_STAIRS,
			ModBlocks.BURNT_PLANKS_SLAB, ModBlocks.BURNT_PLANKS_SLAB_DOUBLE, ModBlocks.BURNT_PLANKS_FENCE,
			ModBlocks.BURNT_CHAIR, ModBlocks.STONE_BRICK_STAIRS_MOSSY, ModBlocks.RADIO, ModBlocks.GLOBE,
			ModBlocks.MERCURY, ModBlocks.SEPARATOR };

	public static final Item[] ITEMS = new Item[] { ModItems.IRON_SCRAP, ModItems.BRASS_SCRAP, ModItems.LEAD_SCRAP,
			ModItems.EMPTY_CAN, ModItems.STONE_AXE, ModItems.PLANK_WOOD, ModItems.SMALL_STONE, ModItems.PLANT_FIBER,
			ModItems.STONE_SHOVEL, ModItems.COOKING_GRILL, ModItems.EMPTY_JAR, ModItems.BOTTLED_MURKY_WATER,
			ModItems.BOTTLED_WATER, ModItems.GLASS_SCRAP, ModItems.BOTTLED_BEER, ModItems.GOLDENROD_TEA,
			ModItems.BOTTLED_COFFEE, ModItems.JAR_OF_HONEY, ModItems.CANNED_MURKY_WATER, ModItems.CANNED_WATER,
			ModItems.CANNED_BEEF, ModItems.CANNED_CATFOOD, ModItems.CANNED_CHICKEN, ModItems.CANNED_CHILI,
			ModItems.CANNED_DOGFOOD, ModItems.CANNED_HAM, ModItems.CANNED_LAMB, ModItems.CANNED_MISO,
			ModItems.CANNED_PASTA, ModItems.CANNED_PEARS, ModItems.CANNED_PEAS, ModItems.CANNED_SALMON,
			ModItems.CANNED_SOUP, ModItems.CANNED_STOCK, ModItems.CANNED_TUNA, ModItems.COFFEE_BEANS, ModItems.CORN,
			ModItems.BLUEBERRY, ModItems.BANEBERRY, ModItems.BANDAGE, ModItems.BANDAGE_ADVANCED, ModItems.FIRST_AID_KIT,
			ModItems.BLOOD_BAG, ModItems.BLOOD_DRAW_KIT, ModItems.INGOT_IRON, ModItems.INGOT_LEAD, ModItems.INGOT_BRASS,
			ModItems.INGOT_STEEL, ModItems.INGOT_COPPER, ModItems.INGOT_TIN, ModItems.INGOT_ZINC, ModItems.INGOT_BRONZE,
			ModItems.INGOT_GOLD, ModItems.MOLD_INGOT, ModItems.CLOTH, ModItems.ANTIBIOTICS, ModItems.CONCRETE_MIX,
			ModItems.BONE_SHIV, ModItems.CEMENT, ModItems.CRUDE_CLUB, ModItems.WOODEN_CLUB,
			ModItems.IRON_REINFORCED_CLUB, ModItems.BARBED_CLUB, ModItems.SPIKED_CLUB, ModItems.CLAWHAMMER,
			ModItems.WRENCH, ModItems.KITCHEN_KNIFE, ModItems.ARMY_KNIFE, ModItems.MACHETE, ModItems.PISTOL,
			ModItems.NINE_MM_BULLET, ModItems.FRIDGE, ModItems.SLEEPING_BAG, ModItems.WOODEN_DOOR_ITEM,
			ModItems.WOODEN_DOOR_REINFORCED_ITEM, ModItems.AK47, ModItems.HUNTING_RIFLE, ModItems.SEVEN_MM_BULLET,
			ModItems.AUGER, ModItems.REALITY_WAND, ModItems.MP3_PLAYER, ModItems.SCREWDRIVER, ModItems.RAM,
			ModItems.CPU, ModItems.HDD, ModItems.GPU, ModItems.MOTHERBOARD, ModItems.POWER_SUPPLY, ModItems.DISC,
			ModItems.LINUX_DISC, ModItems.MAC_DISC, ModItems.WIN10_DISC, ModItems.WIN7_DISC, ModItems.WIN8_DISC,
			ModItems.WIN98_DISC, ModItems.WINXP_DISC, ModItems.LOCKED_DOOR_ITEM, ModItems.WIRE, ModItems.STREET_SIGN,
			ModItems.PHOTO, ModItems.ANALOG_CAMERA, ModItems.SHORTS, ModItems.CAR_BATTERY, ModItems.SMALL_ENGINE,
			ModItems.MINIBIKE_CHASSIS, ModItems.MINIBIKE_HANDLES, ModItems.MINIBIKE_SEAT, ModItems.SKIRT,
			ModItems.JEANS, ModItems.SHORTS_LONG, ModItems.JACKET, ModItems.JUMPER, ModItems.SHIRT,
			ModItems.SHORT_SLEEVED_SHIRT, ModItems.T_SHIRT_0, ModItems.T_SHIRT_1, ModItems.COAT, ModItems.FIBER_HAT,
			ModItems.FIBER_CHESTPLATE, ModItems.FIBER_LEGGINGS, ModItems.FIBER_BOOTS, ModItems.STEEL_HELMET,
			ModItems.STEEL_CHESTPLATE, ModItems.STEEL_LEGGINGS, ModItems.STEEL_BOOTS, ModItems.IRON_AXE, ModItems.CENT,
			ModItems.BACKPACK, ModItems.LEATHER_IRON_BOOTS, ModItems.LEATHER_IRON_CHESTPLATE,
			ModItems.LEATHER_IRON_HELMET, ModItems.LEATHER_IRON_LEGGINGS, ModItems.VOLTMETER, ModItems.CHAINSAW,
			ModItems.COPPER_PICKAXE, ModItems.BRONZE_PICKAXE, ModItems.IRON_PICKAXE, ModItems.STEEL_PICKAXE,
			ModItems.COPPER_AXE, ModItems.BRONZE_AXE, ModItems.STEEL_AXE, ModItems.COPPER_SHOVEL,
			ModItems.BRONZE_SHOVEL, ModItems.IRON_SHOVEL, ModItems.STEEL_SHOVEL, ModItems.BOOK_FORGING,
			ModItems.IRON_PIPE, ModItems.SCRAP_PICKAXE, ModItems.SCRAP_SHOVEL, ModItems.CIRCUIT, ModItems.LINK_TOOL,
			ModItems.SURVIVAL_GUIDE, ModItems.MICROPHONE, ModItems.BULLET_TIP, ModItems.BULLET_CASING,
			ModItems.MOLDY_BREAD, ModItems.SLEDGEHAMMER, ModItems.PISTOL_SLIDE, ModItems.PISTOL_TRIGGER,
			ModItems.PISTOL_GRIP, ModItems.SNIPER_RIFLE_BARREL, ModItems.SNIPER_RIFLE_PARTS,
			ModItems.SNIPER_RIFLE_SCOPE, ModItems.SNIPER_RIFLE_STOCK, ModItems.SNIPER_RIFLE_TRIGGER,
			ModItems.SCRAP_BOOTS, ModItems.SCRAP_CHESTPLATE, ModItems.SCRAP_HELMET, ModItems.SCRAP_LEGGINGS,
			ModItems.SNIPER_RIFLE, ModItems.SHOTGUN, ModItems.SHOTGUN_SAWED_OFF, ModItems.BELLOWS, ModItems.BOOK_AMMO,
			ModItems.BULLET_TIP_MOLD, ModItems.BULLET_CASING_MOLD, ModItems.TEN_MM_BULLET, ModItems.BOOK_COMPUTERS,
			ModItems.BOOK_CONCRETE, ModItems.CEMENT_MOLD, ModItems.BOOK_ELECTRICITY, ModItems.MAGNET,
			ModItems.PHOTO_CELL, ModItems.BOOK_CHEMISTRY, ModItems.POTASSIUM, ModItems.BOOK_METALWORKING,
			ModItems.BOOK_PISTOL, ModItems.BOOK_MINIBIKE, ModItems.GAS_CANISTER, ModItems.SHOTGUN_SCHEMATICS,
			ModItems.SHOTGUN_BARREL, ModItems.SHOTGUN_BARREL_SHORT, ModItems.SHOTGUN_PARTS, ModItems.SHOTGUN_RECEIVER,
			ModItems.SHOTGUN_STOCK, ModItems.SHOTGUN_STOCK_SHORT, ModItems.SNIPER_SCHEMATICS, ModItems.MAGNUM,
			ModItems.MAGNUM_SCOPED, ModItems.FLAMETHROWER, ModItems.MP5, ModItems.RPG, ModItems.M4,
			ModItems.MAGNUM_BULLET, ModItems.MAGNUM_FRAME, ModItems.MAGNUM_CYLINDER, ModItems.MAGNUM_GRIP,
			ModItems.MAGNUM_SCHEMATICS, ModItems.MP5_BARREL, ModItems.MP5_STOCK, ModItems.MP5_TRIGGER,
			ModItems.MP5_SCHEMATICS, ModItems.SHOTGUN_SHELL };

	@SuppressWarnings("deprecation")
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		// RegisterUtil.registerAll(event);
		new RecipeManager().init();

		CapabilityManager.INSTANCE.register(ILockedRecipe.class, new LockedRecipeStorage(), LockedRecipe.class);
		CapabilityManager.INSTANCE.register(IExtendedPlayer.class, new ExtendedPlayerStorage(), ExtendedPlayer.class);
		CapabilityManager.INSTANCE.register(IItemHandlerExtended.class, new ExtendedInventoryStorage(),
				ExtendedInventory.class);
		CapabilityManager.INSTANCE.register(IExtendedChunk.class, new ExtendedChunkStorage(), ExtendedChunk.class);
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		// Registers packets
		new PacketManager().register();
		// Event Handlers
		MinecraftForge.EVENT_BUS.register(new LivingEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
		MinecraftForge.EVENT_BUS.register(new TickHandler());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

		MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainGenEventHandler());
		// Alters Vanilla
		VanillaManager.modifyVanilla();
		// Loads repairs
		RepairManager.INSTANCE.listAllBlocks();
		// Gets streets and city names from a .csv files in assets folder
		CityHelper.getStreetNames();
		CityHelper.getCityNames();

		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		GameRegistry.registerWorldGenerator(new RoadWorldGenerator(), Integer.MIN_VALUE);
		GameRegistry.registerWorldGenerator(new CityWorldGenerator(), Integer.MIN_VALUE);
		GameRegistry.registerWorldGenerator(new StructureGenerator(), 0);
		GameRegistry.registerWorldGenerator(new OreWorldGenerator(), 1);
		GameRegistry.registerWorldGenerator(new SmallFeatureWorldGenerator(), 2);
		GameRegistry.registerWorldGenerator(new GoldenrodWorldGenerator(), 18);
		GameRegistry.registerWorldGenerator(new CornWorldGenerator(), 19);
		GameRegistry.registerWorldGenerator(new BlueberryWorldGenerator(), 20);

		MapGenStructureIO.registerStructure(MapGenCity.Start.class, MODID + ":city");
		StructureCityPieces.registerVillagePieces();

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		CityHelper.loadBuildings();

		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);

		ticketList = new HashMap<ChunkPos, Integer>();
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new LoadingCallback() {
			@Override
			public void ticketsLoaded(List<Ticket> tickets, World world) {

			}
		});
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSetBlockBreak());
		event.registerServerCommand(new CommandGetBlockBreak());
		event.registerServerCommand(new CommandGenerateCity());
		event.registerServerCommand(new CommandSavePrefab());
		event.registerServerCommand(new CommandPlacePrefab());
		event.registerServerCommand(new CommandPlaceLegacyPrefab());
		event.registerServerCommand(new CommandAirdrop());
		event.registerServerCommand(new CommandInfect());

		proxy.serverStarting(event);

		World world = event.getServer().getEntityWorld();
		GameRules rules = world.getGameRules();
		if (!rules.hasRule("handleThirst")) {
			rules.addGameRule("handleThirst", "true", GameRules.ValueType.BOOLEAN_VALUE);
		} else {
			rules.addGameRule("handleThirst", rules.getBoolean("handleThirst") + "", GameRules.ValueType.BOOLEAN_VALUE);
		}

	}

	public static void forceChunkLoad(World w, ChunkPos pos, Entity entity) {
		if (!ticketList.containsKey(pos)) {
			if (chunkLoaderTicket == null) {
				chunkLoaderTicket = ForgeChunkManager.requestTicket(instance, w, ForgeChunkManager.Type.ENTITY);
			}
			ticketList.put(pos, 1);

			chunkLoaderTicket.bindEntity(entity);
			ForgeChunkManager.forceChunk(chunkLoaderTicket, pos);
		} else {
			ticketList.put(pos, ticketList.get(pos) + 1);
		}
	}

	public static void releaseChunkLoad(World w, ChunkPos pos) {
		if (!ticketList.containsKey(pos) || chunkLoaderTicket == null) {
			return;
		} else {
			int num = ticketList.get(pos) - 1;
			if (num > 0)
				ticketList.put(pos, num);
			else
				ForgeChunkManager.unforceChunk(chunkLoaderTicket, pos);
		}
	}
}
