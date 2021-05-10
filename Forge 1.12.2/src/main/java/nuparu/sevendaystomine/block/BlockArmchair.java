package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.util.Utils;

public class BlockArmchair extends BlockHorizontalBase implements IScrapable{
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0F, 0.0F, 0.0625F, 1F, 0.375F, 1F);
	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0F, 0.0F, 0F, 1F, 0.375F, 0.9375F);
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0625F, 0.0F, 0F, 1F, 0.375F, 1F);
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0F, 0.0F, 0F, 0.9375F, 0.375F, 1F);
	
	private EnumMaterial material = EnumMaterial.CLOTH;
	private int weight = 3;

	public BlockArmchair() {
		super(Material.CLOTH);
		setSoundType(SoundType.CLOTH);
		setHardness(1.5F);
		setResistance(2.0F);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		setHarvestLevel("axe", 0);
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumFacing) blockState.getValue(FACING)) {
		case UP:
		default:
			return AABB_SOUTH;
		case NORTH:
			return AABB_NORTH;
		case SOUTH:
			return AABB_SOUTH;
		case WEST:
			return AABB_WEST;
		case EAST:
			return AABB_EAST;
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(FACING)) {
		case UP:
		default:
			return AABB_SOUTH;
		case NORTH:
			return AABB_NORTH;
		case SOUTH:
			return AABB_SOUTH;
		case WEST:
			return AABB_WEST;
		case EAST:
			return AABB_EAST;
		}
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			return Utils.mountBlock(worldIn, pos, playerIn);
		}
		return false;
	}

}
