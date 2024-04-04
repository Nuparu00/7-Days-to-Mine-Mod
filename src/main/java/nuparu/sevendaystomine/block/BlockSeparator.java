package nuparu.sevendaystomine.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntitySeparator;

public class BlockSeparator extends BlockTileProvider<TileEntitySeparator> {
	private static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.125F, 0.0F, 0.3125F, 0.875F, 0.46875F, 0.75);
	private static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.125F, 0.0F, 0.25F, 0.875F, 0.46875F, 0.6875F);
	private static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.3125F, 0.0F, 0.125F, 0.75F, 0.46875F, 0.875F);
	private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.25F, 0.0F, 0.125F, 0.6875F, 0.46875F, 0.875F);

	public BlockSeparator() {
		super(Material.GLASS);
		this.setSoundType(SoundType.GLASS);
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);
		this.setHardness(2F);
		this.setResistance(3.0F);
		this.setDefaultState(this.getDefaultState().withProperty(BlockHorizontalBase.FACING, EnumFacing.SOUTH));
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySeparator();
	}

	@Override
	public TileEntitySeparator createTileEntity(World world, IBlockState state) {
		return new TileEntitySeparator();
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
	public boolean isNormalCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumFacing) blockState.getValue(BlockHorizontalBase.FACING)) {
		default:
		case NORTH:
			return AABB_WEST;
		case SOUTH:
			return AABB_EAST;
		case WEST:
			return AABB_NORTH;
		case EAST:
			return AABB_SOUTH;
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)) {
		default:
		case NORTH:
			return AABB_WEST;
		case SOUTH:
			return AABB_EAST;
		case WEST:
			return AABB_NORTH;
		case EAST:
			return AABB_SOUTH;
		}
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double d0 = (double)((float)pos.getX() + 0.4F + rand.nextFloat() * 0.2F);
        double d1 = (double)((float)pos.getY() + 0.4F + rand.nextFloat() * 0.2F);
        double d2 = (double)((float)pos.getZ() + 0.4F + rand.nextFloat() * 0.2F);
        worldIn.spawnParticle(EnumParticleTypes.WATER_DROP, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking())
			return true;

		playerIn.openGui(SevenDaysToMine.instance, 23, worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof TileEntitySeparator) {
				NonNullList<ItemStack> drops = ((TileEntitySeparator) te).getDrops();
				for (ItemStack stack : drops) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
				}
			}
		}
		super.breakBlock(worldIn, pos, state);

	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		final NonNullList<ItemStack> drops = NonNullList.create();
		super.getDrops(drops, world, pos, state, fortune);

		final TileEntitySeparator tileEntity = getTileEntity(world, pos);
		if (tileEntity != null) {
			drops.addAll(tileEntity.getDrops());
		}

		return drops;
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
				placer.getHorizontalFacing().getOpposite());
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
