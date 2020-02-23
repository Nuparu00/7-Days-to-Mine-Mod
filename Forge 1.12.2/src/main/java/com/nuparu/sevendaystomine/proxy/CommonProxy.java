package com.nuparu.sevendaystomine.proxy;

import java.io.StringWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityBloatedZombie;
import com.nuparu.sevendaystomine.entity.EntityBurntZombie;
import com.nuparu.sevendaystomine.entity.EntityFrigidHunter;
import com.nuparu.sevendaystomine.entity.EntityFrostbittenWorker;
import com.nuparu.sevendaystomine.entity.EntityFrozenLumberjack;
import com.nuparu.sevendaystomine.entity.EntityLootableCorpse;
import com.nuparu.sevendaystomine.entity.EntityMinibike;
import com.nuparu.sevendaystomine.entity.EntityMountableBlock;
import com.nuparu.sevendaystomine.entity.EntityReanimatedCorpse;
import com.nuparu.sevendaystomine.entity.EntityShot;
import com.nuparu.sevendaystomine.entity.EntitySurvivor;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;
import com.nuparu.sevendaystomine.events.TickHandler;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.crafting.campfire.CampfireRecipeManager;
import com.nuparu.sevendaystomine.item.crafting.chemistry.ChemistryRecipeManager;
import com.nuparu.sevendaystomine.item.crafting.forge.ForgeRecipeManager;
import com.nuparu.sevendaystomine.item.crafting.workbench.WorkbenchCraftingManager;
import com.nuparu.sevendaystomine.util.ConfigHandler;
import com.nuparu.sevendaystomine.util.EnumModParticleType;
import com.nuparu.sevendaystomine.util.VersionChecker;
import com.nuparu.sevendaystomine.util.computer.ApplicationRegistry;
import com.nuparu.sevendaystomine.util.computer.ProcessRegistry;
import com.nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.EntityRegistry;
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
		ConfigHandler.loadConfig(event);
		TickHandler.init(Side.SERVER);
		
		mgr = new ScriptEngineManager(null);
		engine = mgr.getEngineByName("javascript");
		sw = new StringWriter();
		engine.getContext().setWriter(sw);
	}

	public void init(FMLInitializationEvent event) {
		versionChecker = new VersionChecker();
		Thread versionCheckThread = new Thread(versionChecker, "7D2M - Version Check");
		versionCheckThread.start();

		ProcessRegistry.INSTANCE.register();
		ApplicationRegistry.INSTANCE.register();
		DialoguesRegistry.INSTANCE.register();

		WorkbenchCraftingManager.addRecipes();

		new CampfireRecipeManager();
		new ForgeRecipeManager();
		new ChemistryRecipeManager();
	}

	public void postInit(FMLPostInitializationEvent event) {
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
		OreDictionary.registerOre("ingotGold", ModItems.INGOT_GOLD);
		OreDictionary.registerOre("ingotIron", ModItems.INGOT_IRON);
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

	private static int entityID = 0;

	public void registerEntities() {
		registerEntity(EntityShot.class, "shot", 128, 1, true);
		registerEntity(EntityMountableBlock.class, "mountable_block", 64, 20, false);
		registerEntity(EntityReanimatedCorpse.class, "reanimated_corpse", 64, 2, true);
		registerEntity(EntityLootableCorpse.class, "lootable_corpse", 64, 2, true);
		registerEntity(EntityBurntZombie.class, "burnt_zombie", 64, 2, true);
		registerEntity(EntityFrigidHunter.class, "frigid_hunter", 64, 2, true);
		registerEntity(EntityFrostbittenWorker.class, "frostbitten_worker", 64, 2, true);
		registerEntity(EntityFrozenLumberjack.class, "frozen_lumberjack", 64, 2, true);
		registerEntity(EntityZombieSoldier.class, "zombie_soldier", 64, 2, true);
		registerEntity(EntitySurvivor.class, "survivor", 64, 1, true);
		registerEntity(EntityMinibike.class, "minibike", 64, 1, true);
		registerEntity(EntityBloatedZombie.class, "bloated_zombie", 64, 2, true);
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

	public void openClientSideGui(int id, int x, int y, int z) {

	}

	public void openClientOnlyGui(int id, ItemStack stack) {

	}

	public void openClientOnlyGui(int id, TileEntity te) {

	}

	public void tryToTakePhoto() {

	}

	public void addRecoil(float recoil, EntityPlayer shooter) {

	}

	public void onGunStop(int useCount) {

	}
	
	public void spawnParticle(World world, EnumModParticleType type, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
		
	}
	
	public int getParticleLevel() {
		return -1;
	}
}
