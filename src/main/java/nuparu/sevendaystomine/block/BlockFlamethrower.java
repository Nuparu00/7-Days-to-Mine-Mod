package nuparu.sevendaystomine.block;

import java.util.Random;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntityFlamethrower;

public class BlockFlamethrower extends BlockTileProvider<TileEntityFlamethrower> {

	public BlockFlamethrower() {
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		this.setHardness(1);
		this.setResistance(1.5F);
		setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontalBase.FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityFlamethrower();
	}

	@Override
	public TileEntityFlamethrower createTileEntity(World world, IBlockState state) {
		return new TileEntityFlamethrower();
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
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand) {

		EnumFacing enumfacing = state.getValue(BlockHorizontalBase.FACING);
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.7 + rand.nextDouble() * 0.0625;
		double d2 = (double) pos.getZ() + 0.5D;
		double d3 = 0.55D;
		double d4 = rand.nextDouble() * 0.0625D - 0.03125D;

		switch (enumfacing) {
		case WEST:
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
			break;
		case EAST:
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
			break;
		case NORTH:
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
			break;
		case SOUTH:
			worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
			break;
		}

	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			playerIn.openGui(SevenDaysToMine.instance, 26, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (!worldIn.isRemote) {
			if (tileentity instanceof TileEntityFlamethrower) {
				{
					TileEntityFlamethrower flamethrower = (TileEntityFlamethrower) tileentity;
					NonNullList<ItemStack> drops = flamethrower.getDrops();
					for (ItemStack stack : drops) {
						worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
					}

				}

				worldIn.updateComparatorOutputLevel(pos, state.getBlock());
			}
		}

	}

	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity != null && tileentity instanceof TileEntityFlamethrower) {
			TileEntityFlamethrower flamethrower = (TileEntityFlamethrower) tileentity;
			return (int) (((float)flamethrower.getTank().getFluidAmount()/flamethrower.getTank().getCapacity())*15);
		}
		return 0;
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
