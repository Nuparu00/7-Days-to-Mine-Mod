package nuparu.sevendaystomine.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.inventory.entity.*;
import nuparu.sevendaystomine.client.gui.overlay.CameraOverlay;
import nuparu.sevendaystomine.client.gui.overlay.GunOverlay;
import nuparu.sevendaystomine.client.gui.overlay.ThirstBarOverlay;
import nuparu.sevendaystomine.client.gui.overlay.UpgradeOverlay;
import nuparu.sevendaystomine.client.model.entity.*;
import nuparu.sevendaystomine.client.particle.ModParticleType;
import nuparu.sevendaystomine.client.renderer.blockentity.*;
import nuparu.sevendaystomine.client.renderer.entity.*;
import nuparu.sevendaystomine.client.renderer.entity.layers.ClothingLayer;
import nuparu.sevendaystomine.init.*;
import nuparu.sevendaystomine.world.item.ClothingItem;

@Mod.EventBusSubscriber(modid=SevenDaysToMine.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static final ModelLayerLocation STONE_SPEAR_LAYER = createModelLayerLocation("stone_spear");
    public static final ModelLayerLocation BONE_SPEAR_LAYER = createModelLayerLocation("bone_spear");
    public static final ModelLayerLocation REANIMATED_CORPSE_LAYER = createModelLayerLocation("reanimated_corpse");
    public static final ModelLayerLocation FRIGID_HUNTER_LAYER = createModelLayerLocation("frigid_hunter");
    public static final ModelLayerLocation FROSTBITTEN_WORKER_LAYER = createModelLayerLocation("frostbitten_worker");
    public static final ModelLayerLocation PLAGUED_NURSE_LAYER = createModelLayerLocation("plagued_nurse");
    public static final ModelLayerLocation MINER_ZOMBIE_LAYER = createModelLayerLocation("miner_zombie");
    public static final ModelLayerLocation BURNT_ZOMBIE_LAYER = createModelLayerLocation("burnt_zombie");
    public static final ModelLayerLocation SOUL_BURNT_ZOMBIE_LAYER = createModelLayerLocation("soul_burnt_zombie");
    public static final ModelLayerLocation BLOATED_ZOMBIE_LAYER = createModelLayerLocation("bloated_zombie");
    public static final ModelLayerLocation SOLDIER_ZOMBIE_LAYER = createModelLayerLocation("soldier_zombie");
    public static final ModelLayerLocation INFECTED_SURVIVOR_LAYER = createModelLayerLocation("infected_survivor");
    public static final ModelLayerLocation FROZEN_LUMBERJACK_LAYER = createModelLayerLocation("frozen_lumberjack");
    public static final ModelLayerLocation FERAL_ZOMBIE_LAYER = createModelLayerLocation("feral_zombie");
    public static final ModelLayerLocation TWISTED_ZOMBIE_LAYER = createModelLayerLocation("twisted_zombie");
    public static final ModelLayerLocation CRAWLER_ZOMBIE_LAYER = createModelLayerLocation("crawler_zombie");
    public static final ModelLayerLocation ZOMBIE_WOLF_LAYER = createModelLayerLocation("zombie_wolf");
    public static final ModelLayerLocation ZOMBIE_PIG_LAYER = createModelLayerLocation("zombie_pig");
    public static final ModelLayerLocation SPIDER_ZOMBIE_LAYER = createModelLayerLocation("spider_zombie");
    public static final ModelLayerLocation SLEEPING_BAG_HEAD_LAYER = createModelLayerLocation("sleeping_bag_head");
    public static final ModelLayerLocation SLEEPING_BAG_FOOT_LAYER = createModelLayerLocation("sleeping_bag_foot");
    public static final ModelLayerLocation GLOBE_LAYER = createModelLayerLocation("globe");
    public static final ModelLayerLocation AIRDROP_MODEL = createModelLayerLocation("airdrop");
    public static final ModelLayerLocation SOLAR_PANEL = createModelLayerLocation("solar_panel");

    public static final ModelLayerLocation MINIBIKE_MODEL = createModelLayerLocation("minibike");
    public static final ModelLayerLocation CAR_MODEL = createModelLayerLocation("car");
    public static final ModelLayerLocation CLOTHING_INNER = createModelLayerLocation("clothing_inner");
    public static final ModelLayerLocation CLOTHING_OUTER = createModelLayerLocation("clothing_outer");



    public static final CubeDeformation OUTER_CLOTHING_DEFORMATION = new CubeDeformation(0.35F);
    public static final CubeDeformation INNER_CLOTHING_DEFORMATION = new CubeDeformation(0.1F);


    @SubscribeEvent
    @SuppressWarnings({"deprecated", "removal"})
    public static void clientSetup(final FMLClientSetupEvent event) {
        ItemProperties.register(ModItems.STONE_SPEAR.get(), new ResourceLocation(SevenDaysToMine.MODID,"throwing"), (p_174585_, p_174586_, p_174587_, p_174588_) -> p_174587_ != null && p_174587_.isUsingItem() && p_174587_.getUseItem() == p_174585_ ? 1.0F : 0.0F);
        ItemProperties.register(ModItems.BONE_SPEAR.get(), new ResourceLocation(SevenDaysToMine.MODID,"throwing"), (p_174585_, p_174586_, p_174587_, p_174588_) -> p_174587_ != null && p_174587_.isUsingItem() && p_174587_.getUseItem() == p_174585_ ? 1.0F : 0.0F);

        ItemProperties.register(ModItems.CRUDE_BOW.get(),new ResourceLocation("pull"), (stack, world, entity, time) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemProperties.register(ModItems.CRUDE_BOW.get(),new ResourceLocation("pulling"), (stack, world, entity, time) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

        ItemProperties.register(ModItems.COMPOUND_BOW.get(),new ResourceLocation("pull"), (stack, world, entity, time) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemProperties.register(ModItems.COMPOUND_BOW.get(),new ResourceLocation("pulling"), (stack, world, entity, time) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

        /*ItemBlockRenderTypes.setRenderLayer(ModBlocks.TORCH_UNLIT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TORCH_UNLIT_WALL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TORCH_LIT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TORCH_LIT_WALL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOUL_TORCH_LIT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOUL_TORCH_LIT_WALL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOUL_TORCH_UNLIT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOUL_TORCH_UNLIT_WALL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LANTERN_UNLIT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.METAL_LADDER.get(), RenderType.cutout());*/


        MenuScreens.register(ModContainers.LOOTABLE_COPRSE.get(), LootableCorpseScreen::new);
        MenuScreens.register(ModContainers.FORGE.get(), ForgeScreen::new);
        MenuScreens.register(ModContainers.COOKING_GRILL.get(), GrillScreen::new);
        MenuScreens.register(ModContainers.CHEMISTRY_STATION.get(), ChemistryStationScreen::new);
        MenuScreens.register(ModContainers.WORKBENCH.get(), WorkbenchScreen::new);
        MenuScreens.register(ModContainers.WORKBENCH_UNCRAFTING.get(), WorkbenchUncraftingScreen::new);
        MenuScreens.register(ModContainers.SMALL.get(), SmallContainerScreen::new);
        MenuScreens.register(ModContainers.TINY.get(), TinyContainerScreen::new);
        MenuScreens.register(ModContainers.MINIBIKE.get(), MinibikeScreen::new);
        MenuScreens.register(ModContainers.CAR.get(), CarScreen::new);
        MenuScreens.register(ModContainers.CAMERA.get(), CameraContainerScreen::new);
        MenuScreens.register(ModContainers.COMBUSTION_GENERATOR.get(), CombustionGeneratorScreen::new);

        /*
        WARNING:
        To ensure transparency of the doors, without having to make a custom .json model files.
        Most likely will have to create them in 1.21+
        */
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOCKED_OAK_DOOR.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOCKED_ACACIA_DOOR.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOCKED_JUNGLE_DOOR.get(), RenderType.cutoutMipped());
        });
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(STONE_SPEAR_LAYER, StoneSpearModel::createBodyLayer);
        event.registerLayerDefinition(BONE_SPEAR_LAYER, BoneSpearModel::createBodyLayer);
        event.registerLayerDefinition(REANIMATED_CORPSE_LAYER, ReanimatedCorpseRenderer::createBodyLayer);
        event.registerLayerDefinition(FRIGID_HUNTER_LAYER, FrigidHunterRenderer::createBodyLayer);
        event.registerLayerDefinition(FROSTBITTEN_WORKER_LAYER, FrostbittenWorkerRenderer::createBodyLayer);
        event.registerLayerDefinition(PLAGUED_NURSE_LAYER, PlaguedNurseRenderer::createBodyLayer);
        event.registerLayerDefinition(MINER_ZOMBIE_LAYER, MinerZombieRenderer::createBodyLayer);
        event.registerLayerDefinition(BURNT_ZOMBIE_LAYER, BurntZombieRenderer::createBodyLayer);
        event.registerLayerDefinition(SOUL_BURNT_ZOMBIE_LAYER, SoulBurntZombieRenderer::createBodyLayer);
        event.registerLayerDefinition(BLOATED_ZOMBIE_LAYER, BloatedZombieModel::createBodyLayer);
        event.registerLayerDefinition(SOLDIER_ZOMBIE_LAYER, SoldierZombieRenderer::createBodyLayer);
        event.registerLayerDefinition(INFECTED_SURVIVOR_LAYER, InfectedSurvivorRenderer::createBodyLayer);
        event.registerLayerDefinition(FROZEN_LUMBERJACK_LAYER, FrozenLumberjackRenderer::createBodyLayer);
        event.registerLayerDefinition(FERAL_ZOMBIE_LAYER, FeralZombieModel::createBodyLayer);
        event.registerLayerDefinition(TWISTED_ZOMBIE_LAYER, TwistedZombieModel::createBodyLayer);
        event.registerLayerDefinition(CRAWLER_ZOMBIE_LAYER, CrawlerZombieModel::createBodyLayer);
        event.registerLayerDefinition(ZOMBIE_WOLF_LAYER, ZombieWolfModel::createBodyLayer);
        event.registerLayerDefinition(ZOMBIE_PIG_LAYER, ZombiePigRenderer::createBodyLayer);
        event.registerLayerDefinition(SPIDER_ZOMBIE_LAYER, SpiderZombieModel::createBodyLayer);
        event.registerLayerDefinition(SLEEPING_BAG_HEAD_LAYER, SleepingBagRenderer::createHeadLayer);
        event.registerLayerDefinition(SLEEPING_BAG_FOOT_LAYER, SleepingBagRenderer::createFootLayer);
        event.registerLayerDefinition(GLOBE_LAYER, GlobeRenderer::createLayer);
        event.registerLayerDefinition(AIRDROP_MODEL, AirdropModel::createLayer);
        event.registerLayerDefinition(MINIBIKE_MODEL, MinibikeModel::createLayer);
        event.registerLayerDefinition(CAR_MODEL, CarModel::createLayer);
        event.registerLayerDefinition(CLOTHING_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(INNER_CLOTHING_DEFORMATION, 0.0F), 64, 64));
        event.registerLayerDefinition(CLOTHING_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(OUTER_CLOTHING_DEFORMATION, 0.0F), 64, 64));
        event.registerLayerDefinition(SOLAR_PANEL, SolarPanelRenderer::createLayer);
    }

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(ModEntities.STONE_SPEAR.get(), StoneSpearRenderer::new);
        event.registerEntityRenderer(ModEntities.BONE_SPEAR.get(), BoneSpearRenderer::new);
        event.registerEntityRenderer(ModEntities.REANIMATED_CORPSE.get(), ReanimatedCorpseRenderer::new);
        event.registerEntityRenderer(ModEntities.FRIGID_HUNTER.get(), FrigidHunterRenderer::new);
        event.registerEntityRenderer(ModEntities.FROSTBITTEN_WORKER.get(), FrostbittenWorkerRenderer::new);
        event.registerEntityRenderer(ModEntities.PLAGUED_NURSE.get(), PlaguedNurseRenderer::new);
        event.registerEntityRenderer(ModEntities.MINER_ZOMBIE.get(), MinerZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.BURNT_ZOMBIE.get(), BurntZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.SOUL_BURNT_ZOMBIE.get(), SoulBurntZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOATED_ZOMBIE.get(), BloatedZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.SOLDIER_ZOMBIE.get(), SoldierZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.INFECTED_SURVIVOR.get(), InfectedSurvivorRenderer::new);
        event.registerEntityRenderer(ModEntities.FROZEN_LUMBERJACK.get(), FrozenLumberjackRenderer::new);
        event.registerEntityRenderer(ModEntities.FERAL_ZOMBIE.get(), FeralZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.TWISTED_ZOMBIE.get(), TwistedZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.CRAWLER_ZOMBIE.get(), CrawlerZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.ZOMBIE_WOLF.get(), ZombieWolfRenderer::new);
        event.registerEntityRenderer(ModEntities.ZOMBIE_PIG.get(), ZombiePigRenderer::new);
        event.registerEntityRenderer(ModEntities.SPIDER_ZOMBIE.get(), SpiderZombieRenderer::new);
        event.registerEntityRenderer(ModEntities.LOOTABLE_CORPSE.get(), LootableCorpseRenderer::new);
        event.registerEntityRenderer(ModEntities.MOUNTABLE_BLOCK.get(), MountableBlockRenderer::new);
        event.registerEntityRenderer(ModEntities.AIRDROP.get(), AirdropRenderer::new);
        event.registerEntityRenderer(ModEntities.MINIBIKE.get(), MinibikeRenderer::new);
        event.registerEntityRenderer(ModEntities.CAR.get(), CarRenderer::new);


        event.registerBlockEntityRenderer(ModBlockEntities.SLEEPING_BAG.get(), SleepingBagRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CALENDAR.get(), CalendarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GLOBE.get(), GlobeRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PHOTO.get(), PhotoRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SOLAR_PANEL.get(), SolarPanelRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WIND_TURBINE.get(), WindTurbineRenderer::new);
    }

    @SubscribeEvent
    public static void onColorHandlerItemEvent(RegisterColorHandlersEvent.Item event) {
        event.register((p_92699_, p_92700_) -> p_92700_ > 0 ? -1 : 0x479BA8, ModItems.MURKY_WATER_BOTTLE.get());
        Item[] clothes = new Item[]{ModItems.SHORTS.get(), ModItems.SKIRT.get(), ModItems.SHORTS_LONG.get(), ModItems.JEANS.get(),
                ModItems.SHIRT.get(), ModItems.SHORT_SLEEVED_SHIRT.get(), ModItems.JACKET.get(), ModItems.JUMPER.get(), ModItems.COAT.get(),
                ModItems.T_SHIRT_0.get(), ModItems.T_SHIRT_1.get()};
        event.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((ClothingItem) stack.getItem()).getColor(stack),clothes);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.BLOOD.get(), ModParticleType.Factory::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.FOOD_LEVEL.id(),SevenDaysToMine.MODID+"-thirst", new ThirstBarOverlay(Minecraft.getInstance()));
        event.registerAboveAll(SevenDaysToMine.MODID+"-upgrade", new UpgradeOverlay(Minecraft.getInstance()));
        event.registerAboveAll(SevenDaysToMine.MODID+"-camera", new CameraOverlay(Minecraft.getInstance()));
        event.registerAboveAll(SevenDaysToMine.MODID+"-gun", new GunOverlay(Minecraft.getInstance()));
    }
    public static ModelLayerLocation createModelLayerLocation(String name){
        return new ModelLayerLocation(new ResourceLocation(SevenDaysToMine.MODID, name), name);
    }


    @SubscribeEvent
    public static void stitcherEventPre(TextureStitchEvent event) {
        TextureAtlas map = event.getAtlas();
        ResourceLocation stitching = map.location();
/*
        if(!stitching.equals(TextureAtlas.LOCATION_BLOCKS))
            return;
        event.getAtlas().(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/red"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/light_gray"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/purple"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/pink"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/magenta"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/lime"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/light_blue"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/green"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/gray"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/cyan"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/brown"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/blue"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/black"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/white"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/yellow"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/orange"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/globe"));
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "item/empty_paper_slot"));*/
    }

    @SubscribeEvent
    public static void addLayersEvent(EntityRenderersEvent.AddLayers event){
        for(String skin : event.getSkins()){
            LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = event.getSkin(skin);
            HumanoidModel inner = new HumanoidModel(Minecraft.getInstance().getEntityModels().bakeLayer(CLOTHING_INNER));
            HumanoidModel outer = new HumanoidModel(Minecraft.getInstance().getEntityModels().bakeLayer(CLOTHING_OUTER));

            renderer.addLayer(new ClothingLayer<>(renderer, inner, outer));
        }
    }

    /*@SubscribeEvent
    public static void registerAdditionalEvent(ModelEvent.RegisterAdditional event){
       event.register(new ResourceLocation(SevenDaysToMine.MODID,"block/wind_turbine_blades"));
    }*/
}
