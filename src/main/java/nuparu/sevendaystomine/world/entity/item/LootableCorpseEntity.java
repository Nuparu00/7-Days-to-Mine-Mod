package nuparu.sevendaystomine.world.entity.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.world.entity.EntityUtils;
import nuparu.sevendaystomine.world.inventory.entity.ContainerLootableCorpse;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LootableCorpseEntity extends Entity implements MenuProvider {

    protected final LazyOptional<ExtendedInventory> inventory = LazyOptional.of(this::initInventory);

    public int health = 40;
    public long age = 0;
    private boolean onEntity = false;
    private static final EntityDataAccessor<CompoundTag> ORIGINAL_NBT = SynchedEntityData
            .defineId(LootableCorpseEntity.class, EntityDataSerializers.COMPOUND_TAG);

    Entity originalCached = null;

    public LootableCorpseEntity(EntityType<LootableCorpseEntity> type, Level world) {
        super(type, world);
    }

    public LootableCorpseEntity(Level world) {
        this(ModEntities.LOOTABLE_CORPSE.get(), world);
    }

    public void setOriginal(Entity entity) {
        if (entity instanceof LivingEntity living) {
            living.hurtTime = 0;
            living.swingTime = 0;
            living.deathTime = 0;
        }

        this.setXRot(entity.getXRot());
        this.xRotO = entity.xRotO;
        this.setYRot(entity.getYRot());
        this.yRotO = entity.yRotO;
        this.setYHeadRot(entity.getYHeadRot());

        entity.setDeltaMovement(new Vec3(0, 0, 0));
        entity.setPose(Pose.STANDING);
        entity.clearFire();
        entity.setYHeadRot(0);
        entity.setXRot(0);
        entity.setYRot(0);

        CompoundTag nbt = new CompoundTag();

        CompoundTag entityNBT = entity.saveWithoutId(new CompoundTag());
        entityNBT.putString("id", entity.getEncodeId());
        nbt.put("entity", entityNBT);
        nbt.putString("resourceLocation", EntityType.getKey(entity.getType()).toString());
        setOriginalNBT(nbt);
    }

    public Entity getOriginal() {
        if (originalCached == null) {
            CompoundTag nbt = getOriginalNBT();
            originalCached = EntityUtils.getEntityByNBTAndResource(new ResourceLocation(nbt.getString("resourceLocation")),
                    nbt.getCompound("entity"), level());
        }
        return originalCached;
    }

    public void setOriginalNBT(CompoundTag nbt) {
        this.getEntityData().set(ORIGINAL_NBT, nbt);
    }

    public CompoundTag getOriginalNBT() {
        return this.getEntityData().get(ORIGINAL_NBT);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(ORIGINAL_NBT, new CompoundTag());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

        if (compound.contains("originalEntity")) {
            setOriginalNBT(compound.getCompound("originalEntity"));
        }
        onEntity = compound.getBoolean("onEntity");
        age = compound.getLong("age");
        health = compound.getInt("health");
        if (getInventory() != null && compound.contains("ItemHandler")) {
            getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

        compound.put("originalEntity", getOriginalNBT());
        compound.putBoolean("onEntity", onEntity);
        compound.putLong("age", age);
        compound.putInt("health", health);
        if (getInventory() != null) {
            compound.put("ItemHandler", getInventory().serializeNBT());
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public @NotNull InteractionResult interact(Player playerEntity, @NotNull InteractionHand hand) {
        if(!playerEntity.isCrouching() && hand == InteractionHand.MAIN_HAND) {
            if(playerEntity instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer, this, (packetBuffer) -> packetBuffer.writeInt(this.getId()));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }


    @Override
    public void tick() {
        super.tick();

        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();

        this.age++;
        if (!level().isClientSide()) {
            if (this.age >= ServerConfig.corpseLifespan.get()) {
                this.kill();
                return;
            }
        }

        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        if (!onGround() && !onEntity) {
            motionY -= 0.03999999910593033D;
        } else {
            motionY = 0;
        }
        if (this.onGround()) {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }
        this.setDeltaMovement(new Vec3(motionX, motionY, motionZ));
        this.move(MoverType.SELF, this.getDeltaMovement());

        this.checkBelowWorld();

        boolean flag = false;
        for (Entity entity : this.level().getEntities(this, getBoundingBox())) {
            if (entity instanceof Player)
                continue;
            if (!this.hasPassenger(entity) && entity.canBeCollidedWith()) {
                flag = true;
            }
        }
        this.onEntity = flag;

    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.age < 20)
            return super.hurt(source, amount);
        if (this.level().isClientSide()) {
            level().playLocalSound(this.getX(), this.getY(), this.getZ(),
                    SoundEvents.GENERIC_HURT, SoundSource.HOSTILE, 1.0F, 1.0F, false);
        }
        else {
            /*for (int i = 0; i < MathUtils.getIntInRange(level.random, 20, 35); i++) {
                PacketManager.sendToTrackingEntity(PacketManager.spawnBlood, new SpawnBloodMessage(position().x, getY() + getBbHeight() * MathUtils.getFloatInRange(0.4f, 0.75f), position().z, MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.1f, 0.22f), MathUtils.getFloatInRange(-0.1f, 0.1f)), () -> this);
            }*/
        }
        if (source.getDirectEntity() instanceof ServerPlayer player) {
            if (!player.isCreative()) {
                ItemStack s = player.getMainHandItem();
                if (s.getMaxDamage() > 0) {
                    s.hurt(1, this.random, player);
                    if (s.getDamageValue() >= s.getMaxDamage()) {
                        s.setCount(0);
                    }
                }
            }
        }
        this.health -= amount;
        if (this.health <= 0) {
            if (getInventory() != null) {
                for (int i = 0; i < getInventory().getSlots(); i++) {
                    ItemStack stack = getInventory().getStackInSlot(i);

                    level().addFreshEntity(new ItemEntity(level(), getX() + this.getBbWidth() / 2, getY(),
                            getZ() + this.getBbWidth() / 2, stack));
                }
            }
            this.kill();
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public ExtendedInventory getInventory() {
        return this.inventory.orElse(null);
    }

    public int getInventorySize() {
        return 9;
    }

    protected ExtendedInventory initInventory() {
        return new ExtendedInventory(getInventorySize());
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        this.getInventory().setStackInSlot(inventorySlot, itemStackIn);
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
    public AbstractContainerMenu createMenu(int windowiD, @NotNull Inventory playerInventory, @NotNull Player playerEntity) {
        return ContainerLootableCorpse.createContainerServerSide(windowiD,playerInventory,this);
    }

    @Override
    public @NotNull Component getDisplayName() {
        if (this.getOriginal() != null){
            return getOriginal().getDisplayName();
        }
        return super.getDisplayName();
    }
}
