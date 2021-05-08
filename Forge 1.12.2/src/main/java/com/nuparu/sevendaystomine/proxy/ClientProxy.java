package com.nuparu.sevendaystomine.proxy;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Maps;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.gui.GuiBook;
import com.nuparu.sevendaystomine.client.gui.GuiCodeSafeLocked;
import com.nuparu.sevendaystomine.client.gui.GuiGun;
import com.nuparu.sevendaystomine.client.gui.GuiKeySafeLocked;
import com.nuparu.sevendaystomine.client.gui.GuiMp3;
import com.nuparu.sevendaystomine.client.gui.GuiPhoto;
import com.nuparu.sevendaystomine.client.gui.GuiPlayerUI;
import com.nuparu.sevendaystomine.client.gui.GuiSubtitles;
import com.nuparu.sevendaystomine.client.gui.GuiUpgradeOverlay;
import com.nuparu.sevendaystomine.client.particle.ParticleBlood;
import com.nuparu.sevendaystomine.client.particle.ParticleMuzzleFlash;
import com.nuparu.sevendaystomine.client.particle.ParticleVomit;
import com.nuparu.sevendaystomine.client.renderer.CloudRenderer;
import com.nuparu.sevendaystomine.client.renderer.RenderGlobalEnhanced;
import com.nuparu.sevendaystomine.client.renderer.SkyRenderer;
import com.nuparu.sevendaystomine.client.renderer.entity.LayerBackpack;
import com.nuparu.sevendaystomine.client.renderer.entity.LayerClothing;
import com.nuparu.sevendaystomine.client.renderer.entity.LayerGuns;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderBurntZombie;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderFrigidHunter;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderFrostbittenWorker;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderFrozenLumberjack;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderLootableCorpse;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderMountableBlock;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderPlayerEnhanced;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderReanimatedCorpse;
import com.nuparu.sevendaystomine.client.renderer.entity.RenderShot;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderAirdropFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderAirplaneFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderBlindZombieFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderBloatedZombieFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderCarFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderChlorineGrenadeFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderFeralZombieFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderFlameFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderFlareFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderFragmentationGrenadeFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderHumanFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderInfectedSurvivorFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderMinibikeFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderMolotovFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderPlaguedNurseFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderProjectileVomitFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderSpiderZombieFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombieCrawlerFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombieMinerFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombiePigFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombiePolicemanFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombieSoldierFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombieWolfFactory;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityAirplaneRotorRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityBigSignRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityCalendarRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityCameraRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityCarRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityFlagRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityGlobeRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityMetalSpikesRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityOldChestRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityPhotoRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityShieldRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntitySleepingBagRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntitySolarPanelRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityStreetSignRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityTurretAdvancedRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityTurretBaseRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityWallClockRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityWindTurbineRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityWoodenLogSpikeRenderer;
import com.nuparu.sevendaystomine.client.sound.MovingSoundCarIdle;
import com.nuparu.sevendaystomine.client.sound.MovingSoundMinibikeIdle;
import com.nuparu.sevendaystomine.client.sound.PositionedLoudSound;
import com.nuparu.sevendaystomine.client.toast.NotificationToast;
import com.nuparu.sevendaystomine.client.util.MP3Helper;
import com.nuparu.sevendaystomine.config.ModConfig;
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
import com.nuparu.sevendaystomine.entity.EntityFlare;
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
import com.nuparu.sevendaystomine.events.ClientEventHandler;
import com.nuparu.sevendaystomine.events.KeyEventHandler;
import com.nuparu.sevendaystomine.events.RenderEventHandler;
import com.nuparu.sevendaystomine.events.TextureStitcherEventHandler;
import com.nuparu.sevendaystomine.events.TickHandler;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.ItemGuide;
import com.nuparu.sevendaystomine.item.ItemRecipeBook;
import com.nuparu.sevendaystomine.tileentity.TileEntityAirplaneRotor;
import com.nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;
import com.nuparu.sevendaystomine.tileentity.TileEntityCalendar;
import com.nuparu.sevendaystomine.tileentity.TileEntityCamera;
import com.nuparu.sevendaystomine.tileentity.TileEntityCar;
import com.nuparu.sevendaystomine.tileentity.TileEntityFlag;
import com.nuparu.sevendaystomine.tileentity.TileEntityGlobe;
import com.nuparu.sevendaystomine.tileentity.TileEntityMetalSpikes;
import com.nuparu.sevendaystomine.tileentity.TileEntityOldChest;
import com.nuparu.sevendaystomine.tileentity.TileEntityPhoto;
import com.nuparu.sevendaystomine.tileentity.TileEntityShield;
import com.nuparu.sevendaystomine.tileentity.TileEntitySleepingBag;
import com.nuparu.sevendaystomine.tileentity.TileEntitySolarPanel;
import com.nuparu.sevendaystomine.tileentity.TileEntityStreetSign;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurretAdvanced;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurretBase;
import com.nuparu.sevendaystomine.tileentity.TileEntityWallClock;
import com.nuparu.sevendaystomine.tileentity.TileEntityWindTurbine;
import com.nuparu.sevendaystomine.tileentity.TileEntityWoodenLogSpike;
import com.nuparu.sevendaystomine.util.EnumModParticleType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

public class ClientProxy extends CommonProxy {

	public static KeyBinding[] keyBindings;

	private Field f_skinMap;

	private static final Map<BlockPos, ISound> mapSoundPositions = Maps.<BlockPos, ISound>newHashMap();

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		OBJLoader.INSTANCE.addDomain(SevenDaysToMine.MODID);

		TickHandler.init(Side.CLIENT);

		MinecraftForge.EVENT_BUS.register(new GuiPlayerUI());
		MinecraftForge.EVENT_BUS.register(new GuiSubtitles());
		MinecraftForge.EVENT_BUS.register(new GuiUpgradeOverlay());
		MinecraftForge.EVENT_BUS.register(new GuiGun());

		MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
		MinecraftForge.EVENT_BUS.register(new TextureStitcherEventHandler());

		MinecraftForge.EVENT_BUS.register(new KeyEventHandler());

		registerRenderers();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		initKeybindings();
		SevenDaysToMine.renderGlobalEnhanced = new RenderGlobalEnhanced(Minecraft.getMinecraft());
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
				.registerReloadListener(SevenDaysToMine.renderGlobalEnhanced);
		ClientEventHandler.init();
		MP3Helper.init();
		registerOldRenderers();
		registerTileEntityRenderers();

		// Tries to inject our own RenderPlayer
		f_skinMap = ObfuscationReflectionHelper.findField(RenderManager.class, "field_178636_l");
		RenderManager rm = FMLClientHandler.instance().getClient().getRenderManager();
		if (ModConfig.client.customPlayerRenderer) {
			try {
				Map<String, RenderPlayer> skinMap = (Map<String, RenderPlayer>) f_skinMap.get(rm);
				skinMap.put("default", new RenderPlayerEnhanced(rm, false, skinMap.get("default")));
				skinMap.put("slim", new RenderPlayerEnhanced(rm, true, skinMap.get("slim")));

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		RenderPlayer defaultModel = skinMap.get("default");
		RenderPlayer slimModel = skinMap.get("slim");
		defaultModel.addLayer(new LayerGuns(defaultModel));
		slimModel.addLayer(new LayerGuns(slimModel));
		defaultModel.addLayer(new LayerClothing(defaultModel));
		slimModel.addLayer(new LayerClothing(slimModel));
		defaultModel.addLayer(new LayerBackpack(defaultModel));
		slimModel.addLayer(new LayerBackpack(slimModel));

		ItemColors colors = Minecraft.getMinecraft().getItemColors();
		Item[] clothes = new Item[] { ModItems.SHORTS, ModItems.SKIRT, ModItems.SHORTS_LONG, ModItems.JEANS,
				ModItems.SHIRT, ModItems.SHORT_SLEEVED_SHIRT, ModItems.JACKET, ModItems.JUMPER, ModItems.COAT,
				ModItems.T_SHIRT_0, ModItems.T_SHIRT_1, ModItems.LEATHER_BOOTS, ModItems.LEATHER_CHESTPLATE,
				ModItems.LEATHER_HELMET, ModItems.LEATHER_LEGGINGS };

		colors.registerItemColorHandler(new IItemColor() {
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				return tintIndex > 0 ? -1 : ((ItemArmor) stack.getItem()).getColor(stack);
			}
		}, clothes);

		for (Entry<Class<? extends Entity>, Render<? extends Entity>> entry : rm.entityRenderMap.entrySet()) {
			if (entry.getKey() == EntityArmorStand.class) {
				Render<? extends Entity> render = entry.getValue();
				((RenderLivingBase) render).addLayer(new LayerClothing(defaultModel));
			}
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);/*
								 * TickHandler.f_MOON_PHASES_TEXTURES =
								 * ObfuscationReflectionHelper.findField(RenderGlobal.class, "field_110927_h");
								 * if (TickHandler.f_MOON_PHASES_TEXTURES != null) {
								 * TickHandler.f_MOON_PHASES_TEXTURES.setAccessible(true);
								 * FieldUtils.removeFinalModifier(TickHandler.f_MOON_PHASES_TEXTURES); }
								 */
	}

	@Override
	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player
				: SevenDaysToMine.proxy.getPlayerEntityFromContext(ctx));
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}

	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieSoldier.class,
				RenderZombieSoldierFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntitySurvivor.class, RenderHumanFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityBandit.class, RenderHumanFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntitySoldier.class, RenderHumanFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityMinibike.class, RenderMinibikeFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityBloatedZombie.class,
				RenderBloatedZombieFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityInfectedSurvivor.class,
				RenderInfectedSurvivorFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpiderZombie.class, RenderSpiderZombieFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpiderZombie.class, RenderSpiderZombieFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityPlaguedNurse.class, RenderPlaguedNurseFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityBlindZombie.class, RenderBlindZombieFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieCrawler.class,
				RenderZombieCrawlerFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityAirdrop.class, RenderAirdropFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityZombiePoliceman.class,
				RenderZombiePolicemanFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectileVomit.class,
				RenderProjectileVomitFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlame.class, RenderFlameFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieWolf.class, RenderZombieWolfFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityZombiePig.class, RenderZombiePigFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityAirplane.class, RenderAirplaneFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieMiner.class, RenderZombieMinerFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityChlorineGrenade.class,
				RenderChlorineGrenadeFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityFragmentationGrenade.class,
				RenderFragmentationGrenadeFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityMolotov.class, RenderMolotovFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityFeralZombie.class, RenderFeralZombieFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityCar.class, RenderCarFactory.INSTANCE);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlare.class, RenderFlareFactory.INSTANCE);

	}

	/*
	 * Registers renderers without proper Factory class ==> should fix this in the
	 * future and should not make any more renderers without a Factory class
	 */
	public void registerOldRenderers() {
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		registerRenderer(EntityShot.class, new RenderShot(renderManager));
		registerRenderer(EntityReanimatedCorpse.class,
				new RenderReanimatedCorpse(renderManager, new ModelBiped(), 0.5f));
		registerRenderer(EntityLootableCorpse.class, new RenderLootableCorpse<EntityLootableCorpse>(renderManager));
		registerRenderer(EntityBurntZombie.class, new RenderBurntZombie(renderManager, new ModelBiped(), 0.5f));
		registerRenderer(EntityFrigidHunter.class, new RenderFrigidHunter(renderManager, new ModelBiped(), 0.5f));
		registerRenderer(EntityFrostbittenWorker.class,
				new RenderFrostbittenWorker(renderManager, new ModelBiped(), 0.5f));
		registerRenderer(EntityFrozenLumberjack.class,
				new RenderFrozenLumberjack(renderManager, new ModelBiped(0.3f), 0.58f));
		registerRenderer(EntityMountableBlock.class, new RenderMountableBlock(renderManager));
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public void registerRenderer(Class clazz, Render renderer) {
		RenderingRegistry.registerEntityRenderingHandler(clazz, renderer);
	}

	@SuppressWarnings("deprecation")
	public void registerTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySleepingBag.class, new TileEntitySleepingBagRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallClock.class, new TileEntityWallClockRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlag.class, new TileEntityFlagRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOldChest.class, new TileEntityOldChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStreetSign.class, new TileEntityStreetSignRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPhoto.class, new TileEntityPhotoRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBigSignMaster.class, new TileEntityBigSignRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCar.class, new TileEntityCarRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAirplaneRotor.class,
				new TileEntityAirplaneRotorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolarPanel.class, new TileEntitySolarPanelRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindTurbine.class, new TileEntityWindTurbineRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurretBase.class, new TileEntityTurretBaseRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurretAdvanced.class,
				new TileEntityTurretAdvancedRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenLogSpike.class,
				new TileEntityWoodenLogSpikeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCamera.class, new TileEntityCameraRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGlobe.class, new TileEntityGlobeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCalendar.class, new TileEntityCalendarRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMetalSpikes.class, new TileEntityMetalSpikesRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShield.class, new TileEntityShieldRenderer());

		for(int i = ModItems.RIOT_SHIELD.getMaxDamage(); i >= 0; i--) {
			ForgeHooksClient.registerTESRItemStack(ModItems.RIOT_SHIELD, i, TileEntityShield.class);
		}
		
	}

	@Override
	public String localize(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
	}

	void initKeybindings() {
		keyBindings = new KeyBinding[8];
		keyBindings[0] = new KeyBinding("key.reload.desc", Keyboard.KEY_R, "key.sevendaystomine.category");
		keyBindings[1] = new KeyBinding("key.camera.width.increase.desc", Keyboard.KEY_NUMPAD6,
				"key.sevendaystomine.category");
		keyBindings[2] = new KeyBinding("key.camera.width.decrease.desc", Keyboard.KEY_NUMPAD4,
				"key.sevendaystomine.category");
		keyBindings[3] = new KeyBinding("key.camera.height.increase.desc", Keyboard.KEY_NUMPAD8,
				"key.sevendaystomine.category");
		keyBindings[4] = new KeyBinding("key.camera.height.decrease.desc", Keyboard.KEY_NUMPAD2,
				"key.sevendaystomine.category");
		keyBindings[5] = new KeyBinding("key.camera.zoom.desc", Keyboard.KEY_ADD, "key.sevendaystomine.category");
		keyBindings[6] = new KeyBinding("key.camera.unzoom.desc", Keyboard.KEY_SUBTRACT,
				"key.sevendaystomine.category");
		keyBindings[7] = new KeyBinding("key.honk.desc", Keyboard.KEY_SPACE, "key.sevendaystomine.category");

		for (int i = 0; i < keyBindings.length; ++i) {
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
	}

	@Override
	public void openClientSideGui(int id, int x, int y, int z) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player == null) {
			return;
		}
		TileEntity te = player.world.getTileEntity(new BlockPos(x, y, z));
		switch (id) {
		case 0:
			mc.displayGuiScreen(new GuiCodeSafeLocked(te, new BlockPos(x, y, z)));
			return;
		case 1:
			mc.displayGuiScreen(new GuiKeySafeLocked(te, new BlockPos(x, y, z)));
			return;
		}
	}

	@Override
	public void openClientOnlyGui(int id, ItemStack stack) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player == null) {
			return;
		}
		switch (id) {
		case 0:
			mc.displayGuiScreen(new GuiMp3());
			return;
		case 1:
			mc.displayGuiScreen(new GuiPhoto(stack.getTagCompound().getString("path")));
			return;
		case 2:
			mc.getToastGui()
					.add(new NotificationToast(stack, new TextComponentTranslation("unlocked.toast"),
							new TextComponentTranslation(!stack.isEmpty() && stack.getItem() instanceof ItemRecipeBook
									? stack.getItem().getRegistryName().getResourcePath() + ".title"
									: "THIS IS NOT BOOK!")));
			return;
		case 3:
			mc.displayGuiScreen(new GuiBook(((ItemGuide) stack.getItem()).data));
			return;
		}

	}

	@Override
	public void openClientOnlyGui(int id, TileEntity te) {
		Minecraft mc = Minecraft.getMinecraft();
		if (te != null && te instanceof TileEntityPhoto && id == 0) {
			mc.displayGuiScreen(new GuiPhoto(((TileEntityPhoto) te).getPath()));
		}
	}

	public void schedulePhoto() {
		/*
		 * Minecraft mc = Minecraft.getMinecraft(); if (mc != null && mc.player != null)
		 * { CameraHelper.INSTANCE.saveScreenshot(mc.displayWidth, mc.displayHeight,
		 * mc.getFramebuffer(), mc.player); }
		 */
		if (ModConfig.players.allowPhotos) {
			ClientEventHandler.takingPhoto = true;
		}
	}

	@Override
	public void addRecoil(float recoil, EntityPlayer shooter) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc != null && mc.player != null) {
			if (mc.player == shooter && mc.getRenderViewEntity() == shooter) {
				TickHandler.recoil += recoil;
			}
		}
	}

	@Override
	public void onGunStop(int useCount) {

	}

	/*
	 * Maybe should check ParticleManager.registerParticle in the future
	 */
	@Override
	public void spawnParticle(World world, EnumModParticleType type, double x, double y, double z, double xMotion,
			double yMotion, double zMotion) {
		if(!ModConfig.client.particles)return;
		IParticleFactory factory = getParticleFactory(type);
		if (factory != null) {
			Particle particle = factory.createParticle(0, world, x, y, z, xMotion, yMotion, zMotion, 0);
			if (particle != null) {
				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			}
		}
	}

	private IParticleFactory getParticleFactory(EnumModParticleType type) {
		switch (type) {
		case BLOOD:
			return new ParticleBlood.Factory();
		case VOMIT:
			return new ParticleVomit.Factory();
		case MUZZLE_FLASH:
			return new ParticleMuzzleFlash.Factory();
		default:
			break;
		}
		return null;
	}

	// 2==max,1==decreased,0==minimal
	@Override
	public int getParticleLevel() {
		return Math.abs(Minecraft.getMinecraft().gameSettings.particleSetting - 2);
	}

	@Override
	public void setSkyRenderer(World world) {
		world.provider.setSkyRenderer(new SkyRenderer(Minecraft.getMinecraft().renderGlobal));
	}

	@Override
	public void setCloudRenderer(World world) {
		world.provider.setCloudRenderer(new CloudRenderer(Minecraft.getMinecraft().renderGlobal));
	}

	@Override
	public void playLoudSound(World world, SoundEvent soundEvent, float volume, BlockPos blockPosIn,
			SoundCategory category) {
		super.playLoudSound(world, soundEvent, volume, blockPosIn, category);
		if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().world != world)
			return;
		ISound isound = (ISound) mapSoundPositions.get(blockPosIn);
		if (isound != null) {
			Minecraft.getMinecraft().getSoundHandler().stopSound(isound);
			mapSoundPositions.remove(blockPosIn);
		}

		if (soundEvent != null && soundEvent.getSoundName() != null) {
			PositionedLoudSound positionedsoundrecord = new PositionedLoudSound(soundEvent.getSoundName(), volume, 1.0F,
					false, 0, ISound.AttenuationType.LINEAR, (float) blockPosIn.getX(), (float) blockPosIn.getY(),
					(float) blockPosIn.getZ(), category);
			mapSoundPositions.put(blockPosIn, positionedsoundrecord);
			Minecraft.getMinecraft().getSoundHandler().playSound(positionedsoundrecord);
		}
	}

	@Override
	public void stopLoudSound(BlockPos blockPosIn) {
		ISound is = mapSoundPositions.get(blockPosIn);
		if (is != null) {
			Minecraft.getMinecraft().getSoundHandler().stopSound(is);
			mapSoundPositions.remove(is);
		}

	}

	@Override
	public boolean isHittingBlock(EntityPlayer player) {
		if (player instanceof EntityPlayerSP) {
			PlayerControllerMP controller = Minecraft.getMinecraft().playerController;
			return ObfuscationReflectionHelper.getPrivateValue(PlayerControllerMP.class, controller, "field_78778_j");
		}
		return super.isHittingBlock(player);
	}

	@Override
	public void playMovingSound(int id, Entity entity) {

		switch (id) {
		case 0:
			if (entity instanceof EntityMinibike) {
				Minecraft.getMinecraft().getSoundHandler()
						.playSound(new MovingSoundMinibikeIdle((EntityMinibike) entity));
			}
			break;
		case 1:
			if (entity instanceof EntityCar) {
				Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundCarIdle((EntityCar) entity));
			}
			break;
		}
	}
}
