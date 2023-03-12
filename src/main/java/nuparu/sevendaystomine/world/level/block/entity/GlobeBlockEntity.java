package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

public class GlobeBlockEntity extends BlockEntity {

    private double speed;
    private double anglePrev;
    private double angle;
    public GlobeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GLOBE.get(),pos,state);
    }

    public void tick() {
        this.anglePrev = angle;
        if (speed != 0) {
            speed = 0.98f * speed;
        }
        angle += speed;
        while (angle > 360) {
            angle -=360;
            anglePrev -= 360;
        }
        while (angle < 0) {
            angle +=360;
            anglePrev += 360;
        }
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        if(compound.contains("Angle", Tag.TAG_DOUBLE)){
            this.angle = compound.getInt("Angle");
        }
        if(compound.contains("Speed", Tag.TAG_DOUBLE)){
            this.speed = compound.getInt("Speed");
        }
        if(compound.contains("AnglePrev", Tag.TAG_DOUBLE)){
            this.anglePrev = compound.getInt("AnglePrev");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        //compound.putDouble("Angle", this.angle);
        compound.putDouble("Speed", this.speed);
        //compound.putDouble("AnglePrev", this.anglePrev);
    }

    public void addSpeed(double speed) {
        this.speed += speed;
        markForUpdate();
    }

    public double getSpeed() {
        return this.speed;
    }
    public double getAngle() {
        return this.angle;
    }

    public double getAnglePrev() {
        return this.anglePrev;
    }


    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket  pkt) {
        CompoundTag tag = pkt.getTag();
        load(tag);
        if (hasLevel()) {
            level.sendBlockUpdated(pkt.getPos(), level.getBlockState(this.worldPosition),
                    level.getBlockState(pkt.getPos()), 2);
        }
    }

    public void markForUpdate() {
        level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
                level.getBlockState(worldPosition), 3);
        setChanged();
    }

}
