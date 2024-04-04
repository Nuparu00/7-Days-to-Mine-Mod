package nuparu.sevendaystomine.entity;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;
import nuparu.sevendaystomine.world.gen.city.Settlement;

public class EntitySurvivor extends EntityHuman implements IMerchant {
	private static final DataParameter<String> CARRIER = EntityDataManager.<String>createKey(EntitySurvivor.class,
			DataSerializers.STRING);

	@Nullable
	private EntityPlayer buyingPlayer;
	private Settlement settlement;
	private MerchantRecipeList buyingList;

	public EntitySurvivor(World worldIn) {
		super(worldIn);
		this.experienceValue = 4;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CARRIER, String.valueOf(EnumCarrier.NONE.getName()));
		// setDialogues(DialoguesRegistry.INSTANCE.getByName("test.dialogues"));
		EnumCarrier carrier = EnumCarrier.values()[1 + world.rand.nextInt(EnumCarrier.values().length - 1)];
		this.setCarrier(carrier);

		this.setDialogues(DialoguesRegistry.INSTANCE.getByName("survivor." + carrier.getName()));
		this.setCurrentDialogue("survivor." + carrier.getName() + ".a");

		EnumSex sex = this.rand.nextInt(2) == 0 ? EnumSex.MALE : EnumSex.FEMALE;
		this.setSex(sex.getName());
		this.setTexture(
				SevenDaysToMine.MODID + ":textures/entity/human/" + carrier.getName() + "_" + sex.getName() + ".png");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true,
				new Predicate<EntityLiving>() {
					@Override
					public boolean apply(@Nullable EntityLiving living) {
						if (living == null) {
							return false;
						}
						return (living instanceof EntityZombieBase);

					}
				}));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.2D, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25000000149011612D);
	}

	public void setCarrier(String carrier) {
		this.dataManager.set(CARRIER, carrier);
	}

	public void setCarrier(EnumCarrier carrier) {
		setCarrier(carrier.getName());
		this.setTexture(SevenDaysToMine.MODID + ":textures/entity/human/" + carrier.getName() + "_" + getSex() + ".png");
	}

	public String getCarrier() {
		return this.dataManager.get(CARRIER);
	}

	public EnumCarrier getCarrierAsEnum() {
		return EnumCarrier.getByName(getCarrier());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("carrier", Constants.NBT.TAG_STRING)) {
			setCarrier(compound.getString("carrier"));
		}
		if (compound.hasKey("Offers", 10)) {
			NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
			this.buyingList = new MerchantRecipeList(nbttagcompound);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (!getCarrier().isEmpty()) {
			compound.setString("carrier", getCarrier());
		}
		if (this.buyingList != null) {
			compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
		}
		return compound;
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
	@Nullable
	public MerchantRecipeList getRecipes(EntityPlayer player) {
		if (this.buyingList == null) {
			this.populateBuyingList();
		}

		return net.minecraftforge.event.ForgeEventFactory.listTradeOffers(this, player, buyingList);
	}

	private void populateBuyingList() {
		buyingList = new MerchantRecipeList();
		switch (getCarrierAsEnum()) {
		case ELECTRICIAN: {
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 3), ModItems.WIRE));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 12), ModItems.CIRCUIT));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 20), ModItems.LINK_TOOL));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 30), ModItems.VOLTMETER));
			break;
		}
		case DOCTOR: {
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 3), ModItems.BANDAGE));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 8), ModItems.BANDAGE_ADVANCED));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 5), ModItems.BLOOD_BAG));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 16), ModItems.FIRST_AID_KIT));
			break;
		}
		case FARMER: {
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 3), ModItems.BANDAGE));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 8), ModItems.BANDAGE_ADVANCED));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 5), ModItems.BLOOD_BAG));
			buyingList.add(new MerchantRecipe(new ItemStack(ModItems.CENT, 16), ModItems.FIRST_AID_KIT));
			break;
		}
		case MINER: {
			buyingList.add(getTradeWithQuality(new ItemStack(ModItems.BRONZE_PICKAXE), 1, 600, 10, 10, world.rand));
			buyingList.add(getTradeWithQuality(new ItemStack(ModItems.IRON_PICKAXE), 1, 600, 5, 10, world.rand));
			break;
		}
		}

	}

	@Override
	public void setRecipes(MerchantRecipeList recipeList) {

	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
		this.livingSoundTime = -this.getTalkInterval();
		this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
		int i = 3 + this.rand.nextInt(4);

		if (recipe.getRewardsExp()) {
			this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
		}
	}

	@Override
	public void verifySellingItem(ItemStack stack) {
		if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES,
					this.getSoundVolume(), this.getSoundPitch());
		}
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
	public ITextComponent getDisplayName() {
		Team team = this.getTeam();
		String s = this.getCustomNameTag();

		if (s != null && !s.isEmpty()) {
			TextComponentString textcomponentstring = new TextComponentString(
					ScorePlayerTeam.formatPlayerName(team, s));
			textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
			textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
			return textcomponentstring;
		} else {
			if (this.buyingList == null) {
				this.populateBuyingList();
			}

			String s1 = this.getCarrier();

			ITextComponent itextcomponent = new TextComponentTranslation("entity.survivor." + s1, new Object[0]);
			itextcomponent.getStyle().setHoverEvent(this.getHoverEvent());
			itextcomponent.getStyle().setInsertion(this.getCachedUniqueIdString());

			if (team != null) {
				itextcomponent.getStyle().setColor(team.getColor());
			}

			return itextcomponent;

		}
	}

	@Override
	public void onDialogue(String dialogue, EntityPlayer player) {
		super.onDialogue(dialogue, player);
		if (dialogue.equals("survivor.trade")) {
			setCustomer(player);
			player.displayVillagerTradeGui(this);
		}
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		return super.processInteract(player, hand);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		if (world.rand.nextInt(2) != 0)
			return;
		ItemStack stack = ItemStack.EMPTY;
		EnumCarrier carrier = this.getCarrierAsEnum();
		switch (carrier) {
		case FARMER:
			stack = new ItemStack(Items.IRON_HOE);
			break;
		case MINER:
			stack = ItemQuality.setQualityForStack(new ItemStack(ModItems.IRON_PICKAXE), 100);
			break;
		}
		this.setHeldItem(EnumHand.MAIN_HAND, stack);
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setEquipmentBasedOnDifficulty(difficulty);
		return livingdata;
	}

	public enum EnumCarrier {
		NONE("none"), DOCTOR("doctor"), FARMER("farmer"), ELECTRICIAN("electrician"), MINER("miner");

		private String name;

		EnumCarrier(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static EnumCarrier getByName(String name) {
			for (EnumCarrier carrier : EnumCarrier.values()) {
				if (carrier.name.equals(name)) {
					return carrier;
				}
			}
			return NONE;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	public static MerchantRecipe getTradeWithQuality(ItemStack buy1, ItemStack buy2, ItemStack sell, int minQuality,
			int maxQuality, Random rand) {
		ItemQuality.setQualityForStack(sell, rand.nextInt(maxQuality - minQuality) + minQuality);
		return new MerchantRecipe(buy1, buy2, sell);
	}

	public static MerchantRecipe getTradeWithQuality(ItemStack sell, int minQuality, int maxQuality, int levelsPerCent,
			int basePrice, Random rand) {
		int quality = rand.nextInt(maxQuality - minQuality) + minQuality;
		ItemQuality.setQualityForStack(sell, quality);
		int amount = basePrice + quality / levelsPerCent;
		return new MerchantRecipe(new ItemStack(ModItems.CENT, MathUtils.clamp(amount, 1, 64)),
				amount > 64 ? new ItemStack(ModItems.CENT, MathUtils.clamp(amount - 64, 1, 64)) : ItemStack.EMPTY,
				sell);
	}

}
