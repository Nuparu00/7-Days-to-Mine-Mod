package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.world.item.quality.IQualityStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class FrozenLumberjackEntity<T extends FrozenLumberjackEntity> extends FrozenZombieEntity {

    public FrozenLumberjackEntity(EntityType<FrozenLumberjackEntity> type, Level world) {
        super(type, world);
    }

    public FrozenLumberjackEntity(Level world) {
        this(ModEntities.FROZEN_LUMBERJACK.get(), world);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15F)
                .add(ForgeMod.SWIM_SPEED.get(), 1.4F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.ARMOR, 2);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33282_, @NotNull DifficultyInstance p_33283_, @NotNull MobSpawnType p_33284_, @Nullable SpawnGroupData p_33285_, @Nullable CompoundTag p_33286_) {
        RandomSource randomsource = p_33282_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_33283_);
        this.populateDefaultEquipmentEnchantments(randomsource, p_33283_);
        return super.finalizeSpawn(p_33282_, p_33283_, p_33284_, p_33285_, p_33286_);
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource p_218949_, @NotNull DifficultyInstance p_218950_) {
        super.populateDefaultEquipmentSlots(p_218949_, p_218950_);
        if (random.nextInt(4) != 0)
            return;
        ItemStack stack = new ItemStack(Items.IRON_AXE);
        ((IQualityStack)(Object)stack).setQuality(random.nextIntBetweenInclusive(1,600));
        this.setItemSlot(EquipmentSlot.MAINHAND,stack);
    }

    public static class Factory implements EntityType.EntityFactory<FrozenLumberjackEntity> {
        @Override
        public @NotNull FrozenLumberjackEntity create(@NotNull EntityType<FrozenLumberjackEntity> type, @NotNull Level world) {
            return new FrozenLumberjackEntity(type, world);
        }
    }
}
