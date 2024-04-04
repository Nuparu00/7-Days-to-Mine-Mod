package nuparu.sevendaystomine.item;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.model.ModelNightVisionDevice;

public class ItemNightVisionDevice extends ItemArmor {

	public ItemNightVisionDevice(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}

	@Nullable
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		if (!stack.isEmpty() && stack.getItem() instanceof ItemNightVisionDevice) {
			return SevenDaysToMine.MODID + ":textures/models/armor/night_vision_device.png";
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
			ModelBiped original) {
		ModelNightVisionDevice model = new ModelNightVisionDevice();
		if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemNightVisionDevice) {
			model.isChild = original.isChild;
			model.isSneak = original.isSneak;
			model.isRiding = original.isRiding;
			model.rightArmPose = original.rightArmPose;
			model.leftArmPose = original.leftArmPose;
		}

		return model;
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack armor) {
		PotionEffect effect_new = new PotionEffect(MobEffects.NIGHT_VISION, 240, 1, false, false);
		effect_new.setCurativeItems(new ArrayList<ItemStack>());
		player.addPotionEffect(effect_new);
	}

}
