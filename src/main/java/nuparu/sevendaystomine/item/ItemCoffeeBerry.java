package nuparu.sevendaystomine.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModBlocks;

public class ItemCoffeeBerry extends ItemFood implements net.minecraftforge.common.IPlantable {
	public ItemCoffeeBerry() {
		super(1, 0.3f, false);
		this.setCreativeTab(CreativeTabs.FOOD);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);
		if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack)
				&& state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this)
				&& worldIn.isAirBlock(pos.up())) {
			worldIn.setBlockState(pos.up(), ModBlocks.COFFEE_PLANT.getDefaultState());

			if (player instanceof EntityPlayerMP) {
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos.up(), itemstack);
			}

			itemstack.shrink(1);
			return EnumActionResult.SUCCESS;
		} else {
			return EnumActionResult.FAIL;
		}
	}

	@Override
	public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return net.minecraftforge.common.EnumPlantType.Crop;
	}

	@Override
	public net.minecraft.block.state.IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos) {
		return ModBlocks.COFFEE_PLANT.getDefaultState();
	}
}
