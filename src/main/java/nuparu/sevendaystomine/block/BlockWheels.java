package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.item.ItemBlockQuality;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.tileentity.TileEntityWheels;

public class BlockWheels extends BlockTileProvider<TileEntityWheels> {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.125d, 0, 0.125d, 0.875d, 0.4375d, 0.875d);

	public BlockWheels() {
		super(Material.CLOTH);
		this.setSoundType(SoundType.CLOTH);
		this.setHardness(0.05f);
		this.setResistance(0.2f);
	}

	@Override
	public ItemBlock createItemBlock() {
		return new ItemBlockQuality(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityWheels();
	}

	@Override
	public TileEntityWheels createTileEntity(World world, IBlockState state) {
		return new TileEntityWheels();
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
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

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity != null && tileentity instanceof TileEntityWheels) {
			TileEntityWheels te = (TileEntityWheels) tileentity;
			if (ItemQuality.hasQualityTag(stack)) {
				te.setItemStack(stack);
			} else {
				te.generateItemStack();
			}
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote) {
			TileEntity tileentity = world.getTileEntity(pos);
			ItemStack stack = ItemStack.EMPTY;
			if (tileentity != null && tileentity instanceof TileEntityWheels) {
				TileEntityWheels te = (TileEntityWheels) tileentity;
				if (!te.getItemStack().isEmpty()) {
					stack = te.getItemStack().copy();
				} else {
					stack = te.generateItemStack().copy();
				}
			}
			if (!stack.isEmpty()) {
				Block.spawnAsEntity(world, pos, stack);
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		TileEntity tileentity = world.getTileEntity(pos);

		ItemStack stack = ItemStack.EMPTY;
		if (tileentity != null && tileentity instanceof TileEntityWheels) {
			TileEntityWheels te = (TileEntityWheels) tileentity;
			if (!te.getItemStack().isEmpty()) {
				stack = te.getItemStack();
			} else {
				stack = te.generateItemStack();
			}
		}
		return stack;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	@Override
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

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

}
