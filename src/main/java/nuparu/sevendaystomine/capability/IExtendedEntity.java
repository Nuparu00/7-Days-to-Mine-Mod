package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import nuparu.sevendaystomine.world.level.horde.Horde;

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
