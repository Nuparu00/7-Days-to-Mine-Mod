package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.world.entity.ai.GoalBreakBlocks;
import org.jetbrains.annotations.NotNull;

public class SpiderZombieEntity<T extends SpiderZombieEntity> extends ZombieBaseEntity {

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(SpiderZombieEntity.class, EntityDataSerializers.BYTE);
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int jumpDelayTicks;
    public SpiderZombieEntity(EntityType<SpiderZombieEntity> type, Level world) {
        super(type, world);
        this.jumpControl = new SpiderZombieEntity.SpiderZombieJumpControl(this);
        this.moveControl = new SpiderZombieEntity.SpiderZombieMoveControl(this);
        this.getNavigation().setSpeedModifier(3);
        corpseRotation = new Vec3(0,0,90);
    }
    public SpiderZombieEntity(Level world) {
        this(ModEntities.SPIDER_ZOMBIE.get(), world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GoalBreakBlocks(this));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 2.5D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 2.5D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        /*if (ServerConfig.zombiesAttackAnimals.get()) {
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
            this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false,
                    Turtle.BABY_ON_LAND_SELECTOR));
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AnimalEntity.class, true));
        }*/
    }


    @Override
    public boolean customCorpseTransform() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(ForgeMod.SWIM_SPEED.get(), 1.9F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MAX_HEALTH, 25)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.JUMP_STRENGTH, 4);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

    }
    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public void makeStuckInBlock(BlockState p_33796_, @NotNull Vec3 p_33797_) {
        if (!p_33796_.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(p_33796_, p_33797_);
        }

    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level p_33802_) {
        return new WallClimberNavigation(this, p_33802_);
    }
    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean p_33820_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_33820_) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    protected float getJumpPower() {
        if (!this.horizontalCollision && (!this.moveControl.hasWanted() || !(this.moveControl.getWantedY() > this.getY() + 0.5D))) {
            Path path = this.navigation.getPath();
            if (path != null && !path.isDone()) {
                Vec3 vec3 = path.getNextEntityPos(this);
                if (vec3.y > this.getY() + 0.5D) {
                    return 0.5F;
                }
            }

            return this.moveControl.getSpeedModifier() <= 0.6D ? 0.2F : 0.3F;
        } else {
            return 0.5F;
        }
    }

    protected void jumpFromGround() {
        super.jumpFromGround();
        double d0 = this.moveControl.getSpeedModifier();
        if (d0 > 0.0D) {
            double d1 = this.getDeltaMovement().horizontalDistanceSqr();
            if (d1 < 0.01D) {
                this.moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
            }
        }

        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)1);
        }

    }

    public float getJumpCompletion(float p_29736_) {
        return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + p_29736_) / (float)this.jumpDuration;
    }

    public void setSpeedModifier(double p_29726_) {
        this.getNavigation().setSpeedModifier(p_29726_);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), p_29726_);
    }

    public void setJumping(boolean p_29732_) {
        super.setJumping(p_29732_);
        if (p_29732_) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
    }

    private void facePoint(double p_29687_, double p_29688_) {
        this.setYRot((float)(Mth.atan2(p_29688_ - this.getZ(), p_29687_ - this.getX()) * (double)(180F / (float)Math.PI)) - 90.0F);
    }

    private void enableJumpControl() {
        ((SpiderZombieEntity.SpiderZombieJumpControl)this.jumpControl).setCanJump(true);
    }

    private void disableJumpControl() {
        ((SpiderZombieEntity.SpiderZombieJumpControl)this.jumpControl).setCanJump(false);
    }

    private void setLandingDelay() {
        if (this.moveControl.getSpeedModifier() < 2.2D) {
            this.jumpDelayTicks = 4;
        } else {
            this.jumpDelayTicks = 1;
        }

    }

    private void checkLandingDelay() {
        this.setLandingDelay();
        this.disableJumpControl();
    }
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }

    }

    @Override
    public void customServerAiStep() {
        if (this.jumpDelayTicks > 0) {
            --this.jumpDelayTicks;
        }


        if (this.onGround) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }

            if (this.jumpDelayTicks == 0) {
                LivingEntity livingentity = this.getTarget();
                if (livingentity != null && this.distanceToSqr(livingentity) < 16.0D) {
                    this.facePoint(livingentity.getX(), livingentity.getZ());
                    this.moveControl.setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), this.moveControl.getSpeedModifier());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }

            SpiderZombieEntity.SpiderZombieJumpControl rabbit$rabbitjumpcontrol = (SpiderZombieEntity.SpiderZombieJumpControl)this.jumpControl;
            if (!rabbit$rabbitjumpcontrol.wantJump()) {
                if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
                    Path path = this.navigation.getPath();
                    Vec3 vec3 = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
                    if (path != null && !path.isDone()) {
                        vec3 = path.getNextEntityPos(this);
                    }

                    this.facePoint(vec3.x, vec3.z);
                    this.startJumping();
                }
            } else if (!rabbit$rabbitjumpcontrol.canJump()) {
                this.enableJumpControl();
            }
        }

        this.wasOnGround = this.onGround;
    }

    public void startJumping() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.ZOMBIE_STEP;
    }
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource p_29715_) {
        return SoundEvents.ZOMBIE_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }
/*
    @Override
    protected int getExperienceReward(PlayerEntity p_70693_1_) {
        return 10;
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (ServerConfig.zombieCorpses.get()) {
            ++this.deathTime;

            if (this.deathTime == 20) {

                remove(false);
                LootableCorpseEntity lootable = new LootableCorpseEntity(level);
                lootable.setOriginal(this);
                lootable.setPos(getX(), getY(), getZ());
                dead = true;
                if (!this.level.isClientSide()) {
                    LootTable loottable = this.level.getServer().getLootTables().get(getDefaultLootTable());
                    LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) this.level)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(this.blockPosition()));
                    if (this.lastHurtByPlayer != null) {
                        lootcontext$builder.withLuck(lastHurtByPlayer.getLuck()).withParameter(LootParameters.THIS_ENTITY, lastHurtByPlayer);
                    }
                    ItemUtils.fill(loottable, lootable.getInventory(), lootcontext$builder.create(LootParameterSets.CHEST));
                    level.addFreshEntity(lootable);
                }

                for (int i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(),
                            this.getRandomZ(1.0D), d0, d1, d2);
                }
            }
        }
    }*/

    public static class SpiderZombieJumpControl extends JumpControl {
        private final SpiderZombieEntity spiderZombie;
        private boolean canJump;

        public SpiderZombieJumpControl(SpiderZombieEntity p_186229_) {
            super(p_186229_);
            this.spiderZombie = p_186229_;
        }

        public boolean wantJump() {
            return this.jump;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean p_29759_) {
            this.canJump = p_29759_;
        }

        public void tick() {
            if (this.jump) {
                this.spiderZombie.startJumping();
                this.jump = false;
            }

        }
    }

    static class SpiderZombieMoveControl extends MoveControl {
        private final SpiderZombieEntity spiderZombie;
        private double nextJumpSpeed;

        public SpiderZombieMoveControl(SpiderZombieEntity p_29766_) {
            super(p_29766_);
            this.spiderZombie = p_29766_;
        }

        public void tick() {
            if (this.spiderZombie.onGround && !this.spiderZombie.jumping && !((SpiderZombieEntity.SpiderZombieJumpControl)this.spiderZombie.jumpControl).wantJump()) {
                this.spiderZombie.setSpeedModifier(0.0D);
            } else if (this.hasWanted()) {
                this.spiderZombie.setSpeedModifier(this.nextJumpSpeed);
            }

            super.tick();
        }

        public void setWantedPosition(double p_29769_, double p_29770_, double p_29771_, double p_29772_) {
            if (this.spiderZombie.isInWater()) {
                p_29772_ = 1.5D;
            }

            super.setWantedPosition(p_29769_, p_29770_, p_29771_, p_29772_);
            if (p_29772_ > 0.0D) {
                this.nextJumpSpeed = p_29772_;
            }

        }
    }
}
