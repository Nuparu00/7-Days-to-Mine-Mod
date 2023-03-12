package nuparu.sevendaystomine.world.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IBattery {
    long getCapacity(ItemStack stack, @Nullable Level world);
    long getVoltage(ItemStack stack, @Nullable Level world);
    void setVoltage(ItemStack stack, @Nullable Level world, long voltage);
    long tryToAddVoltage(ItemStack stack, @Nullable Level world, long deltaVoltage);
    void drainVoltage(ItemStack stack, @Nullable Level world, long deltaVoltage);
}
