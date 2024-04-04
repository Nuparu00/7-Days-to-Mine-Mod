package nuparu.sevendaystomine.entity;

import java.util.Collection;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.entity.ai.EntityAIAttckRangedVomit;
import nuparu.sevendaystomine.entity.ai.EntityAIBreakBlock;
import nuparu.sevendaystomine.entity.ai.EntityAIInfectedAttack;
import nuparu.sevendaystomine.entity.ai.EntityAISwell;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.util.MathUtils;

public class EntityZombiePoliceman extends EntityBipedalZombie implements IRangedAttackMob {
	private final EntityAIAttckRangedVomit rangedAttack = new EntityAIAttckRangedVomit(this, 1.0D, 0, 15.0F);

	private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityBipedalZombie.class,
			DataSerializers.VARINT);
	private static final DataParameter<Integer> ANIMATION = EntityDataManager
			.<Integer>createKey(EntityBipedalZombie.class, DataSerializers.VARINT);

	private static final DataParameter<Integer> VOMIT_TIMER = EntityDataManager
			.<Integer>createKey(EntityBipedalZombie.class, DataSerializers.VARINT);

	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 30;

	private float explosionRadius = 3;
	
	public static final int RECHARGE_TIME = 100;

	public EntityZombiePoliceman(World worldIn) {
		super(worldIn);
		this.experienceValue = 20;
		this.lootTable = ModLootTables.ZOMBIE_POLICEMAN;
		this.tasks.addTask(1, this.rangedAttack);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(STATE, Integer.valueOf(-1));
		this.dataManager.register(ANIMATION, Integer.valueOf(0));
		this.dataManager.register(VOMIT_TIMER, Integer.valueOf(0));
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAIBreakBlock(this));
		this.tasks.addTask(2, new EntityAIInfectedAttack(this, 1.0D, false));
		this.tasks.addTask(0, new EntityAISwell(this));
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, false));
		this.targetTasks.addTask(3,
				new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityHuman>(this, EntityHuman.class, false));
		this.targetTasks.addTask(3,
				new EntityAINearestAttackableTarget<EntityIronGolem>(this, EntityIronGolem.class, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		range.setBaseValue(64.0D);
		speed.setBaseValue(0.15D);
		attack.setBaseValue(7.0D * ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120D* ModConfig.players.balanceModifier);
		armor.setBaseValue(3.0D);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		double d0 = target.posX - this.posX;
		double d2 = target.posZ - this.posZ;
		double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
		setAnimation(EnumAnimationState.VOMITING);
		for (int i = 0; i < MathUtils.getIntInRange(rand, 5, 15); i++) {
			EntityProjectileVomit entityVomit = new EntityProjectileVomit(this.world, this);
			double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - entityVomit.posY;

			entityVomit.setDamage(10);

			entityVomit.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
					(float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
			if (!this.world.isRemote) {
				this.world.spawnEntity(entityVomit);
			}

		}
		this.playSound(SoundEvents.ENTITY_ZOMBIE_HURT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.swingArm(EnumHand.MAIN_HAND);
		this.swingArm(EnumHand.OFF_HAND);
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO Auto-generated method stub

	}

	public int getState() {
		return ((Integer) this.dataManager.get(STATE)).intValue();
	}

	public void setState(int state) {
		this.dataManager.set(STATE, Integer.valueOf(state));
	}

	public EnumAnimationState getAnimation() {
		return EnumAnimationState.values()[((Integer) this.dataManager.get(ANIMATION)).intValue()];
	}

	/**
	 * Sets the state , -1 to idle and 1 to be 'in fuse'
	 */
	public void setAnimation(EnumAnimationState anim) {
		this.dataManager.set(ANIMATION, anim.ordinal());
	}

	@SideOnly(Side.CLIENT)
	public float getFlashIntensity(float p_70831_1_) {
		return ((float) this.lastActiveTime + (float) (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_)
				/ (float) (this.fuseTime - 2);
	}

	@Override
	public void onUpdate() {
		if (this.isEntityAlive()) {
			if (getVomitTimer() > 0) {
				if (getVomitTimer() < 55) {
					setAnimation(EnumAnimationState.DEFAULT);
				}
				setVomitTimer(getVomitTimer() - 1);
			}
			this.lastActiveTime = this.timeSinceIgnited;

			int i = this.getState();

			if (i > 0 && this.timeSinceIgnited == 0) {
				this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;

			if (this.timeSinceIgnited < 0) {
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime) {
				this.timeSinceIgnited = this.fuseTime;
				this.explode();
			}
		}

		super.onUpdate();
	}

	private void explode() {
		if (!this.world.isRemote) {
			boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this);
			float f = 1f;
			this.dead = true;
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius * f, flag);
			this.setDead();
			this.spawnLingeringCloud();
		}
	}

	private void spawnLingeringCloud() {
		Collection<PotionEffect> collection = this.getActivePotionEffects();

		if (!collection.isEmpty()) {
			EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY,
					this.posZ);
			entityareaeffectcloud.setRadius(2.5F);
			entityareaeffectcloud.setRadiusOnUse(-0.5F);
			entityareaeffectcloud.setWaitTime(10);
			entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
			entityareaeffectcloud
					.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());

			for (PotionEffect potioneffect : collection) {
				entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
			}

			this.world.spawnEntity(entityareaeffectcloud);
		}
	}

	public int getVomitTimer() {

		return this.dataManager.get(VOMIT_TIMER);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
	}

	public void setVomitTimer(int vomitTimer) {
		this.dataManager.set(VOMIT_TIMER, vomitTimer);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("vomit_timer", Constants.NBT.TAG_INT)) {
			setVomitTimer(compound.getInteger("vomit_timer"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("vomit_timer", getVomitTimer());

		return compound;
	}

	public enum EnumAnimationState {
		DEFAULT, VOMITING
	}

}
