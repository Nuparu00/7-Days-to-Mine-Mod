package nuparu.sevendaystomine.world.level.block;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

public class TorchBlockBase extends TorchBlock implements IBlockBase{
    public TorchBlockBase(BlockBehaviour.Properties properties, ParticleOptions particleOptions) {
        super(properties, particleOptions);
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        return null;
    }
}
