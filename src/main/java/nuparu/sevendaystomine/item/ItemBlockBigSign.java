package nuparu.sevendaystomine.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityBigSignMaster;

public class ItemBlockBigSign extends ItemBlock {

	public ItemBlockBigSign(Block block) {
		super(block);
	}

	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		boolean flag = iblockstate.getBlock().isReplaceable(worldIn, pos);
		if (facing != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag)
				&& (!flag || facing == EnumFacing.UP)) {
			pos = pos.offset(facing);
			ItemStack itemstack = player.getHeldItem(hand);

			if (player.canPlayerEdit(pos, facing, itemstack)
					&& ModBlocks.BIG_SIGN_MASTER.canPlaceBlockAt(worldIn, pos)) {
				if (worldIn.isRemote) {
					return EnumActionResult.SUCCESS;
				} else {
					pos = flag ? pos.down() : pos;

					if (facing == EnumFacing.UP) {
						return EnumActionResult.FAIL;
					}

					for (int i = -3; i <= 3; i++) {
						for (int j = 0; j < 3; j++) {
							BlockPos pos2 = pos.offset(facing.rotateY(), i).up(j);
							IBlockState state = worldIn.getBlockState(pos2);
							Block block2 = state.getBlock();
							if (!block2.isReplaceable(worldIn, pos2)) {
								return EnumActionResult.FAIL;
							}
						}
					}

					worldIn.setBlockState(pos,
							ModBlocks.BIG_SIGN_MASTER.getDefaultState().withProperty(BlockWallSign.FACING, facing), 11);
					TileEntity tileentity = worldIn.getTileEntity(pos);

					if (tileentity instanceof TileEntityBigSignMaster
							&& !ItemBlock.setTileEntityNBT(worldIn, player, pos, itemstack)) {
						player.openEditSign((TileEntityBigSignMaster) tileentity);
					}

					if (player instanceof EntityPlayerMP) {
						CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, itemstack);
					}

					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		} else {
			return EnumActionResult.FAIL;
		}
	}
}
