package com.nuparu.sevendaystomine.events;

import com.nuparu.sevendaystomine.entity.EntityHuman;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class HumanResponseEvent extends Event {

	public HumanResponseEvent(EntityHuman human, EntityPlayer player, String dialogue) {
		
	}
}
