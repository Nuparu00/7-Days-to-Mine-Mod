package nuparu.sevendaystomine.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPlanksReinforcedIron extends BlockUpgradeable {
	public BlockPlanksReinforcedIron() {
		super(Material.IRON);
		setSoundType(SoundType.WOOD);
		setHardness(3.1f);
		setResistance(10f);
		setHarvestLevel("pickaxe", 1);
	}
	
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isTopSolid(IBlockState state)
    {
        return true;
    }
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return true;
    }
}
