package com.nuparu.sevendaystomine.events;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockMercury;
import com.nuparu.sevendaystomine.entity.EntityBandit;
import com.nuparu.sevendaystomine.entity.EntityHuman;
import com.nuparu.sevendaystomine.entity.EntityZombieBase;
import com.nuparu.sevendaystomine.potions.Potions;
import com.nuparu.sevendaystomine.util.EnumModParticleType;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class LivingEventHandler {

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase livingEntity = event.getEntityLiving();
		World world = livingEntity.world;
		if (livingEntity.getHealth() < livingEntity.getMaxHealth()
				&& world.getBlockState(new BlockPos(livingEntity)).getBlock() instanceof BlockMercury) {
			livingEntity.addPotionEffect(new PotionEffect(Potions.mercuryPoison, 240));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onEntityAttack(LivingAttackEvent event) {
		DamageSource source = event.getSource();
		float amount = event.getAmount();
		EntityLivingBase entity = event.getEntityLiving();
		World world = entity.world;

		if (entity.getIsInvulnerable()) {
			return;
		}
		if (amount < 2)
			return;

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.isCreative() || player.isSpectator()) {
				return;
			}
			if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityZombieBase) {
				if (world.rand.nextInt(15) == 0) {
					Utils.infectPlayer(player, 0);
				}
			}
		}

		if (source == DamageSource.DROWN || source == DamageSource.FALL || source == DamageSource.HOT_FLOOR
				|| source == DamageSource.ON_FIRE || source == DamageSource.OUT_OF_WORLD
				|| source == DamageSource.STARVE || source == DamageSource.WITHER || source == DamageSource.MAGIC) {
			return;
		}

		if (entity instanceof AbstractSkeleton || entity instanceof EntitySquid || entity instanceof EntitySnowman
				|| entity instanceof EntityGuardian || entity instanceof EntityWither || entity instanceof EntityDragon
				|| entity instanceof EntityBlaze || entity instanceof EntityVex || entity instanceof EntitySlime
				|| entity instanceof EntityGolem || entity instanceof EntityEnderman
				|| entity instanceof EntityEndermite) {
			return;
		}

		if (!world.isRemote && amount >= (entity.getMaxHealth() / 100 * 20) && world.rand.nextInt(16) == 0) {
			PotionEffect effect = new PotionEffect(Potions.bleeding, Integer.MAX_VALUE, 1, false, false);
			effect.setCurativeItems(new ArrayList<ItemStack>());
			entity.addPotionEffect(effect);
		}
		if (amount > 0) {
			for (int i = 0; i < (int) Math
					.round(MathUtils.getDoubleInRange(1, 5) * SevenDaysToMine.proxy.getParticleLevel()); i++) {
				double x = entity.posX + MathUtils.getDoubleInRange(-1, 1) * entity.width;
				double y = entity.posY + MathUtils.getDoubleInRange(0, 1) * entity.height;
				double z = entity.posZ + MathUtils.getDoubleInRange(-1, 1) * entity.width;
				SevenDaysToMine.proxy.spawnParticle(world, EnumModParticleType.BLOOD, x, y, z,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d) / 7d,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d);
			}
		}
	}

	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living instanceof EntityPlayer && !living.world.isRemote) {
			if (event.getDistance() > 4
					&& living.world.rand.nextFloat() * (Math.max(256 - (event.getDistance() * 25), 0)) <= 1) {
				PotionEffect effect = new PotionEffect(Potions.brokenLeg,
						(int) (MathUtils.getFloatInRange(0.5f, 1.5f) * (500 * event.getDistance())), 0, false, false);
				effect.setCurativeItems(new ArrayList<ItemStack>());
				living.addPotionEffect(effect);
			}
		}

	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living instanceof EntityPlayer || living instanceof EntityHuman || living instanceof EntityVillager) {
			World world = living.world;
			List<EntityZombieBase> list = world.getEntitiesWithinAABB(EntityZombieBase.class,
					new AxisAlignedBB(living.posX, living.posY, living.posZ, living.posX + 1, living.posY + 1,
							living.posZ + 1).grow(16, 16, 16));
			for (EntityZombieBase zombie : list) {
				if (zombie.getAttackTarget() == null) {
					zombie.setAttackTarget(living);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
		EntityLivingBase living = event.getEntityLiving();
		if (living.world.isRemote)
			return;
		if (living instanceof EntityBandit && living.world.rand.nextInt(30) == 0) {
			EntityHorse horse = new EntityHorse(living.world);
			horse.setPositionAndRotation(living.posX, living.posY, living.posZ, living.rotationYaw,
					living.rotationPitch);
			horse.setHorseTamed(true);

			living.world.spawnEntity(horse);
			living.startRiding(horse);
			horse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).insertItem(0,
					new ItemStack(Items.SADDLE), false);

		}
	}

}
