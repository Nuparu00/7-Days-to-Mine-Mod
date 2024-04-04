package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.electricity.IVoltage;

public class ItemVoltmeter extends Item {

	public ItemVoltmeter() {
		setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos blockPos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.world.isRemote) {
			TileEntity te = worldIn.getTileEntity(blockPos);
			if (te != null && te instanceof IVoltage) {
				player.sendMessage(
						new TextComponentTranslation("voltmeter.message", ((IVoltage) te).getVoltageStored()+""));
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.PASS;
	}
}
