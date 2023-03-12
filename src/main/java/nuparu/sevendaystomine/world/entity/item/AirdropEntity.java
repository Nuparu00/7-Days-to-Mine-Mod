package nuparu.sevendaystomine.world.entity.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.world.inventory.entity.ContainerLootableCorpse;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AirdropEntity extends Entity implements MenuProvider {

    protected final LazyOptional<ExtendedInventory> inventory = LazyOptional.of(this::initInventory);

    public long age = 0;
    private boolean onEntity = false;

    private static final EntityDataAccessor<Boolean> LANDED = SynchedEntityData.defineId(AirdropEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SMOKE_TIME = SynchedEntityData.defineId(AirdropEntity.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEALTH = SynchedEntityData.defineId(AirdropEntity.class,
            EntityDataSerializers.INT);

    public AirdropEntity(EntityType<AirdropEntity> type, Level world) {
        super(type, world);
    }

    public AirdropEntity(Level world) {
        this(ModEntities.AIRDROP.get(), world);
    }

    public AirdropEntity(Level world, BlockPos pos) {
        this(world);
        this.setPos(pos.getX(),pos.getY(),pos.getZ());
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(LANDED, false);
        this.getEntityData().define(SMOKE_TIME, 0);
        this.getEntityData().define(HEALTH, 50);
    }

    public boolean getLanded() {
        return this.getEntityData().get(LANDED);
    }

    public void setLanded(boolean landed) {
        this.getEntityData().set(LANDED, landed);
    }

    public int getSmokeTime() {
        return this.getEntityData().get(SMOKE_TIME);
    }

    public void setSmokeTime(int ticks) {
        this.getEntityData().set(SMOKE_TIME, ticks);
    }

    public int getHealth() {
        return this.getEntityData().get(HEALTH);
    }

    public void setHealth(int health) {
        this.getEntityData().set(HEALTH, health);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

        onEntity = compound.getBoolean("onEntity");
        age = compound.getLong("age");
        if (compound.contains("health")) {
            setHealth(compound.getInt("health"));
        }
        if (compound.contains("smokeTime")) {
            setSmokeTime(compound.getInt("smokeTime"));
        }
        if (compound.contains("landed")) {
            setLanded(compound.getBoolean("landed"));
        }
        if (getInventory() != null && compound.contains("ItemHandler")) {
            getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

        compound.putBoolean("onEntity", onEntity);
        compound.putLong("age", age);
        compound.putInt("health", getHealth());
        compound.putInt("smokeTime", getSmokeTime());
        compound.putBoolean("landed", getLanded());
        if (getInventory() != null) {
            compound.put("ItemHandler", getInventory().serializeNBT());
        }
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public @NotNull InteractionResult interact(Player playerEntity, @NotNull InteractionHand hand) {
        if (!playerEntity.isCrouching() && hand == InteractionHand.MAIN_HAND) {
            if (playerEntity instanceof ServerPlayer serverPlayer) {
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
        if (!level.isClientSide()) {
            if (this.age >= ServerConfig.airdropLifespan.get()) {
                this.kill();
                return;
            }
        }


        if (getLanded() && getSmokeTime() > 0) {
            double height = this.getDimensions(this.getPose()).height;
            for (int i = 0; i < random.nextInt(3) + 1; i++) {
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.position().x, this.position().y + height
                        , this.position().z,
                        MathUtils.getFloatInRange(-0.02f, 0.02f), MathUtils.getFloatInRange(0.2f, 0.5f),
                        MathUtils.getFloatInRange(-0.02f, 0.02f));
                level.addParticle(ParticleTypes.CLOUD, this.position().x, this.position().y + height, this.position().z,
                        MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.2f, 0.5f),
                        MathUtils.getFloatInRange(-0.1f, 0.1f));
            }
            setSmokeTime(getSmokeTime() - 1);
        }

        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        if (!onGround && !onEntity) {
            motionY = !getLanded() ? -0.0625 : -0.1911;
        } else {
            motionY = 0;
            if (!level.isClientSide()) {
                if (!getLanded()) {
                    setSmokeTime(1200);
                    setLanded(true);
                }
            }
        }
        if (this.onGround) {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }
        this.setDeltaMovement(new Vec3(motionX, motionY, motionZ));
        this.move(MoverType.SELF, this.getDeltaMovement());

        checkOutOfWorld();

        boolean flag = false;
        for (Entity entity : this.level.getEntities(this, getBoundingBox())) {
            if (entity instanceof Player)
                continue;
            if (!this.hasPassenger(entity) && entity.canBeCollidedWith()) {
                flag = true;
            }
        }
        this.onEntity = flag;

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
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
        int healthNew = (int) (getHealth() - amount);
        this.setHealth(healthNew);
        if (healthNew <= 0) {
            if (getInventory() != null) {
                for (int i = 0; i < getInventory().getSlots(); i++) {
                    ItemStack stack = getInventory().getStackInSlot(i);
                    Containers.dropItemStack(level, getX() + this.getBbWidth() / 2, getY(),
                            getZ() + this.getBbWidth() / 2, stack);
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
        return ContainerLootableCorpse.createContainerServerSide(windowiD, playerInventory, this);
    }

    public static BlockPos getAirdropPos(Level world) {
        List<? extends Player> players = world.players();
        double xSum = 0;
        double zSum = 0;

        if (players.size() == 1) {
            Player player = players.get(0);

            double angle = 2.0 * Math.PI * world.random.nextDouble();
            double dist = 256 + world.random.nextDouble() * 256;
            double x = player.getX() + dist * Math.cos(angle);
            double z = player.getZ() + dist * Math.sin(angle);

            return new BlockPos(x, 255, z);
        }

        for (Player player : players) {
            xSum += player.getX();
            zSum += player.getZ();
        }

        double angle = 2.0 * Math.PI * world.random.nextDouble();
        double dist = 256 + world.random.nextDouble() * 256;
        double x = xSum / players.size() + dist * Math.cos(angle);
        double z = zSum / players.size() + dist * Math.sin(angle);

        return new BlockPos(x, 255, z);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        double d0 = this.getBoundingBox().getSize();
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 512.0D * getViewScale();
        return p_70112_1_ < d0 * d0;
    }
}
