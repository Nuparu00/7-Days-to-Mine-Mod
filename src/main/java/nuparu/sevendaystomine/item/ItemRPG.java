package nuparu.sevendaystomine.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.enchantment.ModEnchantments;
import nuparu.sevendaystomine.entity.EntityRocket;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.ApplyRecoilMessage;

public class ItemRPG extends ItemGun {

	public ItemRPG() {
		super();
		this.setMaxAmmo(1);
		this.setFullDamage(50f);
		this.setSpeed(1f);
		this.setRecoil(14f);
		this.setCounterDef(0);
		this.setCross(20);
		this.setReloadTime(5000);
		this.setDelay(2);
		this.setType(EnumGun.LAUNCHER);
		this.setLength(EnumLength.LONG);
		this.setWield(EnumWield.TWO_HAND);
	}

	@Override
	public Item getReloadItem(ItemStack stack) {
		return ModItems.ROCKET;
	}

	@Override
	public float getShotSoundVolume() {
		return 1F;
	}

	@Override
	public float getShotSoundPitch() {
		return 1F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F;
	}

	@Override
	public SoundEvent getReloadSound() {
		return SoundHelper.HUNTING_RIFLE_RELOAD;
	}

	@Override
	public SoundEvent getShotSound() {
		return SoundHelper.HUNTING_RIFLE_SHOT;
	}

	@Override
	public SoundEvent getDrySound() {
		return SoundHelper.PISTOL_DRYSHOT;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		ItemStack itemstack = playerIn.getHeldItem(handIn);

		/*
		 * if (handIn == EnumHand.OFF_HAND && (gunWield != EnumWield.DUAL &&
		 * playerIn.getHeldItemMainhand() != ItemStack.EMPTY)) { return new
		 * ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack); }
		 */

		if ((getWield() == EnumWield.TWO_HAND && !playerIn.getHeldItem(getOtherHand(handIn)).isEmpty())) {
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
			float velocity = getSpeed() * (1f+((float)getQuality(itemstack) / (float)ModConfig.players.maxQuality));
			for (int i = 0; i <  getProjectiles()*(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.multishot, itemstack)+1); i++) {
				EntityRocket shot = new EntityRocket(worldIn, playerIn, velocity, ((float) getSpread(playerIn, handIn) / (playerIn.isSneaking() ? 1.5f : 1f)));
				if (!worldIn.isRemote) {
					shot.setDamage(getFinalDamage(itemstack));
					worldIn.spawnEntity(shot);
				}
			}
			itemstack.damageItem(1, playerIn);
			worldIn.playSound(null, new BlockPos(playerIn), getShotSound(), SoundCategory.PLAYERS, getShotSoundVolume(),
					getShotSoundPitch());
			playerIn.swingArm(handIn);
			if (playerIn instanceof EntityPlayerMP) {
				PacketManager.applyRecoil.sendTo(new ApplyRecoilMessage(getRecoil(),handIn==EnumHand.MAIN_HAND, false), (EntityPlayerMP) playerIn);
			}

			if (!flag) {
				itemstack.getTagCompound().setInteger("Ammo", ammo - 1);
			}

			itemstack.getTagCompound().setLong("NextFire", worldIn.getTotalWorldTime() + getDelay());

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		} else {
			worldIn.playSound(null, new BlockPos(playerIn), getDrySound(), SoundCategory.PLAYERS, 0.3F,
					1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
			itemstack.getTagCompound().setLong("NextFire", worldIn.getTotalWorldTime() + (getDelay() / 2));
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
	}

}
