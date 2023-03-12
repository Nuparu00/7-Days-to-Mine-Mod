package nuparu.sevendaystomine.world.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ItemHandlerNameable extends ItemStackHandler implements IItemHandlerNameable {

    private final Component defaultName;

    private Component displayName;

    public ItemHandlerNameable(Component defaultName) {
        this.defaultName = defaultName.copy();
    }

    public ItemHandlerNameable(int size, Component defaultName) {
        super(size);
        this.defaultName = defaultName.copy();
    }

    public ItemHandlerNameable(NonNullList<ItemStack> stacks, Component defaultName) {
        super(stacks);
        this.defaultName = defaultName.copy();
    }

    @Override
    public @NotNull Component getName() {
        return getDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return displayName != null;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return hasCustomName() ? displayName : defaultName;
    }

    public void setDisplayName(Component displayName) {
        this.displayName = displayName.copy();
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tagCompound = super.serializeNBT();

        if (hasCustomName()) {
            tagCompound.putString("DisplayName", Component.Serializer.toJson(getDisplayName()));
        }

        return tagCompound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);

        if (nbt.contains("DisplayName")) {
            setDisplayName(Component.Serializer.fromJson(nbt.getString("DisplayName")));
        }
    }

    public boolean isEmpty(){
        for (ItemStack stack: this.stacks) {
            if(!stack.isEmpty()) return false;
        }
        return true;
    }

    public void clear(){
        stacks.clear();
    }

}
