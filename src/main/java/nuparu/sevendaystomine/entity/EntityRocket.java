package nuparu.sevendaystomine.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.util.MathUtils;

public class EntityRocket extends Entity implements IProjectile {

	@SuppressWarnings("unchecked")
	private static final Predicate<Entity> TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING,
			EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
				public boolean apply(@Nullable Entity p_apply_1_) {
					return p_apply_1_.canBeCollidedWith();
				}
			});

	protected boolean inGround;
	public Entity shootingEntity;
	private int ticksInAir;
	private double damage;
	private int knockbackStrength;

	private static final DataParameter<String> SHOOTER_NAME = EntityDataManager.<String>createKey(EntityHuman.class,
			DataSerializers.STRING);

	private static final float BLOCK_DAMAGE_BASE = 0.003125f;

	public EntityRocket(World worldIn) {
		super(worldIn);
		this.damage = 2.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityRocket(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityRocket(World worldIn, EntityLivingBase shooter) {
		this(worldIn, shooter.posX, shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D,
				shooter.posZ);
		this.shootingEntity = shooter;
	}

	public EntityRocket(World worldIn, Vec3d start, float yaw, float pitch, float velocity, float spread) {
		this(worldIn, start.x, start.y, start.z);
		this.shootingEntity = null;
		this.setLocationAndAngles(start.x, start.y, start.z, yaw + (rand.nextFloat() * spread) - spread / 2,
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

	public EntityRocket(World worldIn, EntityLivingBase shooter, float velocity, float spread) {
		this(worldIn, shooter.posX, shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D,
				shooter.posZ);
		this.shootingEntity = shooter;
		this.setShooterName(shooter.getName());
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
		this.shoot(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 19.11F * spread);
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(SHOOTER_NAME, String.valueOf(""));
	}

	@Override
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.inGround = compound.getByte("inGround") == 1;
		if (compound.hasKey("damage", 99)) {
			this.damage = compound.getDouble("damage");
		}
		if (compound.hasKey("shooterName", Constants.NBT.TAG_STRING)) {
			setShooterName(compound.getString("shooterName"));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		compound.setDouble("damage", this.damage);
		if (getShooterName() != null) {
			compound.setString("shooterName", getShooterName());
		}

	}

	public void onUpdate() {
		super.onUpdate();

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}
		++this.ticksInAir;
		Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
		vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
		vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

		if (raytraceresult != null) {
			vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
		}

		if (raytraceresult != null
				&& !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
			this.onHit(raytraceresult);
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f4)
				* (180D / Math.PI)); this.rotationPitch
						- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float f1 = 0.99F;
		float f2 = 0.05F;

		if (this.isInWater()) {
			for (int i = 0; i < 4; ++i) {
				float f3 = 0.25F;
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D,
						this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY,
						this.motionZ);
			}

		} else {
			for (int i = 0; i < 10; ++i) {
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
						this.posX - this.motionX * 0.25D + MathUtils.getDoubleInRange(0, 0.8),
						this.posY - this.motionY * 0.25D + MathUtils.getDoubleInRange(0, 0.8),
						this.posZ - this.motionZ * 0.25D + MathUtils.getDoubleInRange(0, 0.8), -this.motionX / 10,
						-this.motionY / 10, -this.motionZ / 10);
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
						this.posX - this.motionX * 0.25D + MathUtils.getDoubleInRange(0, 0.8),
						this.posY - this.motionY * 0.25D + MathUtils.getDoubleInRange(0, 0.8),
						this.posZ - this.motionZ * 0.25D + MathUtils.getDoubleInRange(0, 0.8), -this.motionX / 10,
						-this.motionY / 10, -this.motionZ / 10);
				this.world.spawnParticle(EnumParticleTypes.FLAME,
						this.posX - this.motionX * 0.25D + MathUtils.getDoubleInRange(0, 0.8),
						this.posY - this.motionY * 0.25D + MathUtils.getDoubleInRange(0, 0.8),
						this.posZ - this.motionZ * 0.25D + MathUtils.getDoubleInRange(0, 0.8), -this.motionX / 10,
						-this.motionY / 10, -this.motionZ / 10);
			}
		}
		if (ticksInAir <= 5) {
			for (int i = 0; i < 2; ++i) {
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX - this.motionX * 0.25D,
						this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, 0, 0, 0);
			}
		}

		if (this.isWet()) {
			this.extinguish();
		}

		this.motionX *= (double) f1;
		this.motionY *= (double) f1;
		this.motionZ *= (double) f1;

		if (!this.hasNoGravity()) {
			this.motionY -= 0.00500000074505806D;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();

	}

	protected void onHit(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;

		if (entity != null) {
			if(!world.isRemote) {
				world.newExplosion(this, entity.posX,entity.posY, entity.posZ, 5.2f, true, true);
			}
		} else {
			if (!this.world.isRemote) {
				world.newExplosion(this, raytraceResultIn.hitVec.x, raytraceResultIn.hitVec.y, raytraceResultIn.hitVec.z, 5.2f, true, true);
			}
		}
		this.setDead();
	}

	protected void shotHit(EntityLivingBase living) {

	}

	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
		Entity entity = null;
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this,
				this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D), TARGETS);
		double d0 = 0.0D;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = list.get(i);

			if (entity1 != this.getShooter() || this.ticksInAir >= 5) {
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
				RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

				if (raytraceresult != null) {
					double d1 = start.squareDistanceTo(raytraceresult.hitVec);

					if (d1 < d0 || d0 == 0.0D) {
						entity = entity1;
						d0 = d1;
					}
				}
			}
		}

		return entity;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public void setDamage(double damageIn) {
		this.damage = damageIn;
	}

	public double getDamage() {
		return this.damage;
	}

	public Entity getShooter() {
		if (shootingEntity == null && !getShooterName().isEmpty()) {
			shootingEntity = world.getPlayerEntityByName(getShooterName());
		}
		return shootingEntity;
	}

	public void setShooterName(String name) {
		this.dataManager.set(SHOOTER_NAME, name);
	}

	public String getShooterName() {
		return this.dataManager.get(SHOOTER_NAME);
	}

}
