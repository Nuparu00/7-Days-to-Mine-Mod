package com.nuparu.sevendaystomine.proxy;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.advancements.ModTriggers;
import com.nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import com.nuparu.sevendaystomine.computer.process.ProcessRegistry;
import com.nuparu.sevendaystomine.crafting.campfire.CampfireRecipeManager;
import com.nuparu.sevendaystomine.crafting.chemistry.ChemistryRecipeManager;
import com.nuparu.sevendaystomine.crafting.forge.ForgeRecipeManager;
import com.nuparu.sevendaystomine.crafting.separator.SeparatorRecipeManager;
import com.nuparu.sevendaystomine.enchantment.ModEnchantments;
import com.nuparu.sevendaystomine.entity.EntityAirdrop;
import com.nuparu.sevendaystomine.entity.EntityAirplane;
import com.nuparu.sevendaystomine.entity.EntityBandit;
import com.nuparu.sevendaystomine.entity.EntityBlindZombie;
import com.nuparu.sevendaystomine.entity.EntityBloatedZombie;
import com.nuparu.sevendaystomine.entity.EntityBurntZombie;
import com.nuparu.sevendaystomine.entity.EntityCar;
import com.nuparu.sevendaystomine.entity.EntityChlorineGrenade;
import com.nuparu.sevendaystomine.entity.EntityFeralZombie;
import com.nuparu.sevendaystomine.entity.EntityFlame;
import com.nuparu.sevendaystomine.entity.EntityFragmentationGrenade;
import com.nuparu.sevendaystomine.entity.EntityFrigidHunter;
import com.nuparu.sevendaystomine.entity.EntityFrostbittenWorker;
import com.nuparu.sevendaystomine.entity.EntityFrozenLumberjack;
import com.nuparu.sevendaystomine.entity.EntityInfectedSurvivor;
import com.nuparu.sevendaystomine.entity.EntityLootableCorpse;
import com.nuparu.sevendaystomine.entity.EntityMinibike;
import com.nuparu.sevendaystomine.entity.EntityMolotov;
import com.nuparu.sevendaystomine.entity.EntityMountableBlock;
import com.nuparu.sevendaystomine.entity.EntityPlaguedNurse;
import com.nuparu.sevendaystomine.entity.EntityProjectileVomit;
import com.nuparu.sevendaystomine.entity.EntityReanimatedCorpse;
import com.nuparu.sevendaystomine.entity.EntityRocket;
import com.nuparu.sevendaystomine.entity.EntityShot;
import com.nuparu.sevendaystomine.entity.EntitySoldier;
import com.nuparu.sevendaystomine.entity.EntitySpiderZombie;
import com.nuparu.sevendaystomine.entity.EntitySurvivor;
import com.nuparu.sevendaystomine.entity.EntityZombieCrawler;
import com.nuparu.sevendaystomine.entity.EntityZombieMiner;
import com.nuparu.sevendaystomine.entity.EntityZombiePig;
import com.nuparu.sevendaystomine.entity.EntityZombiePoliceman;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;
import com.nuparu.sevendaystomine.entity.EntityZombieWolf;
import com.nuparu.sevendaystomine.events.LoudSoundEvent;
import com.nuparu.sevendaystomine.events.MobBreakEvent;
import com.nuparu.sevendaystomine.events.TickHandler;
import com.nuparu.sevendaystomine.init.ModBiomes;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.loot.function.CityMapFunction;
import com.nuparu.sevendaystomine.loot.function.RandomQualityFunction;
import com.nuparu.sevendaystomine.tileentity.TileEntityAirplaneRotor;
import com.nuparu.sevendaystomine.tileentity.TileEntityBackpack;
import com.nuparu.sevendaystomine.tileentity.TileEntityBatteryStation;
import com.nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;
import com.nuparu.sevendaystomine.tileentity.TileEntityBigSignSlave;
import com.nuparu.sevendaystomine.tileentity.TileEntityBirdNest;
import com.nuparu.sevendaystomine.tileentity.TileEntityBookshelf;
import com.nuparu.sevendaystomine.tileentity.TileEntityCalendar;
import com.nuparu.sevendaystomine.tileentity.TileEntityCamera;
import com.nuparu.sevendaystomine.tileentity.TileEntityCampfire;
import com.nuparu.sevendaystomine.tileentity.TileEntityCarMaster;
import com.nuparu.sevendaystomine.tileentity.TileEntityCarSlave;
import com.nuparu.sevendaystomine.tileentity.TileEntityCardboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityCashRegister;
import com.nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;
import com.nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;
import com.nuparu.sevendaystomine.tileentity.TileEntityCombustionGenerator;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;
import com.nuparu.sevendaystomine.tileentity.TileEntityCorpse;
import com.nuparu.sevendaystomine.tileentity.TileEntityCupboard;
import com.nuparu.sevendaystomine.tileentity.TileEntityDresser;
import com.nuparu.sevendaystomine.tileentity.TileEntityEnergyPole;
import com.nuparu.sevendaystomine.tileentity.TileEntityEnergySwitch;
import com.nuparu.sevendaystomine.tileentity.TileEntityFileCabinet;
import com.nuparu.sevendaystomine.tileentity.TileEntityFlag;
import com.nuparu.sevendaystomine.tileentity.TileEntityFlamethrower;
import com.nuparu.sevendaystomine.tileentity.TileEntityForge;
import com.nuparu.sevendaystomine.tileentity.TileEntityGarbage;
import com.nuparu.sevendaystomine.tileentity.TileEntityGasGenerator;
import com.nuparu.sevendaystomine.tileentity.TileEntityGlobe;
import com.nuparu.sevendaystomine.tileentity.TileEntityKeySafe;
import com.nuparu.sevendaystomine.tileentity.TileEntityLamp;
import com.nuparu.sevendaystomine.tileentity.TileEntityMailBox;
import com.nuparu.sevendaystomine.tileentity.TileEntityMedicalCabinet;
import com.nuparu.sevendaystomine.tileentity.TileEntityMetalSpikes;
import com.nuparu.sevendaystomine.tileentity.TileEntityMicrowave;
import com.nuparu.sevendaystomine.tileentity.TileEntityMonitor;
import com.nuparu.sevendaystomine.tileentity.TileEntityOldChest;
import com.nuparu.sevendaystomine.tileentity.TileEntityPhoto;
import com.nuparu.sevendaystomine.tileentity.TileEntityRadio;
import com.nuparu.sevendaystomine.tileentity.TileEntityRefrigerator;
import com.nuparu.sevendaystomine.tileentity.TileEntityScreenProjector;
import com.nuparu.sevendaystomine.tileentity.TileEntitySeparator;
import com.nuparu.sevendaystomine.tileentity.TileEntityShield;
import com.nuparu.sevendaystomine.tileentity.TileEntitySirene;
import com.nuparu.sevendaystomine.tileentity.TileEntitySleepingBag;
import com.nuparu.sevendaystomine.tileentity.TileEntitySolarPanel;
import com.nuparu.sevendaystomine.tileentity.TileEntityStreetSign;
import com.nuparu.sevendaystomine.tileentity.TileEntityTable;
import com.nuparu.sevendaystomine.tileentity.TileEntityThermometer;
import com.nuparu.sevendaystomine.tileentity.TileEntityToilet;
import com.nuparu.sevendaystomine.tileentity.TileEntityTorch;
import com.nuparu.sevendaystomine.tileentity.TileEntityTrashBin;
import com.nuparu.sevendaystomine.tileentity.TileEntityTrashCan;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurretAdvanced;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurretBase;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurret;
import com.nuparu.sevendaystomine.tileentity.TileEntityWallClock;
import com.nuparu.sevendaystomine.tileentity.TileEntityWheels;
import com.nuparu.sevendaystomine.tileentity.TileEntityWindTurbine;
import com.nuparu.sevendaystomine.tileentity.TileEntityWoodenLogSpike;
import com.nuparu.sevendaystomine.tileentity.TileEntityWoodenSpikes;
import com.nuparu.sevendaystomine.util.EnumModParticleType;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.VersionChecker;
import com.nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("deprecation")
public class CommonProxy {

	private static VersionChecker versionChecker = new VersionChecker();

	public static ScriptEngineManager mgr;
	public static ScriptEngine engine;
	public static StringWriter sw;

	public static VersionChecker getVersionChecker() {
		return versionChecker;
	}

	public void preInit(FMLPreInitializationEvent event) {
		TickHandler.init(Side.SERVER);

		mgr = new ScriptEngineManager(null);
		engine = mgr.getEngineByName("javascript");
		sw = new StringWriter();
		engine.getContext().setWriter(sw);

		registerLootTables();
	}

	public void init(FMLInitializationEvent event) {
		versionChecker = new VersionChecker();

		ProcessRegistry.INSTANCE.register();
		ApplicationRegistry.INSTANCE.register();
		DialoguesRegistry.INSTANCE.register();

		new CampfireRecipeManager();
		new ForgeRecipeManager();
		new ChemistryRecipeManager();
		new SeparatorRecipeManager();


		registerTileEntities();
		registerEntities();
		registerOreDictionary();
		ModBiomes.init();

		// Registers advancement criteria
		Method method = ObfuscationReflectionHelper.findMethod(CriteriaTriggers.class, "func_192118_a",
				ICriterionTrigger.class, ICriterionTrigger.class);
		method.setAccessible(true);

		for (int i = 0; i < ModTriggers.TRIGGER_ARRAY.length; i++) {
			try {
				method.invoke(null, ModTriggers.TRIGGER_ARRAY[i]);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		FurnaceRecipes.instance().addSmelting(ModItems.BOTTLED_MURKY_WATER, new ItemStack(ModItems.BOTTLED_WATER), 2);
		FurnaceRecipes.instance().addSmelting(ModItems.CANNED_MURKY_WATER, new ItemStack(ModItems.CANNED_WATER), 2);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.MARBLE_COBBLESTONE), new ItemStack(ModBlocks.MARBLE), 1);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.BASALT_COBBLESTONE), new ItemStack(ModBlocks.BASALT), 1);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.RHYOLITE_COBBLESTONE), new ItemStack(ModBlocks.RHYOLITE), 1);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.MARBLE_BRICKS), new ItemStack(ModBlocks.MARBLE_BRICKS_CRACKED), 1);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.BASALT_BRICKS), new ItemStack(ModBlocks.BASALT_BRICKS_CRACKED), 1);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.RHYOLITE_BRICKS), new ItemStack(ModBlocks.RHYOLITE_BRICKS_CRACKED), 1);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.GRANITE_BRICKS), new ItemStack(ModBlocks.GRANITE_BRICKS_CRACKED), 1);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.ANDESITE_BRICKS), new ItemStack(ModBlocks.ANDESITE_BRICKS_CRACKED), 1);
		FurnaceRecipes.instance().addSmelting(Item.getItemFromBlock(ModBlocks.DIORITE_BRICKS), new ItemStack(ModBlocks.DIORITE_BRICKS_CRACKED), 1);
	}

	public void postInit(FMLPostInitializationEvent event) {

		EntityRegistry.removeSpawn(EntityZombie.class, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));
		EntityRegistry.removeSpawn(EntitySkeleton.class, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));

	}

	public void serverStarting(FMLServerStartingEvent event) {

	}

	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

	public String localize(String unlocalized, Object... args) {
		return I18n.translateToLocalFormatted(unlocalized, args);
	}

	public void registerOreDictionary() {
		OreDictionary.registerOre("ingotBrass", ModItems.INGOT_BRASS);
		OreDictionary.registerOre("ingotBronze", ModItems.INGOT_BRONZE);
		OreDictionary.registerOre("ingotCopper", ModItems.INGOT_COPPER);
		//OreDictionary.registerOre("ingotGold", ModItems.INGOT_GOLD);
		//OreDictionary.registerOre("ingotIron", ModItems.INGOT_IRON);
		OreDictionary.registerOre("ingotLead", ModItems.INGOT_LEAD);
		OreDictionary.registerOre("ingotSteel", ModItems.INGOT_STEEL);
		OreDictionary.registerOre("ingotTin", ModItems.INGOT_TIN);
		OreDictionary.registerOre("ingotZinc", ModItems.INGOT_ZINC);

		OreDictionary.registerOre("oreCopper", ModBlocks.ORE_COPPER);
		OreDictionary.registerOre("oreTin", ModBlocks.ORE_TIN);
		OreDictionary.registerOre("oreZinc", ModBlocks.ORE_ZINC);
		OreDictionary.registerOre("oreLead", ModBlocks.ORE_LEAD);
		OreDictionary.registerOre("orePotassium", ModBlocks.ORE_POTASSIUM);
		OreDictionary.registerOre("oreMercury", ModBlocks.ORE_CINNABAR);
	}

	public void registerTileEntities() {
		registerTileEntity(TileEntityCampfire.class, "campfire");
		registerTileEntity(TileEntityForge.class, "forge");
		registerTileEntity(TileEntityKeySafe.class, "key_safe");
		registerTileEntity(TileEntityCodeSafe.class, "code_safe");
		registerTileEntity(TileEntityCardboard.class, "cardboard");
		registerTileEntity(TileEntityCupboard.class, "cupboard");
		registerTileEntity(TileEntityTorch.class, "torch");
		registerTileEntity(TileEntityGarbage.class, "garbage");
		registerTileEntity(TileEntityBookshelf.class, "bookshelf");
		registerTileEntity(TileEntityTable.class, "table");
		registerTileEntity(TileEntityMedicalCabinet.class, "medical_cabinet");
		registerTileEntity(TileEntityMailBox.class, "mail_box");
		registerTileEntity(TileEntityBirdNest.class, "bird_nest");
		registerTileEntity(TileEntityTrashCan.class, "trash_can");
		registerTileEntity(TileEntitySleepingBag.class, "sleeping_bag");
		registerTileEntity(TileEntityCarMaster.class, "car_master");
		registerTileEntity(TileEntityCarSlave.class, "car_slave");
		registerTileEntity(TileEntityComputer.class, "computer");
		registerTileEntity(TileEntityMonitor.class, "monitor");
		registerTileEntity(TileEntityWallClock.class, "wall_clock");
		registerTileEntity(TileEntityFlag.class, "flag");
		registerTileEntity(TileEntityToilet.class, "toilet");
		registerTileEntity(TileEntityMicrowave.class, "microwave");
		registerTileEntity(TileEntityRefrigerator.class, "refrigerator");
		registerTileEntity(TileEntityBackpack.class, "backpack");
		registerTileEntity(TileEntityOldChest.class, "old_chest");
		registerTileEntity(TileEntityCorpse.class, "corpse");
		registerTileEntity(TileEntityChemistryStation.class, "chemistry_station");
		registerTileEntity(TileEntityGasGenerator.class, "gas_generator");
		registerTileEntity(TileEntityEnergyPole.class, "energy_pole");
		registerTileEntity(TileEntityLamp.class, "lamp");
		registerTileEntity(TileEntityStreetSign.class, "street_sign");
		registerTileEntity(TileEntityPhoto.class, "photo");
		registerTileEntity(TileEntityScreenProjector.class, "screen_projector");
		registerTileEntity(TileEntityDresser.class, "dresser");
		registerTileEntity(TileEntityBigSignMaster.class, "big_sign_master");
		registerTileEntity(TileEntityBigSignSlave.class, "big_sign_slave");
		registerTileEntity(TileEntityTrashBin.class, "trash_bin");
		registerTileEntity(TileEntityWheels.class, "wheels");
		registerTileEntity(TileEntityWoodenSpikes.class, "wooden_spikes");
		registerTileEntity(TileEntityAirplaneRotor.class, "airplane_rotor");
		registerTileEntity(TileEntitySolarPanel.class, "solar_panel");
		registerTileEntity(TileEntityWindTurbine.class, "wind_turbine");
		registerTileEntity(TileEntityBatteryStation.class, "battery_station");
		registerTileEntity(TileEntityCombustionGenerator.class, "combustion_generator");
		registerTileEntity(TileEntityEnergySwitch.class, "energy_switch");
		registerTileEntity(TileEntityThermometer.class, "thermometer");
		registerTileEntity(TileEntityTurretBase.class, "turret_base");
		registerTileEntity(TileEntityWoodenLogSpike.class, "wooden_log_spike");
		registerTileEntity(TileEntityFileCabinet.class, "file_cabinet");
		registerTileEntity(TileEntityCashRegister.class, "cash_register");
		registerTileEntity(TileEntityCamera.class, "camera");
		registerTileEntity(TileEntityRadio.class, "radio");
		registerTileEntity(TileEntityGlobe.class, "globe");
		registerTileEntity(TileEntitySeparator.class, "separator");
		registerTileEntity(TileEntityTurretAdvanced.class, "turret_advanced");
		registerTileEntity(TileEntitySirene.class, "sirene");
		registerTileEntity(TileEntityCalendar.class, "calendar");
		registerTileEntity(TileEntityFlamethrower.class, "flamethrower");
		registerTileEntity(TileEntityMetalSpikes.class, "metal_spikes");
		registerTileEntity(TileEntityShield.class, "riot_shield");
		
		
		
	}

	public void registerTileEntity(Class<? extends TileEntity> te, String name) {
		GameRegistry.registerTileEntity(te, new ResourceLocation(SevenDaysToMine.MODID, name));
	}

	private static int entityID = 0;

	@SuppressWarnings("unchecked")
	public void registerEntities() {
		registerEntity(EntityShot.class, "shot", 128, 1, true);
		registerEntity(EntityMountableBlock.class, "mountable_block", 64, 20, false);
		registerEntity(EntityReanimatedCorpse.class, "reanimated_corpse", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityLootableCorpse.class, "lootable_corpse", 64, 2, true);
		registerEntity(EntityBurntZombie.class, "burnt_zombie", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityFrigidHunter.class, "frigid_hunter", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityFrostbittenWorker.class, "frostbitten_worker", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityFrozenLumberjack.class, "frozen_lumberjack", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityZombieSoldier.class, "zombie_soldier", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntitySurvivor.class, "survivor", 64, 1, true, 0xffffff, 0xffffff);
		registerEntity(EntityMinibike.class, "minibike", 64, 1, true);
		registerEntity(EntityBloatedZombie.class, "bloated_zombie", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityInfectedSurvivor.class, "infected_survivor", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntitySpiderZombie.class, "spider_zombie", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityPlaguedNurse.class, "plagued_nurse", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityBlindZombie.class, "blind_zombie", 64, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityZombieCrawler.class, "zombie_crawler", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityBandit.class, "bandit", 64, 1, true, 0xffffff, 0xffffff);
		registerEntity(EntityAirdrop.class, "airdrop", 512, 1, true);
		registerEntity(EntityZombiePoliceman.class, "zombie_policeman", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityProjectileVomit.class, "projectile_vomit", 128, 1, true);
		registerEntity(EntityFlame.class, "flame", 128, 1, true);
		registerEntity(EntityRocket.class, "rocket", 128, 1, true);
		registerEntity(EntityZombieWolf.class, "zombie_wolf", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityZombiePig.class, "zombie_pig", 96, 2, true, 0xffffff, 0xffffff);
		registerEntity(EntityAirplane.class, "airplane", 96, 2, true);
		registerEntity(EntitySoldier.class, "soldier", 64, 1, true, 0x0b264b, 0x292929);
		registerEntity(EntityChlorineGrenade.class, "chlorine_grenade", 32, 30, true);
		registerEntity(EntityFragmentationGrenade.class, "fragmentation_grenade", 32, 30, true);
		registerEntity(EntityMolotov.class, "molotov", 32, 30, true);
		registerEntity(EntityZombieMiner.class, "zombie_miner", 64, 1, true, 0x574F45, 0x685452);
		registerEntity(EntityFeralZombie.class, "feral_zombie", 64, 1, true, 0x5D623F, 0x6D3826);
		registerEntity(EntityCar.class, "car", 64, 1, true);
		/*
		 * addEntitySpawn(EntityReanimatedCorpse.class, 1000, 1, 7,
		 * EnumCreatureType.MONSTER, Utils.combine(BiomeDictionary.getBiomes(Type.LUSH),
		 * BiomeDictionary.getBiomes(Type.COLD), BiomeDictionary.getBiomes(Type.DRY),
		 * BiomeDictionary.getBiomes(Type.HOT), BiomeDictionary.getBiomes(Type.DEAD),
		 * BiomeDictionary.getBiomes(Type.WASTELAND),
		 * BiomeDictionary.getBiomes(Type.PLAINS),
		 * BiomeDictionary.getBiomes(Type.CONIFEROUS),
		 * BiomeDictionary.getBiomes(Type.SWAMP), BiomeDictionary.getBiomes(Type.HILLS),
		 * BiomeDictionary.getBiomes(Type.SNOWY)).stream().toArray(Biome[]::new));
		 */

		addEntitySpawn(EntityReanimatedCorpse.class, 70, 3, 7, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));

		addEntitySpawn(EntityBloatedZombie.class, 70, 1, 7, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));

		addEntitySpawn(EntityPlaguedNurse.class, 60, 3, 7, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));

		addEntitySpawn(EntityZombieCrawler.class, 50, 2, 9, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));
		addEntitySpawn(EntityZombiePig.class, 20, 1, 3, EnumCreatureType.MONSTER,
				Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST)).stream().toArray(Biome[]::new));

		addEntitySpawn(EntitySpiderZombie.class, 20, 1, 2, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));

		addEntitySpawn(
				EntityFrozenLumberjack.class, 25, 1, 3, EnumCreatureType.MONSTER, Utils
						.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY),
								BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS))
						.stream().toArray(Biome[]::new));
		addEntitySpawn(EntityFrigidHunter.class, 40, 1, 3, EnumCreatureType.MONSTER,
				Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD)).stream().toArray(Biome[]::new));
		addEntitySpawn(
				EntityFrostbittenWorker.class, 40, 2, 5, EnumCreatureType.MONSTER, Utils
						.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY),
								BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS))
						.stream().toArray(Biome[]::new));
		
		addEntitySpawn(EntityZombieWolf.class, 20, 1, 3, EnumCreatureType.MONSTER,
				Utils.combine(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY),
						BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD)).stream().toArray(Biome[]::new));

		addEntitySpawn(EntityBurntZombie.class, 20, 1, 2, EnumCreatureType.MONSTER,
				Arrays.asList(ModBiomes.BURNT_FOREST, ModBiomes.BURNT_JUNGLE, ModBiomes.WASTELAND).stream().toArray(Biome[]::new));
		
		addEntitySpawn(EntityZombieMiner.class, 30, 1, 1, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));
		
		addEntitySpawn(EntityFeralZombie.class, 1, 1, 1, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));
		

		EntityRegistry.removeSpawn(EntityZombie.class, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));
		EntityRegistry.removeSpawn(EntityHusk.class, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));
		EntityRegistry.removeSpawn(EntitySkeleton.class, EnumCreatureType.MONSTER,
				ForgeRegistries.BIOMES.getValuesCollection().stream().toArray(Biome[]::new));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void registerEntity(ResourceLocation res, Class clazz, String name, int trackingRange, int updateFrequency,
			boolean tracking, int primaryColor, int secondaryColor) {
		entityID++;
		EntityRegistry.registerModEntity(res, clazz, name, entityID, SevenDaysToMine.instance, trackingRange,
				updateFrequency, tracking, primaryColor, secondaryColor);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void registerEntity(ResourceLocation res, Class clazz, String name, int trackingRange, int updateFrequency,
			boolean tracking) {
		entityID++;
		EntityRegistry.registerModEntity(res, clazz, name, entityID, SevenDaysToMine.instance, trackingRange,
				updateFrequency, tracking);
	}

	@SuppressWarnings("rawtypes")
	public void registerEntity(Class clazz, String name, int trackingRange, int updateFrequency, boolean tracking) {
		this.registerEntity(new ResourceLocation(SevenDaysToMine.MODID + ":" + name), clazz, name, trackingRange,
				updateFrequency, tracking);
	}

	@SuppressWarnings("rawtypes")
	public void registerEntity(Class clazz, String name, int trackingRange, int updateFrequency, boolean tracking,
			int primaryColor, int secondaryColor) {
		this.registerEntity(new ResourceLocation(SevenDaysToMine.MODID + ":" + name), clazz, name, trackingRange,
				updateFrequency, tracking, primaryColor, secondaryColor);
	}

	public void addEntitySpawn(Class<? extends EntityLiving> clazz, int weight, int min, int max, EnumCreatureType type,
			Biome... biomes) {
		EntityRegistry.addSpawn(clazz, weight, min, max, type, biomes);
	}

	public void registerLootTables() {
		LootFunctionManager.registerFunction(new RandomQualityFunction.Serializer());
		LootFunctionManager.registerFunction(new CityMapFunction.Serializer());

		LootTableList.register(ModLootTables.AIRDROP);
		LootTableList.register(ModLootTables.DRESSER);
		LootTableList.register(ModLootTables.FRIDGE);
		LootTableList.register(ModLootTables.CARDBOARD);
		LootTableList.register(ModLootTables.TRASH);
		LootTableList.register(ModLootTables.TOILET);
		LootTableList.register(ModLootTables.SINK);
		LootTableList.register(ModLootTables.FILE_CABINET);
		LootTableList.register(ModLootTables.CUPBOARD);
		LootTableList.register(ModLootTables.SEDAN);
		LootTableList.register(ModLootTables.BOOKSHELF_COMMON);
		LootTableList.register(ModLootTables.MICROWAVE);
		LootTableList.register(ModLootTables.FURNACE);
		LootTableList.register(ModLootTables.MEDICAL_CABINET);
		LootTableList.register(ModLootTables.BACKPACK);
		LootTableList.register(ModLootTables.WRITING_TABLE);
		LootTableList.register(ModLootTables.SHOWER_DRAIN);
		LootTableList.register(ModLootTables.NEST);
		LootTableList.register(ModLootTables.CASH_REGISTER);
		LootTableList.register(ModLootTables.ZOMBIE_GENERIC);
		LootTableList.register(ModLootTables.CODE_SAFE);
		LootTableList.register(ModLootTables.ZOMBIE_NURSE);
		LootTableList.register(ModLootTables.BODY_BAG);
		LootTableList.register(ModLootTables.BOOKSHELF_RARE);
		LootTableList.register(ModLootTables.ZOMBIE_POLICEMAN);
		LootTableList.register(ModLootTables.ZOMBIE_MINER);
		LootTableList.register(ModLootTables.SUPPLY_CHEST);
		LootTableList.register(ModLootTables.COFFIN);
		LootTableList.register(ModLootTables.ZOMBIE_FERAL);
		LootTableList.register(ModLootTables.ZOMBIE_SOLDIER);
		LootTableList.register(ModLootTables.TRAPPED_CHEST);
		LootTableList.register(ModLootTables.POLICE_CAR);
	}

	public void openClientSideGui(int id, int x, int y, int z) {

	}

	public void openClientOnlyGui(int id, ItemStack stack) {

	}

	public void openClientOnlyGui(int id, TileEntity te) {

	}

	public void schedulePhoto() {

	}

	public void addRecoil(float recoil, EntityPlayer shooter) {

	}

	public void onGunStop(int useCount) {

	}

	public void spawnParticle(World world, EnumModParticleType type, double x, double y, double z, double xMotion,
			double yMotion, double zMotion) {

	}

	public int getParticleLevel() {
		return -1;
	}

	public void setSkyRenderer(World world) {

	}

	public void setCloudRenderer(World world) {

	}

	public void playLoudSound(World world, SoundEvent resource, float volume, BlockPos blockPosIn,
			SoundCategory category) {
		MinecraftForge.EVENT_BUS.post(new LoudSoundEvent(world, blockPosIn, resource, volume, category));
	}

	public void stopLoudSound(BlockPos blockPosIn) {

	}
	
	public boolean isHittingBlock(EntityPlayer player) {
		if(player instanceof EntityPlayerMP) {
			PlayerInteractionManager manager = ((EntityPlayerMP) player).interactionManager;
			return ObfuscationReflectionHelper.getPrivateValue(PlayerInteractionManager.class, manager, "field_73088_d");
		}
		return false;
	}
}
