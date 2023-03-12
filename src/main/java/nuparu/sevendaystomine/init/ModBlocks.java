package nuparu.sevendaystomine.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.level.block.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SevenDaysToMine.MODID);

    public static final RegistryObject<Block> BOARDS = registerBlockWithItem("plank_wood",
            () -> new BoardsBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(1.5F).noOcclusion()));

    public static final RegistryObject<Block> FUNGI_BOARDS = registerBlockWithItem("plank_fungi",
            () -> new BoardsBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(1.75F).noOcclusion()));
    public static final RegistryObject<WoodenFrameBlock> OAK_FRAME = registerBlockWithItem("oak_planks_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<WoodenFrameBlock> BIRCH_FRAME = registerBlockWithItem("birch_planks_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<WoodenFrameBlock> SPRUCE_FRAME = registerBlockWithItem("spruce_planks_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<WoodenFrameBlock> JUNGLE_FRAME = registerBlockWithItem("jungle_planks_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<WoodenFrameBlock> ACACIA_FRAME = registerBlockWithItem("acacia_planks_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<WoodenFrameBlock> DARK_OAK_FRAME = registerBlockWithItem("dark_oak_planks_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<WoodenFrameBlock> CRIMSON_FRAME = registerBlockWithItem("crimson_planks_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<WoodenFrameBlock> WARPED_FRAME = registerBlockWithItem("warped_planks_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<WoodenFrameBlock> BURNT_FRAME = registerBlockWithItem("burnt_frame",
            () -> new WoodenFrameBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLACK)
                    .strength(0.5F, 1.5F).sound(SoundType.WOOD).noOcclusion()), true);
    public static final RegistryObject<BlockBase> BURNT_PLANKS = registerBlockWithItem("burnt_planks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).color(MaterialColor.COLOR_BLACK)
                    .strength(0.8F, 1.9F)), true);


    public static final RegistryObject<BlockBase> OAK_PLANKS_REINFORCED = registerBlockWithItem("oak_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> BIRCH_PLANKS_REINFORCED = registerBlockWithItem("birch_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> SPRUCE_PLANKS_REINFORCED = registerBlockWithItem("spruce_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> JUNGLE_PLANKS_REINFORCED = registerBlockWithItem("jungle_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> ACACIA_PLANKS_REINFORCED = registerBlockWithItem("acacia_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> DARK_OAK_PLANKS_REINFORCED = registerBlockWithItem("dark_oak_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> CRIMSON_PLANKS_REINFORCED = registerBlockWithItem("crimson_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> WARPED_PLANKS_REINFORCED = registerBlockWithItem("warped_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.8F, 2.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> BURNT_PLANKS_REINFORCED = registerBlockWithItem("burnt_planks_reinforced",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLACK)
                    .strength(0.5F, 1.5F).sound(SoundType.WOOD)), true);

    public static final RegistryObject<BlockBase> OAK_PLANKS_REINFORCED_IRON = registerBlockWithItem("oak_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(3.1F, 6.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> BIRCH_PLANKS_REINFORCED_IRON = registerBlockWithItem("birch_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(3.1F, 6.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> SPRUCE_PLANKS_REINFORCED_IRON = registerBlockWithItem("spruce_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(3.1F, 6.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> JUNGLE_PLANKS_REINFORCED_IRON = registerBlockWithItem("jungle_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(3.1F, 6.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> ACACIA_PLANKS_REINFORCED_IRON = registerBlockWithItem("acacia_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(3.1F, 6.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> DARK_OAK_PLANKS_REINFORCED_IRON = registerBlockWithItem("dark_oak_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(3.1F, 6.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> CRIMSON_PLANKS_REINFORCED_IRON = registerBlockWithItem("crimson_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(3.1F, 6.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> WARPED_PLANKS_REINFORCED_IRON = registerBlockWithItem("warped_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(3.1F, 6.0F).sound(SoundType.WOOD)), true);
    public static final RegistryObject<BlockBase> BURNT_PLANKS_REINFORCED_IRON = registerBlockWithItem("burnt_planks_reinforced_iron",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLACK)
                    .strength(1.5F, 2.5F).sound(SoundType.WOOD)), true);

    public static final RegistryObject<BlockBase> STEEL_BLOCK = registerBlockWithItem("steel_block",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(6, 12).sound(SoundType.METAL)), true);

    public static final RegistryObject<BlockBase> BRONZE_BLOCK = registerBlockWithItem("bronze_block",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_YELLOW)
                    .strength(2f, 5).sound(SoundType.METAL)), true);

    public static final RegistryObject<BlockBase> LEAD_BLOCK = registerBlockWithItem("lead_block",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_CYAN)
                    .strength(4, 11).sound(SoundType.METAL).randomTicks()), true);

    public static final RegistryObject<BlockBase> BRASS_BLOCK = registerBlockWithItem("brass_block",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_YELLOW)
                    .strength(1.9f, 4).sound(SoundType.METAL)), true);
    public static final RegistryObject<Block> TORCH_UNLIT = registerBlockWithItem("torch_unlit", TorchUnlitBlock::new, false);

    public static final RegistryObject<Block> TORCH_UNLIT_WALL = registerBlockWithItem("torch_unlit_wall",
            TorchWallUnlitBlock::new, false);

    public static final RegistryObject<Block> SOUL_TORCH_UNLIT = registerBlockWithItem("soul_torch_unlit", TorchUnlitBlock::new, false);

    public static final RegistryObject<Block> SOUL_TORCH_UNLIT_WALL = registerBlockWithItem("soul_torch_unlit_wall",
            TorchWallUnlitBlock::new, false);

    public static final RegistryObject<Block> TORCH_LIT = registerBlockWithItem("torch_lit",
            () -> new TorchLitBlockBase(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_152607_) -> 14).sound(SoundType.WOOD), ParticleTypes.FLAME), false);

    public static final RegistryObject<Block> TORCH_LIT_WALL = registerBlockWithItem("torch_lit_wall",
            () -> new TorchWallLitBlockBase(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_152607_) -> 14).sound(SoundType.WOOD), ParticleTypes.FLAME), false);

    public static final RegistryObject<Block> SOUL_TORCH_LIT = registerBlockWithItem("soul_torch",
            () -> new TorchLitBlockBase(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_152607_) -> 10).sound(SoundType.WOOD), ParticleTypes.SOUL_FIRE_FLAME), false);

    public static final RegistryObject<Block> SOUL_TORCH_LIT_WALL = registerBlockWithItem("soul_torch_wall",
            () -> new TorchWallLitBlockBase(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().lightLevel((p_152607_) -> 10).sound(SoundType.WOOD), ParticleTypes.SOUL_FIRE_FLAME), false);

    public static final RegistryObject<Block> LANTERN_UNLIT = registerBlockWithItem("lantern_unlit", () -> new LanternBlockBase(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).noOcclusion()), true);

    public static final RegistryObject<Block> BRICK_MOSSY = BLOCKS.register("brick_block_mossy",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.BRICKS)));
    public static final RegistryObject<BlockBase> GRANITE_BRICKS = registerBlockWithItem("granite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);

    public static final RegistryObject<BlockBase> DIORITE_BRICKS = registerBlockWithItem("diorite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);

    public static final RegistryObject<BlockBase> ANDESITE_BRICKS = registerBlockWithItem("andesite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);


    public static final RegistryObject<BlockBase> MOSSY_GRANITE_BRICKS = registerBlockWithItem("mossy_granite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);

    public static final RegistryObject<BlockBase> MOSSY_DIORITE_BRICKS = registerBlockWithItem("mossy_diorite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);

    public static final RegistryObject<BlockBase> MOSSY_ANDESITE_BRICKS = registerBlockWithItem("mossy_andesite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);


    public static final RegistryObject<BlockBase> CRACKED_GRANITE_BRICKS = registerBlockWithItem("cracked_granite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);

    public static final RegistryObject<BlockBase> CRACKED_DIORITE_BRICKS = registerBlockWithItem("cracked_diorite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);

    public static final RegistryObject<BlockBase> CRACKED_ANDESITE_BRICKS = registerBlockWithItem("cracked_andesite_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);

    public static final RegistryObject<StairBlockBase> BRICKS_MOSSY_STAIRS = registerBlockWithItem("brick_mossy_stairs",
            () -> new StairBlockBase(() -> BRICK_MOSSY.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> GRANITE_BRICK_STAIRS = registerBlockWithItem("granite_brick_stairs",
            () -> new StairBlockBase(() -> GRANITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> DIORITE_BRICK_STAIRS = registerBlockWithItem("diorite_brick_stairs",
            () -> new StairBlockBase(() -> DIORITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> ANDESITE_BRICK_STAIRS = registerBlockWithItem("andesite_brick_stairs",
            () -> new StairBlockBase(() -> ANDESITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> MOSSY_GRANITE_BRICK_STAIRS = registerBlockWithItem("mossy_granite_brick_stairs",
            () -> new StairBlockBase(() -> MOSSY_GRANITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> MOSSY_DIORITE_BRICK_STAIRS = registerBlockWithItem("mossy_diorite_brick_stairs",
            () -> new StairBlockBase(() -> MOSSY_DIORITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> MOSSY_ANDESITE_BRICK_STAIRS = registerBlockWithItem("mossy_andesite_brick_stairs",
            () -> new StairBlockBase(() -> MOSSY_ANDESITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> CRACKED_GRANITE_BRICK_STAIRS = registerBlockWithItem("cracked_granite_brick_stairs",
            () -> new StairBlockBase(() -> CRACKED_GRANITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> CRACKED_DIORITE_BRICK_STAIRS = registerBlockWithItem("cracked_diorite_brick_stairs",
            () -> new StairBlockBase(() -> CRACKED_DIORITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<StairBlockBase> CRACKED_ANDESITE_BRICK_STAIRS = registerBlockWithItem("cracked_andesite_brick_stairs",
            () -> new StairBlockBase(() -> CRACKED_ANDESITE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_STAIRS)), true);

    public static final RegistryObject<SlabBlockBase> BRICK_MOSSY_SLAB = registerBlockWithItem("brick_mossy_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> GRANITE_BRICK_SLAB = registerBlockWithItem("granite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> DIORITE_BRICK_SLAB = registerBlockWithItem("diorite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> ANDESITE_BRICK_SLAB = registerBlockWithItem("andesite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> MOSSY_GRANITE_BRICK_SLAB = registerBlockWithItem("mossy_granite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> MOSSY_DIORITE_BRICK_SLAB = registerBlockWithItem("mossy_diorite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> MOSSY_ANDESITE_BRICK_SLAB = registerBlockWithItem("mossy_andesite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> CRACKED_GRANITE_BRICK_SLAB = registerBlockWithItem("cracked_granite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> CRACKED_DIORITE_BRICK_SLAB = registerBlockWithItem("cracked_diorite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<SlabBlockBase> CRACKED_ANDESITE_BRICK_SLAB = registerBlockWithItem("cracked_andesite_brick_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_SLAB)), true);

    public static final RegistryObject<BlockBase> WARPED_STONE_BRICKS = registerBlockWithItem("warped_stone_bricks",
            () -> new BlockBase(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)), true);

    public static final RegistryObject<OreBlockBase> TIN_ORE = registerBlockWithItem("tin_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), true);

    public static final RegistryObject<OreBlockBase> DEEPSLATE_TIN_ORE = registerBlockWithItem("deepslate_tin_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops().strength(4.0F, 4.0F).randomTicks()), true);

    public static final RegistryObject<OreBlockBase> ZINC_ORE = registerBlockWithItem("zinc_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), true);

    public static final RegistryObject<OreBlockBase> DEEPSLATE_ZINC_ORE = registerBlockWithItem("deepslate_zinc_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops().strength(4.0F, 4.0F).randomTicks()), true);

    public static final RegistryObject<OreBlockBase> LEAD_ORE = registerBlockWithItem("lead_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F).randomTicks()), true);

    public static final RegistryObject<OreBlockBase> DEEPSLATE_LEAD_ORE = registerBlockWithItem("deepslate_lead_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops().strength(4.0F, 4.0F).randomTicks()), true);

    public static final RegistryObject<OreBlockBase> POTASSIUM_ORE = registerBlockWithItem("potassium_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), true);

    public static final RegistryObject<OreBlockBase> DEEPSLATE_POTASSIUM_ORE = registerBlockWithItem("deepslate_potassium_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops().strength(4.0F, 4.0F).randomTicks()), true);

    public static final RegistryObject<OreBlockBase> CINNABAR_ORE = registerBlockWithItem("cinnabar_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)), true);

    public static final RegistryObject<OreBlockBase> DEEPSLATE_CINNABAR_ORE = registerBlockWithItem("deepslate_cinnabar_ore",
            () -> new OreBlockBase(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops().strength(4.0F, 4.0F).randomTicks()), true);

    public static final RegistryObject<BlockBase> ASPHALT = registerBlockWithItem("asphalt",
            () -> new BlockBase(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
                    .strength(4f).sound(SoundType.STONE)), true);

    public static final RegistryObject<StairBlockBase> ASPHALT_STAIRS = registerBlockWithItem("asphalt_stairs",
            () -> new StairBlockBase(() -> ModBlocks.ASPHALT.get().defaultBlockState() ,BlockBehaviour.Properties.copy(ModBlocks.ASPHALT.get())), true);

    public static final RegistryObject<SlabBlockBase> ASPHALT_SLAB = registerBlockWithItem("asphalt_slab",
            () -> new SlabBlockBase(BlockBehaviour.Properties.copy(ModBlocks.ASPHALT.get())), true);


    public static final RegistryObject<RebarFrameBlock> REBAR_FRAME = registerBlockWithItem("rebar_frame",
            () -> new RebarFrameBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_GRAY)
                    .strength(1.9f, 4).sound(SoundType.METAL)), true);

    public static final RegistryObject<RebarFrameWoodBlock> REBAR_FRAME_WOOD = registerBlockWithItem("rebar_frame_wood",
            () -> new RebarFrameWoodBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.TERRACOTTA_GRAY)
                    .strength(2.4f, 5).sound(SoundType.METAL)), true);

    public static final RegistryObject<ReinforcedConcreteWet> REINFORCED_CONCRETE_WET = registerBlockWithItem("reinforced_concrete_wet",
            () -> new ReinforcedConcreteWet(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY)
                    .strength(4f, 6).sound(SoundType.MUD).randomTicks()), true);

    public static final RegistryObject<ReinforedConcreteBlock> REINFORCED_CONCRETE = registerBlockWithItem("reinforced_concrete",
            () -> new ReinforedConcreteBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY)
                    .strength(6f, 8).sound(SoundType.STONE)), true);

    public static final RegistryObject<ReinforedConcreteBlock> REINFORCED_CONCRETE_CRACKED_0 = registerBlockWithItem("reinforced_concrete_cracked_0",
            () -> new ReinforedConcreteBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY)
                    .strength(5.8f, 7.8f).sound(SoundType.STONE)), false);

    public static final RegistryObject<ReinforedConcreteBlock> REINFORCED_CONCRETE_CRACKED_1 = registerBlockWithItem("reinforced_concrete_cracked_1",
            () -> new ReinforedConcreteBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY)
                    .strength(5.5f, 7.5f).sound(SoundType.STONE)), false);

    public static final RegistryObject<ReinforedConcreteBlock> REINFORCED_CONCRETE_CRACKED_2 = registerBlockWithItem("reinforced_concrete_cracked_2",
            () -> new ReinforedConcreteBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_GRAY)
                    .strength(5f, 7f).sound(SoundType.STONE)), false);

    public static final RegistryObject<CatwalkBlock> CATWALK = registerBlockWithItem("catwalk",
            () -> new CatwalkBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(1f).sound(SoundType.METAL).noOcclusion()), true);

    public static final RegistryObject<CatwalkStairsBlock> CATWALK_STAIRS = registerBlockWithItem("catwalk_stairs",
            () -> new CatwalkStairsBlock(() -> ModBlocks.CATWALK.get().defaultBlockState(), BlockBehaviour.Properties.copy(ModBlocks.CATWALK.get())), true);

    public static final RegistryObject<Block> TABLE_OAK = registerBlockWithItem("oak_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);
    public static final RegistryObject<Block> TABLE_BIRCH = registerBlockWithItem("birch_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);
    public static final RegistryObject<Block> TABLE_SPRUCE = registerBlockWithItem("spruce_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);
    public static final RegistryObject<Block> TABLE_JUNGLE = registerBlockWithItem("jungle_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);
    public static final RegistryObject<Block> TABLE_ACACIA = registerBlockWithItem("acacia_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);
    public static final RegistryObject<Block> TABLE_BIG_OAK = registerBlockWithItem("big_oak_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);
    public static final RegistryObject<Block> TABLE_CRIMSON = registerBlockWithItem("crimson_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);
    public static final RegistryObject<Block> TABLE_WARPED = registerBlockWithItem("warped_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);
    public static final RegistryObject<Block> TABLE_BURNT = registerBlockWithItem("burnt_table",
            () -> new TableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)), true);

    public static final RegistryObject<Block> CHAIR_OAK = registerBlockWithItem("chair_oak",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);
    public static final RegistryObject<Block> CHAIR_BIRCH = registerBlockWithItem("chair_birch",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);
    public static final RegistryObject<Block> CHAIR_SPRUCE = registerBlockWithItem("chair_spruce",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);
    public static final RegistryObject<Block> CHAIR_JUNGLE = registerBlockWithItem("chair_jungle",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);
    public static final RegistryObject<Block> CHAIR_ACACIA = registerBlockWithItem("chair_acacia",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);
    public static final RegistryObject<Block> CHAIR_BIG_OAK = registerBlockWithItem("chair_big_oak",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);
    public static final RegistryObject<Block> CHAIR_CRIMSON = registerBlockWithItem("chair_crimson",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);
    public static final RegistryObject<Block> CHAIR_WARPED = registerBlockWithItem("chair_warped",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);
    public static final RegistryObject<Block> CHAIR_BURNT = registerBlockWithItem("chair_burnt",
            () -> new ChairBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f)),true);

    public static final RegistryObject<Block> ARMCHAIR_BLACK = registerBlockWithItem("armchair_black",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_WHITE = registerBlockWithItem("armchair_white",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_RED = registerBlockWithItem("armchair_red",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_GREEN = registerBlockWithItem("armchair_green",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_BROWN = registerBlockWithItem("armchair_brown",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_PINK = registerBlockWithItem("armchair_pink",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_YELLOW = registerBlockWithItem("armchair_yellow",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_BLUE = registerBlockWithItem("armchair_blue",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_CYAN = registerBlockWithItem("armchair_cyan",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_PURPLE = registerBlockWithItem("armchair_purple",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_ORANGE = registerBlockWithItem("armchair_orange",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_MAGENTA = registerBlockWithItem("armchair_magenta",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_LIME = registerBlockWithItem("armchair_lime",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_LIGHT_BLUE = registerBlockWithItem("armchair_light_blue",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_SILVER = registerBlockWithItem("armchair_silver",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));
    public static final RegistryObject<Block> ARMCHAIR_GRAY = registerBlockWithItem("armchair_gray",
            () -> new ArmchairBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL)));

    public static final RegistryObject<Block> SOFA_BLACK = registerBlockWithItem("sofa_black",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_WHITE = registerBlockWithItem("sofa_white",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_RED = registerBlockWithItem("sofa_red",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_GREEN = registerBlockWithItem("sofa_green",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_BLUE = registerBlockWithItem("sofa_blue",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_BROWN = registerBlockWithItem("sofa_brown",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_PINK = registerBlockWithItem("sofa_pink",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_YELLOW = registerBlockWithItem("sofa_yellow",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_CYAN = registerBlockWithItem("sofa_cyan",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_PURPLE = registerBlockWithItem("sofa_purple",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_ORANGE = registerBlockWithItem("sofa_orange",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_MAGENTA = registerBlockWithItem("sofa_magenta",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_LIME = registerBlockWithItem("sofa_lime",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_SILVER = registerBlockWithItem("sofa_silver",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_LIGHT_BLUE = registerBlockWithItem("sofa_light_blue",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> SOFA_GRAY = registerBlockWithItem("sofa_gray",
            () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.BLACK_WOOL).noOcclusion()));

    public static final RegistryObject<Block> METAL_LADDER = registerBlockWithItem("metal_ladder",
            () -> new MetalLadderBlock(BlockBehaviour.Properties.of(Material.DECORATION).sound(SoundType.METAL).strength(1.5F)
                    .sound(SoundType.LADDER).noOcclusion()));

    public static final RegistryObject<Block> RADIATOR = registerBlockWithItem("radiator",
            () -> new RadiatorBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.5F).noOcclusion()));

    public static final RegistryObject<Block> SINK_FAUCET = registerBlockWithItem("sink_faucet",
            () -> new SinkFaucetBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(0.75F).noOcclusion()));
    public static final RegistryObject<Block> SHOWER_HEAD = registerBlockWithItem("shower_head",
            () -> new ShowerHeadBlock(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(0.75F).noOcclusion()));

    public static final RegistryObject<Block> STONE_SMALL_ROCK = registerBlockWithItem("stone_small_rock",
            () -> new SmallRockBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE).offsetType(BlockBehaviour.OffsetType.XZ).dynamicShape()));
    public static final RegistryObject<Block> ANDESITE_SMALL_ROCK = registerBlockWithItem("andesite_small_rock",
            () -> new SmallRockBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE).offsetType(BlockBehaviour.OffsetType.XZ).dynamicShape()));
    public static final RegistryObject<Block> DIORITE_SMALL_ROCK = registerBlockWithItem("diorite_small_rock",
            () -> new SmallRockBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE).offsetType(BlockBehaviour.OffsetType.XZ).dynamicShape()));
    public static final RegistryObject<Block> GRANITE_SMALL_ROCK = registerBlockWithItem("granite_small_rock",
            () -> new SmallRockBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE).offsetType(BlockBehaviour.OffsetType.XZ).dynamicShape()));
    public static final RegistryObject<WaterloggableHorizontalBlockBase> LARGE_ROCK = registerBlockWithItem("large_rock",
            () -> new LargeRockBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()), true);
    public static final RegistryObject<Block> PICKABLE_STICK = registerBlockWithItem("pickable_stick",
            () -> new HorizontalPickableBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.WOOD)));
    public static final RegistryObject<Block> GOLDENROD = registerBlockWithItem("goldenrod",
            () -> new FlowerBlockBase(MobEffects.DIG_SPEED, 7,
                    BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> POTTED_GOLDENROD = registerBlockWithItem("potted_goldenrod", () -> new FlowerPotBlockBase( () -> (FlowerPotBlock)Blocks.FLOWER_POT, GOLDENROD, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()),false);
    public static final RegistryObject<Block> CORN_PLANT = registerBlockWithItem("corn_plant",
            () -> new CornPlantBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.CROP)), false);
    public static final RegistryObject<Block> COFFEE_PLANT = registerBlockWithItem("coffee_plant",
            () -> new CoffeePlantBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.CROP)), false);

    public static final RegistryObject<Block> TRAFFIC_LIGHT = registerBlockWithItem("traffic_light",
            () -> new TrafficLightBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(1.5F, 1F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> TRAFFIC_LIGHT_PEDESTRIAN = registerBlockWithItem("traffic_light_pedestrian",
            () -> new PedestrianTrafficLightBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(1.5F, 1F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> CINDER_BLOCKS = registerBlockWithItem("cinder_blocks",
            () -> new HorizontalPickableBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.STONE)));
    public static final RegistryObject<Block> DRY_GROUND = registerBlockWithItem("dry_ground",
            () -> new FallingBlockBase(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.GRAVEL)));
    public static final RegistryObject<Block> SANDBAGS = registerBlockWithItem("sandbags",
            () -> new FallingWaterloggableHorizontalBlockBase(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.GRAVEL).noOcclusion()));

    public static final RegistryObject<Block> SKELETON = registerBlockWithItem("skeleton",
            () -> new SkeletonBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE)
                    .strength(0.2F, 0.2F).sound(SoundType.BONE_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> SKELETON_SITTING = registerBlockWithItem("skeleton_sitting",
            () -> new SkeletonBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE)
                    .strength(0.2F, 0.2F).sound(SoundType.BONE_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> SKELETON_TORSO = registerBlockWithItem("skeleton_torso",
            () -> new SkeletonBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_WHITE)
                    .strength(0.2F, 0.2F).sound(SoundType.BONE_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> BROKEN_REDSTONE_LAMP = registerBlockWithItem("broken_redstone_lamp",
            () -> new BrokenRedstoneLampBlock(BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS).strength(0.3F).sound(SoundType.GLASS)));

    public static final RegistryObject<Block> FORGE = registerBlockWithItem("forge",
            () -> new ForgeBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3F).sound(SoundType.STONE).noOcclusion().lightLevel(litBlockEmission(13))));

    public static final RegistryObject<Block> COOKING_GRILL = registerBlockWithItem("cooking_grill",
            () -> new CookingGrillBLock(BlockBehaviour.Properties.of(Material.METAL).strength(1F).sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> COOKING_POT = registerBlockWithItem("cooking_pot",
            () -> new CookingPotBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.METAL)));

    public static final RegistryObject<Block> BEAKER = registerBlockWithItem("beaker",
            () -> new BeakerBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                    .strength(0.5F, 0.25F).instabreak().sound(SoundType.METAL)));

    public static final RegistryObject<Block> CHEMISTRY_STATION = registerBlockWithItem("chemistry_station",
            () -> new ChemistryStationBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3F).sound(SoundType.METAL).noOcclusion().lightLevel(litBlockEmission(4))));

    public static final RegistryObject<Block> WORKBENCH = registerBlockWithItem("workbench",
            () -> new WorkbenchBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2F).sound(SoundType.WOOD).noOcclusion()));

    public static final RegistryObject<Block> CARDBOARD_BOX = registerBlockWithItem("cardboard_box",
            () -> new CardboardBoxBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(1f).sound(SoundType.WOOL)));

    public static final RegistryObject<Block> CUPBOARD = registerBlockWithItem("cupboard",
            () -> new CupboardBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> DRESSER = registerBlockWithItem("dresser",
            () -> new DresserBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MOLDY_BACKPACK_NORMAL = registerBlockWithItem("moldy_backpack_normal",
            () -> new BackpackBlock(BlockBehaviour.Properties.of(Material.WOOL).strength(1.5f).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> MOLDY_BACKPACK_MEDICAL = registerBlockWithItem("moldy_backpack_medical",
            () -> new BackpackBlock(BlockBehaviour.Properties.of(Material.WOOL).strength(1.75f).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> MOLDY_BACKPACK_ARMY = registerBlockWithItem("moldy_backpack_army",
            () -> new BackpackBlock(BlockBehaviour.Properties.of(Material.WOOL).strength(2f).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> MEDICAL_CABINET = registerBlockWithItem("medical_cabinet",
            () -> new MedicalCabinetBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.25f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> WOODEN_WRITING_TABLE = registerBlockWithItem("wooden_writing_table",
            () -> new WritingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MODERN_WRITING_TABLE = registerBlockWithItem("modern_writing_table",
            () -> new WritingTableBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FILE_CABINET = registerBlockWithItem("file_cabinet",
            () -> new FileCabinetBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.8f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> TOILET = registerBlockWithItem("toilet",
            () -> new ToiletBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> TRASH_CAN = registerBlockWithItem("trash_can",
            () -> new TrashCanBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3.5f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> CASH_REGISTER = registerBlockWithItem("cash_register",
            () -> new CashRegisterBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3.5f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> GARBAGE = registerBlockWithItem("garbage",
            () -> new GarbageBlock(BlockBehaviour.Properties.of(Material.WOOL).strength(0.7f).sound(SoundType.WOOL)));
    public static final RegistryObject<Block> MAILBOX = registerBlockWithItem("mailbox",
            () -> new MailboxBlock(BlockBehaviour.Properties.of(Material.METAL).strength(1.5f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> CODE_SAFE = registerBlockWithItem("code_safe",
            () -> new CodeSafeBlock(BlockBehaviour.Properties.of(Material.METAL).strength(4.5f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> OAK_BOOKSHELF = registerBlockWithItem("oak_bookshelf",
            () -> new BookshelfBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> SPRUCE_BOOKSHELF = registerBlockWithItem("spruce_bookshelf",
            () -> new BookshelfBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
    public static final RegistryObject<Block> BIRCH_BOOKSHELF = registerBlockWithItem("birch_bookshelf",
            () -> new BookshelfBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
    public static final RegistryObject<Block> JUNGLE_BOOKSHELF = registerBlockWithItem("jungle_bookshelf",
            () -> new BookshelfBlock(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
    public static final RegistryObject<Block> ACACIA_BOOKSHELF = registerBlockWithItem("acacia_bookshelf",
            () -> new BookshelfBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
    public static final RegistryObject<Block> DARK_OAK_BOOKSHELF = registerBlockWithItem("dark_oak_bookshelf",
            () -> new BookshelfBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
    public static final RegistryObject<Block> WARPED_BOOKSHELF = registerBlockWithItem("warped_bookshelf",
            () -> new BookshelfBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
    public static final RegistryObject<Block> CRIMSON_BOOKSHELF = registerBlockWithItem("crimson_bookshelf",
            () -> new BookshelfBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));

    public static final RegistryObject<Block> FRIDGE = registerBlockWithItem("fridge",
            () -> new RefrigeratorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.75f).sound(SoundType.METAL)));
    public static final RegistryObject<Block> BIRD_NEST = registerBlockWithItem("bird_nest",
            () -> new BirdNestBlock(BlockBehaviour.Properties.of(Material.GRASS).strength(0.55f).sound(SoundType.GRASS)));

    public static final RegistryObject<Block> WOODEN_SPIKES = registerBlockWithItem("wooden_spikes",
            () -> new WoodenSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.75f).sound(SoundType.WOOD).noOcclusion().noCollission(),1,5));

    public static final RegistryObject<Block> WOODEN_SPIKES_BLOODIED = registerBlockWithItem("wooden_spikes_bloodied",
            () -> new WoodenSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.75f).sound(SoundType.WOOD).noOcclusion().noCollission(),200,5));

    public static final RegistryObject<Block> WOODEN_SPIKES_DAMAGED = registerBlockWithItem("wooden_spikes_damaged",
            () -> new WoodenSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.75f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,4));

    public static final RegistryObject<Block> WOODEN_SPIKES_BROKEN = registerBlockWithItem("wooden_spikes_broken",
            () -> new WoodenSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2.75f).sound(SoundType.WOOD).noOcclusion().noCollission(),100,3));

    public static final RegistryObject<Block> RAZOR_WIRE = registerBlockWithItem("razor_wire",
            () -> new RazorWireBlock(BlockBehaviour.Properties.of(Material.METAL).strength(1.75f).sound(SoundType.METAL).noOcclusion().noCollission(),350,6));

    public static final RegistryObject<Block> OAK_LOG_SPIKE = registerBlockWithItem("oak_log_spike",
            () -> new LogSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,2f));
    public static final RegistryObject<Block> SPRUCE_LOG_SPIKE = registerBlockWithItem("spruce_log_spike",
            () -> new LogSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,2f));
    public static final RegistryObject<Block> BIRCH_LOG_SPIKE = registerBlockWithItem("birch_log_spike",
            () -> new LogSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,2f));
    public static final RegistryObject<Block> JUNGLE_LOG_SPIKE = registerBlockWithItem("jungle_log_spike",
            () -> new LogSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,2f));
    public static final RegistryObject<Block> ACACIA_LOG_SPIKE = registerBlockWithItem("acacia_log_spike",
            () -> new LogSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,2f));
    public static final RegistryObject<Block> DARK_OAK_LOG_SPIKE = registerBlockWithItem("dark_oak_log_spike",
            () -> new LogSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,2f));
    public static final RegistryObject<Block> WARPED_LOG_SPIKE = registerBlockWithItem("warped_log_spike",
            () -> new LogSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,2f));
    public static final RegistryObject<Block> CRIMSON_LOG_SPIKE = registerBlockWithItem("crimson_log_spike",
            () -> new LogSpikeBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).noOcclusion().noCollission(),150,2f));
    public static final RegistryObject<Block> LANDMINE = registerBlockWithItem("landmine",
            () -> new LandmineBlock(BlockBehaviour.Properties.of(Material.METAL).strength(1f).sound(SoundType.METAL).noCollission(),3.5f));

    public static final RegistryObject<Block> METAL_SPIKES = registerBlockWithItem("metal_spikes",
            () -> new MetalSpikeBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f).sound(SoundType.METAL).noOcclusion().noCollission(),400,5));

    public static final RegistryObject<Block> SLEEPING_BAG_BLACK = registerBlockWithItem("sleeping_bag_black",
            () -> new SleepingBagBlock(DyeColor.BLACK, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_WHITE = registerBlockWithItem("sleeping_bag_white",
            () -> new SleepingBagBlock(DyeColor.WHITE, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_RED = registerBlockWithItem("sleeping_bag_red",
            () -> new SleepingBagBlock(DyeColor.RED, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_BLUE = registerBlockWithItem("sleeping_bag_blue",
            () -> new SleepingBagBlock(DyeColor.BLUE, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_GREEN = registerBlockWithItem("sleeping_bag_green",
            () -> new SleepingBagBlock(DyeColor.GREEN, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_YELLOW = registerBlockWithItem("sleeping_bag_yellow",
            () -> new SleepingBagBlock(DyeColor.YELLOW, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_ORANGE = registerBlockWithItem("sleeping_bag_orange",
            () -> new SleepingBagBlock(DyeColor.ORANGE, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_CYAN = registerBlockWithItem("sleeping_bag_cyan",
            () -> new SleepingBagBlock(DyeColor.CYAN, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_LIME = registerBlockWithItem("sleeping_bag_lime",
            () -> new SleepingBagBlock(DyeColor.LIME, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_GRAY = registerBlockWithItem("sleeping_bag_gray",
            () -> new SleepingBagBlock(DyeColor.GRAY, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_SILVER = registerBlockWithItem("sleeping_bag_silver",
            () -> new SleepingBagBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_LIGHT_BLUE = registerBlockWithItem("sleeping_bag_light_blue",
            () -> new SleepingBagBlock(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_PINK = registerBlockWithItem("sleeping_bag_pink",
            () -> new SleepingBagBlock(DyeColor.PINK, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_PURPLE = registerBlockWithItem("sleeping_bag_purple",
            () -> new SleepingBagBlock(DyeColor.PURPLE, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_MAGENTA = registerBlockWithItem("sleeping_bag_magenta",
            () -> new SleepingBagBlock(DyeColor.MAGENTA, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));
    public static final RegistryObject<Block> SLEEPING_BAG_BROWN = registerBlockWithItem("sleeping_bag_brown",
            () -> new SleepingBagBlock(DyeColor.BROWN, BlockBehaviour.Properties.copy(Blocks.BLACK_BED).strength(0.05F)));

    public static final RegistryObject<CalendarBlock> CALENDAR = registerBlockWithItem("calendar",
            () -> new CalendarBlock(BlockBehaviour.Properties.of(Material.CLOTH_DECORATION).strength(0).instabreak().sound(SoundType.WOOL)));

    public static final RegistryObject<RadioBlock> RADIO = registerBlockWithItem("radio",
            () -> new RadioBlock(BlockBehaviour.Properties.of(Material.METAL).strength(0.5f).sound(SoundType.METAL)));

    public static final RegistryObject<GlobeBlock> GLOBE = registerBlockWithItem("globe",
            () -> new GlobeBlock(BlockBehaviour.Properties.of(Material.METAL).strength(1f).sound(SoundType.METAL)));

    public static final RegistryObject<StandBlock> STAND = registerBlockWithItem("stand",
            () -> new StandBlock(BlockBehaviour.Properties.of(Material.METAL).strength(1.8f).sound(SoundType.METAL)));
    public static final RegistryObject<WheelsBlock> WHEELS = registerBlockWithItem("wheels",
            () -> new WheelsBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BLACK)
                    .strength(1).sound(SoundType.WOOL)), true);
    public static final RegistryObject<PhotoBlock> PHOTO = registerBlockWithItem("photo",
            () -> new PhotoBlock(BlockBehaviour.Properties.of(Material.WOOL)
                    .strength(0.5f).sound(SoundType.WOOL).instabreak().noOcclusion()), false);

    public static final RegistryObject<SolarPanelBlock> SOLAR_PANEL = registerBlockWithItem("solar_panel",
            () -> new SolarPanelBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(2.5f).sound(SoundType.GLASS).noOcclusion()));

    public static final RegistryObject<CombustionGeneratorBlock> COMBUSTION_GENERATOR = registerBlockWithItem("combustion_generator",
            () -> new CombustionGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).noOcclusion().lightLevel(litBlockEmission(8))));

    public static final RegistryObject<WindTurbineBlock> WIND_TURBINE = registerBlockWithItem("wind_turbine",
            () -> new WindTurbineBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.5F).sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> WIND_TURBINE_BLADES = BLOCKS.register("wind_turbine_blades",() -> new Block(BlockBehaviour.Properties.copy(ModBlocks.WIND_TURBINE.get())));

    public static final RegistryObject<EnergyPoleBlock> ENERGY_POLE = registerBlockWithItem("energy_pole",
            () -> new EnergyPoleBlock(BlockBehaviour.Properties.of(Material.WOOD)
                    .strength(1.5f).sound(SoundType.WOOD).noOcclusion()));


    private static <BLOCK extends Block> RegistryObject<BLOCK> registerBlockWithItem(String name, Supplier<BLOCK> blockFactory, boolean hasItem) {
        RegistryObject<BLOCK> block = BLOCKS.register(name, blockFactory);
        if (hasItem) {
            RegistryObject<BlockItem> item = ModItems.ITEMS.register(name, () -> ((IBlockBase) block.get()).createBlockItem());

        }
        return block;
    }

    private static <BLOCK extends Block> RegistryObject<BLOCK> registerBlockWithItem(String name, Supplier<BLOCK> blockFactory) {
        return registerBlockWithItem(name,blockFactory,true);
    }


    public static ToIntFunction<BlockState> litBlockEmission(int p_235420_0_) {
        return (p_235421_1_) -> p_235421_1_.getValue(BlockStateProperties.LIT) ? p_235420_0_ : 0;
    }
}