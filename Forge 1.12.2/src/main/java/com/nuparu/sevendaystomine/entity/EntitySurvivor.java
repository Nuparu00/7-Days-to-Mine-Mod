package com.nuparu.sevendaystomine.entity;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntitySurvivor extends EntityHuman implements IMerchant {

	@Nullable
	private EntityPlayer buyingPlayer;

	public EntitySurvivor(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		// setDialogues(DialoguesRegistry.INSTANCE.getByName("test.dialogues"));
		this.setDialogues(DialoguesRegistry.INSTANCE.getByName("survivor.generic"));
		this.setCurrentDialogue("survivor.generic.a");
		this.setSex(this.rand.nextInt(2) == 0 ? EnumSex.MALE.getName() : EnumSex.FEMALE.getName());
		if(this.getSexAsEnum() == EnumSex.MALE) {
			this.setTexture(SevenDaysToMine.MODID+":textures/entity/human/survivor_generic_male.png");
		} else if(this.getSexAsEnum() == EnumSex.FEMALE) {
			this.setTexture(SevenDaysToMine.MODID+":textures/entity/human/survivor_generic_female.png");
		}
	}
	
	@Override
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
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

}
