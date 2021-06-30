package nuparu.sevendaystomine.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.tileentity.TileEntityToilet;

public class BlockToilet extends BlockTileProvider<TileEntityToilet> implements ISalvageable {

	public BlockToilet() {
		super(Material.IRON);
		this.setResistance(2f);
		this.setHardness(3.5f);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityToilet();
	}

	@Override
	public TileEntityToilet createTileEntity(World world, IBlockState state) {
		return new TileEntityToilet();
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
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityToilet) {
			playerIn.openGui(SevenDaysToMine.instance, 5, worldIn, pos.getX(), pos.getY(), pos.getZ());

		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityToilet) {
				((TileEntityToilet) tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityToilet) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityToilet) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : RANDOM;
		if (rand.nextInt(4) == 0) {
			drops.add(new ItemStack(ModItems.IRON_PIPE, rand.nextInt(1) * (fortune + 1)));
		} else {
			drops.add(new ItemStack(ModItems.IRON_SCRAP, rand.nextInt(2) * (fortune + 1)));
		}
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
		return this.getDefaultState().withProperty(BlockHorizontalBase.FACING,
				placer.getHorizontalFacing().getOpposite());
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

	@Override
	public List<ItemStack> getItems(World world, BlockPos pos, IBlockState oldState, EntityPlayer player) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		if (world.rand.nextInt(2) == 0) {
			items.add(new ItemStack(ModItems.IRON_PIPE, 1 + world.rand.nextInt(2)));
		}
		items.add(new ItemStack(ModItems.IRON_SCRAP, 1 + world.rand.nextInt(2)));

		return items;
	}

	@Override
	public SoundEvent getSound() {
		return SoundEvents.BLOCK_ANVIL_LAND;
	}
	
	@Override
	public float getUpgradeRate(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return 5;
	}

	@Override
	public void onSalvage(World world, BlockPos pos, IBlockState oldState) {
		world.destroyBlock(pos, false);
	}

}
