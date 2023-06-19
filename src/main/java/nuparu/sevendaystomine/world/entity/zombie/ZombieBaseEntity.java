package nuparu.sevendaystomine.world.entity.zombie;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.world.entity.item.LootableCorpseEntity;
import nuparu.sevendaystomine.world.level.LevelUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class ZombieBaseEntity extends Monster {

    public final static UUID NIGHT_BOOST_ID = UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a");
    public final static UUID BLOODMOON_SPEED_BOOST_ID = UUID.fromString("2ca21e76-c020-11e7-abc4-cec278b6b50a");
    public final static UUID BLOODMOON_RANGE_BOOST_ID = UUID.fromString("4340be6a-c8bf-11e7-a80b-cec278b6b50a");
    public final static UUID BLOODMOON_DAMAGE_BOOST_ID = UUID.fromString("dc7572f6-d05f-4df6-afee-7fa78046ec54");
    public final static UUID BLOODMOON_ARMOR_BOOST_ID = UUID.fromString("b859cf4a-b7cd-486f-9b59-ebabfdd0985e");

    public static final AttributeModifier[] NIGHT_SPEED_BOOSTS = new AttributeModifier[]{
            new AttributeModifier(
                    UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a"), "nightSpeedBoost", (0.75f/10f)*10,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b3224494-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*9,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b32248cc-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*8,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b3224a02-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*7,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b3224b2e-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*6,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b3224c50-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*5,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b32250ce-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*4,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b32251f0-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*3,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b32252fe-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*2,
                    AttributeModifier.Operation.MULTIPLY_BASE),
            new AttributeModifier(
                    UUID.fromString("b322540c-f702-11ec-b939-0242ac120002"), "nightSpeedBoost", (0.75f/10f)*1,
                    AttributeModifier.Operation.MULTIPLY_BASE)
    };
    public static final AttributeModifier NIGHT_SPEED_BOOST = new AttributeModifier(
            UUID.fromString("da53c6d8-c01f-11e7-abc4-cec278b6b50a"), "nightSpeedBoost", 0.75f,
            AttributeModifier.Operation.MULTIPLY_BASE);
    public static final AttributeModifier BLOODMOON_SPEED_BOOST = new AttributeModifier(
            UUID.fromString("2ca21e76-c020-11e7-abc4-cec278b6b50a"), "bloodmoonSpeedBoost", 0.2f,
            AttributeModifier.Operation.MULTIPLY_BASE);
    public static final AttributeModifier BLOODMOON_DAMAGE_BOOST = new AttributeModifier(
            UUID.fromString("dc7572f6-d05f-4df6-afee-7fa78046ec54"), "bloodmoonDamageBoost", 0.5f,
            AttributeModifier.Operation.MULTIPLY_BASE);
    public static final AttributeModifier BLOODMOON_RANGE_BOOST = new AttributeModifier(
            UUID.fromString("4340be6a-c8bf-11e7-a80b-cec278b6b50a"), "bloodmoonRangeBoost", 0.5f,
            AttributeModifier.Operation.MULTIPLY_BASE);
    public static final AttributeModifier BLOODMOON_ARMOR_BOOST = new AttributeModifier(
            UUID.fromString("b859cf4a-b7cd-486f-9b59-ebabfdd0985e"), "bloodmoonArmorBoost", 4f,
            AttributeModifier.Operation.ADDITION);
    public final boolean nightRun = true;
    public int lightLevelPrev = -1;
    AttributeInstance speed;
    AttributeInstance range;
    AttributeInstance armor;
    AttributeInstance attack;

    protected Vec3 corpseRotation = Vec3.ZERO;
    protected Vec3 corpseTranslation = Vec3.ZERO;

    // public Horde horde;

    public ZombieBaseEntity(EntityType<? extends ZombieBaseEntity> type, Level world) {
        super(type, world);
        this.xpReward = 10;
    }

    @Override
    public void tick() {
        super.tick();

        if (speed == null) {
            speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        }

        if (range == null) {
            range = this.getAttribute(Attributes.FOLLOW_RANGE);
        }

        if (armor == null) {
            armor = this.getAttribute(Attributes.ARMOR);
        }

        if (attack == null) {
            attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
        }

        if (!this.level.isClientSide()) {
            if (LevelUtils.isBloodmoonProper(level)) {
                if (!speed.hasModifier(BLOODMOON_SPEED_BOOST)) {
                    speed.addTransientModifier(BLOODMOON_SPEED_BOOST);
                }
                if (!range.hasModifier(BLOODMOON_RANGE_BOOST)) {
                    range.addTransientModifier(BLOODMOON_RANGE_BOOST);
                }
                if (!armor.hasModifier(BLOODMOON_ARMOR_BOOST)) {
                    armor.addTransientModifier(BLOODMOON_ARMOR_BOOST);
                }
                if (!attack.hasModifier(BLOODMOON_DAMAGE_BOOST)) {
                    attack.addTransientModifier(BLOODMOON_DAMAGE_BOOST);
                }
            } else {
                if (speed.hasModifier(BLOODMOON_SPEED_BOOST)) {
                    speed.removeModifier(BLOODMOON_SPEED_BOOST);
                }
                if (range.hasModifier(BLOODMOON_RANGE_BOOST)) {
                    range.removeModifier(BLOODMOON_RANGE_BOOST);
                }
                if (armor.hasModifier(BLOODMOON_ARMOR_BOOST)) {
                    armor.removeModifier(BLOODMOON_ARMOR_BOOST);
                }
                if (attack.hasModifier(BLOODMOON_DAMAGE_BOOST)) {
                    attack.removeModifier(BLOODMOON_DAMAGE_BOOST);
                }
            }

            if (nightRun) {
                BlockPos pos = this.blockPosition();
                int light = level.getMaxLocalRawBrightness(pos);
                applyNightSpeedModifier(light);
            }
        }
    }

    private void applyNightSpeedModifier(int lightLevel){
        if(lightLevel == lightLevelPrev) return;
        if(lightLevel > 9 && lightLevelPrev < 10 && lightLevelPrev != -1){
            if (speed.hasModifier(NIGHT_SPEED_BOOSTS[lightLevelPrev])) {
                speed.removeModifier(NIGHT_SPEED_BOOSTS[lightLevelPrev]);
            }
            lightLevelPrev = lightLevel;
            return;
        }
        if(lightLevel < 10){
            if(lightLevelPrev != -1 && lightLevelPrev < 10){
                if (speed.hasModifier(NIGHT_SPEED_BOOSTS[lightLevelPrev])) {
                    speed.removeModifier(NIGHT_SPEED_BOOSTS[lightLevelPrev]);
                }
            }
            lightLevelPrev = lightLevel;
            if (!speed.hasModifier(NIGHT_SPEED_BOOSTS[lightLevel])) {
                speed.addPermanentModifier(NIGHT_SPEED_BOOSTS[lightLevel]);
            }
        }
    }

    public float getDigSpeed(BlockState p_36282_, @Nullable BlockPos pos) {
        float f = this.getMainHandItem().getDestroySpeed(p_36282_);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getBlockEfficiency(this);
            ItemStack itemstack = this.getMainHandItem();
            if (i > 0 && !itemstack.isEmpty()) {
                f += (float)(i * i + 1);
            }
        }

        if (MobEffectUtil.hasDigSpeed(this)) {
            f *= 1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2F;
        }

        if (this.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f1 = switch (this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                case 3 -> 8.1E-4F;
                default -> 8.1E-4F;
            };

            f *= f1;
        }

        if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("LightLevelPrev", lightLevelPrev);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.lightLevelPrev = compoundTag.getInt("LightLevelPrev");
    }

    public Vec3 corpseRotation() {
        return corpseRotation;
    }

    public Vec3 corpseTranslation() {
        return corpseTranslation;
    }

    public boolean customCorpseTransform() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource p_184601_1_) {
        return SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    protected void playStepSound(@NotNull BlockPos p_180429_1_, @NotNull BlockState p_180429_2_) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        super.die(damageSource);
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (ServerConfig.zombieCorpses.get()) {
            ++this.deathTime;

            if (this.deathTime == 20) {

                remove(RemovalReason.DISCARDED);
                LootableCorpseEntity lootable = new LootableCorpseEntity(level);
                lootable.setOriginal(this);
                lootable.setPos(getX(), getY(), getZ());
                dead = true;
                if (!this.level.isClientSide()) {
                    LootTable loottable = this.level.getServer().getLootTables().get(getDefaultLootTable());
                    LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.blockPosition()));
                    if (this.lastHurtByPlayer != null) {
                        lootcontext$builder.withLuck(lastHurtByPlayer.getLuck()).withParameter(LootContextParams.THIS_ENTITY, lastHurtByPlayer);
                    }
                    ItemUtils.fill(loottable, lootable.getInventory(), lootcontext$builder.create(LootContextParamSets.CHEST));
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
    }
}
