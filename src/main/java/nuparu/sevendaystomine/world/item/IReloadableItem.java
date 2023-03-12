package nuparu.sevendaystomine.world.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IReloadableItem {
    Item getReloadItem(ItemStack stack);
    int getReloadTime(ItemStack stack);
    SoundEvent getReloadSound();
    void onReloadStart(Level world, Player player, ItemStack stack, int reloadTime);
    void onReloadEnd(Level world,Player player, ItemStack stack, ItemStack bullet);
    int getAmmo(ItemStack stack, Player player);
    void setAmmo(ItemStack stack, @Nullable Player player, int ammo);
    int getCapacity(ItemStack stack, @Nullable Player player);
}
