package nuparu.sevendaystomine.entity;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.util.ItemUtils;

public class EntityZombiePig extends EntityZombieBase {
	/** true is the wolf is wet else false */
	private boolean isWet;
	/** True if the wolf is shaking else False */
	private boolean isShaking;
	/** This time increases while wolf is shaking and emitting water particles. */
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;

	public EntityZombiePig(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 0.85F);
		this.experienceValue = 8;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		range.setBaseValue(32.0D);
		speed.setBaseValue(0.19D);
		attack.setBaseValue(3.0D * ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(55D* ModConfig.players.balanceModifier);
		armor.setBaseValue(0.0D);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_PIG_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PIG_DEATH;
	}

	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
			this.isShaking = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.world.setEntityState(this, (byte) 8);
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.isWet()) {
			this.isWet = true;
			this.isShaking = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else if ((this.isWet || this.isShaking) && this.isShaking) {
			if (this.timeWolfIsShaking == 0.0F) {
				this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(),
						(this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;

			if (this.prevTimeWolfIsShaking >= 2.0F) {
				this.isWet = false;
				this.isShaking = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
			}

			if (this.timeWolfIsShaking > 0.4F) {
				float f = (float) this.getEntityBoundingBox().minY;
				int i = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);

				for (int j = 0; j < i; ++j) {
					float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double) f1,
							(double) (f + 0.8F), this.posZ + (double) f2, this.motionX, this.motionY, this.motionZ);
				}
			}
		}
	}

	/**
	 * True if the wolf is wet
	 */
	@SideOnly(Side.CLIENT)
	public boolean isWolfWet() {
		return this.isWet;
	}

	/**
	 * Used when calculating the amount of shading to apply while the wolf is wet.
	 */
	@SideOnly(Side.CLIENT)
	public float getShadingWhileWet(float p_70915_1_) {
		return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_)
				/ 2.0F * 0.25F;
	}

	@SideOnly(Side.CLIENT)
	public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
		float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_
				+ p_70923_2_) / 1.8F;

		if (f < 0.0F) {
			f = 0.0F;
		} else if (f > 1.0F) {
			f = 1.0F;
		}

		return MathHelper.sin(f * (float) Math.PI) * MathHelper.sin(f * (float) Math.PI * 11.0F) * 0.15F
				* (float) Math.PI;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.8F;
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 8) {
			this.isShaking = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@SideOnly(Side.CLIENT)
	public float getTailRotation() {
		return 1.5393804F;

	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);

	}

	@Override
	protected void onDeathUpdate() {

		if (ModConfig.mobs.zombieCorpses) {
			++this.deathTime;

			if (this.deathTime == 20) {
				if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot()
						&& this.world.getGameRules().getBoolean("doMobLoot"))) {
					int i = this.getExperiencePoints(this.attackingPlayer);
					i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
					while (i > 0) {
						int j = EntityXPOrb.getXPSplit(i);
						i -= j;
						this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
					}
				}

				this.setDead();
				EntityLootableCorpse lootable = new EntityLootableCorpse(world);
				lootable.setOriginal(this);
				lootable.setPosition(posX, posY, posZ);
				isDead = true;
				if (!world.isRemote) {
					ItemUtils.fillWithLoot(lootable.getInventory(), lootTable, world, rand);
					world.spawnEntity(lootable);
				}

				for (int k = 0; k < 20; ++k) {
					double d2 = this.rand.nextGaussian() * 0.02D;
					double d0 = this.rand.nextGaussian() * 0.02D;
					double d1 = this.rand.nextGaussian() * 0.02D;
					this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
							this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
							this.posY + (double) (this.rand.nextFloat() * this.height),
							this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2,
							d0, d1);
				}
			}
		} else {
			super.onDeathUpdate();
		}
	}

	@Override
	public Vec3d corpseRotation() {
		return new Vec3d(1.5, 0.6, 1.2);
	}

	@Override
	public Vec3d corpseTranslation() {
		return new Vec3d(-0.9, 0, -0.5);
	}

	@Override
	public boolean customCoprseTransform() {
		return true;
	}
}
