package nuparu.sevendaystomine.world.entity.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.inventory.InventoryUtils;
import nuparu.sevendaystomine.world.item.IBattery;
import nuparu.sevendaystomine.world.item.quality.QualityTier;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

public abstract class VehicleEntity extends LivingEntity implements MenuProvider {
    public final static UUID SPEED_MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");
    public static final float MAX_FUEL = 5000;

    protected static final EntityDataAccessor<Integer> CHASSIS_QUALITY = SynchedEntityData
            .defineId(VehicleEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> CALCULATED_QUALITY = SynchedEntityData
            .defineId(VehicleEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> CHARGED = SynchedEntityData.defineId(VehicleEntity.class,
            EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> FUEL = SynchedEntityData.defineId(VehicleEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> TURNING = SynchedEntityData.defineId(VehicleEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> TURNING_PREV = SynchedEntityData.defineId(VehicleEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> FRONT_ROTATION = SynchedEntityData.defineId(VehicleEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> FRONT_ROTATION_PREV = SynchedEntityData.defineId(VehicleEntity.class,
            EntityDataSerializers.FLOAT);
    protected final LazyOptional<ExtendedInventory> inventory = LazyOptional.of(this::initInventory);
    public float wheelAngle;
    public float wheelAnglePrev;
    public long nextIdleSound = 0;

    //Only for reading outside of this class, syncing is not ensured
    public double kmh;

    public VehicleEntity(EntityType<? extends VehicleEntity> type, Level level) {
        super(type, level);
        this.maxUpStep = 1.5f;
    }

    protected ExtendedInventory initInventory() {
        final VehicleEntity minibike = this;
        return new ExtendedInventory(getInventorySize()) {
            @Override
            protected void onContentsChanged(int slot) {
                minibike.onInventoryChanged(this);
            }
        };
    }

    public int getInventorySize() {
        return 16;
    }

    public ExtendedInventory getInventory() {
        return this.inventory.orElse(null);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(CHARGED, false);
        this.getEntityData().define(CHASSIS_QUALITY, 1);
        this.getEntityData().define(CALCULATED_QUALITY, -1);
        this.getEntityData().define(FUEL, 0f);
        this.getEntityData().define(TURNING, 0f);
        this.getEntityData().define(TURNING_PREV, 0f);
        this.getEntityData().define(FRONT_ROTATION, 0f);
        this.getEntityData().define(FRONT_ROTATION_PREV, 0f);
    }

    public int getChassisQuality() {
        return this.getEntityData().get(CHASSIS_QUALITY);
    }

    protected void setChassisQuality(int quality) {
        this.getEntityData().set(CHASSIS_QUALITY, quality);
    }

    public int getCalculatedQuality() {
        return this.getEntityData().get(CALCULATED_QUALITY);
    }

    protected void setCalculatedQuality(int quality) {
        this.getEntityData().set(CALCULATED_QUALITY, quality);
    }

    public float getFuel() {
        return this.getEntityData().get(FUEL);
    }

    protected void setFuel(float fuel) {
        this.getEntityData().set(FUEL, MathUtils.clamp(fuel, 0, MAX_FUEL));
    }

    public boolean getCharged() {
        return this.getEntityData().get(CHARGED);
    }

    protected void setCharged(boolean state) {
        this.getEntityData().set(CHARGED, state);
    }

    public float getTurning() {
        return this.getEntityData().get(TURNING);
    }

    protected void setTurning(float turning) {
        this.getEntityData().set(TURNING, turning);
    }

    public float getTurningPrev() {
        return this.getEntityData().get(TURNING_PREV);
    }

    protected void setTurningPrev(float turning) {
        this.getEntityData().set(TURNING_PREV, turning);
    }

    public float getFrontRotation() {
        return this.getEntityData().get(FRONT_ROTATION);
    }

    protected void setFrontRotation(float rotation) {
        this.getEntityData().set(FRONT_ROTATION, rotation);
    }

    public float getFrontRotationPrev() {
        return this.getEntityData().get(FRONT_ROTATION_PREV);
    }

    protected void setFrontRotationPrev(float rotationPrev) {
        this.getEntityData().set(FRONT_ROTATION_PREV, rotationPrev);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("chassis_quality", Tag.TAG_INT)) {
            this.setChassisQuality(compound.getInt("chassis_quality"));
        }

        if (compound.contains("calculated_quality", Tag.TAG_INT)) {
            this.setCalculatedQuality(compound.getInt("calculated_quality"));
        }

        if (compound.contains("charged", Tag.TAG_BYTE)) {
            this.setCharged(compound.getBoolean("charged"));
        }

        if (compound.contains("fuel", Tag.TAG_FLOAT)) {
            this.setFuel(compound.getFloat("fuel"));
        }

        if (compound.contains("turning", Tag.TAG_FLOAT)) {
            this.setTurning(compound.getFloat("turning"));
        }

        if (compound.contains("turning_prev", Tag.TAG_FLOAT)) {
            this.setTurningPrev(compound.getFloat("turning_prev"));
        }

        if (compound.contains("front_rotation", Tag.TAG_FLOAT)) {
            this.setFrontRotation(compound.getFloat("front_rotation"));
        }

        if (compound.contains("front_rotation_prev", Tag.TAG_FLOAT)) {
            this.setFrontRotationPrev(compound.getFloat("front_rotation_prev"));
        }

        if (getInventory() != null && compound.contains("ItemHandler")) {
            getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("chassis_quality", getChassisQuality());
        compound.putInt("calculated_quality", getCalculatedQuality());
        compound.putBoolean("charged", getCharged());
        compound.putFloat("fuel", getFuel());
        compound.putFloat("turning", getTurning());
        compound.putFloat("turning_prev", getTurningPrev());
        compound.putFloat("front_rotation", getFrontRotation());
        compound.putFloat("front_rotation_prev", getFrontRotationPrev());

        ExtendedInventory inv = this.getInventory();
        if(inv == null) return;
        compound.put("ItemHandler", inv.serializeNBT());

    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        ExtendedInventory inv = this.getInventory();
        if(inv == null) return false;
        inv.setStackInSlot(inventorySlot, itemStackIn);
        return true;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ExtendedInventoryProvider.EXTENDED_INV_CAP) {
            return inventory.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void kill() {
        this.inventory.invalidate();
        super.kill();
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot p_184582_1_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot p_184201_1_, @NotNull ItemStack p_184201_2_) {

    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getDimensions(Pose.STANDING).height * 0.62D;
    }

    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    public abstract boolean isComplete();

    public void onInventoryChanged(ExtendedInventory inv) {
        this.updateInventory();
    }

    public Vec2 getPitchYawServerSide() {
        return new Vec2(this.getXRot(), this.getXRot());
    }

    public Vec3 getForwardServerSide() {
        return getViewVector(1);
    }


    @Override
    public float getViewXRot(float p_195050_1_) {
        if(!level().isClientSide()) return this.getXRot();
        return p_195050_1_ == 1.0F ? this.getXRot() : Mth.lerp(p_195050_1_, this.xRotO, this.getXRot());
    }
    @Override
    public float getViewYRot(float p_195046_1_) {
        if(!level().isClientSide()) return this.getYRot();
        return p_195046_1_ == 1.0F ? this.getYRot() : Mth.lerp(p_195046_1_, this.yRotO, this.getXRot());
    }


    public abstract void updateInventory();

    @Override
    public @NotNull InteractionResult interact(@NotNull Player playerEntity, @NotNull InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = playerEntity.getItemInHand(hand);
            if (playerEntity.isCrouching()) {
                if (playerEntity instanceof ServerPlayer) {
                    if (stack.getItem() == ModItems.GAS_CANISTER.get()) {
                        if (this.getFuel() < MAX_FUEL) {
                            this.setFuel(this.getFuel() + 250);
                            stack.shrink(1);
                            level().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_EMPTY,
                                    SoundSource.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
                                    MathUtils.getFloatInRange(0.9f, 1f));
                        }
                    }
                    else if(stack.getItem() == ModItems.WRENCH.get()){
                        if (this.getHealth() < this.getMaxHealth()) {
                            ItemStack toConsume = new ItemStack(Items.IRON_INGOT, 1);
                            if (InventoryUtils.hasItemStack(playerEntity, toConsume)) {
                                InventoryUtils.removeItemStack(playerEntity.getInventory(), toConsume);
                                stack.hurt(1, random, (ServerPlayer) playerEntity);
                                this.heal(this.getMaxHealth() / 5f);
                                level().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ANVIL_USE,
                                        SoundSource.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
                                        MathUtils.getFloatInRange(0.9f, 1f));
                            }
                            else {
                                playerEntity.sendSystemMessage(Component.translatable("repair.missing",toConsume.getDisplayName(),this.getTypeName()));
                            }
                        }
                        else{
                            playerEntity.sendSystemMessage(Component.translatable("repair.repaired", this.getTypeName()));
                        }
                    }
                    else {
                        ServerPlayer serverPlayer = (ServerPlayer) playerEntity;
                        NetworkHooks.openScreen(serverPlayer, this, (packetBuffer) -> packetBuffer.writeInt(this.getId()));
                    }
                }

                return InteractionResult.SUCCESS;
            } else {
                return playerEntity.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }
    @Nullable
    public LivingEntity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (LivingEntity) this.getPassengers().get(0);
    }

    public boolean canBeDriven() {
        return this.canBeSteered() && this.isComplete() && getCharged() && !this.isInFluidType();
    }

    public double getAcceleration(){
        return (1 + (double) this.getCalculatedQuality() / QualityManager.getMaxLevel()) * 0.05;
    }

    @Override
    public void travel(Vec3 vec) {

        double acceleration = getAcceleration();
        double strafe = vec.x;
        double forward = vec.y;
        double vertical = vec.z;
        if (this.getControllingPassenger() != null && canBeDriven() && this.getFuel() > 0) {
            LivingEntity entitylivingbase = (LivingEntity) this.getControllingPassenger();
            strafe = entitylivingbase.xxa;
            forward = entitylivingbase.zza;
            //vertical = entitylivingbase.zza;

            if (forward < 0) {
                forward *= 0.4;
                strafe = -strafe;
            }

            if (!this.verticalCollision)
                acceleration *= 0.05;

            Vec3 Vec3 = this.getForwardServerSide();
            this.setDeltaMovement(this.getDeltaMovement().add(forward * Vec3.x * acceleration, 0, forward * Vec3.z * acceleration));


            if (forward != 0) {
                if (strafe != 0) {
                    this.setTurning((float) (getTurning() + strafe * 2f));
                    double yP = getYRot();
                    this.setYRot((float) (getYRot() - strafe * 4));
                    this.yBodyRot = this.getYRot();
                }
                if (getYRot() > 180) {
                    this.setYRot(getYRot() - 360);
                }
                if (!level().isClientSide()) {
                    ExtendedInventory inv = this.getInventory();
                    if(inv == null) return;
                    ItemStack engine = inv.getStackInSlot(4);
                    if (engine.hasTag()) {
                        int quality = engine.getTag().getInt("Quality");
                        this.setFuel(getFuel() - (1.5f - (quality / QualityManager.getMaxLevel())));
                    }

                    ItemStack battery = inv.getStackInSlot(3);
                    if (!battery.isEmpty()) {
                        level().random.nextInt(1);
                        if (battery.getItem() instanceof IBattery bat) {
                            bat.drainVoltage(battery, level(), 1);
                        }
                    }

                }
            }

        }

        if (!level().isClientSide()) {
            //System.out.println(strafe);
            float prevFront = this.getFrontRotation() * 0.9f;
            if (Math.abs(prevFront) <= 0.0001) {
                prevFront = 0;
            }

            prevFront += strafe * 0.05f * (forward < 0 ? 1 : -1);

            prevFront = MathUtils.clamp(prevFront, -1, 1);
            setFrontRotationPrev(getFrontRotation());
            setFrontRotation(prevFront);
        }
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction posY) {
        if (this.hasPassenger(passenger)) {
            float f = 0.0F;
            float f1 = (float) ((this.isRemoved() ? (double) 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }

                if (passenger instanceof Animal) {
                    f = (float) ((double) f + 0.2D);
                }
            }

            Vec3 Vec3 = (new Vec3(f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
            posY.accept(passenger, this.getX() + Vec3.x, this.getY() + (double) f1, this.getZ() + Vec3.z);
            passenger.setYRot(passenger.getYRot() + this.getYRot() - this.yRotO);
            passenger.setYHeadRot(passenger.getYHeadRot() + this.getYRot() - this.yRotO);
            this.applyYawToEntity(passenger);

            if (passenger instanceof Animal && this.getPassengers().size() > 1) {
                int j = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setYBodyRot(((Animal) passenger).yBodyRot + (float) j);
                passenger.setYHeadRot(passenger.getYHeadRot() + (float) j);
            }

        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setYBodyRot(this.getXRot());
        float f = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -90F, 90F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }

    @Override
    public @NotNull Component getName() {
        Component itextcomponent = this.getCustomName();
        if(itextcomponent != null) return super.getName();

        int quality = this.getCalculatedQuality();

        if(quality > 0){
            QualityTier tier = QualityManager.getQualityTier(quality);
            MutableComponent qualityTitle = Component.translatable("stat.quality." + tier.getUnlocalizedName());
            Style style = qualityTitle.getStyle().withColor(tier.textColor);

            Component type = getTypeName();
            if(type instanceof MutableComponent){
                ((MutableComponent )type).setStyle(style);
                qualityTitle.setStyle(style);
            }
            return qualityTitle.append(" ").append(type);
        }
        return Component.translatable("stat.unfinished").append(" ").append(this.getTypeName().getString());
    }


    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource p_184601_1_) {
        return SoundEvents.ZOMBIE_ATTACK_IRON_DOOR;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DAMAGE;
    }

    @Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider)
    {
        return true;
    }


    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
}
