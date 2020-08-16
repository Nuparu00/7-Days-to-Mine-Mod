package com.nuparu.sevendaystomine.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFlame extends EntityThrowable {

	private int ticksAlive;
	private int lifetime = 40;

	public EntityFlame(World worldIn) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	public EntityFlame(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
		this.setSize(0.25F, 0.25F);
	}

	public EntityFlame(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
		this.setSize(0.25F, 0.25F);
	}

	public EntityFlame(World worldIn, EntityLivingBase shooter, float velocity, float spread, int lifetime) {
		super(worldIn, shooter);
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(), shooter.posZ,
				shooter.rotationYaw + (rand.nextFloat() * spread) - spread / 2,
				shooter.rotationPitch + (rand.nextFloat() * spread) - spread / 2);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		this.shoot(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F * spread);
		this.lifetime=lifetime;

	}

	public EntityFlame(World worldIn, double x, double y, double z, float yaw, float pitch, float velocity,
			float spread) {
		super(worldIn, x, y, z);
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(x, y, z, yaw + (rand.nextFloat() * spread) - spread / 2,
				pitch + (rand.nextFloat() * spread) - spread / 2);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		this.shoot(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F * spread);

	}

	@Override
	protected void onImpact(RayTraceResult rayTraceResult) {

		if (!this.world.isRemote) {

			if (rayTraceResult.entityHit != null) {
				if (!rayTraceResult.entityHit.isImmuneToFire()) {
					rayTraceResult.entityHit.setFire(16);
				}
			} else {
				BlockPos blockpos = rayTraceResult.getBlockPos().offset(rayTraceResult.sideHit);

				if (this.world.isAirBlock(blockpos)) {
					this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
				}
			}
			this.setDead();
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();
		if (this.ticksAlive >= 2) {
			this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D,
					new int[0]);
		}
		if (this.ticksAlive >= 10) {
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D,
					new int[0]);
		}
		if (this.ticksAlive >= lifetime) {

			this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D,
					new int[0]);
		}
		if (this.isInWater()) {
			this.setDead();
		}
		++this.ticksAlive;
		if (this.ticksAlive >= 40) {
			this.setDead();
		}

	}

	protected float getGravityVelocity() {
		return -0.0002F;
	}

	public float getBrightness(float partialTicks) {
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float partialTicks) {
		return 15728880;
	}

}