package nuparu.sevendaystomine.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CommonProxy {
    public Player getPlayerEntityFromContext(Supplier<NetworkEvent.Context> ctx) {
        return ctx.get().getSender();
    }

    public int getQualityForCurrentPlayer(){
        return 0;
    }

    public Level getLevel(){
        return null;
    }

    public void openClientSideGui(int id, int x, int y, int z) {

    }
}
