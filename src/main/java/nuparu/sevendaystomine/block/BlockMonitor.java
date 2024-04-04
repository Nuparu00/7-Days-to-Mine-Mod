package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityMonitor;

public class BlockMonitor extends BlockTileProvider<TileEntityMonitor> {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0F, 0.0F, 0.3125F, 1F, 0.75F, 0.75);
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0F, 0.0F, 0.25F, 1F, 0.75F, 0.6875F);
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.3125F, 0.0F, 0.0F, 0.75F, 0.75F, 1F);
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.25F, 0.0F, 0.0F, 0.6875F, 0.75F, 1F);

	public BlockMonitor() {
		super(Material.IRON);
		this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
		setHardness(1.4F);
		setResistance(1.0F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity TE = worldIn.getTileEntity(pos);
		if (TE != null && TE instanceof TileEntityMonitor) {
			TileEntityMonitor monitorTE = (TileEntityMonitor) TE;
			if (playerIn.isSneaking()) {
				if (!worldIn.isRemote) {
					monitorTE.setState(!monitorTE.getState());
					if (monitorTE.getState() == true) {
						TextComponentTranslation text = new TextComponentTranslation("computer.turn.on");
						text.getStyle().setColor(TextFormatting.GREEN);
						playerIn.sendMessage(text);
					} else {
						TextComponentTranslation text = new TextComponentTranslation("computer.turn.off");
						text.getStyle().setColor(TextFormatting.GREEN);
						playerIn.sendMessage(text);
					}
				}
				return true;
			} else {
				playerIn.openGui(SevenDaysToMine.instance, 8, worldIn, pos.getX(), pos.getY(), pos.getZ());
				if (!worldIn.isRemote) {
					monitorTE.addPlayer(playerIn);
				}
				return true;
			}
		}
		return true;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(ModBlocks.MONITOR_OFF);
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
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityMonitor();
	}

	@Override
	public TileEntityMonitor createTileEntity(World world, IBlockState state) {
		return new TileEntityMonitor();
	}

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);

		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}

		return getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(FACING)).getIndex();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

}
