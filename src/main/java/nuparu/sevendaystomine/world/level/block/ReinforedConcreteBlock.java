package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import nuparu.sevendaystomine.init.ModBlocks;

public class ReinforedConcreteBlock extends BlockBase{
    public ReinforedConcreteBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player)
    {
        return new ItemStack(ModBlocks.REINFORCED_CONCRETE.get());
    }

}
