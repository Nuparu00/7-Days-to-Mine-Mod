package nuparu.sevendaystomine.capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;

public class CapabilityHelper {
    public static IExtendedPlayer getExtendedPlayer(Player player) {
        return player.getCapability(ExtendedPlayerProvider.EXTENDED_PLAYER_CAPABILITY, null).orElse(null);
    }
    public static IChunkData getChunkData(LevelChunk chunk) {
        return chunk.getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null).orElse(null);
    }

    public static IExtendedEntity getExtendedEntity(Entity entity) {
        return entity.getCapability(ExtendedEntityProvider.EXTENDED_ENTITY_CAPABILITY, null).orElse(null);
    }

}
