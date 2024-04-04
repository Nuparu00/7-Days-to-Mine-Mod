package nuparu.sevendaystomine.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class PacketEventHandler {

	@SubscribeEvent
	public void playerStartedTracking(PlayerEvent.StartTracking event) {
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		if (target instanceof EntityPlayer) {

			EntityPlayer targetPlayer = (EntityPlayer) target;
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(targetPlayer);
			extendedPlayer.onStartedTracking(targetPlayer);
		}
	}
}