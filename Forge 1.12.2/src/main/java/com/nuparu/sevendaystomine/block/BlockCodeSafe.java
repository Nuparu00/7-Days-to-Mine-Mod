package com.nuparu.sevendaystomine.block;

import java.util.Random;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;
import com.nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCodeSafe extends BlockSafe<TileEntityCodeSafe> {

	public static final PropertyBool LOCKED = PropertyBool.create("locked");
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	private static boolean keepInventory;

	public BlockCodeSafe() {
		super();
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LOCKED,true));

	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCodeSafe();
	}

	@Override
	public TileEntityCodeSafe createTileEntity(World world, IBlockState state) {
		return new TileEntityCodeSafe();
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityCodeSafe) {
				String displayName = stack.getDisplayName();
				TileEntityCodeSafe safe = (TileEntityCodeSafe)tileentity;
				if(isNumeric(displayName)) {
					double d = Double.parseDouble(displayName);
					if((d % 1) == 0 && d >= 0 && d < 1000) {
						safe.setCorrectCode((int)d);
						int selectedCode = 0;
						while ((int)d == selectedCode) {
							Random rand = worldIn.rand;
							selectedCode = rand.nextInt(1000);
						}
						safe.setSelectedCode(selectedCode);
						safe.setInit(true);
						return;
					}
				}
				safe.setCustomInventoryName(displayName);
			}
		}
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityCodeSafe) {
			TileEntityCodeSafe safe = (TileEntityCodeSafe) te;

			if (!worldIn.isRemote) {
				PacketManager.syncTileEntity.sendTo(new SyncTileEntityMessage(te.writeToNBT(new NBTTagCompound()), pos),
						(EntityPlayerMP) playerIn);
				System.out.println(safe.superSecretMethod());
			}
			if (!safe.locked) {
				if (!playerIn.isSneaking()) {
					if (!worldIn.isRemote) {
						playerIn.openGui(SevenDaysToMine.instance, 2, worldIn, pos.getX(), pos.getY(), pos.getZ());
					}
				}
				else {
					playerIn.openGui(SevenDaysToMine.instance, 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
				}
			} else {
				playerIn.openGui(SevenDaysToMine.instance, 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
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
				ModBlocks.CODE_SAFE.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(LOCKED, locked), 3);
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

			if (tileentity instanceof TileEntityCodeSafe) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityCodeSafe) tileentity);
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