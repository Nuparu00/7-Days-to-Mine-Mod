package nuparu.sevendaystomine.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

public class EntityProjectileVomit extends Entity implements IProjectile {

	@SuppressWarnings("unchecked")
	private static final Predicate<Entity> TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING,
			EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
				public boolean apply(@Nullable Entity p_apply_1_) {
					return p_apply_1_.canBeCollidedWith();
				}
			});

	protected boolean inGround;
	public boolean particle;
	public Entity shootingEntity;
	private int ticksInAir;
	private double damage;
	private int knockbackStrength;

	private static final DataParameter<String> SHOOTER_NAME = EntityDataManager.<String>createKey(EntityHuman.class,
			DataSerializers.STRING);

	private static final float BLOCK_DAMAGE_BASE = 0.01325f;

	public EntityProjectileVomit(World worldIn) {
		super(worldIn);
		this.damage = 2.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityProjectileVomit(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityProjectileVomit(World worldIn, EntityLivingBase shooter) {
		this(worldIn, shooter.posX, shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D,
				shooter.posZ);
		this.shootingEntity = shooter;
	}

	public EntityProjectileVomit(World worldIn, Vec3d start, float yaw, float pitch, float velocity, float spread) {
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

	public EntityProjectileVomit(World worldIn, EntityLivingBase shooter, float velocity, float spread) {
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
		this.particle = compound.getBoolean("particle");
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
		compound.setBoolean("particle", particle);
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

		Entity entity = this.findEntityOnPath(vec3d1, vec3d);

		if (entity != null) {
			raytraceresult = new RayTraceResult(entity);
		}

		if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

			if (this.getShooter() instanceof EntityPlayer
					&& !((EntityPlayer) this.getShooter()).canAttackPlayer(entityplayer)) {
				raytraceresult = null;
			}
		}
		if (raytraceresult != null && raytraceresult.entityHit instanceof EntityZombiePoliceman) {
			raytraceresult = null;
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

			f1 = 0.6F;
		}

		if (this.isWet()) {
			this.extinguish();
		}

		this.motionX *= (double) f1;
		this.motionY *= (double) f1;
		this.motionZ *= (double) f1;

		if (!this.hasNoGravity()) {
			this.motionY -= 0.05000000074505806D;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.doBlockCollisions();

	}

	protected void onHit(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;

		if (entity != null) {

			float f = MathHelper
					.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			int i = MathHelper.ceil((double) f * this.damage);

			DamageSource damagesource = null;

			if (this.getShooter() == null) {
				damagesource = DamageSources.causeShotDamage(this, this);
			} else {
				damagesource = DamageSources.causeShotDamage(this, this.getShooter());
			}

			if (this.isBurning() && !(entity instanceof EntityEnderman)) {
				entity.setFire(5);
			}

			if (entity.attackEntityFrom(damagesource, (float) i)) {
				if (entity instanceof EntityLivingBase) {
					EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
					if (this.knockbackStrength > 0) {
						float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

						if (f1 > 0.0F) {
							entitylivingbase.addVelocity(
									this.motionX * (double) this.knockbackStrength * 0.6000000238418579D / (double) f1,
									0.1D,
									this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D / (double) f1);
						}
					}

					if (this.getShooter() instanceof EntityLivingBase) {
						EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.getShooter());
						EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.getShooter(),
								entitylivingbase);
					}

					this.shotHit(entitylivingbase);

					if (this.getShooter() != null && entitylivingbase != this.getShooter()
							&& entitylivingbase instanceof EntityPlayer
							&& this.getShooter() instanceof EntityPlayerMP) {
						((EntityPlayerMP) this.getShooter()).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
					}
				}

				if (!(entity instanceof EntityEnderman)) {
					this.setDead();
				}
			} else {
				this.motionX *= -0.10000000149011612D;
				this.motionY *= -0.10000000149011612D;
				this.motionZ *= -0.10000000149011612D;
				this.rotationYaw += 180.0F;
				this.prevRotationYaw += 180.0F;
				this.ticksInAir = 0;

				if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY
						+ this.motionZ * this.motionZ < 0.0010000000474974513D) {
					this.setDead();
				}
			}
		} else {
			if (!this.world.isRemote) {
				BlockPos blockpos = raytraceResultIn.getBlockPos();
				IBlockState iblockstate = this.world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();
				int meta = block.getMetaFromState(iblockstate);
				this.motionX = (double) ((float) (raytraceResultIn.hitVec.x - this.posX));
				this.motionY = (double) ((float) (raytraceResultIn.hitVec.y - this.posY));
				this.motionZ = (double) ((float) (raytraceResultIn.hitVec.z - this.posZ));
				float f2 = MathHelper
						.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
				this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
				this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
				this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;

				if (iblockstate.getMaterial() != Material.AIR) {
					block.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
				}
				if (iblockstate.getMaterial() == Material.GLASS) {
					this.world.destroyBlock(blockpos, false);
				} else if(ModConfig.mobs.zombiesBreakBlocks){
					Utils.damageBlock(world, blockpos,
							(float) (damage / iblockstate.getBlockHardness(world, blockpos)) * BLOCK_DAMAGE_BASE, true);

				}
				world.playSound(null, raytraceResultIn.hitVec.x, raytraceResultIn.hitVec.y, raytraceResultIn.hitVec.z, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.HOSTILE, MathUtils.getFloatInRange(0.8f, 1.2f), MathUtils.getFloatInRange(0.8f, 1.2f));
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
