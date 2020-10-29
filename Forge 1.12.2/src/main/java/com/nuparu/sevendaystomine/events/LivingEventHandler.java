package com.nuparu.sevendaystomine.events;

import java.util.ArrayList;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.block.BlockMercury;
import com.nuparu.sevendaystomine.entity.EntityZombieBase;
import com.nuparu.sevendaystomine.potions.Potions;
import com.nuparu.sevendaystomine.util.EnumModParticleType;
import com.nuparu.sevendaystomine.util.MathUtils;
import com.nuparu.sevendaystomine.util.Utils;

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
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LivingEventHandler {
	
	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
			EntityLivingBase livingEntity = event.getEntityLiving();
			World world = livingEntity.world;
			if(livingEntity.getHealth() < livingEntity.getMaxHealth() && world.getBlockState(new BlockPos(livingEntity)).getBlock() instanceof BlockMercury) {
				livingEntity.addPotionEffect(new PotionEffect(Potions.mercuryPoison,240));
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
			if(source.getTrueSource() != null && source.getTrueSource() instanceof EntityZombieBase) {
				if(world.rand.nextInt(20) == 0) {
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

		if (amount >= (entity.getMaxHealth() / 100 * 20) && world.rand.nextInt(16) == 0) {
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
		if (living instanceof EntityPlayer) {
			if (event.getDistance() > 4
					&& living.world.rand.nextFloat() * (Math.max(256 - (event.getDistance() * 25), 0)) <= 1) {
				PotionEffect effect = new PotionEffect(Potions.brokenLeg,
						(int) (MathUtils.getFloatInRange(0.5f, 1.5f) * (500 * event.getDistance())), 0, false, false);
				effect.setCurativeItems(new ArrayList<ItemStack>());
				living.addPotionEffect(effect);
			}
		}

	}
}
