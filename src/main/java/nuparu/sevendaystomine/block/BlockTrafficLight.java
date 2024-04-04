package nuparu.sevendaystomine.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

public class BlockTrafficLight extends BlockHorizontalBase {

	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.375D, 0, 0.5625D, 0.75D, 1, 1D);
	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.25D, 0, 0D, 0.625D, 1, 0.4375D);
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.5625D, 0.0D, 0.25D, 1, 1D, 0.625D);
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0D, 0.0D, 0.375D, 0.4375D, 1D, 0.75D);

	public BlockTrafficLight() {
		super(Material.ROCK);
		setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setHardness(1);
		this.setResistance(1.5F);
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return getBBByState(state);
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
		return getBBByState(state).offset(pos);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return getBBByState(state);
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}

	@SuppressWarnings("incomplete-switch")
	public static AxisAlignedBB getBBByState(IBlockState state) {
		EnumFacing facing = state.getValue(FACING);

		switch (facing) {
		case NORTH:
			return AABB_NORTH;
		case SOUTH:
			return AABB_SOUTH;
		case WEST:
			return AABB_WEST;
		case EAST:
			return AABB_EAST;
		}

		return AABB_SOUTH;
	}

}
