package nuparu.sevendaystomine.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.block.CodeSafeBlock;
import nuparu.sevendaystomine.world.level.block.entity.*;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,
            SevenDaysToMine.MODID);
    public static final RegistryObject<BlockEntityType<TorchBlockEntity>> TORCH = TILE_ENTITIES.register("torch",
            () -> BlockEntityType.Builder.of(TorchBlockEntity::new, ModBlocks.TORCH_LIT.get(), ModBlocks.TORCH_LIT_WALL.get(), ModBlocks.SOUL_TORCH_LIT.get(), ModBlocks.SOUL_TORCH_LIT_WALL.get()).build(null));


    public static final RegistryObject<BlockEntityType<ForgeBlockEntity>> FORGE = TILE_ENTITIES.register("forge",
            () -> BlockEntityType.Builder.of(ForgeBlockEntity::new, ModBlocks.FORGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<GrillBlockEntity>> COOKING_GRILL = TILE_ENTITIES.register("cooking_grill",
            () -> BlockEntityType.Builder.of(GrillBlockEntity::new, ModBlocks.COOKING_GRILL.get(), ModBlocks.COOKING_POT.get(), ModBlocks.BEAKER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ChemistryBlockEntity>> CHEMISTRY_STATION = TILE_ENTITIES.register("chemistry_station",
            () -> BlockEntityType.Builder.of(ChemistryBlockEntity::new, ModBlocks.CHEMISTRY_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<WorkbenchBlockEntity>> WORKBENCH = TILE_ENTITIES.register("workbench",
            () -> BlockEntityType.Builder.of(WorkbenchBlockEntity::new, ModBlocks.WORKBENCH.get()).build(null));

    public static final RegistryObject<BlockEntityType<SmallContainerBlockEntity>> SMALL_CONTAINER = TILE_ENTITIES.register("small_container",
            () -> BlockEntityType.Builder.of(SmallContainerBlockEntity::new, ModBlocks.CARDBOARD_BOX.get(), ModBlocks.CUPBOARD.get()
                    , ModBlocks.DRESSER.get(), ModBlocks.MOLDY_BACKPACK_NORMAL.get(), ModBlocks.MOLDY_BACKPACK_MEDICAL.get(), ModBlocks.MOLDY_BACKPACK_ARMY.get(), ModBlocks.MEDICAL_CABINET.get(), ModBlocks.WOODEN_WRITING_TABLE.get()
                    , ModBlocks.MODERN_WRITING_TABLE.get(), ModBlocks.FILE_CABINET.get(), ModBlocks.TOILET.get(), ModBlocks.TRASH_CAN.get(), ModBlocks.CASH_REGISTER.get(), ModBlocks.GARBAGE.get(), ModBlocks.MAILBOX.get()
                    , ModBlocks.FRIDGE.get(), ModBlocks.BIRD_NEST.get()).build(null));
    public static final RegistryObject<BlockEntityType<CodeSafeBlockEntity>> CODE_SAFE = TILE_ENTITIES.register("code_safe",
            () -> BlockEntityType.Builder.of(CodeSafeBlockEntity::new, ModBlocks.CODE_SAFE.get()).build(null));
    public static final RegistryObject<BlockEntityType<BookshelfBlockEntity>> BOOKSHELF = TILE_ENTITIES.register("bookshelf",
            () -> BlockEntityType.Builder.of(BookshelfBlockEntity::new, ModBlocks.OAK_BOOKSHELF.get(), ModBlocks.SPRUCE_BOOKSHELF.get(),
                    ModBlocks.BIRCH_BOOKSHELF.get(), ModBlocks.JUNGLE_BOOKSHELF.get(), ModBlocks.ACACIA_BOOKSHELF.get(),
                    ModBlocks.DARK_OAK_BOOKSHELF.get(), ModBlocks.WARPED_BOOKSHELF.get(),
                    ModBlocks.CRIMSON_BOOKSHELF.get()).build(null));

    public static final RegistryObject<BlockEntityType<SpikesBlockEntity>> SPIKES = TILE_ENTITIES.register("spikes",
            () -> BlockEntityType.Builder.of(SpikesBlockEntity::new, ModBlocks.WOODEN_SPIKES.get(), ModBlocks.WOODEN_SPIKES_BLOODIED.get(),
                    ModBlocks.WOODEN_SPIKES_BROKEN.get(), ModBlocks.WOODEN_SPIKES_DAMAGED.get(), ModBlocks.RAZOR_WIRE.get(), ModBlocks.METAL_SPIKES.get()).build(null));


    public static final RegistryObject<BlockEntityType<SleepingBagBlockEntity>> SLEEPING_BAG = TILE_ENTITIES.register("sleeping_bag",
            () -> BlockEntityType.Builder.of(SleepingBagBlockEntity::new, ModBlocks.SLEEPING_BAG_BLACK.get(), ModBlocks.SLEEPING_BAG_WHITE.get(),
                    ModBlocks.SLEEPING_BAG_RED.get(), ModBlocks.SLEEPING_BAG_PINK.get(),
                    ModBlocks.SLEEPING_BAG_BLUE.get(), ModBlocks.SLEEPING_BAG_LIGHT_BLUE.get(),
                    ModBlocks.SLEEPING_BAG_YELLOW.get(), ModBlocks.SLEEPING_BAG_ORANGE.get(),
                    ModBlocks.SLEEPING_BAG_GREEN.get(), ModBlocks.SLEEPING_BAG_LIME.get(),
                    ModBlocks.SLEEPING_BAG_BROWN.get(), ModBlocks.SLEEPING_BAG_GRAY.get(),
                    ModBlocks.SLEEPING_BAG_PURPLE.get(), ModBlocks.SLEEPING_BAG_MAGENTA.get(),
                    ModBlocks.SLEEPING_BAG_SILVER.get(), ModBlocks.SLEEPING_BAG_CYAN.get()).build(null));

    public static final RegistryObject<BlockEntityType<CalendarBlockEntity>> CALENDAR = TILE_ENTITIES.register("calendar",
            () -> BlockEntityType.Builder.of(CalendarBlockEntity::new, ModBlocks.CALENDAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<RadioBlockEntity>> RADIO = TILE_ENTITIES.register("radio",
            () -> BlockEntityType.Builder.of(RadioBlockEntity::new, ModBlocks.RADIO.get()).build(null));
    public static final RegistryObject<BlockEntityType<GlobeBlockEntity>> GLOBE = TILE_ENTITIES.register("globe",
            () -> BlockEntityType.Builder.of(GlobeBlockEntity::new, ModBlocks.GLOBE.get()).build(null));

}
