package com.nuparu.sevendaystomine.events;

import com.nuparu.sevendaystomine.world.gen.structure.MapGenNull;

import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TerrainGenEventHandler {

	// Called after chunk has been populated

	@SubscribeEvent
	public void onInitMapGenEvent(final InitMapGenEvent event) {

		// Hook village generation
		if (event.getType() == EventType.VILLAGE && event.getNewGen() instanceof MapGenVillage) {
			event.setNewGen(new MapGenNull());
		}
	}
}
