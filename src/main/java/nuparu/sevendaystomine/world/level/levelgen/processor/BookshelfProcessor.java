package nuparu.sevendaystomine.world.level.levelgen.processor;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import nuparu.sevendaystomine.init.ModStructureProcessors;
import nuparu.sevendaystomine.world.level.block.BookshelfBlock;

import javax.annotation.Nullable;

public class BookshelfProcessor extends StructureProcessor {


    public static final Codec<BookshelfProcessor> CODEC = Codec.unit(BookshelfProcessor::new);
    public static final BookshelfProcessor INSTANCE = new BookshelfProcessor();
    private boolean[] oldConnections;

    @Override
    public StructureTemplate.StructureBlockInfo  process(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData, @Nullable StructureTemplate template) {
        BlockState blockState = structureBlockInfoWorld.state();
        BlockPos worldPos = structureBlockInfoWorld.pos();
        RandomSource random = structurePlacementData.getRandom(worldPos);

        if (blockState.getBlock() instanceof BookshelfBlock) {
            CompoundTag nbt = new CompoundTag();
            if(random.nextInt(4) == 0) {
                nbt.putString("LootTable", "sevendaystomine:containers/bookshelf/bookshelf_rare");
            }
            else {
                nbt.putString("LootTable", "sevendaystomine:containers/bookshelf/bookshelf_common");
            }
            return new StructureTemplate.StructureBlockInfo(worldPos, blockState, nbt);
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.BOOKSHELF_PROCESSOR;
    }

}
