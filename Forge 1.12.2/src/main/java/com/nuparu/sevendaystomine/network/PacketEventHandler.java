package com.nuparu.sevendaystomine.network;

import com.nuparu.sevendaystomine.capability.CapabilityHelper;
import com.nuparu.sevendaystomine.capability.IExtendedPlayer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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