package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import nuparu.sevendaystomine.world.entity.ai.GoalBreakBlocks;

public class ZombieBipedEntity<T extends ZombieBipedEntity> extends ZombieBaseEntity {

    public ZombieBipedEntity(EntityType<T> type, Level world) {
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

}
