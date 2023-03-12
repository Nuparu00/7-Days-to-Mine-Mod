package nuparu.sevendaystomine.world.inventory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class ItenMenuProvider implements MenuProvider {

    public ItemStack stack;
    public Component name;

    public ItenMenuProvider(ItemStack stack, Component name){
        this.stack = stack;
        this.name = name;
    }


    @Override
    public @NotNull Component getDisplayName() {
        return name;
    }

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int p_createMenu_1_, @NotNull Inventory p_createMenu_2_, @NotNull Player p_createMenu_3_);
}
