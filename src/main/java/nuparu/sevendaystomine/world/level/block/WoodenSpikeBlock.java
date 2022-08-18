package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlocks;

public class WoodenSpikeBlock extends SpikeBlock{

    public WoodenSpikeBlock(Properties p_49795_, int maxHealth, int damage) {
        super(p_49795_, maxHealth, damage);
    }

    @Override
    public void degradeBlock(BlockPos pos, Level level) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if(!(block instanceof WoodenSpikeBlock)) return;

        BlockState result = null;
        if(block == ModBlocks.WOODEN_SPIKES.get()){
            result = ModBlocks.WOODEN_SPIKES_BLOODIED.get().defaultBlockState();
        }
        else if(block == ModBlocks.WOODEN_SPIKES_BLOODIED.get()){
            result = ModBlocks.WOODEN_SPIKES_DAMAGED.get().defaultBlockState();
        } else if(block == ModBlocks.WOODEN_SPIKES_DAMAGED.get()){
            result = ModBlocks.WOODEN_SPIKES_BROKEN.get().defaultBlockState();
        } else if(block == ModBlocks.WOODEN_SPIKES_BROKEN.get()){
            level.destroyBlock(pos,false);
            return;
        }

        if(result != null){
            result = result.setValue(FACING,state.getValue(FACING)).setValue(WATERLOGGED,state.getValue(WATERLOGGED));
            level.setBlockAndUpdate(pos,result);
        }
    }
}
