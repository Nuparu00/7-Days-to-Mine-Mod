package nuparu.sevendaystomine.world.entity.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModDamageSources;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.inventory.entity.ContainerCar;
import nuparu.sevendaystomine.world.item.IBattery;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import nuparu.sevendaystomine.world.level.block.ArmchairBlock;
import nuparu.sevendaystomine.world.level.block.SofaBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class CarEntity extends VehicleEntity {

    private static final EntityDataAccessor<Boolean> ENGINE = SynchedEntityData.defineId(CarEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BATTERY = SynchedEntityData.defineId(CarEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WHEELS_FRONT = SynchedEntityData.defineId(CarEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WHEELS_BACK = SynchedEntityData.defineId(CarEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SEAT = SynchedEntityData.defineId(CarEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HANDLES = SynchedEntityData.defineId(CarEntity.class,
            EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(CarEntity.class, EntityDataSerializers.INT);

    public int honkCooldown = 0;

    public CarEntity(EntityType<CarEntity> type, Level world) {
        super(type, world);
        this.maxUpStep = 1.5f;
    }

    public CarEntity(Level world) {
        this(ModEntities.CAR.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.175F).add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 60);
    }

    public int getInventorySize() {
        return 16;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ENGINE, false);
        this.getEntityData().define(BATTERY, false);
        this.getEntityData().define(WHEELS_FRONT, false);
        this.getEntityData().define(WHEELS_BACK, false);
        this.getEntityData().define(SEAT, false);
        this.getEntityData().define(HANDLES, false);
        this.entityData.define(COLOR, DyeColor.RED.getId());
    }

    @Override
    public InteractionResult interact(Player playerEntity, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            ItemStack itemstack = playerEntity.getItemInHand(hand);
            Item item = itemstack.getItem();
            if(item instanceof DyeItem){
                DyeColor dyecolor = ((DyeItem)item).getDyeColor();
                if(dyecolor != getColor()){
                    setColor(dyecolor);
                    if (!playerEntity.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }

        return super.interact(playerEntity, hand);
    }

    public boolean getEngine() {
        return this.getEntityData().get(ENGINE);
    }

    protected void setEngine(boolean state) {
        this.getEntityData().set(ENGINE, state);
    }

    public boolean getBattery() {
        return this.getEntityData().get(BATTERY);
    }

    protected void setBattery(boolean state) {
        this.getEntityData().set(BATTERY, state);
    }

    public boolean getWheelsFront() {
        return this.getEntityData().get(WHEELS_FRONT);
    }

    protected void setWheelsFront(boolean state) {
        this.getEntityData().set(WHEELS_FRONT, state);
    }

    public boolean getWheelsBack() {
        return this.getEntityData().get(WHEELS_BACK);
    }

    protected void setWheelsBack(boolean state) {
        this.getEntityData().set(WHEELS_BACK, state);
    }

    public boolean getSeat() {
        return this.getEntityData().get(SEAT);
    }

    protected void setSeat(boolean state) {
        this.getEntityData().set(SEAT, state);
    }

    public boolean getHandles() {
        return this.getEntityData().get(HANDLES);
    }

    protected void setHandles(boolean state) {
        this.getEntityData().set(HANDLES, state);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(COLOR));
    }

    public void setColor(DyeColor p_175547_1_) {
        this.entityData.set(COLOR, p_175547_1_.getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {

        super.readAdditionalSaveData(compound);
        if (compound.contains("engine", Tag.TAG_BYTE)) {
            this.setEngine(compound.getBoolean("engine"));
        }
        if (compound.contains("battery", Tag.TAG_BYTE)) {
            this.setBattery(compound.getBoolean("battery"));
        }
        if (compound.contains("wheels_front", Tag.TAG_BYTE)) {
            this.setWheelsFront(compound.getBoolean("wheels_front"));
        }
        if (compound.contains("wheels_back", Tag.TAG_BYTE)) {
            this.setWheelsBack(compound.getBoolean("wheels_back"));
        }
        if (compound.contains("seat", Tag.TAG_BYTE)) {
            this.setSeat(compound.getBoolean("seat"));
        }
        if (compound.contains("handles", Tag.TAG_BYTE)) {
            this.setHandles(compound.getBoolean("handles"));
        }
        if (compound.contains("color", Tag.TAG_BYTE)) {
            this.setColor(DyeColor.byId(compound.getInt("color")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {


        super.addAdditionalSaveData(compound);
        compound.putBoolean("engine", getEngine());
        compound.putBoolean("battery", getBattery());
        compound.putBoolean("wheels_front", getWheelsFront());
        compound.putBoolean("wheels_back", getWheelsBack());
        compound.putBoolean("seat", getSeat());
        compound.putBoolean("handles", getHandles());
        compound.putByte("color", (byte)this.getColor().getId());

    }


    @Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
        return this.getPassengers().size() < 4;
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.02D;
    }

    @Override
    public boolean isComplete() {
        return this.getBattery() && this.getEngine() && this.getHandles() && this.getSeat() && this.getWheelsFront() && this.getWheelsBack();
    }

    public boolean isBateryCharged() {
        ExtendedInventory inv = this.getInventory();
        if(inv == null) return false;
        ItemStack battery = inv.getStackInSlot(3);
        if (battery.isEmpty())
            return false;
        Item item = battery.getItem();
        if (!(item instanceof IBattery bat))
            return false;
        return bat.getVoltage(battery, level()) > 0;
    }

    @Override
    public void updateInventory() {
        ExtendedInventory inv = this.getInventory();
        if(inv == null) return;

        this.setHandles(inv.getStackInSlot(0).getItem() == ModItems.MINIBIKE_HANDLES.get());
        this.setWheelsFront(inv.getStackInSlot(1).getItem() == ModBlocks.WHEELS.get().asItem());
        this.setWheelsBack(inv.getStackInSlot(2).getItem() == ModBlocks.WHEELS.get().asItem());
        ItemStack seats = inv.getStackInSlot(3);
        this.setSeat(!seats.isEmpty() && seats.getItem() instanceof BlockItem blockItem && (blockItem.getBlock() instanceof ArmchairBlock || blockItem.getBlock() instanceof SofaBlock));
        this.setBattery(inv.getStackInSlot(4).getItem() instanceof IBattery);
        this.setEngine(inv.getStackInSlot(5).getItem() == ModItems.SMALL_ENGINE.get());

        int quality = -1;

        if (this.getHandles() && this.getBattery() && this.getWheelsFront() && this.getWheelsBack() && this.getSeat() && this.getEngine()) {
            if (ServerConfig.quality.get()) {
                quality = 0;
                int items = 0;
                for (int i = 0; i < 6; i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (!stack.isEmpty() && ((IQualityStack)(Object)stack).canHaveQuality()) {
                        quality += ((IQualityStack)(Object)stack).getQuality();
                        items++;
                    }
                }
                if (items > 0) {
                    quality = (int) Math.floor(quality / items);
                }
            } else {
                quality = 600;
            }
        }

        this.setCalculatedQuality(quality);
    }

    @Override
    public void tick() {
        if(honkCooldown > 0){
            honkCooldown--;
        }


        yBodyRot = getYRot();
        yBodyRotO = yRotO;

        ExtendedInventory inv = this.getInventory();
        if(inv == null) return;

        //System.out.println(this.lerpX);
        kmh = Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z) * 20;

        //System.out.println(this.getCalculatedQuality() + " " + kmh);
        if (level().isClientSide()) {
            if (System.currentTimeMillis() - nextIdleSound >= (4750 / (1d + kmh / 40d))
                    && this.getControllingPassenger() != null && this.canBeDriven()) {
                //SevenDaysToMine.proxy.playMovingSound(0, this);
                this.nextIdleSound = System.currentTimeMillis();
            }
        }

        super.tick();

        Vec3 forward = this.getForwardServerSide();
        double drag = this.verticalCollision ? 0.88 : 0.99;

        this.setDeltaMovement(this.getDeltaMovement().multiply(drag, drag, drag));

        if (!level().isClientSide()) {
            float turning = this.getTurning();
            this.setTurningPrev(turning);
            if (turning != 0) {
                this.setTurning(turning * (float) drag);
            }
        }
        if (this.horizontalCollision && this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.30000001192092896D, this.getDeltaMovement().z);
        }

        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.1, 0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!level().isClientSide()) {
            this.setCharged(this.isBateryCharged());
        }
        this.wheelAnglePrev = this.wheelAngle;
        this.wheelAngle += forward.x * this.getDeltaMovement().x + forward.z * this.getDeltaMovement().z;
        if (wheelAngle > 360) {
            wheelAngle -= 360;
        } else if (wheelAngle < 0) {
            wheelAngle += 360;
        }

        if (this.getFuel() < 0) {
            this.setFuel(0);
        }

        double d2 = Math.cos((double) this.getYRot() * Math.PI / 180.0D);
        double d4 = Math.sin((double) this.getYRot() * Math.PI / 180.0D);

        //Replacing grass with dirt
        if (kmh > 3) {
            if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof LivingEntity) {
                BlockPos pos = new BlockPos((int) this.xOld, (int) (this.yOld - 1), (int) this.zOld);
                BlockState state = this.level().getBlockState(pos);

                if (!level().isClientSide()) {
                    if (state.getBlock() == Blocks.GRASS_BLOCK) {
                        level().setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
                    } /*else if (level.getBlockState(pos.above()).getBlock() instanceof BlockBush) {
                        level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
                    }*/

                    if (level().random.nextInt(15) == 0) {
                        this.setFuel((this.getFuel() - (10F / ((IQualityStack)(Object)(inv.getStackInSlot(4))).getQuality())));
                    }
                }
                if (state.isFaceSturdy(level(), pos, Direction.UP)) {

                    for (int j = 0; j < 4; j++) {
                        double f = ((j == 0 || j == 3) ? 1.2D : -1.2D);
                        double g = ((j == 0 || j == 1) ? -0.75D : 0.75);
                        Vec3 vec3d = (new Vec3(f, 0.0D, g))
                                .yRot(-this.getYRot() * 0.017453292F - ((float) Math.PI / 2F));
                        for (int k = 0; k < 5; k++) {
                            level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state),
                                    getX() + vec3d.x + (random.nextDouble() * 0.1 - 0.05),
                                    getY() + vec3d.y + (random.nextDouble() * 0.1 - 0.05),
                                    getZ() + vec3d.z + (random.nextDouble() * 0.1 - 0.05), getDeltaMovement().x, getDeltaMovement().y,
                                    getDeltaMovement().z);
                        }
                    }
                    Vec3 vec3d = (new Vec3(-2.6, 0.15D + (random.nextDouble() * 0.1 - 0.05),
                            0.4 + (random.nextDouble() * 0.1 - 0.05)))
                            .yRot(-this.getYRot() * 0.017453292F - ((float) Math.PI / 2F));
                    for (int k = 0; k < 4; k++) {

                        level().addParticle(ParticleTypes.SMOKE, getX() + vec3d.x, getY() + vec3d.y,
                                getZ() + vec3d.z, -getDeltaMovement().x / 20, -getDeltaMovement().y / 20, -getDeltaMovement().z / 2);
                    }


                    /*for (int j = 0; j < 10; j++) {
                        level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state).setPos(pos), getX() + random.nextDouble() * 0.2,
                                getY() + random.nextDouble() * 0.2, getZ() + random.nextDouble() * 0.2, getDeltaMovement().x, getDeltaMovement().y,
                                getDeltaMovement().z);
                    }*/
                }
            }
            /*if (kmh > 5) {

                for (int k = 0; (double) k < 1.0D + kmh / 8; ++k) {
                    double d5 = (double) (this.random.nextFloat() * 2.0F - 1.0F);
                    double d6 = (double) (this.random.nextInt(2) * 2 - 1) * 0.7D;

                    double d7 = this.getX();
                    double d8 = this.getZ();

                    level.addParticle(ParticleTypes.SMOKE, d7, this.getY() + 0.12, d8, this.getDeltaMovement().x,
                            this.getDeltaMovement().y, this.getDeltaMovement().z);

                }
            }*/
        }


        if (this.getHealth() <= this.getMaxHealth() * 0.2 && !isInFluidType()) {
            if (this.getHealth() <= this.getMaxHealth() * 0.1) {

                Vec3 vec3d = (new Vec3(1.8 + (random.nextDouble() * 0.25 - 0.125), 0.5,
                        0 + (random.nextDouble() * 0.25 - 0.125)))
                        .yRot(-this.getYRot() * 0.017453292F - ((float) Math.PI / 2F));

                for (int k = 0; k < random.nextInt(2); k++) {
                    level().addParticle(ParticleTypes.FLAME, getX() + vec3d.x,
                            getY() + vec3d.y, getZ() + vec3d.z, this.getDeltaMovement().x,
                            this.getDeltaMovement().y + MathUtils.getDoubleInRange(0.02,0.035), this.getDeltaMovement().z);
                }
            }
            for (int k = 0; k < random.nextInt(3); k++) {
                Vec3 vec3d = (new Vec3(1.8 + (random.nextDouble() * 0.5 - 0.25), 0.75,
                        0 + (random.nextDouble() * 0.5 - 0.25)))
                        .yRot(-this.getYRot() * 0.017453292F - ((float) Math.PI / 2F));

                level().addParticle(ParticleTypes.SMOKE, getX() + vec3d.x,
                        getY() + vec3d.y, getZ() + vec3d.z, this.getDeltaMovement().x,
                        this.getDeltaMovement().y + MathUtils.getDoubleInRange(0.015,0.035), this.getDeltaMovement().z);
            }
        }


        //System.out.println(kmh);
        //Entity collision handling
        if (kmh >= 3 && !level().isClientSide()) {
            List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F),  (p_20442_) -> p_20442_.isAlive() && p_20442_ instanceof LivingEntity);
            if (!list.isEmpty()) {

                for (int j = 0; j < list.size(); ++j) {
                    Entity entity = list.get(j);
                    if (entity instanceof LivingEntity livingEntity) {
                        if (!entity.hasPassenger(this) && !this.hasPassenger(entity)) {
                            double force = kmh / 6d;
                            entity.push(-Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * (float) force * 0.5F, 0.1D, Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * (float) force * 0.5F);
                            entity.hurt(ModDamageSources.VEHICLE.apply(level(), this, entity), (float) Math.floor(1.5 * kmh));
                            hurt(level().damageSources().mobAttack(livingEntity), (float) Math.floor(0.25 * kmh));
                            //this.push(entity,90);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (LivingEntity) this.getPassengers().get(0);
    }

    @Override
    public double getAcceleration(){
        return (1 + (double) this.getCalculatedQuality() / QualityManager.getMaxLevel()) * 0.05;
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction posY) {
        if (this.hasPassenger(passenger)) {
            /*float f = 0.0F;
            float f1 = (float) ((this.removed ? (double) 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());
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

            Vec3 vector3d = (new Vec3((double) f, 0.0D, 0.0D)).yRot(-this.yRot * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
            posY.accept(passenger, this.getX() + vector3d.x, this.getY() + (double) f1, this.getZ() + vector3d.z);
            passenger.yRot += this.yRot - this.yRotO;
            passenger.setYHeadRot(passenger.getYHeadRot() + this.yRot - this.yRotO);
            this.applyYawToEntity(passenger);

            if (passenger instanceof Animal && this.getPassengers().size() > 1) {
                int j = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setYBodyRot(((Animal) passenger).yBodyRot + (float) j);
                passenger.setYHeadRot(passenger.getYHeadRot() + (float) j);
            }*/


            float f = 0.18F;
            float g = -0.4F;
            float f1 = (float) ((this.isRemoved() ? 0.009999999776482582D : this.getPassengersRidingOffset())
                    + passenger.getMyRidingOffset());

            int i = this.getPassengers().indexOf(passenger);

            if (i > 1) {
                f = -0.6f;
            }

            if (i % 2 == 1) {
                g = 0.4F;
            }

            if (passenger instanceof Animal) {
                f = (float) ((double) f + 0.2D);
            }

            Vec3 vec3d = (new Vec3(f, 0.0D, g))
                    .yRot(-this.getYRot() * 0.017453292F - ((float) Math.PI / 2F));
            passenger.setPos(this.getX() + vec3d.x, this.getY() + (double) f1, this.getZ() + vec3d.z);
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

    @Override
    public AbstractContainerMenu createMenu(int windowiD, @NotNull Inventory playerInventory, @NotNull Player playerEntity) {
        return ContainerCar.createContainerServerSide(windowiD, playerInventory, this);
    }
}