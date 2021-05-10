package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;

public class BlockRadiator extends BlockHorizontalBase implements IScrapable{

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 3;
	
	public BlockRadiator() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHardness(1.5F);
		setResistance(1.0F);
	}

	@Override
	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	@Override
	public EnumMaterial getMaterial() {
		return material;
	}

	@Override
	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canBeScraped() {
		return true;
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
	
	@Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
	        switch ((EnumFacing)blockState.getValue(FACING))
	        {
	            case UP:
	            default:
	                return new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1.0F, 0.75F, 1F);
	            case NORTH:
	            	return new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1.0F, 0.75F, 1F);
	            case SOUTH:
	            	return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 0.3F);
	            case WEST:
	            	return new AxisAlignedBB(0.7F, 0.0F, 0F, 1F, 0.75F, 1F);
	            case EAST:
	            	return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.3F, 0.75F, 1F);
	        }
	    }
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		switch ((EnumFacing)state.getValue(FACING))
        {
            case UP:
            default:
                return new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1.0F, 0.75F, 1F);
            case NORTH:
            	return new AxisAlignedBB(0.0F, 0.0F, 0.7F, 1.0F, 0.75F, 1F);
            case SOUTH:
            	return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 0.3F);
            case WEST:
            	return new AxisAlignedBB(0.7F, 0.0F, 0F, 1F, 0.75F, 1F);
            case EAST:
            	return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.3F, 0.75F, 1F);
        }
    }

}
