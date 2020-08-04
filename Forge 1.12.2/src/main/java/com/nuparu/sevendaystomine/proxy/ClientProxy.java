package com.nuparu.sevendaystomine.proxy;

import java.lang.reflect.Field;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Maps;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.gui.GuiBook;
import com.nuparu.sevendaystomine.client.gui.GuiGun;
import com.nuparu.sevendaystomine.client.gui.GuiMp3;
import com.nuparu.sevendaystomine.client.gui.GuiPhoto;
import com.nuparu.sevendaystomine.client.gui.GuiPlayerUI;
import com.nuparu.sevendaystomine.client.gui.GuiSubtitles;
import com.nuparu.sevendaystomine.client.gui.GuiUpgradeOverlay;
import com.nuparu.sevendaystomine.client.particle.ParticleBlood;
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
import com.nuparu.sevendaystomine.client.renderer.factory.RenderBlindZombieFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderBloatedZombieFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderHumanFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderInfectedSurvivorFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderMinibikeFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderPlaguedNurseFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderProjectileVomitFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderSpiderZombieFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombieCrawlerFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombiePolicemanFactory;
import com.nuparu.sevendaystomine.client.renderer.factory.RenderZombieSoldierFactory;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityAirplaneRotorRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityBigSignRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityCameraRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityFlagRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityGlobeRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityOldChestRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityPhotoRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntitySedanRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntitySleepingBagRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntitySolarPanelRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityStreetSignRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityTurretBaseRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityWallClockRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityWindTurbineRenderer;
import com.nuparu.sevendaystomine.client.renderer.tileentity.TileEntityWoodenLogSpikeRenderer;
import com.nuparu.sevendaystomine.client.sound.PositionedLoudSound;
import com.nuparu.sevendaystomine.client.toast.NotificationToast;
import com.nuparu.sevendaystomine.entity.EntityAirdrop;
import com.nuparu.sevendaystomine.entity.EntityBandit;
import com.nuparu.sevendaystomine.entity.EntityBlindZombie;
import com.nuparu.sevendaystomine.entity.EntityBloatedZombie;
import com.nuparu.sevendaystomine.entity.EntityBurntZombie;
import com.nuparu.sevendaystomine.entity.EntityFrigidHunter;
import com.nuparu.sevendaystomine.entity.EntityFrostbittenWorker;
import com.nuparu.sevendaystomine.entity.EntityFrozenLumberjack;
import com.nuparu.sevendaystomine.entity.EntityInfectedSurvivor;
import com.nuparu.sevendaystomine.entity.EntityLootableCorpse;
import com.nuparu.sevendaystomine.entity.EntityMinibike;
import com.nuparu.sevendaystomine.entity.EntityMountableBlock;
import com.nuparu.sevendaystomine.entity.EntityPlaguedNurse;
import com.nuparu.sevendaystomine.entity.EntityProjectileVomit;
import com.nuparu.sevendaystomine.entity.EntityReanimatedCorpse;
import com.nuparu.sevendaystomine.entity.EntityShot;
import com.nuparu.sevendaystomine.entity.EntitySpiderZombie;
import com.nuparu.sevendaystomine.entity.EntitySurvivor;
import com.nuparu.sevendaystomine.entity.EntityZombieCrawler;
import com.nuparu.sevendaystomine.entity.EntityZombiePoliceman;
import com.nuparu.sevendaystomine.entity.EntityZombieSoldier;
import com.nuparu.sevendaystomine.events.ClientEventHandler;
import com.nuparu.sevendaystomine.events.KeyEventHandler;
import com.nuparu.sevendaystomine.events.RenderEventHandler;
import com.nuparu.sevendaystomine.events.TextureStitcherEventHandler;
import com.nuparu.sevendaystomine.events.TickHandler;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.item.ItemClothing;
import com.nuparu.sevendaystomine.item.ItemGuide;
import com.nuparu.sevendaystomine.item.ItemRecipeBook;
import com.nuparu.sevendaystomine.tileentity.TileEntityAirplaneRotor;
import com.nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;
import com.nuparu.sevendaystomine.tileentity.TileEntityCamera;
import com.nuparu.sevendaystomine.tileentity.TileEntityCar;
import com.nuparu.sevendaystomine.tileentity.TileEntityFlag;
import com.nuparu.sevendaystomine.tileentity.TileEntityGlobe;
import com.nuparu.sevendaystomine.tileentity.TileEntityOldChest;
import com.nuparu.sevendaystomine.tileentity.TileEntityPhoto;
import com.nuparu.sevendaystomine.tileentity.TileEntitySleepingBag;
import com.nuparu.sevendaystomine.tileentity.TileEntitySolarPanel;
import com.nuparu.sevendaystomine.tileentity.TileEntityStreetSign;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurretBase;
import com.nuparu.sevendaystomine.tileentity.TileEntityWallClock;
import com.nuparu.sevendaystomine.tileentity.TileEntityWindTurbine;
import com.nuparu.sevendaystomine.tileentity.TileEntityWoodenLogSpike;
import com.nuparu.sevendaystomine.util.EnumModParticleType;
import com.nuparu.sevendaystomine.util.client.CameraHelper;
import com.nuparu.sevendaystomine.util.client.MP3Helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
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

		// Tries to inject our own RenderPlayer
		f_skinMap = ObfuscationReflectionHelper.findField(RenderManager.class, "field_178636_l");

		try {
			RenderManager rm = FMLClientHandler.instance().getClient().getRenderManager();
			@SuppressWarnings("unchecked")
			Map<String, RenderPlayer> skinMap = (Map<String, RenderPlayer>) f_skinMap.get(rm);
			skinMap.put("default", new RenderPlayerEnhanced(rm, false, skinMap.get("default")));
			skinMap.put("slim", new RenderPlayerEnhanced(rm, true, skinMap.get("slim")));

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
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
		ItemClothing[] clothes = new ItemClothing[] { (ItemClothing) ModItems.SHORTS, (ItemClothing) ModItems.SKIRT,
				(ItemClothing) ModItems.SHORTS_LONG, (ItemClothing) ModItems.JEANS, (ItemClothing) ModItems.SHIRT,
				(ItemClothing) ModItems.SHORT_SLEEVED_SHIRT, (ItemClothing) ModItems.JACKET,
				(ItemClothing) ModItems.JUMPER, (ItemClothing) ModItems.COAT, (ItemClothing) ModItems.T_SHIRT_0,
				(ItemClothing) ModItems.T_SHIRT_1 };

		colors.registerItemColorHandler(new IItemColor() {
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				return tintIndex > 0 ? -1 : ((ItemArmor) stack.getItem()).getColor(stack);
			}
		}, clothes);
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
		registerTileEntityRenderers();
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public void registerRenderer(Class clazz, Render renderer) {
		RenderingRegistry.registerEntityRenderingHandler(clazz, renderer);
	}

	public void registerTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySleepingBag.class, new TileEntitySleepingBagRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWallClock.class, new TileEntityWallClockRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFlag.class, new TileEntityFlagRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOldChest.class, new TileEntityOldChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStreetSign.class, new TileEntityStreetSignRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPhoto.class, new TileEntityPhotoRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBigSignMaster.class, new TileEntityBigSignRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCar.class, new TileEntitySedanRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAirplaneRotor.class,
				new TileEntityAirplaneRotorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolarPanel.class, new TileEntitySolarPanelRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindTurbine.class, new TileEntityWindTurbineRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurretBase.class, new TileEntityTurretBaseRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenLogSpike.class,
				new TileEntityWoodenLogSpikeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCamera.class, new TileEntityCameraRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGlobe.class, new TileEntityGlobeRenderer());
	}

	@Override
	public String localize(String unlocalized, Object... args) {
		return I18n.format(unlocalized, args);
	}

	void initKeybindings() {
		keyBindings = new KeyBinding[3];
		keyBindings[0] = new KeyBinding("key.reload.desc", Keyboard.KEY_R, "key.sevendaystomine.category");
		keyBindings[1] = new KeyBinding("key.accelerate.desc", Keyboard.KEY_W, "key.sevendaystomine.category");
		keyBindings[2] = new KeyBinding("key.brakes.desc", Keyboard.KEY_SPACE, "key.sevendaystomine.category");

		for (int i = 0; i < keyBindings.length; ++i) {
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
	}

	@Override
	public void openClientSideGui(int id, int x, int y, int z) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player == null) {
			return;
		}
		player.openGui(SevenDaysToMine.instance, id, player.world, x, y, z);
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

	public void tryToTakePhoto() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc != null && mc.player != null) {
			CameraHelper.INSTANCE.saveScreenshot(mc.displayWidth, mc.displayHeight, mc.getFramebuffer(), mc.player);
		}
	}

	@Override
	public void addRecoil(float recoil, EntityPlayer shooter) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc != null && mc.player != null) {
			if (mc.player == shooter) {
				TickHandler.recoil += recoil;
			}
		}
	}

	@Override
	public void onGunStop(int useCount) {

	}

	@Override
	public void spawnParticle(World world, EnumModParticleType type, double x, double y, double z, double xMotion,
			double yMotion, double zMotion) {
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
	public void playLoudSound(ResourceLocation resource, float volume, BlockPos blockPosIn, SoundCategory category) {
		ISound isound = (ISound) mapSoundPositions.get(blockPosIn);
		if (isound != null) {
			Minecraft.getMinecraft().getSoundHandler().stopSound(isound);
			mapSoundPositions.remove(blockPosIn);
		}

		if (resource != null) {
			PositionedLoudSound positionedsoundrecord = new PositionedLoudSound(resource, volume, 1.0F, false, 0,
					ISound.AttenuationType.LINEAR, (float) blockPosIn.getX(), (float) blockPosIn.getY(),
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
}
