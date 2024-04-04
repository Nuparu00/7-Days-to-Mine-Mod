package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;
import nuparu.sevendaystomine.tileentity.TileEntityKeySafe;

public class BlockKeySafe extends BlockSafe<TileEntityKeySafe> {

	public static final PropertyBool LOCKED = PropertyBool.create("locked");
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	private static boolean keepInventory;
	
	public BlockKeySafe() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LOCKED,true));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityKeySafe();
	}

	@Override
	public TileEntityKeySafe createTileEntity(World world, IBlockState state) {
		return new TileEntityKeySafe();
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityKeySafe) {
				String displayName = stack.getDisplayName();
				TileEntityKeySafe safe = (TileEntityKeySafe)tileentity;
				
				safe.setCustomInventoryName(displayName);
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityKeySafe) {
			TileEntityKeySafe safe = (TileEntityKeySafe) te;
			if (!worldIn.isRemote) {
				PacketManager.syncTileEntity.sendTo(new SyncTileEntityMessage(te.writeToNBT(new NBTTagCompound()), pos),
						(EntityPlayerMP) playerIn);
			}
			if (!safe.locked && !playerIn.isSneaking()) {
				playerIn.openGui(SevenDaysToMine.instance, 2, worldIn, pos.getX(), pos.getY(), pos.getZ());
			} else {
				SevenDaysToMine.proxy.openClientSideGui(1, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	public IBlockState getStateFromMeta(int meta) {
		boolean lock = true;
		if(meta >= 4) {
			lock = false;
			meta-=4;
		}
		
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(LOCKED, lock);
	}

	public int getMetaFromState(IBlockState state) {
		boolean lock = state.getValue(LOCKED);
		int facing = ((EnumFacing) state.getValue(FACING)).getIndex();
		
		int meta = facing;
		if(!lock) {
			meta+=4;
		}
		return meta;
	}
	
	public static void setState(boolean locked, World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		IBlockState iblockstate = worldIn.getBlockState(pos);
		keepInventory = true;

		worldIn.setBlockState(pos,
				ModBlocks.KEY_SAFE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(LOCKED, locked), 3);
		worldIn.notifyBlockUpdate(pos, iblockstate, worldIn.getBlockState(pos), 2);
		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!keepInventory) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityKeySafe) {
				NonNullList<ItemStack> drops = ((TileEntityKeySafe) tileentity).getDrops();
				for (ItemStack stack : drops) {
					worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
				}
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
	}

	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, LOCKED });
	}



}
