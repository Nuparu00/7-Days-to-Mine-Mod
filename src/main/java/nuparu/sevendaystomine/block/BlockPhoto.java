package nuparu.sevendaystomine.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.tileentity.TileEntityPhoto;

public class BlockPhoto extends BlockTileProvider<TileEntityPhoto> {

	public BlockPhoto() {
		super(Material.CIRCUITS);
		this.setSoundType(SoundType.CLOTH);
		this.setHardness(0.001f);
		this.setResistance(0.1f);
	}

	@Override
	public ItemBlock createItemBlock() {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPhoto();
	}

	@Override
	public TileEntityPhoto createTileEntity(World world, IBlockState state) {
		return new TileEntityPhoto();
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityPhoto) {
			if (!playerIn.isSneaking()) {
				SevenDaysToMine.proxy.openClientOnlyGui(0, te);
			}

		}

		return true;
	}

	@Override
	 public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        return 0;
    }
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityPhoto) {
			TileEntityPhoto t = (TileEntityPhoto) te;
			if (t.getPath() != null) {
				ItemStack stack = new ItemStack(ModItems.PHOTO);
				if (stack.getTagCompound() == null) {
					stack.setTagCompound(new NBTTagCompound());
				}
				stack.getTagCompound().setString("path", t.getPath());
				InventoryHelper.spawnItemStack(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, stack);
			}
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityPhoto) {
			TileEntityPhoto t = (TileEntityPhoto) te;
			if (t.getPath() != null) {
				ItemStack stack = new ItemStack(ModItems.PHOTO);
				if (stack.getTagCompound() == null) {
					stack.setTagCompound(new NBTTagCompound());
				}
				stack.getTagCompound().setString("path", t.getPath());
				return stack;

			}
		}
		return null;
	}

	@Override
	public void neighborChanged(IBlockState blockstate, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		IBlockState state = world
				.getBlockState(pos.offset(world.getBlockState(pos).getValue(BlockHorizontalBase.FACING).getOpposite()));
		if (state.getBlock() == Blocks.AIR) {
			dropBlockAsItem(world, pos, world.getBlockState(pos), 1);
			world.setBlockToAir(pos);
		}
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		switch ((EnumFacing) blockState.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.0F, 0.0F, 0.9625F, 1.0F, 1F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.9625F, 1.0F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1F, 0.0375F);
		case WEST:
			return new AxisAlignedBB(0.9625F, 0.0F, 0F, 1F, 1F, 1F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.0375F, 1F, 1F);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)) {
		case UP:
		default:
			return new AxisAlignedBB(0.0F, 0.0F, 0.9625F, 1.0F, 1F, 1F);
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.9625F, 1.0F, 1F, 1F);
		case SOUTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1F, 0.0375F);
		case WEST:
			return new AxisAlignedBB(0.9625F, 0.0F, 0F, 1F, 1F, 1F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.0375F, 1F, 1F);
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
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(BlockHorizontalBase.FACING,
				rot.rotate((EnumFacing) state.getValue(BlockHorizontalBase.FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(BlockHorizontalBase.FACING)));
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to
	 * allow for adjustments to the IBlockstate
	 */
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING,facing);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING, EnumFacing.getHorizontal(meta));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing) state.getValue(BlockHorizontalBase.FACING)).getHorizontalIndex();
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BlockHorizontalBase.FACING });
	}

}
