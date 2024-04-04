package nuparu.sevendaystomine.network.packets;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.SevenDaysToMine;

public class SchedulePhotoHandler implements IMessageHandler<SchedulePhotoMessage, SchedulePhotoMessage> {

	@Override
	public SchedulePhotoMessage onMessage(SchedulePhotoMessage message, MessageContext ctx) {
		SevenDaysToMine.proxy.schedulePhoto();
		return null;
	}

}
