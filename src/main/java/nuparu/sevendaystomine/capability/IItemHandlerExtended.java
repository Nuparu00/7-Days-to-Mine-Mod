package nuparu.sevendaystomine.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Nameable;
import net.minecraftforge.items.IItemHandler;

public interface IItemHandlerExtended extends IItemHandler, Nameable {
    void copy(IItemHandler from);
    CompoundTag serializeNBT();
    void deserializeNBT(CompoundTag nbt);
    void setSize(int size);
}
