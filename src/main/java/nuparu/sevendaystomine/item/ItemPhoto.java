package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityPhoto;

public class ItemPhoto extends Item {

	public ItemPhoto() {
		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("path", Constants.NBT.TAG_STRING)) {
			tooltip.add(TextFormatting.ITALIC + nbt.getString("path"));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (!playerIn.isSneaking()) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt != null && nbt.hasKey("path", Constants.NBT.TAG_STRING)) {
				SevenDaysToMine.proxy.openClientOnlyGui(1, stack);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (facing == EnumFacing.UP) {
			return EnumActionResult.PASS;
		} else {
			ItemStack stack = player.getHeldItem(hand);

			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (!block.isReplaceable(worldIn, pos)) {
				pos = pos.offset(facing);
			}
			if (iblockstate.getBlockFaceShape(worldIn, pos, facing.getOpposite()) == BlockFaceShape.UNDEFINED) {
				return EnumActionResult.PASS;
			}
			if (!player.canPlayerEdit(pos, facing, stack)) {
				return EnumActionResult.PASS;
			} else if (!ModBlocks.PHOTO.canPlaceBlockAt(worldIn, pos)) {
				return EnumActionResult.PASS;
			} else {
				worldIn.setBlockState(pos, ModBlocks.PHOTO.getDefaultState().withProperty(BlockHorizontalBase.FACING,
						facing));
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if (tileEntity != null && tileEntity instanceof TileEntityPhoto) {
					((TileEntityPhoto) tileEntity).setPath(stack.getTagCompound().getString("path"));
				}
				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}

		}
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

}
