package com.nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.util.ColorDetector;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemClothing extends ItemArmor implements ISpecialArmor {

	public ResourceLocation texture;
	public ResourceLocation overlay;
	public float scale = 0f;
	public boolean isDyeable = false;
	public boolean hasOverlay = false;
	public int defaultColor = 16777215;

	public ItemClothing(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
		this.setCreativeTab(SevenDaysToMine.TAB_CLOTHING);
	}
	
	@Nullable
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return SevenDaysToMine.MODID+":textures/misc/empty.png";
    }
	
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 4;
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage,
			int slot) {
		if (source == DamageSource.IN_FIRE || source == DamageSource.LAVA || source == DamageSource.ON_FIRE) {
			return new ArmorProperties(1, 1, MathHelper.floor(damage * .30D));
		}
		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
		stack.damageItem(damage * 2, entity);
	}

	@Override
	public boolean getIsRepairable(ItemStack armor, ItemStack stack) {
		return stack.getItem() == ModItems.CLOTH;
	}

	@Override
	public boolean hasColor(ItemStack stack) {
		return !isDyeable ? false
				: (!stack.hasTagCompound() ? false
						: (!stack.getTagCompound().hasKey("display", 10) ? false
								: stack.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	@Override
	public int getColor(ItemStack stack) {
		if (!isDyeable) {
			return -1;
		} else {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null) {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

				if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3)) {
					return nbttagcompound1.getInteger("color");
				}
			}

			return defaultColor;
		}
	}

	/**
	 * Remove the color from the specified armor ItemStack.
	 */
	@Override
	public void removeColor(ItemStack stack) {
		if (isDyeable) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null) {
				NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

				if (nbttagcompound1.hasKey("color")) {
					nbttagcompound1.removeTag("color");
				}
			}
		}
	}

	/**
	 * Sets the color of the specified armor ItemStack
	 */
	@Override
	public void setColor(ItemStack stack, int color) {
		if (!isDyeable) {
			throw new UnsupportedOperationException("Can\'t dye non-dyeable!");
		} else {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound == null) {
				nbttagcompound = new NBTTagCompound();
				stack.setTagCompound(nbttagcompound);
			}

			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (!nbttagcompound.hasKey("display", 10)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", color);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		String base = super.getItemStackDisplayName(stack);
		if (isDyeable) {
			int c = getColor(stack);
			float r = (float) (c >> 16 & 255);
			float g = (float) (c >> 8 & 255);
			float b = (float) (c & 255);
			String colorName = ColorDetector.INSTANCE.getColorMatch(r, g, b);
			if (colorName != null) {
				return SevenDaysToMine.proxy.localize(colorName) + " " + base;
			}
		}
		return base;

	}
	
	@SideOnly(Side.CLIENT)
	protected ModelPlayer model;
	
	@SideOnly(Side.CLIENT)
	public ModelPlayer getModel(EntityPlayer player, ItemStack stack) {
		if(model == null) {
			model = new ModelPlayer(0.35f,false);
		}
		return model;
	}

}
