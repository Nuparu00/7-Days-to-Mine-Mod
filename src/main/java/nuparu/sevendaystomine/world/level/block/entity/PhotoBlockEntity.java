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
import net.minecraft.world.phys.AABB;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.init.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

public class PhotoBlockEntity extends BlockEntity {
    private String path;

    //Client only
    public long nextUpdate;
    public ResourcesHelper.Image image;
    public PhotoBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PHOTO.get(), pos, state);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        if(compound.contains("Path", Tag.TAG_STRING)){
            this.path = compound.getString("Path");
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putString("Path", this.path);
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

    public String getPath(){
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        setChanged();
    }


    @Override
    public AABB getRenderBoundingBox()
    {
        return super.getRenderBoundingBox().inflate(2);
    }
}
