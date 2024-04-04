package nuparu.sevendaystomine.item;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemMurkyWater extends ItemDrink {

	public ItemMurkyWater(int amount, int thirst, int stamina) {
		super(amount, thirst, stamina);
	}

	public ItemMurkyWater(int amount, float saturation, int thirst, int stamina) {
		super(amount, saturation, thirst, stamina);
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		if(worldIn.isRemote) return;
		if (worldIn.rand.nextInt(5) == 0) {
			player.addPotionEffect((new PotionEffect(Potions.dysentery,  worldIn.rand.nextInt(4000) + 18000, 4, false, false)));
		}

	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (raytraceresult != null) {
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = raytraceresult.getBlockPos();

				IBlockState state = worldIn.getBlockState(blockpos);
				if (state.getBlock() instanceof BlockCauldron) {
					int level = state.getValue(BlockCauldron.LEVEL);
					if (level < 3) {
						if (!worldIn.isRemote) {
							worldIn.setBlockState(blockpos, state.withProperty(BlockCauldron.LEVEL, level + 1));
						}
						worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ,
								SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
						return new ActionResult(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn,
								new ItemStack(nuparu.sevendaystomine.init.ModItems.EMPTY_JAR)));
					}
				}
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	protected ItemStack turnBottleIntoItem(ItemStack p_185061_1_, EntityPlayer player, ItemStack stack) {
		p_185061_1_.shrink(1);
		player.addStat(StatList.getObjectUseStats(this));

		if (p_185061_1_.isEmpty()) {
			return stack;
		} else {
			if (!player.inventory.addItemStackToInventory(stack)) {
				player.dropItem(stack, false);
			}

			return p_185061_1_;
		}
	}

}
