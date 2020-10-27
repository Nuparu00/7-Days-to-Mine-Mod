package com.nuparu.sevendaystomine.entity;

import java.util.UUID;

import com.nuparu.sevendaystomine.entity.ai.EntityAIBreakBlock;
import com.nuparu.sevendaystomine.entity.ai.EntityAIInfectedAttack;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.horde.BloodmoonHorde;
import com.nuparu.sevendaystomine.world.horde.Horde;
import com.nuparu.sevendaystomine.world.horde.HordeSavedData;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class EntityZombieBase extends EntityMob {

	public final static UUID NIGHT_BOOST_ID = UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a");
	public final static UUID BLOODMOON_SPEED_BOOST_ID = UUID.fromString("2ca21e76-c020-11e7-abc4-cec278b6b50a");
	public final static UUID BLOODMOON_RANGE_BOOST_ID = UUID.fromString("4340be6a-c8bf-11e7-a80b-cec278b6b50a");
	public final static UUID BLOODMOON_DAMAGE_BOOST_ID = UUID.fromString("dc7572f6-d05f-4df6-afee-7fa78046ec54");
	public final static UUID BLOODMOON_ARMOR_BOOST_ID = UUID.fromString("b859cf4a-b7cd-486f-9b59-ebabfdd0985e");

	IAttributeInstance speed;
	IAttributeInstance range;
	IAttributeInstance attack;
	IAttributeInstance armor;

	public Horde horde;

	public EntityZombieBase(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
	}

	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIInfectedAttack(this, 1.0D, false));
		this.tasks.addTask(1, new EntityAIBreakBlock(this));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.applyEntityAI();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void applyEntityAI() {
		this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityHuman.class, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		speed = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		range = getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
		attack = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		armor = getEntityAttribute(SharedMonsterAttributes.ARMOR);

		range.setBaseValue(64.0D);
		speed.setBaseValue(0.175D);
		attack.setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D);
		armor.setBaseValue(0.0D);
	}

	protected void entityInit() {
		super.entityInit();

	}

	public Vec3d getPositionEyesServer(float partialTicks) {
		if (partialTicks == 1.0F) {
			return new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ);
		} else {
			double d0 = this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks;
			double d1 = this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks
					+ (double) this.getEyeHeight();
			double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks;
			return new Vec3d(d0, d1, d2);
		}
	}

	public RayTraceResult rayTraceServer(double blockReachDistance, float partialTicks) {
		Vec3d vec3 = this.getPositionEyesServer(partialTicks);
		Vec3d vec31 = this.getLook(partialTicks);
		Vec3d vec32 = vec3.addVector(vec31.x * blockReachDistance, vec31.y * blockReachDistance,
				vec31.z * blockReachDistance);
		return this.world.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		IAttributeInstance speed = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		IAttributeInstance range = getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
		IAttributeInstance attack = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		IAttributeInstance armor = getEntityAttribute(SharedMonsterAttributes.ARMOR);

		if (!this.world.isRemote) {
			if (Utils.isBloodmoon(world)) {

				if (speed.getModifier(BLOODMOON_SPEED_BOOST_ID) == null) {
					AttributeModifier speedModifier = new AttributeModifier(BLOODMOON_SPEED_BOOST_ID,
							"BLOODMOON_SPEED_BOOST", 0.2f, 2);
					speed.applyModifier(speedModifier);
				}
				if (range.getModifier(BLOODMOON_RANGE_BOOST_ID) == null) {
					AttributeModifier rangeModifier = new AttributeModifier(BLOODMOON_RANGE_BOOST_ID,
							"BLOODMOON_RANGE_BOOST", 0.5f, 2);
					range.applyModifier(rangeModifier);
				}

				if (attack.getModifier(BLOODMOON_DAMAGE_BOOST_ID) == null) {
					AttributeModifier rangeModifier = new AttributeModifier(BLOODMOON_DAMAGE_BOOST_ID,
							"BLOODMOON_DAMAGE_BOOST", 0.5f, 2);
					attack.applyModifier(rangeModifier);
				}

				if (armor.getModifier(BLOODMOON_ARMOR_BOOST_ID) == null) {
					AttributeModifier rangeModifier = new AttributeModifier(BLOODMOON_ARMOR_BOOST_ID,
							"BLOODMOON_ARMOR_BOOST", 4f, 0);
					armor.applyModifier(rangeModifier);
				}

			} else {
				if (speed.getModifier(BLOODMOON_SPEED_BOOST_ID) != null) {
					speed.removeModifier(speed.getModifier(BLOODMOON_SPEED_BOOST_ID));
				}
				if (range.getModifier(BLOODMOON_RANGE_BOOST_ID) != null) {
					range.removeModifier(range.getModifier(BLOODMOON_RANGE_BOOST_ID));
				}

				if (armor.getModifier(BLOODMOON_ARMOR_BOOST_ID) != null) {
					armor.removeModifier(armor.getModifier(BLOODMOON_ARMOR_BOOST_ID));
				}

				if (attack.getModifier(BLOODMOON_DAMAGE_BOOST_ID) != null) {
					attack.removeModifier(attack.getModifier(BLOODMOON_DAMAGE_BOOST_ID));
				}
			}
			BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
			int i = this.world.getLight(blockpos, true);
			if (i >= 10) {

				if (speed.getModifier(NIGHT_BOOST_ID) != null) {
					speed.removeModifier(speed.getModifier(NIGHT_BOOST_ID));
				}
			} else {

				if (speed.getModifier(NIGHT_BOOST_ID) == null) {
					AttributeModifier speedModifier = new AttributeModifier(NIGHT_BOOST_ID, "NIGHT_BOOST", 0.75f, 2);
					speed.applyModifier(speedModifier);
				}

			}
		}

	}

	@Override
	public float getEyeHeight() {
		return 1.74F;
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (horde != null) {
			horde.onZombieKill(this);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (horde == null && compound.hasKey("horde", Constants.NBT.TAG_COMPOUND)) {
			horde = HordeSavedData.get(world).getHordeByUUID(NBTUtil.getUUIDFromTag(compound.getCompoundTag("horde")));
			if (horde != null) {
				horde.addZombie(this);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		if (horde != null) {
			compound.setTag("horde", NBTUtil.createUUIDTag(horde.uuid));
		}
		return compound;
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		if (horde != null) {
			horde.onPlayerStartTacking(player, this);
		}
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		if (horde != null) {
			horde.onPlayerStopTacking(player, this);
		}
	}

	@Override
	protected boolean canDespawn() {
		return (horde == null || !Utils.isBloodmoon(world));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			if (horde != null) {
				horde.onZombieKill(this);
			}
			this.setDead();
		}
	}

	@Override
	protected boolean isValidLightLevel() {
		return true;
	}

	public Vec3d corpseRotation() {
		return Vec3d.ZERO;
	}
	
	public Vec3d corpseTranslation() {
		return Vec3d.ZERO;
	}
	
	public boolean customCoprseTransform() {
		return false;
	}
	
	protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
}
