package com.nuparu.sevendaystomine.entity;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityZombieWolf extends EntityZombieBase {

	private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.<Integer>createKey(EntityWolf.class,
			DataSerializers.VARINT);

	/** true is the wolf is wet else false */
	private boolean isWet;
	/** True if the wolf is shaking else False */
	private boolean isShaking;
	/** This time increases while wolf is shaking and emitting water particles. */
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;

	public EntityZombieWolf(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 0.85F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		range.setBaseValue(64.0D);
		speed.setBaseValue(0.25000001192092896D);
		attack.setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60D);
		armor.setBaseValue(0.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(COLLAR_COLOR, Integer.valueOf(EnumDyeColor.RED.getDyeDamage()));
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
	}

	@Override
	protected SoundEvent getAmbientSound() {

		return SoundEvents.ENTITY_WOLF_GROWL;

	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_WOLF_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WOLF_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableList.ENTITIES_WOLF;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setByte("CollarColor", (byte) this.getCollarColor().getDyeDamage());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		if (compound.hasKey("CollarColor", 99)) {
			this.setCollarColor(EnumDyeColor.byDyeDamage(compound.getByte("CollarColor")));
		}
	}

	public EnumDyeColor getCollarColor() {
		return EnumDyeColor.byDyeDamage(((Integer) this.dataManager.get(COLLAR_COLOR)).intValue() & 15);
	}

	public void setCollarColor(EnumDyeColor collarcolor) {
		this.dataManager.set(COLLAR_COLOR, Integer.valueOf(collarcolor.getDyeDamage()));
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
		EntityLootableCorpse lootable = new EntityLootableCorpse(world);
		lootable.setOriginal(this);
		lootable.setPosition(posX, posY, posZ);
		isDead = true;
		if (!world.isRemote) {
			world.spawnEntity(lootable);
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
