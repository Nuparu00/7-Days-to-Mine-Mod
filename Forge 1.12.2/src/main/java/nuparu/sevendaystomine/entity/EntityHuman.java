package nuparu.sevendaystomine.entity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.events.HumanResponseEvent;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.AddSubtitleMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.dialogue.Dialogues;
import nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

public abstract class EntityHuman extends EntityCreature {

	// Should not be used outside of the Entity class - especially on client side!!
	private EnumSex sex = EnumSex.UNKNOWN;
	private ResourceLocation res = new ResourceLocation("textures/entity/steve.png");

	public BlockPos bedLocation;

	public float renderOffsetX;
	@SideOnly(Side.CLIENT)
	public float renderOffsetY;
	public float renderOffsetZ;

	private static final DataParameter<Dialogues> DIALOGUES = EntityDataManager.<Dialogues>createKey(EntityHuman.class,
			Utils.DIALOGUES);

	private static final DataParameter<String> CURRENT_DIALOGUE = EntityDataManager.<String>createKey(EntityHuman.class,
			DataSerializers.STRING);

	private static final DataParameter<String> TEXTURE = EntityDataManager.<String>createKey(EntityHuman.class,
			DataSerializers.STRING);

	private static final DataParameter<String> SEX = EntityDataManager.<String>createKey(EntityHuman.class,
			DataSerializers.STRING);

	public EntityHuman(World worldIn) {
		super(worldIn);
		this.setSize(0.6F, 1.95F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(DIALOGUES, Dialogues.EMPTY);
		this.dataManager.register(CURRENT_DIALOGUE, String.valueOf("none"));
		this.dataManager.register(TEXTURE, String.valueOf("textures/entity/steve.png"));
		this.dataManager.register(SEX, String.valueOf(EnumSex.UNKNOWN.getName()));
	}
	
	@Override
	protected void initEntityAI()
    {
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
    }

	public void setDialogues(Dialogues dialogues) {
		this.dataManager.set(DIALOGUES, dialogues);
	}

	public Dialogues getDialogues() {
		return this.dataManager.get(DIALOGUES);
	}

	public void setCurrentDialogue(String dialogues) {
		this.dataManager.set(CURRENT_DIALOGUE, dialogues);
	}

	public String getCurrentDialogue() {
		return this.dataManager.get(CURRENT_DIALOGUE);
	}

	public void setTexture(String tex) {
		this.dataManager.set(TEXTURE, tex);
		this.setRes(new ResourceLocation(tex));
	}

	public String getTexture() {
		return this.dataManager.get(TEXTURE);
	}

	public void setSex(String sex) {
		this.dataManager.set(SEX, sex);
		this.sex = EnumSex.getByName(sex);
	}

	public String getSex() {
		return this.dataManager.get(SEX);
	}

	public EnumSex getSexAsEnum() {
		return this.sex;
	}

	public boolean canTalkTo(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (canTalkTo(player)) {
			player.openGui(SevenDaysToMine.instance, 15, world, this.getEntityId(), 0, 0);
			return true;
		}
		return super.processInteract(player, hand);
	}

	public void onDialogue(String dialogue, EntityPlayer player) {
		Event event = new HumanResponseEvent(this, player, dialogue);
		if (!MinecraftForge.EVENT_BUS.post(event)) {
			if (!this.world.isRemote) {
				PacketManager.addSubtitle.sendTo(new AddSubtitleMessage(this, dialogue, 40), (EntityPlayerMP) player);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("sex", Constants.NBT.TAG_STRING)) {
			setSex(compound.getString("sex"));
		}
		if (compound.hasKey("texture", Constants.NBT.TAG_STRING)) {
			setTexture(compound.getString("texture"));
		}
		if (compound.hasKey("current_dialogue", Constants.NBT.TAG_STRING)) {
			setCurrentDialogue(compound.getString("current_dialogue"));
		}
		if (compound.hasKey("dialogues", Constants.NBT.TAG_STRING)) {
			setDialogues(DialoguesRegistry.INSTANCE.getByRes(new ResourceLocation(compound.getString("dialogues"))));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (!getSex().isEmpty()) {
			compound.setString("sex", getSex());
		}
		if (!getTexture().isEmpty()) {
			compound.setString("texture", getTexture());
		}
		if (!getCurrentDialogue().isEmpty()) {
			compound.setString("current_dialogue", getCurrentDialogue());
		}
		if (getDialogues() != null) {
			compound.setString("dialogues", getDialogues().getKey().toString());
		}
		return compound;
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer))
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }
            this.swingArm(EnumHand.MAIN_HAND);

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }
	

	@SideOnly(Side.CLIENT)
	public float getBedOrientationInDegrees() {
		IBlockState state = this.bedLocation == null ? null : this.world.getBlockState(bedLocation);
		if (state != null && state.getBlock().isBed(state, world, bedLocation, this)) {
			EnumFacing enumfacing = state.getBlock().getBedDirection(state, world, bedLocation);

			switch (enumfacing) {
			case SOUTH:
				return 90.0F;
			case WEST:
				return 0.0F;
			case NORTH:
				return 270.0F;
			case EAST:
				return 180.0F;
			default:
				break;
			}
		}

		return 0.0F;
	}
	
	@Override
	public double getYOffset()
    {
        return -0.6D;
    }

	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_PLAYER_SWIM;
	}

	@Override
	protected SoundEvent getSplashSound() {
		return SoundEvents.ENTITY_PLAYER_SPLASH;
	}

	@Override
	@Nullable
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundHelper.HUMAN_HURT;
	}

	@Override
	@Nullable
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_GENERIC_DEATH;
	}

	@Override
	protected SoundEvent getFallSound(int heightIn) {
		return heightIn > 4 ? SoundEvents.ENTITY_PLAYER_BIG_FALL : SoundEvents.ENTITY_PLAYER_SMALL_FALL;
	}

	public ResourceLocation getRes() {
		return res;
	}

	public void setRes(ResourceLocation res) {
		this.res = res;
	}

	public enum EnumSex {
		MALE("male"), FEMALE("female"), UNKNOWN("unknown");

		private String name;

		EnumSex(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static EnumSex getByName(String name) {
			for (EnumSex sex : EnumSex.values()) {
				if (sex.name.equals(name)) {
					return sex;
				}
			}
			return UNKNOWN;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

}
