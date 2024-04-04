package nuparu.sevendaystomine.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.dialogue.Dialogues;

public class EntitySoldier extends EntityHuman implements IMerchant, IRangedAttackMob {
	private final EntityAIAttackRanged rangedAttack = new EntityAIAttackRanged(this, 1.0D, 20, 15.0F);
	private final EntityAIAttackMelee attackOnCollide = new EntityAIAttackMelee(this, 1.2D, false);

	@Nullable
	private EntityPlayer buyingPlayer;

	public EntitySoldier(World worldIn) {
		super(worldIn);
		setCombatTask();
		this.experienceValue = 10;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.setDialogues(Dialogues.EMPTY);
		this.setCurrentDialogue("");
		this.setSex(this.rand.nextInt(2) == 0 ? EnumSex.MALE.getName() : EnumSex.FEMALE.getName());
		this.setTexture(SevenDaysToMine.MODID + ":textures/entity/human/soldier_male.png");
	}

	public void setCombatTask() {
		if (this.world != null && !this.world.isRemote) {
			this.tasks.removeTask(this.attackOnCollide);
			this.tasks.removeTask(this.rangedAttack);
			ItemStack itemstack = this.getHeldItemMainhand();

			if (itemstack.getItem() instanceof ItemGun) {
				this.tasks.addTask(4, this.rangedAttack);
			} else {
				this.tasks.addTask(4, this.attackOnCollide);
			}
		}
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityZombieBase>(this, EntityZombieBase.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityBandit>(this, EntityBandit.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityCreeper>(this, EntityCreeper.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntitySpider>(this, EntitySpider.class, true));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24000000149011612D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D* ModConfig.players.balanceModifier);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4D);
	}
	
	@Override
	public void setCustomer(EntityPlayer player) {
		this.buyingPlayer = player;
	}

	@Override
	public EntityPlayer getCustomer() {
		return buyingPlayer;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player) {
		return null;
	}

	@Override
	public void setRecipes(MerchantRecipeList recipeList) {

	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {

	}

	@Override
	public void verifySellingItem(ItemStack stack) {

	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public BlockPos getPos() {
		return new BlockPos(this);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		ItemStack stack = ItemStack.EMPTY;
		stack = new ItemStack(ModItems.PISTOL);

		if (rand.nextInt(10) == 0) {
			stack = new ItemStack(ModItems.SHOTGUN);
		} else if (rand.nextInt(10) == 0) {
			stack = new ItemStack(ModItems.MP5);
		}

		ItemQuality.setQualityForStack(stack, MathUtils.getIntInRange(rand, 50, 300));

		this.setHeldItem(EnumHand.MAIN_HAND, stack);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setCombatTask();
		return livingdata;
	}

	@Override
	public boolean canTalkTo(EntityPlayer player) {
		return false;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		if (!this.getHeldItemMainhand().isEmpty()) {
			if (this.getHeldItemMainhand().getItem() instanceof ItemGun) {
				ItemGun gun = (ItemGun) this.getHeldItemMainhand().getItem();
				for (int i = 0; i < gun.getProjectiles(); i++) {
					EntityShot shot = new EntityShot(this.world, this);
					double d0 = target.posX - this.posX;
					double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - shot.posY;
					double d2 = target.posZ - this.posZ;
					double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
					shot.setDamage(gun.getFinalDamage(getHeldItemMainhand()));

					shot.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
							(float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
					if (!this.world.isRemote) {
						this.world.spawnEntity(shot);
					}
				}
				this.playSound(gun.getShotSound(), gun.getShotSoundVolume(), gun.getShotSoundPitch());
				this.swingArm(EnumHand.MAIN_HAND);
			}
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO Auto-generated method stub

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setCombatTask();
	}

}
