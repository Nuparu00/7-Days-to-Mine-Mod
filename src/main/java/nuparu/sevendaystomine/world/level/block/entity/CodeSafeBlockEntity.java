package nuparu.sevendaystomine.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.sevendaystomine.init.ModBlockEntities;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.world.level.block.CodeSafeBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CodeSafeBlockEntity extends SmallContainerBlockEntity{

    private boolean locked = true;
    private boolean init = false;
    private int correctCode = 0;
    private int selectedCode = 0;

    private final ArrayList<Player> lookingPlayers = new ArrayList<>();

    public CodeSafeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CODE_SAFE.get(),pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.locked = compound.getBoolean("Locked");
        this.init = compound.getBoolean("Init");
        correctCode = compound.getInt("CorrectCode");
        selectedCode = compound.getInt("SelectedCode");

    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("Locked", this.locked);
        compound.putBoolean("Init", this.init);
        compound.putInt("CorrectCode", correctCode);
        compound.putInt("SelectedCode", selectedCode);
    }

    public void unlock() {
        level.playSound(null, worldPosition, ModSounds.SAFE_UNLOCK.get(), SoundSource.BLOCKS, 0.9f + level.random.nextFloat() / 4f,
                0.9f + level.random.nextFloat() / 4f);
        locked = false;
        setChanged();
        CodeSafeBlock.setState(locked, level, worldPosition);
    }

    public void lock() {
        locked = true;
        setChanged();
        CodeSafeBlock.setState(locked, level, worldPosition);
    }


    public boolean tryToUnlock() {
        if (correctCode == selectedCode && locked) {
            unlock();
            return true;
        } else if (correctCode != selectedCode && !locked) {
            lock();
            return false;
        }
        return false;

    }

    public boolean isLocked(){
        return this.locked;
    }

    public void tick() {
        if (!level.isClientSide()) {
            if (!init) {
                RandomSource random = level.random;
                while (correctCode == selectedCode && locked) {
                    correctCode = random.nextInt(1000);
                    selectedCode = random.nextInt(1000);
                }
                setInit(true);
            }
            tryToUnlock();
        }
    }

    public void setInit(boolean init) {
        this.init = init;
        setChanged();
    }

    public int getSelectedCode() {
        return this.selectedCode;
    }

    public int getCorrectCode() {
        return this.correctCode;
    }

    public void setSelectedCode(int code, @Nullable ServerPlayer player ) {
        this.selectedCode = code;
        boolean prevState = locked;
        tryToUnlock();
        System.out.println(this.getCorrectCode());
        if(player != null && prevState && !locked) {
            //ModTriggers.SAFE_UNLOCK.trigger(player, o -> true);
        }
        setChanged();
    }

    public void setCorrectCode(int code) {
        this.correctCode = code;
        setChanged();
    }

    public void markForUpdate() {
        level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
                level.getBlockState(worldPosition), 3);
        setChanged();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = saveWithoutMetadata();
        nbt.remove("CorrectCode");
        return nbt;
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

    @Override
    public void onContainerOpened(Player player) {
       super.onContainerOpened(player);
       lookingPlayers.add(player);
    }

    @Override
    public void onContainerClosed(Player player) {
        super.onContainerClosed(player);
        lookingPlayers.remove(player);
    }

    public boolean isLooking(Player player){
        return lookingPlayers.contains(player);
    }
}
