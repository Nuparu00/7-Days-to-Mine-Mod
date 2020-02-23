package com.nuparu.sevendaystomine.events;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.EnumModParticleType;
import com.nuparu.sevendaystomine.util.MathUtils;

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
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LivingEventHandler {

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

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.isCreative() || player.isSpectator()) {
				return;
			}
		}

		if (amount < 2)
			return;

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

		for (int i = 0; i < (int) Math.round(MathUtils.getDoubleInRange(1, 5)*SevenDaysToMine.proxy.getParticleLevel()); i++) {
			double x = entity.posX + MathUtils.getDoubleInRange(-1, 1) * entity.width;
			double y = entity.posY + MathUtils.getDoubleInRange(0, 1) * entity.height;
			double z = entity.posZ + MathUtils.getDoubleInRange(-1, 1) * entity.width;
			SevenDaysToMine.proxy.spawnParticle(world, EnumModParticleType.BLOOD, x, y, z,
					MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d)  / 7d, MathUtils.getDoubleInRange(-1d, 1d) / 7d);
		}
	}
}
