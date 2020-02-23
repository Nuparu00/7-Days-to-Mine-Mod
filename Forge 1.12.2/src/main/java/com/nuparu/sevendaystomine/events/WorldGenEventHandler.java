package com.nuparu.sevendaystomine.events;

import com.nuparu.sevendaystomine.SevenDaysToMine;
import com.nuparu.sevendaystomine.world.gen.city.MapGenCity;

import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldGenEventHandler {

	//Called after chunk has been populated
	
	@SubscribeEvent
	public void onInitMapGenEvent(InitMapGenEvent event) {
		//System.out.println("AAAAAAAAAAAAAAAA");
		//if(event.getOriginalGen() instanceof MapGenVillage) {
			//event.setNewGen(new MapGenCity());
		//}
	}
}
