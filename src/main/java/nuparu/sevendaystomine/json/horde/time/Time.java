package nuparu.sevendaystomine.json.horde.time;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface Time {
    default boolean test(Player player, Level level) {
        return false;
    }
}
