package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.MathUtils;

public class ItemQuality extends Item implements IQuality {

	public ItemQuality() {

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
		if (!ModConfig.players.qualitySystem) return super.getItemStackDisplayName(itemstack);
		EnumQuality quality = getQualityTierFromStack(itemstack);
		return quality.color + SevenDaysToMine.proxy.localize(this.getUnlocalizedName() + ".name");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (ModConfig.players.qualitySystem) {
			int quality = getQuality(stack);
			EnumQuality tier = getQualityTierFromInt(quality);
			tooltip.add(
					tier.color + SevenDaysToMine.proxy.localize("stat.quality." + tier.name().toLowerCase() + ".name"));
			tooltip.add(tier.color + SevenDaysToMine.proxy.localize("stat.quality.name") + quality);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = new ItemStack(this, 1, 0);
			if (player != null) {
				setQualityForPlayer(stack, player);
			}
			items.add(stack);
		}
	}

	public static ItemStack setQualityForPlayer(ItemStack stack, EntityPlayer player) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("Quality", (int) MathUtils.clamp(player.experienceTotal / ModConfig.players.xpPerQuality, 1,
				ModConfig.players.maxQuality));

		return stack;
	}

	public static ItemStack setQualityForStack(ItemStack stack, int quality) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger("Quality", quality);

		return stack;
	}

	/*
	 * Static version of ItemQuality#getQuality(), should replace the non-static one
	 * in the future
	 */
	public static int getQualityForStack(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			if (nbt.hasKey("Quality")) {
				return nbt.getInteger("Quality");
			}
		}
		return 0;
	}

	public static boolean hasQualityTag(ItemStack stack) {
		return stack.getTagCompound() == null ? false : stack.getTagCompound().hasKey("Quality", Constants.NBT.TAG_INT);
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

	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer player) {
		if (this.getQuality(itemstack) <= 0) {
			setQuality(itemstack,
					(int) (int) Math.min(Math.floor(player.experienceTotal / ModConfig.players.xpPerQuality),
							ModConfig.players.maxQuality));
		}
	}

}
