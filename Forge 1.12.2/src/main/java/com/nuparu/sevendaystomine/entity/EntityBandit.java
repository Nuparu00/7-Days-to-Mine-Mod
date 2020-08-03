package com.nuparu.sevendaystomine.entity;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.entity.EntityHuman.EnumSex;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityBandit extends EntityHuman implements IMerchant, IRangedAttackMob {
	private final EntityAIAttackRanged rangedAttack = new EntityAIAttackRanged(this, 1.0D, 20, 15.0F);

	@Nullable
	private EntityPlayer buyingPlayer;

	public EntityBandit(World worldIn) {
		super(worldIn);
		this.setCombatTask();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		// setDialogues(DialoguesRegistry.INSTANCE.getByName("test.dialogues"));
		this.setDialogues(DialoguesRegistry.INSTANCE.getByName("survivor.generic"));
		this.setCurrentDialogue("survivor.generic.a");
		this.setSex(this.rand.nextInt(2) == 0 ? EnumSex.MALE.getName() : EnumSex.FEMALE.getName());
		if(this.getSexAsEnum() == EnumSex.MALE) {
			this.setTexture(SevenDaysToMine.MODID + ":textures/entity/human/bandit_soldier_male.png");
		} else if(this.getSexAsEnum() == EnumSex.FEMALE) {
			this.setTexture(SevenDaysToMine.MODID+":textures/entity/human/survivor_generic_female.png");
		}
		/*
		 * if(this.getSexAsEnum() == EnumSex.MALE) {
		 * this.setTexture(SevenDaysToMine.MODID+
		 * ":textures/entity/human/survivor_generic_male.png"); } else
		 * if(this.getSexAsEnum() == EnumSex.FEMALE) {
		 * this.setTexture(SevenDaysToMine.MODID+
		 * ":textures/entity/human/survivor_generic_female.png"); }
		 */

	}

	public void setCombatTask() {
		/*
		 * this.tasks.removeTask(this.aiAttackOnCollide3);
		 * this.tasks.removeTask(this.aiArrowAttack); ItemStack itemstack =
		 * this.getHeldItem();
		 * 
		 * if (itemstack != null && itemstack.getItem() instanceof
		 * mcreator_gunItem.ItemGun) {
		 */
		System.out.println(rangedAttack == null);
		this.tasks.addTask(1, this.rangedAttack);
		/*
		 * } else { this.tasks.addTask(2, this.aiAttackOnCollide3); }
		 */
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
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
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
	}

	@Override
	public boolean canTalkTo(EntityPlayer player) {
		return false;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		if (!this.getHeldItemMainhand().isEmpty()) {
			if (this.getHeldItemMainhand().getItem() instanceof ItemGun) {

				EntityShot shot = new EntityShot(this.world, this);
				double d0 = target.posX - this.posX;
				double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - shot.posY;
				double d2 = target.posZ - this.posZ;
				double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
				shot.setDamage(
						((ItemGun) this.getHeldItemMainhand().getItem()).getDamage(getHeldItemMainhand()) + 15);

				shot.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
						(float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
				this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F,
						1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
				this.swingArm(EnumHand.MAIN_HAND);
				if (!this.world.isRemote) {
					this.world.spawnEntity(shot);
				}
			}
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO Auto-generated method stub

	}

}
