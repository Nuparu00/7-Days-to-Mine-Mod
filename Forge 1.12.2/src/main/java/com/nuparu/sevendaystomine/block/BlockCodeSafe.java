package com.nuparu.sevendaystomine.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;
import com.nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;
import com.nuparu.sevendaystomine.tileentity.TileEntityKeySafe;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCodeSafe extends BlockSafe<TileEntityCodeSafe> {

	public static final PropertyBool LOCKED = PropertyBool.create("locked");
	public static final PropertyDirection FACING = BlockDirectional.FACING;

	private static boolean keepInventory;

	public BlockCodeSafe() {
		super();
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LOCKED, true));
		this.setCreativeTab(SevenDaysToMine.TAB_BUILDING);

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
		worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);

		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileEntityCodeSafe) {

			TileEntityCodeSafe safe = (TileEntityCodeSafe) tileentity;
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null && nbt.hasKey("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
				NBTTagCompound tileNBT = nbt.getCompoundTag("BlockEntityTag");
				tileNBT.setInteger("x", pos.getX());
				tileNBT.setInteger("y", pos.getY());
				tileNBT.setInteger("z", pos.getZ());
				safe.readFromNBT(tileNBT);
			}
			if (stack.hasDisplayName()) {
				String displayName = stack.getDisplayName();
				if (isNumeric(displayName)) {
					double d = Double.parseDouble(displayName);
					if ((d % 1) == 0 && d >= 0 && d < 1000) {
						safe.setCorrectCode((int) d);
						int selectedCode = 0;
						while ((int) d == selectedCode) {
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

	public static boolean isNumeric(String str) {
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
			}
			if (!safe.locked && !playerIn.isSneaking()) {
				playerIn.openGui(SevenDaysToMine.instance, 2, worldIn, pos.getX(), pos.getY(), pos.getZ());
			} else {
				SevenDaysToMine.proxy.openClientSideGui(0, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(LOCKED,
				!Boolean.valueOf((meta & 8) > 0));
	}

	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | ((EnumFacing) state.getValue(FACING)).getIndex();

		if (((Boolean) state.getValue(LOCKED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	public static void setState(boolean locked, World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		IBlockState iblockstate = worldIn.getBlockState(pos);
		keepInventory = true;

		worldIn.setBlockState(pos, ModBlocks.CODE_SAFE.getDefaultState()
				.withProperty(FACING, iblockstate.getValue(FACING)).withProperty(LOCKED, locked), 3);
		worldIn.notifyBlockUpdate(pos, iblockstate, worldIn.getBlockState(pos), 2);
		keepInventory = false;

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (!worldIn.isRemote) {
			if (tileentity instanceof TileEntityCodeSafe) {
				{
					TileEntityCodeSafe codeSafe = (TileEntityCodeSafe) tileentity;
					ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
					if (codeSafe.locked) {
						NBTTagCompound nbttagcompound = new NBTTagCompound();
						NBTTagCompound nbttagcompound1 = new NBTTagCompound();
						nbttagcompound.setTag("BlockEntityTag", codeSafe.writeToNBT(nbttagcompound1));
						itemstack.setTagCompound(nbttagcompound);
					} else if (!keepInventory) {
						NonNullList<ItemStack> drops = codeSafe.getDrops();
						for (ItemStack stack : drops) {
							worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack));
						}
					}
					spawnAsEntity(worldIn, pos, itemstack);

				}

				worldIn.updateComparatorOutputLevel(pos, state.getBlock());
			}
		}

	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag", 10)) {
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");

			if (nbttagcompound1.hasKey("LootTable", 8)) {
				tooltip.add("???????");
			}

			if (nbttagcompound1.hasKey("Items", 9)) {
				NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
				int i = 0;
				int j = 0;

				for (ItemStack itemstack : nonnulllist) {
					if (!itemstack.isEmpty()) {
						++j;

						if (i <= 4) {
							++i;
							tooltip.add(String.format("%s x%d", itemstack.getDisplayName(), itemstack.getCount()));
						}
					}
				}

				if (j - i > 0) {
					tooltip.add(String
							.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), j - i));
				}
			}
		}
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