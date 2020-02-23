package com.nuparu.sevendaystomine.item;

import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import net.minecraft.world.World;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.block.Block;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.nuparu.sevendaystomine.SevenDaysToMine;

import java.util.List;

@SuppressWarnings("deprecation")
public class ItemQualityTool extends ItemTool implements IQuality {

	public static final double DEFAULT_SPEED = -2.4000000953674316D;
	
	public EnumLength length = EnumLength.LONG;
	public double speed;

	public ItemQualityTool(float attackDamageIn, float attackSpeedIn, Item.ToolMaterial materialIn,
			Set<Block> effectiveBlocksIn) {
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
		this.speed = attackSpeedIn;
	}

	public ItemQualityTool(Item.ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
		super(materialIn, effectiveBlocksIn);
	}

	public ItemQualityTool setAttackSpeed(double speed) {
		attackSpeed = (float)speed;
		this.speed = speed;
		return this;
	}

	public ItemQualityTool setAttackDamage(float damage) {
		attackDamage = damage;
		return this;
	}

	public ItemQualityTool setLength(EnumLength length) {
		this.length = length;
		return this;
	}

	public int getQuality(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			if (nbt.hasKey("Quality")) {
				return nbt.getInteger("Quality");
			}
		}
		return 0;
	}

	public EnumQuality getQualityTierFromStack(ItemStack stack) {
		return getQualityTierFromInt(getQuality(stack));
	}

	public EnumQuality getQualityTierFromInt(int quality) {
		return EnumQuality.getFromQuality(quality);
	}

	public void setQuality(ItemStack stack, int quality) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("Quality", quality);
	}

	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
		setQuality(itemstack,
				(int) Math.min(Math.max(Math.floor(player.getScore() / ItemQuality.XP_PER_QUALITY_POINT), 1),
						ItemQuality.MAX_QUALITY));
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		int i = 0;
		if (stack.getTagCompound() != null) {
			i = getQuality(stack);
		}
		return super.getMaxDamage(stack) + i;
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		EnumQuality quality = getQualityTierFromStack(itemstack);
		return quality.color + SevenDaysToMine.proxy.localize(this.getUnlocalizedName() + ".name");
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		int quality = getQuality(stack);
		EnumQuality tier = getQualityTierFromInt(quality);
		tooltip.add(tier.color + SevenDaysToMine.proxy.localize("stat.quality." + tier.name().toLowerCase() + ".name"));
		tooltip.add(tier.color + SevenDaysToMine.proxy.localize("stat.quality.name") + quality);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = new ItemStack(this, 1, 0);
			if (player != null) {
				ItemQuality.setQualityForPlayer(stack, player);
			}
			items.add(stack);
		}
	}
	

}