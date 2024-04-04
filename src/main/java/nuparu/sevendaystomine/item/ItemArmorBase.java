package nuparu.sevendaystomine.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

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
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;

public class ItemArmorBase extends ItemArmor implements IQuality {
	public static final UUID[] ARMOR_MODIFIERS = new UUID[] { UUID.fromString("ed70b160-0ade-11eb-adc1-0242ac120002"),
			UUID.fromString("ed70b3c2-0ade-11eb-adc1-0242ac120002"),
			UUID.fromString("ed70b598-0ade-11eb-adc1-0242ac120002"),
			UUID.fromString("ed70b67e-0ade-11eb-adc1-0242ac120002") };

	public ItemArmorBase(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn,
			String name) {
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
		if (!ModConfig.players.qualitySystem) {
			return super.getItemStackDisplayName(itemstack);
		}
		EnumQuality quality = getQualityTierFromStack(itemstack);
		return quality.color + SevenDaysToMine.proxy.localize(this.getUnlocalizedName() + ".name");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (!ModConfig.players.qualitySystem)
			return;
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
		if (!ModConfig.players.qualitySystem)
			return super.getRGBDurabilityForDisplay(stack);
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

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == this.armorType) {
			multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(
					ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double) this.damageReduceAmount, 0));
			multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(
					ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", (double) this.toughness, 0));
		}

		return multimap;
	}

	public double getDamageReduction(ItemStack stack) {
		return this.damageReduceAmount * (1 + ((float) getQuality(stack) / (float) ModConfig.players.maxQuality));
	}

	public double getToughness(ItemStack stack) {
		return this.toughness / (1 + ((float) getQuality(stack) / (float) ModConfig.players.maxQuality));
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

		if (slot == this.armorType) {
			if (!ModConfig.players.qualitySystem) {
				multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(
						ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", (double) this.damageReduceAmount, 0));
				multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(
						ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", (double) this.toughness, 0));

			} else {
				multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(
						ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", getDamageReduction(stack), 0));
				multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(
						ARMOR_MODIFIERS[slot.getIndex()], "Armor toughness", getToughness(stack), 0));
			}
		}

		return multimap;
	}

	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
		setQuality(itemstack,
				(int) Math.min(Math.max(Math.floor(player.experienceTotal / ModConfig.players.xpPerQuality), 1),
						ModConfig.players.maxQuality));
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
