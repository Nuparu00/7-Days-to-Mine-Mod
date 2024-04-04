package nuparu.sevendaystomine.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockComputer;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.tileentity.TileEntityComputer.EnumSystem;

public class ItemInstallDisc extends Item {

	public final EnumSystem system;

	public ItemInstallDisc(EnumSystem system) {
		this.system = system;
		setCreativeTab(SevenDaysToMine.TAB_ELECTRICITY);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			IBlockState state = worldIn.getBlockState(pos);
			if (state.getBlock() instanceof BlockComputer) {
				if (playerIn.isSneaking()) {
					ItemStack stack = playerIn.getHeldItem(hand);
					if (!stack.isEmpty()) {
						Item item = stack.getItem();
						if (item instanceof ItemInstallDisc) {
							TileEntity TE = worldIn.getTileEntity(pos);
							if (TE != null && TE instanceof TileEntityComputer) {
								TileEntityComputer computerTE = (TileEntityComputer) TE;
								if (computerTE.getSystem() == EnumSystem.NONE && computerTE.isCompleted()) {
									EnumSystem system = ((ItemInstallDisc) item).system;
									computerTE.installSystem(system);
									TextComponentTranslation text = new TextComponentTranslation("computer.install", system.getReadeable());
									text.getStyle().setColor(TextFormatting.GREEN);
									playerIn.sendMessage(
											text);
									return EnumActionResult.SUCCESS;
								}
							}
						}
					}
				}
			}
		}

		return EnumActionResult.FAIL;
	}
}
