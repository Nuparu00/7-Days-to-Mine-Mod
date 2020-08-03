package com.nuparu.sevendaystomine.entity;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nullable;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.init.ModBlocks;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.inventory.itemhandler.MinibikeInventoryHandler;
import com.nuparu.sevendaystomine.item.IQuality;
import com.nuparu.sevendaystomine.item.ItemQuality;
import com.nuparu.sevendaystomine.util.DamageSources;
import com.nuparu.sevendaystomine.util.ItemUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EntityMinibike extends EntityLivingBase implements IControllable {

	private static final DataParameter<Boolean> ENGINE = EntityDataManager.<Boolean>createKey(EntityMinibike.class,
			DataSerializers.BOOLEAN);

	private static final DataParameter<Boolean> BATTERY = EntityDataManager.<Boolean>createKey(EntityMinibike.class,
			DataSerializers.BOOLEAN);

	private static final DataParameter<Boolean> WHEELS = EntityDataManager.<Boolean>createKey(EntityMinibike.class,
			DataSerializers.BOOLEAN);

	private static final DataParameter<Boolean> SEAT = EntityDataManager.<Boolean>createKey(EntityMinibike.class,
			DataSerializers.BOOLEAN);

	private static final DataParameter<Boolean> HANDLES = EntityDataManager.<Boolean>createKey(EntityMinibike.class,
			DataSerializers.BOOLEAN);

	private static final DataParameter<Boolean> CHEST = EntityDataManager.<Boolean>createKey(EntityMinibike.class,
			DataSerializers.BOOLEAN);

	private static final DataParameter<Integer> CHASSIS_QUALITY = EntityDataManager
			.<Integer>createKey(EntityMinibike.class, DataSerializers.VARINT);

	private static final DataParameter<Integer> CALCULATED_QUALITY = EntityDataManager
			.<Integer>createKey(EntityMinibike.class, DataSerializers.VARINT);

	private static final DataParameter<Boolean> CHARGED = EntityDataManager.<Boolean>createKey(EntityMinibike.class,
			DataSerializers.BOOLEAN);

	private static final DataParameter<Float> FUEL = EntityDataManager.<Float>createKey(EntityMinibike.class,
			DataSerializers.FLOAT);

	private static final DataParameter<BlockPos> OLD_POS = EntityDataManager.<BlockPos>createKey(EntityMinibike.class,
			DataSerializers.BLOCK_POS);

	public final static UUID SPEED_MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");

	private ItemStackHandler inventory = null;

	private double velocity;

	public float wheelAngle;
	public float wheelAnglePrev;
	private float deltaRotation;
	
	public static final float MAX_FUEL = 5000;

	public EntityMinibike(World world) {
		super(world);
		this.setSize(0.8F, 0.75F);
		this.stepHeight = 1.25f;
		initInventory();
	}

	protected void initInventory() {
		this.inventory = new MinibikeInventoryHandler(getInventorySize(), this);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ENGINE, false);
		this.dataManager.register(BATTERY, false);
		this.dataManager.register(WHEELS, false);
		this.dataManager.register(SEAT, false);
		this.dataManager.register(HANDLES, false);
		this.dataManager.register(CHEST, false);
		this.dataManager.register(CHARGED, false);
		this.dataManager.register(CHASSIS_QUALITY, 1);
		this.dataManager.register(CALCULATED_QUALITY, -1);
		this.dataManager.register(FUEL, 100f);
		this.dataManager.register(OLD_POS, new BlockPos(0, -64, 0));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(700);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
	}

	protected void setEngine(boolean state) {
		this.dataManager.set(ENGINE, state);
	}

	public boolean getEngine() {
		return this.dataManager.get(ENGINE);
	}

	protected void setBattery(boolean state) {
		this.dataManager.set(BATTERY, state);
	}

	public boolean getBattery() {
		return this.dataManager.get(BATTERY);
	}

	protected void setWheels(boolean state) {
		this.dataManager.set(WHEELS, state);
	}

	public boolean getWheels() {
		return this.dataManager.get(WHEELS);
	}

	protected void setSeat(boolean state) {
		this.dataManager.set(SEAT, state);
	}

	public boolean getSeat() {
		return this.dataManager.get(SEAT);
	}

	protected void setHandles(boolean state) {
		this.dataManager.set(HANDLES, state);
	}

	public boolean getHandles() {
		return this.dataManager.get(HANDLES);
	}

	protected void setChest(boolean state) {
		this.dataManager.set(CHEST, state);
	}

	public boolean getChest() {
		return this.dataManager.get(CHEST);
	}

	protected void setChassisQuality(int quality) {
		this.dataManager.set(CHASSIS_QUALITY, quality);
	}

	public int getChassisQuality() {
		return this.dataManager.get(CHASSIS_QUALITY);
	}

	protected void setCalculatedQuality(int quality) {
		this.dataManager.set(CALCULATED_QUALITY, quality);
	}

	public int getCalculatedQuality() {
		return this.dataManager.get(CALCULATED_QUALITY);
	}

	protected void setFuel(float fuel) {
		this.dataManager.set(FUEL, fuel);
	}

	public float getFuel() {
		return this.dataManager.get(FUEL);
	}

	protected void setOldPos(BlockPos pos) {
		this.dataManager.set(OLD_POS, pos);
	}

	public BlockPos getOldPos() {
		return this.dataManager.get(OLD_POS);
	}

	protected void setCharged(boolean state) {
		this.dataManager.set(CHARGED, state);
	}

	public boolean getCharged() {
		return this.dataManager.get(CHARGED);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("inventory")) {
			inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		}
		if (compound.hasKey("engine", Constants.NBT.TAG_BYTE)) {
			this.setEngine(compound.getBoolean("engine"));
		}
		if (compound.hasKey("battery", Constants.NBT.TAG_BYTE)) {
			this.setBattery(compound.getBoolean("battery"));
		}
		if (compound.hasKey("wheels", Constants.NBT.TAG_BYTE)) {
			this.setWheels(compound.getBoolean("wheels"));
		}
		if (compound.hasKey("seat", Constants.NBT.TAG_BYTE)) {
			this.setSeat(compound.getBoolean("seat"));
		}
		if (compound.hasKey("handles", Constants.NBT.TAG_BYTE)) {
			this.setHandles(compound.getBoolean("handles"));
		}
		if (compound.hasKey("chest", Constants.NBT.TAG_BYTE)) {
			this.setChest(compound.getBoolean("chest"));
		}
		if (compound.hasKey("chassis_quality", Constants.NBT.TAG_INT)) {
			this.setChassisQuality(compound.getInteger("chassis_quality"));
		}

		if (compound.hasKey("calculated_quality", Constants.NBT.TAG_INT)) {
			this.setCalculatedQuality(compound.getInteger("calculated_quality"));
		}

		if (compound.hasKey("charged", Constants.NBT.TAG_BYTE)) {
			this.setCharged(compound.getBoolean("charged"));
		}

		if (compound.hasKey("fuel", Constants.NBT.TAG_FLOAT)) {
			this.setFuel(compound.getFloat("fuel"));
		}

		if (compound.hasKey("speed", Constants.NBT.TAG_LONG)) {
			this.setOldPos(BlockPos.fromLong(compound.getLong("old_pos")));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (inventory != null) {
			compound.setTag("inventory", inventory.serializeNBT());
		}
		compound.setBoolean("engine", getEngine());
		compound.setBoolean("battery", getBattery());
		compound.setBoolean("wheels", getWheels());
		compound.setBoolean("seat", getSeat());
		compound.setBoolean("handles", getHandles());
		compound.setBoolean("chest", getChest());
		compound.setInteger("chassis_quality", getChassisQuality());
		compound.setInteger("calculated_quality", getCalculatedQuality());
		compound.setBoolean("charged", getCharged());
		compound.setFloat("fuel", getFuel());
		compound.setLong("old_pos", getOldPos().toLong());

		return compound;
	}

	@Override
	public ITextComponent getDisplayName() {
		TextComponentString textcomponentstring = new TextComponentString(
				ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
		textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
		textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
		return textcomponentstring;
	}

	public ItemStackHandler getInventory() {
		return this.inventory;
	}

	public int getInventorySize() {
		return 16;
	}

	@Override
	public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
		this.inventory.setStackInSlot(inventorySlot, itemStackIn);
		return true;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {

		if (player.isSneaking()) {
			player.openGui(SevenDaysToMine.instance, 17, this.world, (int) 0, (int) this.getEntityId(), (int) 0);
		} else {
			if (!this.world.isRemote) {
				player.startRiding(this);
			}

		}
		return true;
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return getChest() ? this.getPassengers().size() < 1 : this.getPassengers().size() < 2;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public double getMountedYOffset() {
		return (double) this.height * 0.5D;
	}

	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability,
			@Nullable net.minecraft.util.EnumFacing facing) {

		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) inventory;
		}
		return super.getCapability(capability, facing);
	}

	public void onInventoryChanged(ItemStackHandler inv) {
		this.updateInventory();
	}

	public void updateInventory() {
		this.setHandles(getInventory().getStackInSlot(0).getItem() == ModItems.MINIBIKE_HANDLES);
		this.setWheels(getInventory().getStackInSlot(1).getItem() == Item.getItemFromBlock(ModBlocks.WHEELS));
		this.setSeat(getInventory().getStackInSlot(2).getItem() == ModItems.MINIBIKE_SEAT);
		this.setBattery(getInventory().getStackInSlot(3).getItem() == ModItems.CAR_BATTERY);
		this.setEngine(getInventory().getStackInSlot(4).getItem() == ModItems.SMALL_ENGINE);
		this.setChest(getInventory().getStackInSlot(5).getItem() == Item.getItemFromBlock(Blocks.CHEST));

		int quality = -1;

		if (this.getHandles() && this.getBattery() && this.getWheels() && this.getSeat() && this.getEngine()) {
			quality = 0;
			for (int i = 0; i < 5; i++) {
				ItemStack stack = this.getInventory().getStackInSlot(i);
				if (!stack.isEmpty()) {
					Item item = stack.getItem();
					if (item instanceof IQuality) {
						quality += ((IQuality) item).getQuality(stack);
					}
				}
			}
			quality = (int) Math.floor(quality / 5);
		}
		this.setCalculatedQuality(quality);

		if (this.isComplete()) {
			AttributeModifier speedModifier = new AttributeModifier(SPEED_MODIFIER_UUID, "Speed Modifier",
					(quality / 3333f), 0);
			if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
					.getModifier(SPEED_MODIFIER_UUID) != null) {
				this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(this
						.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_UUID));
			}

			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(speedModifier);
		} else {
			if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
					.getModifier(SPEED_MODIFIER_UUID) != null) {
				this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(this
						.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_UUID));
			}
		}
	}

	/*
	 * 
	 * MOVEMENT
	 * 
	 */

	@Override
	protected void collideWithEntity(Entity entityIn) {
		super.collideWithEntity(entityIn);

		if (isServerWorld()) {

			if (!entityIn.getPassengers().contains(this) && entityIn.getRidingEntity() != this) {

				if (this.getControllingPassenger() != null
						&& this.getControllingPassenger() instanceof EntityLivingBase) {
					if (!entityIn.noClip && !this.noClip) {

						Entity riddenBy = getControllingPassenger();

						if (velocity > 0.1D) {

							DamageSource damagesource = null;
							if (riddenBy != null) {
								damagesource = DamageSources.causeVehicleDamage(this, riddenBy);
							} else {
								damagesource = DamageSources.causeVehicleDamage(this, this);
							}
							entityIn.attackEntityFrom(damagesource, (float) velocity * 50F
									* (float) (getCalculatedQuality() / ItemQuality.MAX_QUALITY));
						}

					}
				}
			}
		}

	}

	@Nullable
	public Entity getControllingPassenger() {
		return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
	}

	public boolean canBeSteered() {
		return this.getControllingPassenger() instanceof EntityLivingBase;
	}

	public boolean isComplete() {
		return this.getBattery() && this.getEngine() && this.getHandles() && this.getSeat() && this.getWheels();
	}

	public boolean isBateryCharged() {
		ItemStack battery = getInventory().getStackInSlot(3);
		NBTTagCompound nbt = battery.getTagCompound();
		return getBattery() && nbt != null && nbt.hasKey("voltage", Constants.NBT.TAG_INT)
				&& nbt.getInteger("voltage") > 0;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!world.isRemote) {
			this.setCharged(this.isBateryCharged());
		}
		if (this.getOldPos().getY() < -1) {
			this.setOldPos(new BlockPos(this));
		}
		BlockPos delta = this.getOldPos().subtract(new BlockPos(this));
		this.velocity = Math.sqrt(delta.getX() * delta.getX() + delta.getZ() * delta.getZ());
		this.wheelAnglePrev = this.wheelAngle;
		if (wheelAngle > 360) {
			wheelAngle -= 360;
		} else if (wheelAngle < 0) {
			wheelAngle += 360;
		}
		if (this.getFuel() > 100) {
			this.setFuel(100);
		}
		if (this.getFuel() < 0) {
			this.setFuel(0);
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		int i = 5;
		double d0 = 0.0D;

		double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

		if (d9 > 0.1) {
			if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityLivingBase) {
				if (world.rand.nextInt(10) < 4) {
					IBlockState iblockstate = this.world
							.getBlockState(new BlockPos(this.prevPosX, this.prevPosY - 1, this.prevPosZ));
					this.world.playEvent(2001, new BlockPos(this.prevPosX, this.prevPosY - 1, this.prevPosZ),
							Block.getStateId(iblockstate));
				}
				if (world.rand.nextInt(15) == 0) {
					this.setFuel((this.getFuel() - (10F / ItemUtils.getQuality(getInventory().getStackInSlot(4)))));
				}

			}
			if (d9 > 0.4) {

				double d2 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D);
				double d4 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D);

				for (int k = 0; (double) k < 1.0D + d9 * 60.0D; ++k) {
					double d5 = (double) (this.rand.nextFloat() * 2.0F - 1.0F);
					double d6 = (double) (this.rand.nextInt(2) * 2 - 1) * 0.7D;

					if (this.rand.nextBoolean()) {
						double d7 = this.posX;
						double d8 = this.posZ;

						this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d7, this.posY, d8, this.motionX,
								this.motionY, this.motionZ, new int[0]);
					} else {
						double d24 = this.posX;
						double d25 = this.posZ;
						this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d24, this.posY, d25, this.motionX,
								this.motionY, this.motionZ, new int[0]);
					}
				}
			}
		}
		this.setOldPos(new BlockPos(this));
	}

	public void travel(float strafe, float vertical, float forward) {
		if (this.isBeingRidden() && this.canBeSteered() && this.isComplete() && this.getFuel() >= 0 && getCharged()) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();

			strafe = entitylivingbase.moveStrafing * 0.5F;
			forward = entitylivingbase.moveForward;

			if (velocity > 0) {
				this.rotationYaw = entitylivingbase.rotationYaw;
				// this.prevRotationYaw = this.rotationYaw;
				this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
				/*
				 * double deltaYaw = -strafe*10d; if(forward < 0) { deltaYaw=-deltaYaw; }
				 * entitylivingbase.rotationYaw+=deltaYaw;
				 * entitylivingbase.prevRotationYaw=entitylivingbase.rotationYaw;
				 * this.rotationYaw+=deltaYaw; this.prevRotationYaw = this.rotationYaw;
				 */
				this.setRotation(this.rotationYaw, this.rotationPitch);
				this.renderYawOffset = this.rotationYaw;
				this.rotationYawHead = this.renderYawOffset;
			}
			if (forward <= 0.0F) {
				forward *= 0.25F;
				this.wheelAngle += (velocity * -1f);
			} else {
				this.wheelAngle += velocity;
			}
			if (forward != 0 && !this.world.isRemote) {
				if (world.rand.nextInt(30) == 0) {
					ItemStack battery = getInventory().getStackInSlot(3);
					if (!battery.isEmpty()) {
						NBTTagCompound nbt = battery.getTagCompound();
						if (nbt != null && nbt.hasKey("voltage", Constants.NBT.TAG_INT)) {
							nbt.setInteger("voltage", nbt.getInteger("voltage") - 1);
						}
					}
				}
			}

			if (this.canPassengerSteer()) {
				this.setAIMoveSpeed(
						(float) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
				super.travel(0, 0, forward);
			}
		} else {
			super.travel(0, 0, forward);
		}
	}

	@Override
	public void handleKey(int key, byte state) {

	}

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return new ArrayList<ItemStack>();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {

	}

	@Override
	public EnumHandSide getPrimaryHand() {
		return null;
	}

	protected void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setRenderYawOffset(this.rotationYaw);
		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
		float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
		entityToUpdate.prevRotationYaw += f1 - f;
		entityToUpdate.rotationYaw += f1 - f;
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (this.isPassenger(passenger)) {
			float f = 0.0F;
			float f1 = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset())
					+ passenger.getYOffset());

			if (this.getPassengers().size() > 1) {
				int i = this.getPassengers().indexOf(passenger);

				if (i == 0) {
					f = 0.2F;
				} else {
					f = -0.6F;
				}

				if (passenger instanceof EntityAnimal) {
					f = (float) ((double) f + 0.2D);
				}
			}

			Vec3d vec3d = (new Vec3d((double) f, 0.0D, 0.0D))
					.rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
			passenger.setPosition(this.posX + vec3d.x, this.posY + (double) f1, this.posZ + vec3d.z);
			passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
			if (this.getControllingPassenger() != passenger) {
				passenger.rotationYaw += this.rotationYaw - this.prevRotationYaw;
				this.applyYawToEntity(passenger);
			}

			if (passenger instanceof EntityAnimal && this.getPassengers().size() > 1) {
				int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
				passenger.setRenderYawOffset(((EntityAnimal) passenger).renderYawOffset + (float) j);
				passenger.setRotationYawHead(passenger.getRotationYawHead() + (float) j);
			}
		}
	}
}
