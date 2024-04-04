package nuparu.sevendaystomine.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.chunk.Chunk;

public class CapabilityHelper {

	public static IExtendedPlayer getExtendedPlayer(EntityPlayer player) {
		return player.getCapability(ExtendedPlayerProvider.EXTENDED_PLAYER_CAP, null);
	}

	public static IItemHandlerExtended getExtendedInventory(EntityPlayer player) {
		return player.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, null);
	}
	
	public static IExtendedChunk getExtendedChunk(Chunk chunk) {
		return chunk.getCapability(ExtendedChunkProvider.EXTENDED_INV_CAP, null);
	}

}