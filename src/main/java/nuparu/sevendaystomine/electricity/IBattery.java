package nuparu.sevendaystomine.electricity;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBattery {

	public long getCapacity(ItemStack stack,@Nullable World world);
	public long getVoltage(ItemStack stack,@Nullable World world);
	public void setVoltage(ItemStack stack,@Nullable World world, long voltage);
	public long tryToAddVoltage(ItemStack stack,@Nullable World world, long deltaVoltage);
	public void drainVoltage(ItemStack stack,@Nullable World world, long deltaVoltage);
}
