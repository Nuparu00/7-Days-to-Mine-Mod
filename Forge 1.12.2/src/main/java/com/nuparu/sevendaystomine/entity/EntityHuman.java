package com.nuparu.sevendaystomine.entity;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.events.HumanResponseEvent;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.AddSubtitleMessage;
import com.nuparu.sevendaystomine.util.Utils;
import com.nuparu.sevendaystomine.util.dialogue.Dialogue;
import com.nuparu.sevendaystomine.util.dialogue.Dialogues;
import com.nuparu.sevendaystomine.util.dialogue.DialoguesRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityHuman extends EntityCreature {

	//Should not be used outside of the Entity class - especially on client side!!
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
		this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ModItems.PISTOL));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
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

	public void onDialogue(String dialogue, EntityPlayer player) {
		Event event = new HumanResponseEvent(this, player, dialogue);
		if (!MinecraftForge.EVENT_BUS.post(event)) {
			if (!this.world.isRemote) {
				PacketManager.addSubtitle.sendTo(new AddSubtitleMessage(this, dialogue, 120), (EntityPlayerMP) player);
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
		compound.setString("dialogues", getDialogues().getKey().toString());
		if (getDialogues() != null) {
			compound.setString("dialogues", getDialogues().getKey().toString());
		}
		return compound;
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
		return SoundEvents.ENTITY_PLAYER_HURT;
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
