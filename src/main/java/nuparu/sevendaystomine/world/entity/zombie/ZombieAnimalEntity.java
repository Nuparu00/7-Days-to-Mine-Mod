package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.world.entity.ai.GoalBreakBlocks;

public class ZombieAnimalEntity<T extends ZombieAnimalEntity> extends ZombieBaseEntity {

    public ZombieAnimalEntity(EntityType<T> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GoalBreakBlocks(this));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        /*if (ServerConfig.zombiesAttackAnimals.get()) {
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
            this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false,
                    Turtle.BABY_ON_LAND_SELECTOR));
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AnimalEntity.class, true));
        }*/
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

}
