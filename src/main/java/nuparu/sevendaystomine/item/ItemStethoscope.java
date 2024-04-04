package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

public class ItemStethoscope extends Item {
	
	public ItemStethoscope() {
		this.setMaxDamage(32);
		
	}

	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		
		if (!world.isRemote && player.isSneaking() && hand == EnumHand.MAIN_HAND) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity == null || !(tileEntity instanceof TileEntityCodeSafe)) {
				return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
			}
			TileEntityCodeSafe te = (TileEntityCodeSafe) tileEntity;

			int numPos = world.rand.nextInt(3);
			int guess = world.rand.nextInt(10);

			int test = te.testDigit(player, guess, numPos);

			player.sendMessage(new TextComponentTranslation(
					test == -1 ? "safe.code.less" : (test == 1 ? "safe.code.greater" : "safe.code.equal"), numPos + 1,
					guess));
			itemstack.damageItem(1, player);
			return EnumActionResult.SUCCESS;

		}
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
}
