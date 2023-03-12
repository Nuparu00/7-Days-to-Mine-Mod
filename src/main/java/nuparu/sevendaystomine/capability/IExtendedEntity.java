package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import nuparu.sevendaystomine.world.level.horde.Horde;

import java.util.List;

public interface IExtendedEntity {
    Horde getHorde();
    void setHorde(Horde horde);
    boolean hasHorde();
    void readNBT(CompoundTag nbt);
    CompoundTag writeNBT(CompoundTag nbt);
    void copy(IExtendedEntity iep);
    Entity getOwner();
    void setOwner(Entity owner);
}
