package com.nuparu.sevendaystomine.entity;

import java.util.UUID;

import com.nuparu.sevendaystomine.entity.ai.EntityAIBreakBlock;
import com.nuparu.sevendaystomine.entity.ai.EntityAIInfectedAttack;

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
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityZombieBase extends EntityMob {

	public final static UUID NIGHT_BOOST_ID = UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a");
	public final static UUID LIGHT_BOOST_ID = UUID.fromString("2ca21a02-c020-11e7-abc4-cec278b6b50a");
	public final static UUID BLOODMOON_SPEED_BOOST_ID = UUID.fromString("2ca21e76-c020-11e7-abc4-cec278b6b50a");
	public final static UUID BLOODMOON_RANGE_BOOST_ID = UUID.fromString("4340be6a-c8bf-11e7-a80b-cec278b6b50a");
	public final static UUID BLOODMOON_DAMAGE_BOOST_ID = UUID.fromString("dc7572f6-d05f-4df6-afee-7fa78046ec54");
	public final static UUID BLOODMOON_ARMOR_BOOST_ID = UUID.fromString("b859cf4a-b7cd-486f-9b59-ebabfdd0985e");
	
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
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));	
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.08D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(500D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
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
		if (!this.world.isRemote) {
			int day = (int) Math.round(this.world.getWorldTime() / 24000);
			if (day % 7 == 0) {
				if (day != 0) {

					if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
							.getModifier(BLOODMOON_SPEED_BOOST_ID) == null) {
						AttributeModifier speedModifier = new AttributeModifier(BLOODMOON_SPEED_BOOST_ID, "BLOODMOON_SPEED_BOOST", 0.15f, 0);
						this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(speedModifier);
					}
					if (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE)
							.getModifier(BLOODMOON_RANGE_BOOST_ID) == null) {
						AttributeModifier rangeModifier = new AttributeModifier(BLOODMOON_RANGE_BOOST_ID, "BLOODMOON_RANGE_BOOST", 0.25f, 0);
						this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(rangeModifier);
					}
					
					if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
							.getModifier(BLOODMOON_DAMAGE_BOOST_ID) == null) {
						AttributeModifier rangeModifier = new AttributeModifier(BLOODMOON_DAMAGE_BOOST_ID, "BLOODMOON_DAMAGE_BOOST", 0.5f, 0);
						this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(rangeModifier);
					}
					
					if (this.getEntityAttribute(SharedMonsterAttributes.ARMOR)
							.getModifier(BLOODMOON_ARMOR_BOOST_ID) == null) {
						AttributeModifier rangeModifier = new AttributeModifier(BLOODMOON_ARMOR_BOOST_ID, "BLOODMOON_ARMOR_BOOST", 0.5f, 0);
						this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(rangeModifier);
					}

				} else {
					if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
							.getModifier(BLOODMOON_SPEED_BOOST_ID) != null) {
						this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(this
								.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(BLOODMOON_SPEED_BOOST_ID));
					}
					if (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE)
							.getModifier(BLOODMOON_RANGE_BOOST_ID) != null) {
						this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).removeModifier(this
								.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getModifier(BLOODMOON_RANGE_BOOST_ID));
					}
					
					if (this.getEntityAttribute(SharedMonsterAttributes.ARMOR)
							.getModifier(BLOODMOON_ARMOR_BOOST_ID) != null) {
						this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(this
								.getEntityAttribute(SharedMonsterAttributes.ARMOR).getModifier(BLOODMOON_ARMOR_BOOST_ID));
					}
					
					if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
							.getModifier(BLOODMOON_DAMAGE_BOOST_ID) != null) {
						this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(this
								.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getModifier(BLOODMOON_DAMAGE_BOOST_ID));
					}
				}
			} else {
				if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
						.getModifier(BLOODMOON_SPEED_BOOST_ID) != null) {
					this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(this
							.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(BLOODMOON_SPEED_BOOST_ID));
				}
				if (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE)
						.getModifier(BLOODMOON_RANGE_BOOST_ID) != null) {
					this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).removeModifier(this
							.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getModifier(BLOODMOON_RANGE_BOOST_ID));
				}
				
				if (this.getEntityAttribute(SharedMonsterAttributes.ARMOR)
						.getModifier(BLOODMOON_ARMOR_BOOST_ID) != null) {
					this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(this
							.getEntityAttribute(SharedMonsterAttributes.ARMOR).getModifier(BLOODMOON_ARMOR_BOOST_ID));
				}
				
				if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
						.getModifier(BLOODMOON_DAMAGE_BOOST_ID) != null) {
					this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(this
							.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getModifier(BLOODMOON_DAMAGE_BOOST_ID));
				}
			}
			BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
			int i = this.world.getLightFromNeighbors(blockpos);

			if (this.world.isThundering()) {
				int j = this.world.getSkylightSubtracted();
				this.world.setSkylightSubtracted(10);
				i = this.world.getLightFromNeighbors(blockpos);
				this.world.setSkylightSubtracted(j);
			}

			if (i >= 10) {

				if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
						.getModifier(NIGHT_BOOST_ID) != null) {
					this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(
							this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(NIGHT_BOOST_ID));
				}
				AttributeModifier speedModifier = new AttributeModifier(LIGHT_BOOST_ID, "LIGHT_BOOST", 0.45f, 1);
				if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
						.getModifier(LIGHT_BOOST_ID) == null) {
					this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(speedModifier);
				}

			} else {

				if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
						.getModifier(LIGHT_BOOST_ID) != null) {
					this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(
							this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(LIGHT_BOOST_ID));
				}
				if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
						.getModifier(NIGHT_BOOST_ID) == null) {
					AttributeModifier speedModifier = new AttributeModifier(NIGHT_BOOST_ID, "NIGHT_BOOST", 1.4f, 1);
					this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(speedModifier);
				}

			}
		}

	}

	@Override
	public float getEyeHeight() {
		return 1.74F;
	}
}
