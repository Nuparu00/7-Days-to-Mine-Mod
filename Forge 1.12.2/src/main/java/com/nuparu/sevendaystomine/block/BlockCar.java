package com.nuparu.sevendaystomine.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.item.ItemBattery;
import com.nuparu.sevendaystomine.item.ItemBlockCar;
import com.nuparu.sevendaystomine.item.ItemQuality;
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
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockCar extends BlockTileProvider<TileEntityCar> implements ISalvageable {

	public static final PropertyInteger PIECE = PropertyInteger.create("piece", 0, 16);
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	// Master block handles inventory
	public static final PropertyBool MASTER = PropertyBool.create("master");

	/*
	 * Format: Height/Width/Length 0 = Empty; Any other value = block Width should
	 * ideally be odd number
	 */
	public byte[][][] shape;
	public ResourceLocation lootTable = ModLootTables.SEDAN;
	public boolean special = false;

	public BlockCar(byte[][][] shape) {
		super(Material.IRON);
		this.shape = shape;
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MASTER, true));
	}

	/*
	 * For perfect cuboids
	 */
	public BlockCar(int width, int length, int height) {
		super(Material.ROCK);
		this.shape = new byte[height][width][length];
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MASTER, true));
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.AIR);
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
			generate(worldIn, pos, facing, false, masterTE);

		}
	}

	public void generate(World worldIn, BlockPos pos, EnumFacing facing, boolean placeMaster,
			TileEntityCarMaster masterTE) {
		if (placeMaster) {
			IBlockState state = getDefaultState().withProperty(FACING, facing).withProperty(MASTER, true);
			worldIn.setBlockState(pos, state);
			TileEntity TE = worldIn.getTileEntity(pos);
			if (!(TE instanceof TileEntityCarMaster)) {
				return;
			}
			masterTE = (TileEntityCarMaster) TE;
		}
		int index = 1;

		for (int height = 0; height < shape.length; height++) {
			byte[][] shape2d = shape[height];
			for (int length = 0; length < shape2d.length; length++) {
				byte[] shape1d = shape2d[length];
				for (int width = 0; width < shape1d.length; width++) {
					byte point = shape1d[width];
					if (point == 0)
						continue;
					BlockPos pos2 = pos.offset(facing.rotateY(), width - Math.round(shape2d.length / 2) + 1)
							.offset(facing, length - Math.round(shape1d.length / 2) - 1).up(height);
					if (pos2 == pos) {
						continue;
					}
					IBlockState state2 = getDefaultState().withProperty(FACING, facing).withProperty(MASTER, false);
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

	public boolean canBePlaced(World world, BlockPos pos, EnumFacing facing) {
		for (int height = 0; height < shape.length; height++) {
			byte[][] shape2d = shape[height];
			for (int length = 0; length < shape2d.length; length++) {
				byte[] shape1d = shape2d[length];
				for (int width = 0; width < shape1d.length; width++) {
					byte point = shape1d[width];
					if (point == 0)
						continue;
					BlockPos pos2 = pos.offset(facing.rotateY(), width - Math.round(shape2d.length / 2) + 1)
							.offset(facing, length - Math.round(shape1d.length / 2) - 1).up(height);
					IBlockState state = world.getBlockState(pos2);
					Block block2 = state.getBlock();
					if (!block2.isReplaceable(world, pos2)) {
						return false;
					}
				}
			}
		}
		return true;
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
					if (Utils.compareBlockPos(slave2.masterPos, slave.masterPos)) {
						worldIn.destroyBlock(pos2, false);
					}
				} else if (TE instanceof TileEntityCarMaster) {
					TileEntityCarMaster master = (TileEntityCarMaster) TE;
					if (Utils.compareBlockPos(master.getPos(), slave.masterPos)) {
						worldIn.destroyBlock(pos2, false);
					}
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public List<ItemStack> getItems(World world, BlockPos pos, IBlockState oldState, EntityPlayer player) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		if (world.rand.nextDouble() < 0.05) {
			ItemStack engine = new ItemStack(ModItems.SMALL_ENGINE);
			ItemQuality.setQualityForPlayer(engine, player);
			items.add(engine);
		}
		if (world.rand.nextDouble() < 0.05) {
			ItemStack battery = new ItemStack(ModItems.CAR_BATTERY);
			ItemQuality.setQualityForPlayer(battery, player);
			if (battery.getTagCompound() == null) {
				battery.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound nbt = battery.getTagCompound();
			nbt.setInteger("voltage", MathHelper.getInt(world.rand, ItemBattery.MAX_VOLTAGE/2, ItemBattery.MAX_VOLTAGE));
			items.add(battery);
		}
		items.add(new ItemStack(ModItems.IRON_PIPE,1+world.rand.nextInt(5)));
		items.add(new ItemStack(ModItems.IRON_SCRAP,1+world.rand.nextInt(5)));
		
		return items;
	}

	@Override
	public SoundEvent getSound() {
		return SoundEvents.BLOCK_ANVIL_LAND;
	}

	@Override
	public void onSalvage(World world, BlockPos pos, IBlockState oldState) {
		world.destroyBlock(pos, false);
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, MASTER, PIECE });
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

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasCustomStateMapper() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IStateMapper getStateMapper() {
		return new StateMap.Builder().ignore(FACING).ignore(PIECE).ignore(MASTER).build();
	}

}
