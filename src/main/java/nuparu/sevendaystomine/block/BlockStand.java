package nuparu.sevendaystomine.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.EnumMaterial;


public class BlockStand extends BlockBase{
	
	public BlockStand() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHardness(1.5F);
		setResistance(1.0F);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

}
