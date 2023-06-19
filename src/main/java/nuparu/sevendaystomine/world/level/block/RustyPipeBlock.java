package nuparu.sevendaystomine.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;

public class RustyPipeBlock extends PipeBlock implements IBlockBase, SimpleWaterloggedBlock {
    public RustyPipeBlock() {
        super(0,BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.COLOR_BROWN).strength(2.5f).sound(SoundType.METAL));
    }

    @Nullable
    @Override
    public BlockItem createBlockItem() {
        return null;
    }
}
