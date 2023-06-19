package nuparu.sevendaystomine.world.level.levelgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModBlocksTags;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ExtendedBlockAgeProcessor extends StructureProcessor {
    /*public static final Codec<ExtendedBlockAgeProcessor> CODEC = Codec.FLOAT.fieldOf("mossiness").xmap(ExtendedBlockAgeProcessor::new, (p_74023_) -> {
        return p_74023_.mossiness;
    }).codec();*/
    public static final Codec<ExtendedBlockAgeProcessor> CODEC = RecordCodecBuilder.create((p_74246_) -> {
        return p_74246_.group(Codec.floatRange(0.0F, 1.0F).fieldOf("mossiness").forGetter((p_163747_) -> {
            return p_163747_.mossiness;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter((p_163745_) -> {
            return p_163745_.chance;
        })).apply(p_74246_, ExtendedBlockAgeProcessor::new);
    });

    private static final float PROBABILITY_OF_REPLACING_FULL_BLOCK = 0.5F;
    private static final float PROBABILITY_OF_REPLACING_STAIRS = 0.5F;
    private static final float PROBABILITY_OF_REPLACING_OBSIDIAN = 0.15F;
    private static final BlockState[] NON_MOSSY_REPLACEMENTS = new BlockState[]{Blocks.STONE_SLAB.defaultBlockState(), Blocks.STONE_BRICK_SLAB.defaultBlockState()};
    private final float mossiness;
    private final float chance;

    private final HashMap<Block, BlockState[]> entries = new HashMap<>();

    public ExtendedBlockAgeProcessor(float mossiness, float chance) {
        this.mossiness = mossiness;
        this.chance = chance;

        entries.put(Blocks.STONE_BRICKS, new BlockState[]{
                Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState(), Blocks.MOSSY_COBBLESTONE.defaultBlockState()
                , Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState(), Blocks.STONE_BRICK_STAIRS.defaultBlockState()});

        entries.put(Blocks.STONE_BRICK_STAIRS, new BlockState[]{Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState()});

        entries.put(Blocks.STONE, new BlockState[]{Blocks.COBBLESTONE.defaultBlockState(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
                Blocks.STONE_STAIRS.defaultBlockState(), Blocks.COBBLESTONE_STAIRS.defaultBlockState(), Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState()});
        entries.put(Blocks.SMOOTH_STONE, new BlockState[]{Blocks.COBBLESTONE.defaultBlockState(), Blocks.MOSSY_COBBLESTONE.defaultBlockState()});
        entries.put(Blocks.SMOOTH_STONE_SLAB, new BlockState[]{Blocks.COBBLESTONE_SLAB.defaultBlockState(), Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState()});
        entries.put(Blocks.COBBLESTONE, new BlockState[]{Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
                Blocks.COBBLESTONE_STAIRS.defaultBlockState(), Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState()});

        entries.put(Blocks.BRICKS, new BlockState[]{ModBlocks.BRICK_MOSSY.get().defaultBlockState(),
                Blocks.BRICK_STAIRS.defaultBlockState(), ModBlocks.BRICKS_MOSSY_STAIRS.get().defaultBlockState()});
        entries.put(Blocks.BRICK_STAIRS, new BlockState[]{ModBlocks.BRICKS_MOSSY_STAIRS.get().defaultBlockState()});
        entries.put(Blocks.COBBLESTONE_WALL, new BlockState[]{Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState()});
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader p_74016_, BlockPos p_74017_, BlockPos p_74018_, StructureTemplate.StructureBlockInfo p_74019_, StructureTemplate.StructureBlockInfo p_74020_, StructurePlaceSettings p_74021_) {
        RandomSource randomsource = p_74021_.getRandom(p_74020_.pos);
        BlockState blockstate = p_74020_.state;
        BlockPos blockpos = p_74020_.pos;
        BlockState blockstate1 = null;

        BlockState[] replacements = entries.get(blockstate.getBlock());
        if (replacements != null) {
            blockstate1 = maybeReplaceBlock(randomsource, blockstate, replacements, ((block, randomSource) -> randomSource.nextFloat() < mossiness || !block.is(ModBlocksTags.MOSSY)));
        }

        /*

        if (!blockstate.is(Blocks.STONE_BRICKS) && !blockstate.is(Blocks.STONE) && !blockstate.is(Blocks.CHISELED_STONE_BRICKS)) {
            if (blockstate.is(BlockTags.STAIRS)) {
                blockstate1 = this.maybeReplaceStairs(randomsource, p_74020_.state);
            } else if (blockstate.is(BlockTags.SLABS)) {
                blockstate1 = this.maybeReplaceSlab(randomsource);
            } else if (blockstate.is(BlockTags.WALLS)) {
                blockstate1 = this.maybeReplaceWall(randomsource);
            } else if (blockstate.is(Blocks.OBSIDIAN)) {
                blockstate1 = this.maybeReplaceObsidian(randomsource);
            }
        } else {
            blockstate1 = this.maybeReplaceFullStoneBlock(randomsource);
        }*/

        return blockstate1 != null ? new StructureTemplate.StructureBlockInfo(blockpos, blockstate1, p_74020_.nbt) : p_74020_;
    }

    @Nullable
    private BlockState maybeReplaceBlock(RandomSource source, BlockState from, BlockState[] blocks, BiFunction<BlockState, RandomSource, Boolean> selector) {
        if (source.nextFloat() > chance) {
            return null;
        }
        if (blocks.length == 0) return null;
        int index = source.nextInt(blocks.length);
        BlockState state = blocks[index];

        if (!selector.apply(state, source)) return null;

        if (from.getBlock() instanceof StairBlock) {
            if (state.getBlock() instanceof StairBlock) {
                state = state.setValue(StairBlock.FACING, from.getValue(StairBlock.FACING)).setValue(StairBlock.HALF, from.getValue(StairBlock.HALF)).setValue(StairBlock.SHAPE, from.getValue(StairBlock.SHAPE));
            } else if (state.getBlock() instanceof SlabBlock) {
                state = state.setValue(SlabBlock.TYPE, from.getValue(StairBlock.HALF) == Half.BOTTOM ? SlabType.BOTTOM : SlabType.TOP);
            }
        } else if (from.getBlock() instanceof SlabBlock) {
            if (state.getBlock() instanceof SlabBlock) {
                state = state.setValue(SlabBlock.TYPE, from.getValue(SlabBlock.TYPE));
            }
        } else if (state.getBlock() instanceof StairBlock) {
            state = state.setValue(StairBlock.FACING, Direction.Plane.HORIZONTAL.
                    getRandomDirection(source)).setValue(StairBlock.HALF, Half.values()[source.nextInt(Half.values().length)]);

        } else if (from.getBlock() instanceof WallBlock) {
            if (state.getBlock() instanceof WallBlock) {
                state = state.setValue(WallBlock.UP, from.getValue(WallBlock.UP))
                        .setValue(WallBlock.EAST_WALL, from.getValue(WallBlock.EAST_WALL))
                        .setValue(WallBlock.WEST_WALL, from.getValue(WallBlock.WEST_WALL))
                        .setValue(WallBlock.NORTH_WALL, from.getValue(WallBlock.NORTH_WALL))
                        .setValue(WallBlock.SOUTH_WALL, from.getValue(WallBlock.SOUTH_WALL));
            }

        }

        return state;
    }

    @Nullable
    private BlockState maybeReplaceFullStoneBlock(RandomSource p_230256_) {
        if (p_230256_.nextFloat() >= 0.5F) {
            return null;
        } else {
            BlockState[] ablockstate = new BlockState[]{Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(p_230256_, Blocks.STONE_BRICK_STAIRS)};
            BlockState[] ablockstate1 = new BlockState[]{Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(p_230256_, Blocks.MOSSY_STONE_BRICK_STAIRS)};
            return this.getRandomBlock(p_230256_, ablockstate, ablockstate1);
        }
    }

    @Nullable
    private BlockState maybeReplaceStairs(RandomSource p_230261_, BlockState p_230262_) {
        Direction direction = p_230262_.getValue(StairBlock.FACING);
        Half half = p_230262_.getValue(StairBlock.HALF);
        if (p_230261_.nextFloat() >= 0.5F) {
            return null;
        } else {
            BlockState[] ablockstate = new BlockState[]{Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState().setValue(StairBlock.FACING, direction).setValue(StairBlock.HALF, half), Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState()};
            return this.getRandomBlock(p_230261_, NON_MOSSY_REPLACEMENTS, ablockstate);
        }
    }

    @Nullable
    private BlockState maybeReplaceSlab(RandomSource p_230271_) {
        return p_230271_.nextFloat() < this.mossiness ? Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState() : null;
    }

    @Nullable
    private BlockState maybeReplaceWall(RandomSource p_230273_) {
        return p_230273_.nextFloat() < this.mossiness ? Blocks.MOSSY_STONE_BRICK_WALL.defaultBlockState() : null;
    }

    @Nullable
    private BlockState maybeReplaceObsidian(RandomSource p_230275_) {
        return p_230275_.nextFloat() < 0.15F ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : null;
    }

    private static BlockState getRandomFacingStairs(RandomSource p_230258_, Block p_230259_) {
        return p_230259_.defaultBlockState().setValue(StairBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(p_230258_)).setValue(StairBlock.HALF, Half.values()[p_230258_.nextInt(Half.values().length)]);
    }

    private BlockState getRandomBlock(RandomSource p_230267_, BlockState[] p_230268_, BlockState[] p_230269_) {
        return p_230267_.nextFloat() < this.mossiness ? getRandomBlock(p_230267_, p_230269_) : getRandomBlock(p_230267_, p_230268_);
    }

    private static BlockState getRandomBlock(RandomSource p_230264_, BlockState[] p_230265_) {
        return p_230265_[p_230264_.nextInt(p_230265_.length)];
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_AGE;
    }
}
