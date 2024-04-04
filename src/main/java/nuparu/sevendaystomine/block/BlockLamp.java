package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.tileentity.TileEntityLamp;

public class BlockLamp extends BlockTileProvider<TileEntityLamp> {

	public static final PropertyBool LIT = PropertyBool.create("lit");

	public BlockLamp() {
		super(Material.ROCK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(LIT, false));
		this.setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
		setHardness(0.8f);
		setResistance(0.2f);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityLamp();
	}

	@Override
	public TileEntityLamp createTileEntity(World world, IBlockState state) {
		return new TileEntityLamp();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(LIT, meta == 1 ? true : false);
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
	public int getMetaFromState(IBlockState state) {
		return state.getValue(LIT) == true ? 1 : 0;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(LIT) == true ? 14 : 0;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return new AxisAlignedBB(0.25F, 0.5F, 0.25F, 0.75F, 1F, 0.75F);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.25F, 0.5F, 0.25F, 0.75F, 1F, 0.75F);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { LIT });
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity TE = worldIn.getTileEntity(pos);
		if (TE != null && TE instanceof TileEntityLamp) {
			TileEntityLamp lampTE = (TileEntityLamp) TE;
			if (playerIn.isSneaking()) {
				if (!worldIn.isRemote) {
					lampTE.setState(!lampTE.getState());
				}
				return true;
			}
		}
		return false;
	}

	public static void setState(World world, BlockPos pos, boolean state) {
		NBTTagCompound nbt = world.getTileEntity(pos).writeToNBT(new NBTTagCompound());
		world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, state));
		world.getTileEntity(pos).readFromNBT(nbt);
	}

	public static boolean isLit(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockLamp)) {
			return false;
		}
		return state.getValue(LIT);
	}

}
