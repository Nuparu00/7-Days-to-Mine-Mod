package nuparu.sevendaystomine;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import nuparu.sevendaystomine.command.*;
import nuparu.sevendaystomine.config.ConfigHelper;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.*;
import nuparu.sevendaystomine.loot.function.ModLootFunctionManager;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.proxy.ClientProxy;
import nuparu.sevendaystomine.proxy.CommonProxy;
import nuparu.sevendaystomine.world.item.crafting.DummyRecipe;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(SevenDaysToMine.MODID)
public class SevenDaysToMine {
    public static final String MODID = "sevendaystomine";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new,
            () -> CommonProxy::new);

    public SevenDaysToMine() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHelper.serverConfig);
        ConfigHelper.loadConfig(ConfigHelper.serverConfig,
                FMLPaths.CONFIGDIR.get().resolve(MODID + "-server.toml").toString());

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHelper.clientConfig);
        ConfigHelper.loadConfig(ConfigHelper.clientConfig,
                FMLPaths.CONFIGDIR.get().resolve(MODID + "-client.toml").toString());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHelper.commonConfig);
        ConfigHelper.loadConfig(ConfigHelper.commonConfig,
                FMLPaths.CONFIGDIR.get().resolve(MODID + "-common.toml").toString());

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        ModSounds.SOUNDS.register(bus);
        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModEffects.EFFECTS.register(bus);
        ModRecipeTypes.RECIPE_TYPES.register(bus);
        ModRecipeSerializers.SERIALIZERS.register(bus);
        ModEntities.ENTITIES.register(bus);
        ModBlockEntities.TILE_ENTITIES.register(bus);
        ModContainers.CONTAINERS.register(bus);
        ModPaintingTypes.PAINTING_TYPES.register(bus);
        ModParticleTypes.PARTICLE_TYPES.register(bus);
        ModLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(bus);
        ModCommandArguments.ARGUMENT_TYPES.register(bus);
        //ModConfiguredFeatures.CONFIGURED_FEATURES.register(bus);
        //ModPlacedFeatures.PLACED_FEATURES.register(bus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModGameRules::register);
        PacketManager.setup();
        TierSortingRegistry.registerTier(ModTiers.STEEL, new ResourceLocation(MODID, "steel"), List.of(Tiers.WOOD, Tiers.STONE, Tiers.GOLD, Tiers.IRON), List.of(Tiers.DIAMOND, Tiers.NETHERITE));

        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(ModBlocks.GOLDENROD.get()), ModBlocks.POTTED_GOLDENROD);
        //Forces Java to initialize the static fields in ModLootFunctionManager. Maybe adding a register() method would be a better idea?
        event.enqueueWork(ModLootFunctionManager::new);
        event.enqueueWork(ModStructureProcessors::register);
        event.enqueueWork(ModStructurePoolElements::new);
        event.enqueueWork(ModProcessorLists::new);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        QualityManager.reload();
        /*
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = event.getServer().getRecipeManager().recipes;
        Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> newRecipez = new HashMap<>();
        for (IRecipeType<?> key : recipes.keySet()) {
            Map<ResourceLocation, IRecipe<?>> original = recipes.get(key);
            HashMap<ResourceLocation, IRecipe<?>> newMap = new HashMap<>();
            if(original != null){
                for(Map.Entry<ResourceLocation, IRecipe<?>> recipe : original.entrySet()){
                    ResourceLocation location = recipe.getKey();
                    IRecipe<?> value = recipe.getValue();

                    if(!CommonConfig.disabledRecipes.get().contains(location.toString())){
                        newMap.put(location,value);
                    }
                }
            }
            newRecipez.put(key,newMap);
        }
        event.getServer().getRecipeManager().recipes = newRecipez;*/
        ArrayList<Recipe<?>> toReplace = new ArrayList<>();
        List<? extends String> disabledRecipes = ServerConfig.disabledRecipes.get();
        for (Recipe<?> recipe : event.getServer().getRecipeManager().getRecipes()) {
            if (disabledRecipes.contains(recipe.getId().toString())) {
                toReplace.add(new DummyRecipe(recipe.getId()));
            } else {
                toReplace.add(recipe);
            }
        }

        event.getServer().getRecipeManager().replaceRecipes(toReplace);
    }


    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        CommandSetBreakData.register(commandDispatcher);
        CommandInfect.register(commandDispatcher);
        CommandAirdrop.register(commandDispatcher);
        CommandInfect.register(commandDispatcher);
        CommandCure.register(commandDispatcher);
        CommandSetQuality.register(commandDispatcher);
        CommandBloodmoon.register(commandDispatcher);
        CommandHorde.register(commandDispatcher);

    }
}
