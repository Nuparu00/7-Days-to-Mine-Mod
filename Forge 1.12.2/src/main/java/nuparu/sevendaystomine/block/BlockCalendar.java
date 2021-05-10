package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityCalendar;

public class BlockCalendar extends BlockTileProvider<TileEntityCalendar> {

	public BlockCalendar() {
		super(Material.CIRCUITS);
		setHardness(0f);
		setResistance(0.05f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontalBase.FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCalendar();
	}

	@Override
	public TileEntityCalendar createTileEntity(World world, IBlockState state) {
		return new TileEntityCalendar();
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
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumFacing) blockState.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.F, 0.4375F, 0.9375F, 0.75F, 1F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.25F, 0.4375F, 0.9375F, 0.75F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.4375F, 0.0F, 0.75F, 1F, 0.0625F);
		case WEST:
			return new AxisAlignedBB(0.9375F, 0.4375F, 0.25F, 1F, 1F, 0.75F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.4375F, 0.25F, 0.0625F, 1F, 0.75F);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.25F, 0.4375F, 0.9375F, 0.75F, 1F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.25F, 0.4375F, 0.9375F, 0.75F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.4375F, 0.0F, 0.75F, 1F, 0.0625F);
		case WEST:
			return new AxisAlignedBB(0.9375F, 0.4375F, 0.25F, 1F, 1F, 0.75F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.4375F, 0.25F, 0.0625F, 1F, 0.75F);
		}
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		 EnumFacing enumfacing = (EnumFacing)state.getValue(BlockHorizontalBase.FACING);

	        if (!world.getBlockState(pos.offset(enumfacing.getOpposite())).getMaterial().isSolid())
	        {
	            this.dropBlockAsItem(world, pos, state, 0);
	            world.setBlockToAir(pos);
	        }

	        super.neighborChanged(state, world, pos, blockIn, fromPos);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
		return (side != EnumFacing.DOWN && side != EnumFacing.UP) && worldIn.getBlockState(pos.offset(side.getOpposite())).getMaterial().isSolid();
    }

	

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(BlockHorizontalBase.FACING,
				rot.rotate((EnumFacing) state.getValue(BlockHorizontalBase.FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(BlockHorizontalBase.FACING)));
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING,
				facing);
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING, EnumFacing.getHorizontal(meta));
	}

	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)).getHorizontalIndex();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontalBase.FACING });
	}

}
