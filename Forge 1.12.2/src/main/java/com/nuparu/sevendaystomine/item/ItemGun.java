package com.nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.client.sound.SoundHelper;
import com.nuparu.sevendaystomine.enchantment.ModEnchantments;
import com.nuparu.sevendaystomine.entity.EntityShot;
import com.nuparu.sevendaystomine.events.TickHandler;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings({ "deprecation", "unused" })
public class ItemGun extends Item implements IQuality {

	private String shotSound = null;
	private String drySound = null;
	private String reloadSound = null;

	private int maxAmmo = 0;
	private float fullDamage = 0f;
	private float speed = 0f;

	private float recoil = 0f;
	private float counterDef = 0f;

	private float cross = 10F;
	private float spread = 10f;
	private int reloadTime = 1500;
	private int delay = 0;

	private int projectiles = 1;

	private EnumGun type;
	private EnumLength length;
	private EnumWield wield;

	public ItemGun(String shotSound, String drySound, String reloadSound, int maxAmmo, float fullDamage, float speed,
			float recoil, float counterDef, float cross, int reloadTime, int delay, EnumGun gunType,
			EnumLength gunLength, EnumWield gunWield) {
		this.shotSound = shotSound;
		this.drySound = drySound;
		this.reloadSound = reloadSound;
		this.maxAmmo = maxAmmo;
		this.fullDamage = fullDamage;
		this.speed = speed;
		this.recoil = recoil;
		this.counterDef = counterDef;
		this.cross = cross;
		this.spread = cross * 3.14f;
		this.reloadTime = reloadTime;
		this.delay = delay;
		this.type = gunType;
		this.length = gunLength;
		this.wield = gunWield;

		this.setCreativeTab(CreativeTabs.COMBAT);
		this.maxStackSize = 1;
		this.setFull3D();
		this.setNoRepair();
	}

	public Item getBullet() {
		return null;
	}

	public SoundEvent getReloadSound() {
		return SoundHelper.getSoundByName(reloadSound);
	}

	public SoundEvent getShotSound() {
		return SoundHelper.getSoundByName(shotSound);
	}

	public SoundEvent getDrySound() {
		return SoundHelper.getSoundByName(drySound);
	}

	public float getSpeed() {
		return speed;
	}

	public float getRecoil() {
		return recoil;
	}

	public float getCross() {
		return cross;
	}

	public int getMaxAmmo() {
		return maxAmmo;
	}

	public int getReloadTime() {
		return reloadTime;
	}

	public int getReloadTime(ItemStack stack) {
		return (int) Math.ceil((float) getReloadTime()
				/ (float) (1 + EnchantmentHelper.getEnchantmentLevel(ModEnchantments.fast_reload, stack)));
	}

	public float getFullDamage() {
		return fullDamage;
	}

	public EnumGun getType() {
		return type;
	}

	public EnumLength getLength() {
		return length;
	}

	public EnumWield getWield() {
		return wield;
	}

	public void recoil(EntityPlayer entity) {
		entity.rotationPitch -= recoil;
		entity.rotationPitch += recoil * 0.2F;
	}

	public float getFinalDamage(ItemStack stack) {
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
			return getFullDamage();
		}
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null) {
			if (nbt.hasKey("Quality")) {
				return getFullDamage() * ((float) nbt.getInteger("Quality") / (float) ItemQuality.MAX_QUALITY);
			}
		}
		return getFullDamage();
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
		setQuality(itemstack, (int) (int) Math.min(Math.floor(player.getScore() / ItemQuality.XP_PER_QUALITY_POINT),
				ItemQuality.MAX_QUALITY));
		itemstack.getTagCompound().setInteger("Capacity", maxAmmo);
		itemstack.getTagCompound().setInteger("Ammo", 0);
		itemstack.getTagCompound().setInteger("ReloadTime", 90000);
		itemstack.getTagCompound().setBoolean("Reloading", false);
		itemstack.getTagCompound().setLong("NextFire", 0);
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

		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("Ammo") && nbt.hasKey("Capacity")) {
			tooltip.add(TextFormatting.YELLOW + SevenDaysToMine.proxy.localize("stat.ammo.name")
					+ stack.getTagCompound().getInteger("Ammo"));
			tooltip.add(TextFormatting.YELLOW + SevenDaysToMine.proxy.localize("stat.capacity.name")
					+ stack.getTagCompound().getInteger("Capacity"));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = new ItemStack(this, 1, 0);
			if (player != null) {
				setQuality(stack,
						(int) (int) Math.min(
								Math.max(Math.floor(player.getScore() / ItemQuality.XP_PER_QUALITY_POINT), 1),
								ItemQuality.MAX_QUALITY));
				stack.getTagCompound().setInteger("Capacity", maxAmmo);
				stack.getTagCompound().setInteger("Ammo", 0);
				stack.getTagCompound().setInteger("ReloadTime", 90000);
				stack.getTagCompound().setBoolean("Reloading", false);
				stack.getTagCompound().setLong("NextFire", 0);
			}
			items.add(stack);
		}
	}

	public EnumHand getOtherHand(EnumHand original) {
		if (original == EnumHand.MAIN_HAND) {
			return EnumHand.OFF_HAND;
		}
		return EnumHand.MAIN_HAND;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		ItemStack itemstack = playerIn.getHeldItem(handIn);

		/*
		 * if (handIn == EnumHand.OFF_HAND && (gunWield != EnumWield.DUAL &&
		 * playerIn.getHeldItemMainhand() != ItemStack.EMPTY)) { return new
		 * ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack); }
		 */

		if ((wield == EnumWield.TWO_HAND && !playerIn.getHeldItem(getOtherHand(handIn)).isEmpty())) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		if (itemstack.isEmpty() || itemstack.getTagCompound() == null) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (!nbt.hasKey("Ammo") || !nbt.hasKey("Capacity") || !nbt.hasKey("NextFire")) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		if (itemstack.getTagCompound().getLong("NextFire") > worldIn.getTotalWorldTime()
				|| itemstack.getTagCompound().getBoolean("Reloading")) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}

		int ammo = nbt.getInteger("Ammo");
		boolean flag = playerIn.isCreative();
		if (ammo > 0 || flag) {
			float velocity = getSpeed() * (getQuality(itemstack) / 5f);
			for (int i = 0; i < projectiles; i++) {
				EntityShot shot = new EntityShot(worldIn, playerIn, velocity, ((float) getSpread(playerIn, handIn)
						* (float) (Math.abs(playerIn.motionX) + Math.abs(playerIn.motionY) + Math.abs(playerIn.motionZ))
						* 5f) / (playerIn.isSneaking() ? 4 : 3));
				if (!worldIn.isRemote) {
					shot.setDamage(getFinalDamage(itemstack));
					worldIn.spawnEntity(shot);
				}
			}
			itemstack.damageItem(1, playerIn);
			worldIn.playSound(null, new BlockPos(playerIn), getShotSound(), SoundCategory.PLAYERS, getShotSoundVolume(),
					getShotSoundPitch());
			playerIn.swingArm(handIn);
			if (worldIn.isRemote) {
				SevenDaysToMine.proxy.addRecoil(getRecoil(), playerIn);
			}

			if (!flag) {
				itemstack.getTagCompound().setInteger("Ammo", ammo - 1);
			}

			itemstack.getTagCompound().setLong("NextFire", worldIn.getTotalWorldTime() + delay);

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		} else {
			worldIn.playSound(null, new BlockPos(playerIn), getDrySound(), SoundCategory.PLAYERS, 0.3F,
					1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
			itemstack.getTagCompound().setLong("NextFire", worldIn.getTotalWorldTime() + (delay / 2));
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
	}

	public float getShotSoundVolume() {
		return 0.3F;
	}

	public float getShotSoundPitch() {
		return 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F;
	}

	public double getSpread(EntityPlayer player, EnumHand hand) {

		float mult = 1;

		if (Utils.isPlayerDualWielding(player)) {
			mult += 0.11f;
		} else if (!player.getHeldItem(getOtherHand(hand)).isEmpty()) {
			mult += 0.5f;
		}

		ItemStack stack = player.getHeldItem(hand);
		int quality = getQuality(stack);

		double spread_local = spread * mult * (1d - ((double) quality / (ItemQuality.MAX_QUALITY + 1)));
		return (spread_local
				* (double) (Math.abs(player.motionX) + Math.abs(player.motionY) + Math.abs(player.motionZ)))
				/ (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.marksman, stack) + 1d);
	}

	public double getCross(EntityPlayer player, EnumHand hand) {
		float mult = 1;

		if (Utils.isPlayerDualWielding(player)) {
			mult += 0.11f;
		} else if (!player.getHeldItem(getOtherHand(hand)).isEmpty()) {
			mult += 0.5f;
		}

		ItemStack stack = player.getHeldItem(hand);
		int quality = getQuality(stack);

		return (spread * mult * (1d - ((double) quality / (ItemQuality.MAX_QUALITY + 1))))
				/ (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.marksman, stack) + 1d);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return Integer.MAX_VALUE;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		if (worldIn.isRemote) {
			SevenDaysToMine.proxy.onGunStop(getMaxItemUseDuration(stack) - timeLeft);
		}
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			double value = this.getFinalDamage(stack) - 1;
			map.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
					new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", value, 0));
		}
		return map;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
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

	public float getFOVFactor(ItemStack stack) {
		return 1.0f;
	}

	public int getProjectiles() {
		return projectiles;
	}

	public void setProjectiles(int projectiles) {
		this.projectiles = projectiles;
	}

	public static enum EnumGun {
		PISTOL, SHOTGUN, RIFLE, LAUNCHER, MACHINE, SUBMACHINE
	}

	public static enum EnumWield {
		ONE_HAND, TWO_HAND, DUAL;
	}

}
