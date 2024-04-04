package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public interface IReloadable {

	Item getReloadItem(ItemStack stack);
	int getReloadTime(ItemStack stack);
	SoundEvent getReloadSound();
	void onReloadStart(World world, EntityPlayer player, ItemStack stack, int reloadTime);
	void onReloadEnd(World world,EntityPlayer player, ItemStack stack, ItemStack bullet);
	int getAmmo(ItemStack stack, EntityPlayer player);
	void setAmmo(ItemStack stack, EntityPlayer player, int ammo);
	int getCapacity(ItemStack stack, EntityPlayer player);
}
