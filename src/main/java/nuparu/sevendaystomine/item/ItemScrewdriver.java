package nuparu.sevendaystomine.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockDoorLocked;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.util.MathUtils;

public class ItemScrewdriver extends ItemQuality {

	public ItemScrewdriver() {
		this.setMaxDamage(8);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (player.isSneaking()) {
			TileEntity TE = worldIn.getTileEntity(pos);
			if (TE != null) {
				if (TE instanceof TileEntityComputer) {
					itemstack.damageItem(1, player);
					player.openGui(SevenDaysToMine.instance, 7, worldIn, pos.getX(), pos.getY(), pos.getZ());
				}
			} else {
				IBlockState state = worldIn.getBlockState(pos);
				if (!worldIn.isRemote && state.getBlock() instanceof BlockDoorLocked) {
					if (worldIn.rand.nextInt(16) == 0) {
						((BlockDoorLocked) state.getBlock()).unlock(worldIn, pos, state);
						worldIn.playSound(null,pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS,
								0.75f + MathUtils.getFloatInRange(0, 0.25f), 0.8f + MathUtils.getFloatInRange(0, 0.2f));
					}else {
						worldIn.playSound(null,pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS,
								0.75f + MathUtils.getFloatInRange(0, 0.25f), 0.8f + MathUtils.getFloatInRange(0, 0.2f));
					}
					itemstack.damageItem(1, player);
				}
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		int i = 0;
		if (stack.getTagCompound() != null) {
			i = getQuality(stack);
		}
		return super.getMaxDamage(stack) + i;
	}
}
