package com.nuparu.sevendaystomine.entity;

import java.util.UUID;

import com.nuparu.sevendaystomine.config.ModConfig;
import com.nuparu.sevendaystomine.entity.ai.EntityAIBreakBlock;
import com.nuparu.sevendaystomine.entity.ai.EntityAIInfectedAttack;
import com.nuparu.sevendaystomine.init.ModLootTables;
import com.nuparu.sevendaystomine.item.ItemQualityPickaxe;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.world.horde.BloodmoonHorde;
import com.nuparu.sevendaystomine.world.horde.Horde;
import com.nuparu.sevendaystomine.world.horde.HordeSavedData;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;

public class EntityZombieBase extends EntityMob {

	public boolean nightRun = true;

	public final static UUID NIGHT_BOOST_ID = UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a");
	public final static UUID BLOODMOON_SPEED_BOOST_ID = UUID.fromString("2ca21e76-c020-11e7-abc4-cec278b6b50a");
	public final static UUID BLOODMOON_RANGE_BOOST_ID = UUID.fromString("4340be6a-c8bf-11e7-a80b-cec278b6b50a");
	public final static UUID BLOODMOON_DAMAGE_BOOST_ID = UUID.fromString("dc7572f6-d05f-4df6-afee-7fa78046ec54");
	public final static UUID BLOODMOON_ARMOR_BOOST_ID = UUID.fromString("b859cf4a-b7cd-486f-9b59-ebabfdd0985e");

	public IAttributeInstance speed;
	public IAttributeInstance range;
	public IAttributeInstance attack;
	public IAttributeInstance armor;

	public static final AttributeModifier NIGHT_SPEED_BOOST = new AttributeModifier(
			UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a"), "nightSpeedBoost", 0.75f, 2);
	public static final AttributeModifier BLOODMOON_SPEED_BOOST = new AttributeModifier(
			UUID.fromString("2ca21e76-c020-11e7-abc4-cec278b6b50a"), "bloodmoonSpeedBoost", 0.2f, 2);
	public static final AttributeModifier BLOODMOON_DAMAGE_BOOST = new AttributeModifier(
			UUID.fromString("dc7572f6-d05f-4df6-afee-7fa78046ec54"), "bloodmoonDamageBoost", 0.5f, 2);
	public static final AttributeModifier BLOODMOON_RANGE_BOOST = new AttributeModifier(
			UUID.fromString("4340be6a-c8bf-11e7-a80b-cec278b6b50a"), "bloodmoonRangeBoost", 0.5f, 2);
	public static final AttributeModifier BLOODMOON_ARMOR_BOOST = new AttributeModifier(
			UUID.fromString("b859cf4a-b7cd-486f-9b59-ebabfdd0985e"), "bloodmoonArmorBoost", 4f, 0);

	public Horde horde;

	public ResourceLocation lootTable = ModLootTables.ZOMBIE_GENERIC;

	public EntityZombieBase(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
		this.experienceValue = 15;
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
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityHuman.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
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
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60D);
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

		if (!this.world.isRemote) {
			if (Utils.isBloodmoonProper(world)) {
				if (!speed.hasModifier(BLOODMOON_SPEED_BOOST)) {
					speed.applyModifier(BLOODMOON_SPEED_BOOST);
				}
				if (!range.hasModifier(BLOODMOON_RANGE_BOOST)) {
					range.applyModifier(BLOODMOON_RANGE_BOOST);
				}
				if (!armor.hasModifier(BLOODMOON_ARMOR_BOOST)) {
					armor.applyModifier(BLOODMOON_ARMOR_BOOST);
				}
				if (!attack.hasModifier(BLOODMOON_DAMAGE_BOOST)) {
					attack.applyModifier(BLOODMOON_DAMAGE_BOOST);
				}
			} else {
				if (speed.hasModifier(BLOODMOON_SPEED_BOOST)) {
					speed.removeModifier(BLOODMOON_SPEED_BOOST);
				}
				if (range.hasModifier(BLOODMOON_RANGE_BOOST)) {
					range.removeModifier(BLOODMOON_RANGE_BOOST);
				}
				if (armor.hasModifier(BLOODMOON_ARMOR_BOOST)) {
					armor.removeModifier(BLOODMOON_ARMOR_BOOST);
				}
				if (attack.hasModifier(BLOODMOON_DAMAGE_BOOST)) {
					attack.removeModifier(BLOODMOON_DAMAGE_BOOST);
				}
			}

			if (nightRun) {
				BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
				int light = this.world.getLight(pos, true);
				if (ModConfig.mobs.zombiesRunMode == 2 || (ModConfig.mobs.zombiesRunMode == 1 && light < 10)) {
					if (!speed.hasModifier(NIGHT_SPEED_BOOST)) {
						speed.applyModifier(NIGHT_SPEED_BOOST);
					}
				} else {
					if (speed.hasModifier(NIGHT_SPEED_BOOST)) {
						speed.removeModifier(NIGHT_SPEED_BOOST);
					}
				}
			}
		}
	}
	
	public float getBlockBreakSpeed(IBlockState state, BlockPos pos) {
		Block block = state.getBlock();
		
		float f = 0.1f;

		if (this.isPotionActive(MobEffects.HASTE)) {
			f *= 1.0F + (float) (this.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
		}

		if (this.isPotionActive(MobEffects.MINING_FATIGUE)) {
			float f1 = 1.0F;

			switch (this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
			case 0:
				f1 = 0.3F;
				break;
			case 1:
				f1 = 0.09F;
				break;
			case 2:
				f1 = 0.0027F;
				break;
			case 3:
			default:
				f1 = 8.1E-4F;
			}

			f *= f1;
		}

		if (this.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
			f /= 5.0F;
		}

		if (!this.onGround) {
			f /= 5.0F;
		}
		
		if(Utils.isBloodmoonProper(this.world)) {
			f*=2;
		}
		
		if (speed.hasModifier(BLOODMOON_SPEED_BOOST)) {
			f*=1.5f;
		}
		
		
		ItemStack stack = this.getHeldItem(EnumHand.MAIN_HAND);
		if(!stack.isEmpty()) {
			if(ForgeHooks.isToolEffective(world, pos, stack)) {
				f*=2.5f;
			}
		}

		return f;
	}

	@Override
	public float getEyeHeight() {
		return 1.74F;
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (!this.world.isRemote && horde != null) {
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

	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIE_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.ENTITY_ZOMBIE_STEP;
	}

	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.isExplosion()) {
			amount *= 2;
		}
		return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
	}
}
