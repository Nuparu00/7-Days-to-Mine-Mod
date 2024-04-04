package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.advancements.ModTriggers;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.enchantment.ModEnchantments;
import nuparu.sevendaystomine.entity.EntityShot;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.ApplyRecoilMessage;
import nuparu.sevendaystomine.util.Utils;

@SuppressWarnings({ "deprecation", "unused" })
public class ItemGun extends Item implements IQuality, IReloadable {

	private SoundEvent shotSound = null;
	private SoundEvent drySound = null;
	private SoundEvent reloadSound = null;

	private int maxAmmo = 0;
	private float fullDamage = 0f;
	private float speed = 0f;

	private float recoil = 0f;
	private float counterDef = 0f;

	private float cross = 10F;
	private float spread = 10f;
	private int reloadTime = 1500;
	private int delay = 0;
	private float fovFactor = 1;
	private boolean scoped = false;

	private int projectiles = 1;
	private int shots = 1;

	private EnumGun type;
	private EnumLength length;
	private EnumWield wield;
	private Vec3d aimPosition = new Vec3d(0, 0, 0);

	public ItemGun() {
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.maxStackSize = 1;
		this.setFull3D();
		this.setNoRepair();
	}

	public Item getReloadItem(ItemStack stack) {
		return null;
	}

	public SoundEvent getReloadSound() {
		return reloadSound;
	}

	public SoundEvent getShotSound() {
		return shotSound;
	}

	public SoundEvent getDrySound() {
		return drySound;
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
				* (float) (1f - EnchantmentHelper.getEnchantmentLevel(ModEnchantments.fast_reload, stack) / 10f));
	}

	public float getFullDamage() {
		return (float) (fullDamage * ModConfig.players.balanceModifier);
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

	public float getFinalDamage(ItemStack stack) {
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemGun)) {
			return getFullDamage();
		}
		NBTTagCompound nbt = stack.getTagCompound();
		if (ModConfig.players.qualitySystem && nbt != null) {
			if (nbt.hasKey("Quality")) {
				return getFullDamage() * ((float) nbt.getInteger("Quality") / (float) ModConfig.players.maxQuality);
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
		if (this.getQuality(itemstack) <= 0) {
			setQuality(itemstack,
					(int) (int) Math.min(Math.floor(player.experienceTotal / ModConfig.players.xpPerQuality),
							ModConfig.players.maxQuality));
		}
		initNBT(itemstack);
	}

	public void initNBT(ItemStack itemstack) {
		if (itemstack.getTagCompound() == null) {
			itemstack.setTagCompound(new NBTTagCompound());
		}
		itemstack.getTagCompound().setInteger("Capacity", getMaxAmmo());
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
		if (!ModConfig.players.qualitySystem)
			return super.getItemStackDisplayName(itemstack);
		EnumQuality quality = getQualityTierFromStack(itemstack);
		return quality.color + SevenDaysToMine.proxy.localize(this.getUnlocalizedName() + ".name");
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (ModConfig.players.qualitySystem) {
			int quality = getQuality(stack);
			EnumQuality tier = getQualityTierFromInt(quality);
			tooltip.add(
					tier.color + SevenDaysToMine.proxy.localize("stat.quality." + tier.name().toLowerCase() + ".name"));
			tooltip.add(tier.color + SevenDaysToMine.proxy.localize("stat.quality.name") + quality);
		}

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
								Math.max(Math.floor(player.experienceTotal / ModConfig.players.xpPerQuality), 1),
								ModConfig.players.maxQuality));
				stack.getTagCompound().setInteger("Capacity", getMaxAmmo());
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
	public void onReloadStart(World world, EntityPlayer player, ItemStack stack, int reloadTime) {
		if (stack == null || world == null || player == null)
			return;
		if (stack.getTagCompound() == null) {
			player.sendMessage(new TextComponentTranslation("gun.warning"));
			return;
		}
		stack.getTagCompound().setLong("NextFire",
				world.getTotalWorldTime() + (long) Math.ceil((reloadTime / 1000d) * 20));
	}

	@Override
	public void onReloadEnd(World world, EntityPlayer player, ItemStack stack, ItemStack bullet) {
		if (stack == null || player == null)
			return;
		NBTTagCompound nbt = stack.getTagCompound();
		if (bullet != null && !bullet.isEmpty() && stack != null && nbt.hasKey("Capacity") && nbt.hasKey("Ammo")) {

			nbt.setBoolean("Reloading", false);
			int toReload = (int) Math
					.ceil((double) (getCapacity(stack, player) - nbt.getInteger("Ammo")) / (double) getShotsPerAmmo());

			if (toReload < 1)
				return;

			int reload = Math.min(toReload, Utils.getItemCount(player.inventory, bullet.getItem()));

			setAmmo(stack, player, getAmmo(stack, player) + reload * getShotsPerAmmo());
			player.inventory.clearMatchingItems(bullet.getItem(), -1, reload, null);
		} else if(!nbt.hasKey("Capacity") && !nbt.hasKey("Ammo")) {
			player.sendMessage(new TextComponentTranslation("gun.warning"));
		}
	}

	@Override
	public int getAmmo(ItemStack stack, EntityPlayer player) {
		if (stack == null || stack.isEmpty() || stack.getTagCompound() == null
				|| !stack.getTagCompound().hasKey("Ammo"))
			return -1;
		return stack.getTagCompound().getInteger("Ammo");
	}

	@Override
	public int getCapacity(ItemStack stack, @Nullable EntityPlayer player) {
		if (stack.getTagCompound() == null)
			return this.getMaxAmmo();
		NBTTagCompound nbt = stack.getTagCompound();
		if (!nbt.hasKey("Capacity"))
			return this.getMaxAmmo();
		return (int) Math.ceil((nbt.getInteger("Capacity")
				* (1d + EnchantmentHelper.getEnchantmentLevel(ModEnchantments.big_mag, stack) / 10d)
				* (1d - EnchantmentHelper.getEnchantmentLevel(ModEnchantments.small_mag, stack) / 10d)));
	}

	@Override
	public void setAmmo(ItemStack stack, EntityPlayer player, int ammo) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger("Ammo", ammo);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);

		if ((getWield() == EnumWield.TWO_HAND && !player.getHeldItem(getOtherHand(hand)).isEmpty())) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		if (itemstack.isEmpty() || itemstack.getTagCompound() == null) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		NBTTagCompound nbt = itemstack.getTagCompound();
		if (!nbt.hasKey("Ammo") || !nbt.hasKey("Capacity") || !nbt.hasKey("NextFire")) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
		if (itemstack.getTagCompound().getLong("NextFire") > world.getTotalWorldTime()
				|| itemstack.getTagCompound().getBoolean("Reloading")) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}

		int ammo = nbt.getInteger("Ammo");
		boolean flag = player.isCreative();

		if ((ammo > 0 || flag) && (!(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.jamming, itemstack) > 0)
				|| world.rand.nextInt(20) != 0)) {

			float velocity = getSpeed() * (1f + ((float) getQuality(itemstack) / (float) ModConfig.players.maxQuality));
			boolean explosive = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.explosive, itemstack) != 0;
			boolean sparking = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.sparking, itemstack) != 0;
			if (sparking && explosive && (player instanceof EntityPlayerMP)
					&& itemstack.getItem() instanceof ItemShotgun) {
				ModTriggers.GUN_INTERACT.trigger((EntityPlayerMP) player);
			}
			for (int i = 0; i < projectiles
					* (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.multishot, itemstack) + 1); i++) {
				EntityShot shot = new EntityShot(world, player, velocity,
						((float) getSpread(player, hand) / (player.isSneaking() ? 1.5f : 1f)));
				shot.setExplosive(explosive);
				shot.setSparking(sparking);
				if (!world.isRemote) {
					shot.setDamage(getFinalDamage(itemstack));
					world.spawnEntity(shot);
				}
			}
			itemstack.damageItem(1, player);
			world.playSound(null, new BlockPos(player), getShotSound(), SoundCategory.PLAYERS, getShotSoundVolume(),
					getShotSoundPitch());
			player.swingArm(hand);

			// SevenDaysToMine.proxy.addRecoil(getRecoil(), playerIn);
			if (player instanceof EntityPlayerMP) {
				PacketManager.applyRecoil.sendTo(new ApplyRecoilMessage(getRecoil(), hand == EnumHand.MAIN_HAND, true),
						(EntityPlayerMP) player);
			}
			if (!flag) {
				itemstack.getTagCompound().setInteger("Ammo", ammo - 1);
			}

			itemstack.getTagCompound().setLong("NextFire", world.getTotalWorldTime() + getDelay());

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		} else

		{
			world.playSound(null, new BlockPos(player), getDrySound(), SoundCategory.PLAYERS, 0.3F,
					1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
			itemstack.getTagCompound().setLong("NextFire", world.getTotalWorldTime() + (getDelay() / 2));
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

		float mult = 0.045f;

		if (Utils.isPlayerDualWielding(player)) {
			mult += 0.05f;
		} else if (!player.getHeldItem(getOtherHand(hand)).isEmpty()) {
			mult += 0.1f;
		}

		ItemStack stack = player.getHeldItem(hand);
		int quality = getQuality(stack);

		double spread_local = spread * mult
				* (2d - (ModConfig.players.qualitySystem ? (double) quality / (ModConfig.players.maxQuality) : 1));
		return (spread_local
				* (((double) (Math.abs(player.motionX) + Math.abs(player.motionY) + Math.abs(player.motionZ)))))
				* (1 - (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.marksman, stack)
						- EnchantmentHelper.getEnchantmentLevel(ModEnchantments.strabismus, stack)) * 0.2);
	}

	public double getCross(EntityPlayer player, EnumHand hand) {
		float mult = 1f;

		if (Utils.isPlayerDualWielding(player)) {
			mult += 0.11f;
		} else if (!player.getHeldItem(getOtherHand(hand)).isEmpty()) {
			mult += 0.5f;
		}

		ItemStack stack = player.getHeldItem(hand);
		int quality = getQuality(stack);

		return (spread * mult
				* (1.2d - (ModConfig.players.qualitySystem ? (double) quality / (ModConfig.players.maxQuality + 1) : 1)
						* 0.9))
				* (1 - (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.marksman, stack)
						- EnchantmentHelper.getEnchantmentLevel(ModEnchantments.strabismus, stack)) * 0.2);
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

	@Override
	public int getItemEnchantability() {
		return 15;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	public float getFOVFactor(ItemStack stack) {
		return fovFactor;
	}

	public ItemGun setFOVFactor(float factor) {
		fovFactor = factor;
		return this;
	}

	public boolean getScoped() {
		return this.scoped;
	}

	public ItemGun setScoped(boolean scoped) {
		this.scoped = scoped;
		return this;
	}

	public int getProjectiles() {
		return projectiles;
	}

	public ItemGun setProjectiles(int projectiles) {
		this.projectiles = projectiles;
		return this;
	}

	public ItemGun setFullDamage(float fullDamage) {
		this.fullDamage = fullDamage;
		return this;
	}

	public ItemGun setSpeed(float speed) {
		this.speed = speed;
		return this;
	}

	public ItemGun setRecoil(float recoil) {
		this.recoil = recoil;
		return this;
	}

	public float getCounterDef() {
		return counterDef;
	}

	/*
	 * Not really sure what this actually does. Just keep it at 0
	 */
	public ItemGun setCounterDef(float counterDef) {
		this.counterDef = counterDef;
		return this;
	}

	public ItemGun setReloadTime(int reloadTime) {
		this.reloadTime = reloadTime;
		return this;
	}

	public ItemGun setCross(float cross) {
		this.spread = cross * 3.14f;
		this.cross = cross;
		return this;
	}

	public int getDelay() {
		return delay;
	}

	/*
	 * Sets delay between shots?
	 */
	public ItemGun setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	public ItemGun setType(EnumGun type) {
		this.type = type;
		return this;
	}

	public ItemGun setLength(EnumLength length) {
		this.length = length;
		return this;
	}

	public ItemGun setWield(EnumWield wield) {
		this.wield = wield;
		return this;
	}

	public ItemGun setShotSound(SoundEvent shotSound) {
		this.shotSound = shotSound;
		return this;
	}

	public ItemGun setReloadSound(SoundEvent reloadSound) {
		this.reloadSound = reloadSound;
		return this;
	}

	public ItemGun setDrySound(SoundEvent drySound) {
		this.drySound = drySound;
		return this;
	}

	public ItemGun setMaxAmmo(int maxAmmo) {
		this.maxAmmo = maxAmmo;
		return this;
	}

	public void setShotsPerAmmo(int shots) {
		this.shots = shots;
	}

	public int getShotsPerAmmo() {
		return this.shots;
	}

	public Vec3d getAimPosition() {
		return this.aimPosition;
	}

	public ItemGun setAimPosition(Vec3d aimPosition) {
		this.aimPosition = aimPosition;
		return this;
	}

	public ItemGun setAimPosition(double x, double y, double z) {
		return setAimPosition(new Vec3d(x, y, z));
	}

	public Vec3d getMuzzleFlashPositionMain() {
		return Vec3d.ZERO;
	}

	public Vec3d getMuzzleFlashPositionSide() {
		return Vec3d.ZERO;
	}

	@Nullable
	public Vec3d getMuzzleFlashAimPosition() {
		return Vec3d.ZERO;
	}

	public double getMuzzleFlashSize() {
		return 0.25;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return true;
	}

	@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
		return false;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return -1;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving) {
		worldIn.setBlockState(pos, state);
		return false;
	}

	public static enum EnumGun {
		PISTOL, SHOTGUN, RIFLE, LAUNCHER, MACHINE, SUBMACHINE
	}

	public static enum EnumWield {
		ONE_HAND, TWO_HAND, DUAL;
	}

}
