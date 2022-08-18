package nuparu.sevendaystomine.world.entity.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModEntities;

import java.util.List;

public class MountableBlockEntity extends Entity {

    private BlockState blockState = null;
    private BlockPos blockPos = BlockPos.ZERO;

    private static final EntityDataAccessor<Float> DELTA_Y = SynchedEntityData.defineId(MountableBlockEntity.class,
            EntityDataSerializers.FLOAT);

    public MountableBlockEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public MountableBlockEntity(Level world) {
        this(ModEntities.MOUNTABLE_BLOCK.get(), world);
    }

    public MountableBlockEntity(Level world, double x, double y, double z) {
        this(world);
        this.setPos(x, y, z);
        blockPos = new BlockPos(x, y, z);
        blockState = world.getBlockState(blockPos);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!level.isClientSide()) {
            if (this.getPassengers().isEmpty() || this.level.getBlockState(blockPos) != blockState) {
                this.kill();
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return getDeltaY();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DELTA_Y, 0f);
    }

    public void setDeltaY(float deltaY) {
        this.entityData.set(DELTA_Y, deltaY);
    }

    public float getDeltaY() {
        return this.entityData.get(DELTA_Y);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundNBT) {
        setDeltaY(compoundNBT.getFloat("deltaY"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundNBT) {
        compoundNBT.putDouble("deltaY", getDeltaY());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public static boolean mountBlock(Level world, BlockPos pos, Player player) {
        return mountBlock(world, pos, player, 0);
    }

    public static boolean mountBlock(Level world, BlockPos pos, Player player, float deltaY) {
        double x = pos.getX()+0.5;
        double y = pos.getY();
        double z = pos.getZ()+0.5;
        List<MountableBlockEntity> list = world.getEntitiesOfClass(MountableBlockEntity.class,
                new AABB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D).inflate(1D));
        for (MountableBlockEntity mount : list) {
            if (mount.getBlockPos() == pos) {
                if (!mount.isVehicle()) {
                    player.startRiding(mount);
                }
                return true;
            }
        }
        if (list.size() == 0) {
            MountableBlockEntity mount = new MountableBlockEntity(world, x, y, z);
            mount.setDeltaY(deltaY);
            world.addFreshEntity(mount);
            player.startRiding(mount);
            return true;
        }
        return false;
    }
}
