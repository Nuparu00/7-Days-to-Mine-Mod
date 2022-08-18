package nuparu.sevendaystomine.world.inventory;

import net.minecraft.world.entity.player.Player;

public interface IContainerCallbacks {

    void onContainerOpened(Player player);

    void onContainerClosed(Player player);

    boolean isUsableByPlayer(Player player);
}