package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntityEnergyPole;

public class BlockEnergyPole extends BlockTileProvider<TileEntityEnergyPole> {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockEnergyPole() {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setHardness(1);
		setResistance(2);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
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
		switch ((EnumFacing) blockState.getValue(FACING)) {
		case UP:
			return new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1F, 0.625F);
		case DOWN:
		default:
			return new AxisAlignedBB(0.25F, 0.5F, 0.25F, 0.75F, 1F, 0.75F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.25F, 0F, 0.75F, 0.75F, 0.5F);
		case NORTH:
			return new AxisAlignedBB(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1F);
		case EAST:
			return new AxisAlignedBB(0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
		case WEST:
			return new AxisAlignedBB(0.5F, 0.25F, 0.25F, 1F, 0.75F, 0.75F);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(FACING)) {
		case UP:
			return new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1F, 0.625F);
		case DOWN:
		default:
			return new AxisAlignedBB(0.25F, 0.5F, 0.25F, 0.75F, 1F, 0.75F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.25F, 0F, 0.75F, 0.75F, 0.5F);
		case NORTH:
			return new AxisAlignedBB(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1F);
		case EAST:
			return new AxisAlignedBB(0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
		case WEST:
			return new AxisAlignedBB(0.5F, 0.25F, 0.25F, 1F, 0.75F, 0.75F);
		}
	}

	@Override
	public void neighborChanged(IBlockState blockstate, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		IBlockState state = world.getBlockState(pos.offset(world.getBlockState(pos).getValue(FACING).getOpposite()));
		if (state.getBlock() == Blocks.AIR) {
			dropBlockAsItem(world, pos, world.getBlockState(pos), 1);
			world.setBlockToAir(pos);
		}
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, facing);
	}

	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing;

		switch (meta) {
		case 0:
			enumfacing = EnumFacing.DOWN;
			break;
		case 1:
			enumfacing = EnumFacing.EAST;
			break;
		case 2:
			enumfacing = EnumFacing.WEST;
			break;
		case 3:
			enumfacing = EnumFacing.SOUTH;
			break;
		case 4:
			enumfacing = EnumFacing.NORTH;
			break;
		case 5:
		default:
			enumfacing = EnumFacing.UP;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		int i;

		switch ((EnumFacing) state.getValue(FACING)) {
		case EAST:
			i = 1;
			break;
		case WEST:
			i = 2;
			break;
		case SOUTH:
			i = 3;
			break;
		case NORTH:
			i = 4;
			break;
		case UP:
		default:
			i = 5;
			break;
		case DOWN:
			i = 0;
		}

		return i;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityEnergyPole();
	}

	@Override
	public TileEntityEnergyPole createTileEntity(World world, IBlockState state) {
		return new TileEntityEnergyPole();
	}

}
