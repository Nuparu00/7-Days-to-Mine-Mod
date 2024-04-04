package nuparu.sevendaystomine.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import nuparu.sevendaystomine.entity.EntityHuman;

@Cancelable
public class HumanResponseEvent extends Event {

	public HumanResponseEvent(EntityHuman human, EntityPlayer player, String dialogue) {
		
	}
}
