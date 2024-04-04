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
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.item.ItemLinkTool;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;

public class BlockComputer extends BlockTileProvider<TileEntityComputer> {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.3125F, 0.0F, 0.125F, 0.6875F, 1F, 0.9375);
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.3125F, 0.0F, 0.0625F, 0.6875F, 1F, 0.875F);
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0625F, 0.0F, 0.3125F, 0.875F, 1F, 0.6875F);
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.125F, 0.0F, 0.3125F, 0.9375, 1F, 0.6875F);

	public BlockComputer() {
		super(Material.IRON);
		this.setDefaultState(this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH));
		setHardness(1.5F);
		setResistance(1.0F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemLinkTool)
			return false;
		if (!worldIn.isRemote) {
			if (!playerIn.isSneaking()) {
				TileEntity TE = worldIn.getTileEntity(pos);
				if (TE != null && TE instanceof TileEntityComputer) {
					TileEntityComputer computerTE = (TileEntityComputer) TE;
					if (computerTE.getSystem() != EnumSystem.NONE && computerTE.isCompleted()) {
						if (computerTE.on) {
							computerTE.turnOff();
							TextComponentTranslation text = new TextComponentTranslation("computer.turn.off");
							text.getStyle().setColor(TextFormatting.GREEN);
							playerIn.sendMessage(text);
							return true;
						} else {
							computerTE.turnOn();
							TextComponentTranslation text = new TextComponentTranslation("computer.turn.on");
							text.getStyle().setColor(TextFormatting.GREEN);
							playerIn.sendMessage(text);
							return true;
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityComputer();
	}

	@Override
	public TileEntityComputer createTileEntity(World world, IBlockState state) {
		return new TileEntityComputer();
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityComputer) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityComputer) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}
		if (tileentity instanceof INetwork) {
			((INetwork) tileentity).disconnectAll();
		}

		super.breakBlock(worldIn, pos, state);
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
