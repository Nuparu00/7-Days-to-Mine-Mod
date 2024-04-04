package nuparu.sevendaystomine.network.packets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import nuparu.sevendaystomine.client.sound.SoundHelper;
import nuparu.sevendaystomine.entity.EntityCar;

public class HonkHandler implements IMessageHandler<HonkMessage, HonkMessage> {

	@Override
	public HonkMessage onMessage(HonkMessage message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().player;
		Entity riding = player.getRidingEntity();
		if(riding != null && riding instanceof EntityCar) {
			if(riding.getPassengers().indexOf(player) == 0) {
				System.out.println("Beep beep beep");
				player.world.playSound(null,player.posX, player.posY, player.posZ, SoundHelper.HONK, SoundCategory.PLAYERS, player.world.rand.nextFloat()*0.2f+1.4f, player.world.rand.nextFloat()*0.05f+0.8f);
			}
		}

		return null;
	}

	

}
