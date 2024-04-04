package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.tileentity.TileEntityCamera;

public class BlockCamera extends BlockTileProvider<TileEntityCamera> {

	public BlockCamera() {
		super(Material.IRON);
		setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
		setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontalBase.FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCamera();
	}

	@Override
	public TileEntityCamera createTileEntity(World world, IBlockState state) {
		return new TileEntityCamera();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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
			return new AxisAlignedBB(0.25F, 0.375F, 0.7F, 0.75F, 0.6875F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.25F, 0.375F, 0.7F, 0.75F, 0.6875F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.375F, 0.0F, 0.75F, 0.6875F, 0.3F);
		case WEST:
			return new AxisAlignedBB(0.7F, 0.375F, 0.25F, 1F, 0.6875F, 0.75F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.375F, 0.25F, 0.3F, 0.6875F, 0.75F);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.25F, 0.375F, 0.7F, 0.75F, 0.6875F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.25F, 0.375F, 0.7F, 0.75F, 0.6875F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.25F, 0.375F, 0.0F, 0.75F, 0.6875F, 0.3F);
		case WEST:
			return new AxisAlignedBB(0.7F, 0.375F, 0.25F, 1F, 0.6875F, 0.75F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.375F, 0.25F, 0.3F, 0.6875F, 0.75F);
		}
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
		if (facing == EnumFacing.DOWN || facing == EnumFacing.UP)
			facing = placer.getHorizontalFacing().getOpposite();
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING, facing);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getFront(meta);

		if (facing.getAxis() == EnumFacing.Axis.Y) {
			facing = EnumFacing.NORTH;
		}

		return getDefaultState().withProperty(BlockHorizontalBase.FACING, facing);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityCamera) {
				TileEntityCamera cam = (TileEntityCamera)tileentity;
				cam.setCustomInventoryName(stack.getDisplayName());
				if(stack.getDisplayName().equals("state=off")) {
					cam.setOn(false);
				}
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof INetwork) {
			((INetwork) tileentity).disconnectAll();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(BlockHorizontalBase.FACING).build();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)).getIndex();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontalBase.FACING });
	}
}
