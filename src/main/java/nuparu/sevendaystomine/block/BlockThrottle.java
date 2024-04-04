package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.util.Utils;

public class BlockThrottle extends BlockArmchair {
	
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0F, 0.0F, 0.0625F, 1F, 0.75F, 1F);
	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0F, 0.0F, 0F, 1F, 0.75F, 0.9375F);
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0625F, 0.0F, 0F, 1F, 0.75F, 1F);
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0F, 0.0F, 0F, 0.9375F, 0.75F, 1F);
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) {
			return Utils.mountBlock(worldIn, pos, playerIn,0.25d);
		}
		return false;
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
}
