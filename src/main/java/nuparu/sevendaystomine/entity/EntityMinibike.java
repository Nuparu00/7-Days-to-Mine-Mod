package nuparu.sevendaystomine.entity;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.ModConfig;
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.itemhandler.MinibikeInventoryHandler;
import nuparu.sevendaystomine.item.IQuality;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.Wec2f;

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

	private static final DataParameter<Float> TURNING = EntityDataManager.<Float>createKey(EntityMinibike.class,
			DataSerializers.FLOAT);

	private static final DataParameter<Float> TURNING_PREV = EntityDataManager.<Float>createKey(EntityMinibike.class,
			DataSerializers.FLOAT);

	public final static UUID SPEED_MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");

	private ItemStackHandler inventory = null;

	public float wheelAngle;
	public float wheelAnglePrev;

	public static final float MAX_FUEL = 5000;

	public long nextIdleSound = 0;

	public EntityMinibike(World world) {
		super(world);
		this.setSize(0.8F, 0.75F);
		this.stepHeight = 1.25f;
		initInventory();
	}

	public EntityMinibike(World worldIn, double x, double y, double z) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
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
		this.dataManager.register(TURNING, 0f);
		this.dataManager.register(TURNING_PREV, 0f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
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
		this.dataManager.set(FUEL, MathUtils.clamp(fuel, 0, MAX_FUEL));
	}

	public float getFuel() {
		return this.dataManager.get(FUEL);
	}

	protected void setCharged(boolean state) {
		this.dataManager.set(CHARGED, state);
	}

	public boolean getCharged() {
		return this.dataManager.get(CHARGED);
	}

	protected void setTurning(float turning) {
		this.dataManager.set(TURNING, turning);
	}

	public float getTurning() {
		return this.dataManager.get(TURNING);
	}

	protected void setTurningPrev(float turning) {
		this.dataManager.set(TURNING_PREV, turning);
	}

	public float getTurningPrev() {
		return this.dataManager.get(TURNING_PREV);
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

		if (compound.hasKey("turning", Constants.NBT.TAG_FLOAT)) {
			this.setTurning(compound.getFloat("turning"));
		}

		if (compound.hasKey("turning_prev", Constants.NBT.TAG_FLOAT)) {
			this.setTurningPrev(compound.getFloat("turning_prev"));
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
		compound.setFloat("turning", getTurning());
		compound.setFloat("turningPrev", getTurningPrev());

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
			ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
			if (!stack.isEmpty()) {
				Item item = stack.getItem();
				if (item == ModItems.GAS_CANISTER) {
					if (this.getFuel() < MAX_FUEL) {
						this.setFuel(this.getFuel() + 250);
						stack.shrink(1);
						world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_BOTTLE_EMPTY,
								SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
								MathUtils.getFloatInRange(0.9f, 1f));
					}
					return true;
				} else if (item == ModItems.WRENCH) {
					if (this.getHealth() < this.getMaxHealth()) {
						ItemStack toConsume = new ItemStack(Items.IRON_INGOT, 1);
						if (Utils.hasItemStack(player, toConsume)) {
							Utils.removeItemStack(player.inventory, toConsume);
							stack.damageItem(1, player);
							this.heal(this.getMaxHealth() / 5f);
							world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ANVIL_USE,
									SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
									MathUtils.getFloatInRange(0.9f, 1f));
						}
					}
					return true;
				}
			} else {
				player.openGui(SevenDaysToMine.instance, 17, this.world, (int) 0, (int) this.getEntityId(), (int) 0);
			}
		} else {
			if (!this.world.isRemote && !this.getPassengers().contains(player)) {
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
		return (double) this.height * 0.62D;
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
		this.setBattery(getInventory().getStackInSlot(3).getItem() instanceof IBattery);
		this.setEngine(getInventory().getStackInSlot(4).getItem() == ModItems.SMALL_ENGINE);
		this.setChest(getInventory().getStackInSlot(5).getItem() == Item.getItemFromBlock(Blocks.CHEST));

		int quality = -1;

		if (this.getHandles() && this.getBattery() && this.getWheels() && this.getSeat() && this.getEngine()) {
			if (ModConfig.players.qualitySystem) {
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
			} else {
				quality = 600;
			}
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

		if (!entityIn.canBeAttackedWithItem())
			return;
		if (isServerWorld()) {

			if (!entityIn.getPassengers().contains(this) && entityIn.getRidingEntity() != this) {

				if (this.getControllingPassenger() != null
						&& this.getControllingPassenger() instanceof EntityLivingBase) {
					if (!entityIn.noClip && !this.noClip) {

						Entity riddenBy = getControllingPassenger();

						double velocity = motionX * motionX + motionY * motionY + motionZ * motionZ;
						if (velocity > 0.1D) {

							DamageSource damagesource = null;
							if (riddenBy != null) {
								damagesource = DamageSources.causeVehicleDamage(this, riddenBy);
							} else {
								damagesource = DamageSources.causeVehicleDamage(this, this);
							}
							entityIn.attackEntityFrom(damagesource, (float) velocity * 128
									* (float) (getCalculatedQuality() / ModConfig.players.maxQuality));
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
		if (battery.isEmpty())
			return false;
		Item item = battery.getItem();
		if (!(item instanceof IBattery))
			return false;
		IBattery bat = (IBattery) item;
		return bat.getVoltage(battery, world) > 0;
	}
	
    public Wec2f getPitchYawServerSide()
    {
        return new Wec2f(this.rotationPitch, this.rotationYaw);
    }
    
    public Vec3d getForwardServerSide()
    {
        return Wec2f.fromPitchYawVector(this.getPitchYawServerSide());
    }

	@Override
	public void onUpdate() {

		double kmh = 0;

		if (world.isRemote) {
			kmh = MathUtils.getSpeedKilometersPerHour(this);
			if (System.currentTimeMillis() - nextIdleSound >= (4750 / (1d + kmh / 100d))
					&& this.getControllingPassenger() != null && this.canBeDriven()) {
				SevenDaysToMine.proxy.playMovingSound(0, this);
				this.nextIdleSound = System.currentTimeMillis();
			}
		} else {
			kmh = Math.sqrt(motionX * motionX + motionZ * motionZ) * 20;
		}

		super.onUpdate();
		Vec3d forward = this.getForwardServerSide();
		double drag = this.collidedVertically ? 0.88 : 0.99;
		motionX *= drag;
		motionZ *= drag;
		if (!world.isRemote) {
			float turning = this.getTurning();
			this.setTurningPrev(turning);
			if (turning != 0) {
				this.setTurning(turning * (float) drag);
			}
		}
		if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX,
				this.motionY + 0.6000000238418579D - this.posY + posY, this.motionZ)) {
			this.motionY = 0.30000001192092896D;
		}

		if (!this.hasNoGravity()) {
			this.motionY -= 0.08D;
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

		if (!world.isRemote) {
			this.setCharged(this.isBateryCharged());
		}
		this.wheelAnglePrev = this.wheelAngle;
		this.wheelAngle += forward.x * motionX + forward.z * motionZ;
		if (wheelAngle > 360) {
			wheelAngle -= 360;
		} else if (wheelAngle < 0) {
			wheelAngle += 360;
		}

		if (this.getFuel() < 0) {
			this.setFuel(0);
		}

		if (kmh > 3) {
			if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityLivingBase) {
				BlockPos pos = new BlockPos(this.prevPosX, this.prevPosY - 1, this.prevPosZ);
				IBlockState state = this.world.getBlockState(pos);

				if (!world.isRemote) {
					if (state.getBlock() == Blocks.GRASS) {
						world.setBlockState(pos, Blocks.DIRT.getDefaultState());
					}
					if (world.getBlockState(pos.up()).getBlock() instanceof BlockBush || world.getBlockState(pos.up())
							.getBlock() instanceof nuparu.sevendaystomine.block.BlockBush) {
						world.setBlockToAir(pos.up());
					}

					if (world.rand.nextInt(15) == 0) {
						this.setFuel((this.getFuel() - (10F / ItemUtils.getQuality(getInventory().getStackInSlot(4)))));
					}
				}
				if (state.isSideSolid(world, pos, EnumFacing.UP)) {
					double d2 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D);
					double d4 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D);
					for (int j = 0; j < 10; j++) {
						world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + rand.nextDouble() * 0.2,
								posY + rand.nextDouble() * 0.2, posZ + rand.nextDouble() * 0.2, motionX, motionY,
								motionZ, Block.getStateId(state));
					}
				}
			}
			if (kmh > 5) {

				double d2 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D);
				double d4 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D);

				for (int k = 0; (double) k < 1.0D + kmh / 8; ++k) {
					double d5 = (double) (this.rand.nextFloat() * 2.0F - 1.0F);
					double d6 = (double) (this.rand.nextInt(2) * 2 - 1) * 0.7D;

					if (this.rand.nextBoolean()) {
						double d7 = this.posX;
						double d8 = this.posZ;

						this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d7, this.posY + 0.12, d8, this.motionX,
								this.motionY, this.motionZ, new int[0]);
					} else {
						double d24 = this.posX;
						double d25 = this.posZ;
						this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d24, this.posY + 0.12, d25,
								this.motionX, this.motionY, this.motionZ, new int[0]);
					}
				}
			}
		}
		if (this.getHealth() <= this.getMaxHealth() * 0.2) {
			for (int k = 0; k < rand.nextInt(3); k++) {
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + world.rand.nextDouble() * 0.3 - 0.15,
						posY + 0.25, posZ + world.rand.nextDouble() * 0.3 - 0.15, this.motionX, this.motionY + 0.25,
						this.motionZ, new int[0]);
			}
		}
	}

	public void travel(float strafe, float vertical, float forward) {
		double acceleration = (1 + (double) this.getCalculatedQuality() / ModConfig.players.maxQuality) * 0.04;
		if (this.isBeingRidden() && canBeDriven() && this.getFuel() > 0) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) this.getControllingPassenger();
			strafe = entitylivingbase.moveStrafing;
			forward = entitylivingbase.moveForward;
			vertical = entitylivingbase.moveVertical;

			if (forward < 0) {
				forward *= 0.4;
				strafe = -strafe;
			}

			if (!collidedVertically)
				acceleration *= 0.05;

			Vec3d vec3d = this.getLookVec();
			motionX += forward * vec3d.x * acceleration;
			motionZ += forward * vec3d.z * acceleration;
			// 
			if (forward != 0) {
				if (strafe != 0) {
					this.setTurning(getTurning() + strafe * 2f);
					this.rotationYaw -= strafe * 6;
				}
				if (rotationYaw > 180) {
					rotationYaw -= 360;
				}
				if (!world.isRemote) {
					ItemStack engine = this.getInventory().getStackInSlot(4);
					if (engine != null && engine.getTagCompound() != null) {
						int quality = engine.getTagCompound().getInteger("Quality");
						this.setFuel(getFuel() - (1.5f - (quality / ModConfig.players.maxQuality)));
					}

					ItemStack battery = getInventory().getStackInSlot(3);
					if (!battery.isEmpty() && world.rand.nextInt(1) == 0 && battery.getItem() instanceof IBattery) {
						IBattery bat = (IBattery) battery.getItem();
						bat.drainVoltage(battery, world, 1);
					}

				}
			}

		}
	}

	public boolean canBeDriven() {
		return this.canBeSteered() && this.isComplete() && getCharged();
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

	@Override
	@SideOnly(Side.CLIENT)
	public void applyOrientationToEntity(Entity entityToUpdate) {
		this.applyYawToEntity(entityToUpdate);
	}

	protected void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setRenderYawOffset(this.rotationYaw);
		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
		float f1 = MathHelper.clamp(f, -90F, 90F);
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
					f += 0.2;
				}
			}

			Vec3d vec3d = (new Vec3d((double) f, 0.0D, 0.0D))
					.rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
			passenger.setPosition(this.posX + vec3d.x, this.posY + (double) f1, this.posZ + vec3d.z);
			passenger.rotationYaw += this.rotationYaw - this.prevRotationYaw;
			passenger.setRotationYawHead(passenger.getRotationYawHead() + this.rotationYaw - this.prevRotationYaw);
			this.applyYawToEntity(passenger);

			if (passenger instanceof EntityAnimal && this.getPassengers().size() > 1) {
				int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
				passenger.setRenderYawOffset(((EntityAnimal) passenger).renderYawOffset + (float) j);
				passenger.setRotationYawHead(passenger.getRotationYawHead() + (float) j);
			}
		}
	}

	@Override
	public void onDeath(DamageSource source) {
		this.getPassengers().forEach(passanger -> {
			passanger.dismountRidingEntity();
		});
		for (int i = 0; i < this.getInventory().getSlots(); i++) {
			ItemStack stack = this.getInventory().getStackInSlot(i);
			this.entityDropItem(stack, 0);
		}
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
}
