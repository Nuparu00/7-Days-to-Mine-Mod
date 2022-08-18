package nuparu.sevendaystomine.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.inventory.entity.*;
import nuparu.sevendaystomine.client.gui.overlay.ThirstBarOverlay;
import nuparu.sevendaystomine.client.gui.overlay.UpgradeOverlay;
import nuparu.sevendaystomine.client.model.entity.*;
import nuparu.sevendaystomine.client.particle.ModParticleType;
import nuparu.sevendaystomine.client.renderer.blockentity.CalendarRenderer;
import nuparu.sevendaystomine.client.renderer.blockentity.GlobeRenderer;
import nuparu.sevendaystomine.client.renderer.blockentity.SleepingBagRenderer;
import nuparu.sevendaystomine.client.renderer.entity.*;
import nuparu.sevendaystomine.init.*;

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


    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ItemProperties.register(ModItems.STONE_SPEAR.get(), new ResourceLocation(SevenDaysToMine.MODID,"throwing"), (p_174585_, p_174586_, p_174587_, p_174588_) -> p_174587_ != null && p_174587_.isUsingItem() && p_174587_.getUseItem() == p_174585_ ? 1.0F : 0.0F);
        ItemProperties.register(ModItems.BONE_SPEAR.get(), new ResourceLocation(SevenDaysToMine.MODID,"throwing"), (p_174585_, p_174586_, p_174587_, p_174588_) -> p_174587_ != null && p_174587_.isUsingItem() && p_174587_.getUseItem() == p_174585_ ? 1.0F : 0.0F);


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


        event.registerBlockEntityRenderer(ModBlockEntities.SLEEPING_BAG.get(), SleepingBagRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CALENDAR.get(), CalendarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.GLOBE.get(), GlobeRenderer::new);
    }

    @SubscribeEvent
    public static void onColorHandlerItemEvent(RegisterColorHandlersEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        itemColors.register((p_92699_, p_92700_) -> {
            return p_92700_ > 0 ? -1 : 0x479BA8;
        }, ModItems.MURKY_WATER_BOTTLE.get());
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticleTypes.BLOOD.get(), ModParticleType.Factory::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(SevenDaysToMine.MODID+"-thirst", new ThirstBarOverlay(Minecraft.getInstance()));
        event.registerAboveAll(SevenDaysToMine.MODID+"-upgrade", new UpgradeOverlay(Minecraft.getInstance()));
    }
    public static ModelLayerLocation createModelLayerLocation(String name){
        return new ModelLayerLocation(new ResourceLocation(SevenDaysToMine.MODID, name), name);
    }


    @SubscribeEvent
    public static void stitcherEventPre(TextureStitchEvent.Pre event) {
        TextureAtlas map = event.getAtlas();
        ResourceLocation stitching = map.location();

        if(!stitching.equals(TextureAtlas.LOCATION_BLOCKS))
            return;
        event.addSprite(new ResourceLocation(SevenDaysToMine.MODID, "entity/sleeping_bag/red"));
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
    }

    @SubscribeEvent
    public static void stitcherEventPost(TextureStitchEvent.Post event) {
        TextureAtlas map = event.getAtlas();
        ResourceLocation stitching = map.location();

    }
}
