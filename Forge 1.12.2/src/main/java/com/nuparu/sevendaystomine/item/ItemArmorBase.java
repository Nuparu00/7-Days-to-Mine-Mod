package com.nuparu.sevendaystomine.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.nuparu.sevendaystomine.SevenDaysToMine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorBase extends ItemArmor implements IQuality{
	public static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
	
	public ItemArmorBase(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String name) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
	}

	@Override
	public int getQuality(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			if (nbt.hasKey("Quality")) {
				return nbt.getInteger("Quality");
			}
		}
		return 0;
	}

	@Override
	public EnumQuality getQualityTierFromStack(ItemStack stack) {
		return getQualityTierFromInt(getQuality(stack));
	}

	@Override
	public EnumQuality getQualityTierFromInt(int quality) {
		return EnumQuality.getFromQuality(quality);
	}

	@Override
	public void setQuality(ItemStack stack, int quality) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("Quality", quality);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack itemstack) {
		EnumQuality quality = getQualityTierFromStack(itemstack);
		return quality.color + SevenDaysToMine.proxy.localize(this.getUnlocalizedName() + ".name");
	}

	@Override
	@SideOnly(Side.CLIENT)
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
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		switch (getQualityTierFromStack(stack)) {
		case FLAWLESS:
			return 0xA300A3;
		case GREAT:
			return 0x4545CC;
		case FINE:
			return 0x37A337;
		case GOOD:
			return 0xB2B23C;
		case POOR:
			return 0xF09900;
		case FAULTY:
			return 0x89713C;
		case NONE:
		default:
			return super.getRGBDurabilityForDisplay(stack);
		}
	}
	
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == this.armorType)
        {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)this.damageReduceAmount, 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double)this.toughness, 0));
        }

		return multimap;
	}

	public double getDamageReduction(ItemStack stack) {
		return this.damageReduceAmount * (1+((float)getQuality(stack) / (float)ItemQuality.MAX_QUALITY));
	}
	
	public double getToughness(ItemStack stack) {
		return this.toughness / (1+((float)getQuality(stack) / (float)ItemQuality.MAX_QUALITY));
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
		if (slot == this.armorType)
        {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", getDamageReduction(stack), 0));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", getToughness(stack), 0));
        }

		return multimap;
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

}
