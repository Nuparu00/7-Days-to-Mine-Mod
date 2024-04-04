package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemWire;
import nuparu.sevendaystomine.tileentity.TileEntityEnergySwitch;

public class BlockEnergySwitch extends BlockTileProvider<TileEntityEnergySwitch> {

	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockEnergySwitch() {
		super(Material.WOOD);
		setSoundType(SoundType.WOOD);
		setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
		setDefaultState(this.blockState.getBaseState().withProperty(BlockHorizontalBase.FACING, EnumFacing.SOUTH)
				.withProperty(POWERED, Boolean.valueOf(false)));
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
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {

		return new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1F, 0.625F);

	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		return new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1F, 0.625F);
	}

	@Override
	public void neighborChanged(IBlockState blockstate, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		IBlockState state = world.getBlockState(pos.down());
		if (state.getBlock() == Blocks.AIR) {
			dropBlockAsItem(world, pos, world.getBlockState(pos), 1);
			world.setBlockToAir(pos);
		} else if (world.isBlockPowered(pos)) {
			NBTTagCompound nbt = world.getTileEntity(pos).writeToNBT(new NBTTagCompound());
			world.setBlockState(pos, blockstate.withProperty(POWERED, !blockstate.getValue(POWERED)));
			float f = ((Boolean) blockstate.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F;
			world.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
			world.getTileEntity(pos).readFromNBT(nbt);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItem(hand).getItem() instanceof ItemWire) {
			return false;
		}

		if (worldIn.isRemote) {
			return true;
		} else {
			NBTTagCompound nbt = worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound());
			state = state.cycleProperty(POWERED);
			worldIn.setBlockState(pos, state, 3);
			float f = ((Boolean) state.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F;
			worldIn.playSound((EntityPlayer) null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
			worldIn.notifyNeighborsOfStateChange(pos, this, false);
			EnumFacing enumfacing = state.getValue(BlockHorizontalBase.FACING);
			worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
			worldIn.getTileEntity(pos).readFromNBT(nbt);
			return true;
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING,
				placer.getHorizontalFacing().getOpposite());
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING, EnumFacing.getHorizontal(meta & 7))
				.withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(BlockHorizontalBase.FACING).getHorizontalIndex();

		if (((Boolean) state.getValue(POWERED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(BlockHorizontalBase.FACING,
				rot.rotate((EnumFacing) state.getValue(BlockHorizontalBase.FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(BlockHorizontalBase.FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontalBase.FACING, POWERED });
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityEnergySwitch();
	}

	@Override
	public TileEntityEnergySwitch createTileEntity(World world, IBlockState state) {
		return new TileEntityEnergySwitch();
	}

}
