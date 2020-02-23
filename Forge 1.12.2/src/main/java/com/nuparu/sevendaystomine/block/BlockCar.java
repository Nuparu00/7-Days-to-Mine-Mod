package com.nuparu.sevendaystomine.block;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.item.ItemBlockCar;
import com.nuparu.sevendaystomine.tileentity.TileEntityCar;
import com.nuparu.sevendaystomine.tileentity.TileEntityCarMaster;
import com.nuparu.sevendaystomine.tileentity.TileEntityCarSlave;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockCar extends BlockTileProvider<TileEntityCar> {

	public static final PropertyInteger PIECE = PropertyInteger.create("piece", 0, 16);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool MASTER = PropertyBool.create("master");

	private int widthHalf;
	private int lengthHalf;
	private int height;

	public BlockCar(int widthHalf, int lengthHalf, int height) {
		super(Material.ROCK);
		this.setWidthHalf(widthHalf);
		this.setLengthHalf(lengthHalf);
		this.setHeight(height);

		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MASTER, true));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		if (meta <= 3) {
			return new TileEntityCarMaster();
		}
		return new TileEntityCarSlave();
	}

	@Override
	public TileEntityCar createTileEntity(World world, IBlockState state) {
		if (state.getValue(MASTER)) {
			return new TileEntityCarMaster();
		}
		return new TileEntityCarSlave();
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

	public IBlockState getStateFromMeta(int meta) {
		boolean master = meta <= 3;
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(master ? meta : meta - 4))
				.withProperty(MASTER, master);
	}

	public int getMetaFromState(IBlockState state) {
		int meta = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();

		if (!state.getValue(MASTER)) {
			meta += 4;
		}

		return meta;
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		int piece = 0;
		if (!state.getValue(MASTER)) {

		}

		return state.withProperty(PIECE, piece);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		TileEntity TE = worldIn.getTileEntity(pos);
		if (!(TE instanceof TileEntityCarMaster)) {
			return;
		}
		TileEntityCarMaster masterTE = (TileEntityCarMaster) TE;

		if (stack.hasDisplayName()) {
			masterTE.setCustomInventoryName(stack.getDisplayName());
		}
		if (state.getValue(MASTER)) {
			EnumFacing facing = state.getValue(FACING);

			int index = 1;
			for (int width = -getWidthHalf(); width <= getWidthHalf(); width++) {
				for (int length = -getLengthHalf(); length <= getLengthHalf(); length++) {
					for (int height2 = 0; height2 < getHeight(); height2++) {
						if (width == 0 && length == 0 && height2 == 0) {
							continue;
						}
						BlockPos pos2 = pos.offset(facing.rotateY(), width).offset(facing, length).up(height2);
						IBlockState state2 = getState().withProperty(FACING, facing).withProperty(MASTER, false);
						worldIn.setBlockState(pos2, state2);
						TileEntity TE2 = worldIn.getTileEntity(pos2);
						if (TE2 instanceof TileEntityCarSlave) {
							TileEntityCarSlave slave = (TileEntityCarSlave) TE2;
							slave.setMaster(pos, masterTE);
							slave.setIndex(index);
						}
						index++;
					}
				}
			}

		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking())
			return true;

		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null) {
			if (te instanceof TileEntityCarMaster) {
				playerIn.openGui(SevenDaysToMine.instance, 6, worldIn, te.getPos().getX(), te.getPos().getY(),
						te.getPos().getZ());
			} else if (te instanceof TileEntityCarSlave) {
				TileEntityCarSlave slave = (TileEntityCarSlave) te;
				TileEntityCarMaster master = slave.getMaster();
				if (master != null) {
					playerIn.openGui(SevenDaysToMine.instance, 6, worldIn, master.getPos().getX(),
							master.getPos().getY(), master.getPos().getZ());
				}
			}
		}

		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TileEntityCarMaster) {
			TileEntityCarMaster master = (TileEntityCarMaster) tileentity;
			for (EnumFacing facing : EnumFacing.VALUES) {
				BlockPos pos2 = pos.offset(facing);
				if (pos.getY() < 0 || pos.getY() > 255) {
					continue;
				}
				TileEntity TE = worldIn.getTileEntity(pos2);
				if (TE instanceof TileEntityCarSlave) {
					TileEntityCarSlave slave2 = (TileEntityCarSlave) TE;
					if (slave2.masterPos == master.getPos()) {
						worldIn.destroyBlock(pos2, false);
					}
				}
			}
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityCarMaster) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		else if (tileentity instanceof TileEntityCarSlave) {
			TileEntityCarSlave slave = (TileEntityCarSlave) tileentity;
			for (EnumFacing facing : EnumFacing.VALUES) {
				BlockPos pos2 = pos.offset(facing);
				if (pos.getY() < 0 || pos.getY() > 255) {
					continue;
				}
				TileEntity TE = worldIn.getTileEntity(pos2);
				if (TE instanceof TileEntityCarSlave) {
					TileEntityCarSlave slave2 = (TileEntityCarSlave) TE;
					if (Utils.compareBlockPos(slave2.masterPos,slave.masterPos)) {
						worldIn.destroyBlock(pos2, false);
					}
				} else if (TE instanceof TileEntityCarMaster) {
					TileEntityCarMaster master = (TileEntityCarMaster) TE;
					if (Utils.compareBlockPos(master.getPos(),slave.masterPos)) {
						worldIn.destroyBlock(pos2, false);
					}
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, MASTER, PIECE });
	}

	public abstract IBlockState getState();

	public int getWidthHalf() {
		return widthHalf;
	}

	public void setWidthHalf(int widthHalf) {
		this.widthHalf = widthHalf;
	}

	public int getLengthHalf() {
		return lengthHalf;
	}

	public void setLengthHalf(int lengthHalf) {
		this.lengthHalf = lengthHalf;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ItemBlock createItemBlock() {
		return new ItemBlockCar(this);
	}

	public static ItemBlock createItemBlock(Block block) {
		if (block instanceof BlockCar) {
			return new ItemBlockCar((BlockCar) block);
		}
		return null;
	}

}
