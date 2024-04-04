package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemGuide extends Item {
	
	public ResourceLocation data;
	public ItemGuide(ResourceLocation data) {
		this.data = data;
		setMaxStackSize(1);
		setCreativeTab(SevenDaysToMine.TAB_BOOKS);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.openGui(SevenDaysToMine.instance, 22, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	public net.minecraftforge.common.IRarity getForgeRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(SevenDaysToMine.proxy.localize(this.getRegistryName().getResourcePath() + ".title"));
		tooltip.add(
				TextFormatting.ITALIC + SevenDaysToMine.proxy.localize(this.getRegistryName().getResourcePath() + ".desc"));
	}
}
