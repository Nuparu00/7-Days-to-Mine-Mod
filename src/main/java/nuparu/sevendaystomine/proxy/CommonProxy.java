package nuparu.sevendaystomine.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CommonProxy {
    public Player getPlayerEntityFromContext(Supplier<NetworkEvent.Context> ctx) {
        return ctx.get().getSender();
    }

    public int getQualityForCurrentPlayer(){
        return 0;
    }

    @Nullable
    public Player getPlayer(){
        return null;
    }
    @Nullable
    public Level getLevel(){
        return null;
    }

    public void openClientSideGui(int id, int x, int y, int z) {

    }

    public void openClientOnlyGui(int id, ItemStack stack) {

    }

    public void addNotificationToast(ItemStack stack, Component title, Component description){

    }


    public void openPhoto(String path){

    }
    public void schedulePhoto() {

    }
}
