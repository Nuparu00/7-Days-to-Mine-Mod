package nuparu.sevendaystomine.world.level.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IContainerBlockWithSounds {

    @Nullable
    default SoundEvent getOpeningSound(Level level, Player player){
        return null;
    }


    @Nullable
    default SoundEvent getClosingSound(Level level, Player player){
        return null;
    }
}
