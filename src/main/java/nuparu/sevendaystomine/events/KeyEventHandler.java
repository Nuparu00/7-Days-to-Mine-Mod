package nuparu.sevendaystomine.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityCar;
import nuparu.sevendaystomine.item.ItemAnalogCamera;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.CameraDimensionsMessage;
import nuparu.sevendaystomine.network.packets.HonkMessage;
import nuparu.sevendaystomine.proxy.ClientProxy;
import nuparu.sevendaystomine.util.ReloadHelper;

public class KeyEventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onKeyPressed(KeyInputEvent event) {
		KeyBinding[] keyBindings = ClientProxy.keyBindings;
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player != null) {
			if (keyBindings[0].isPressed()) {
				ReloadHelper.tryToReload();
			} else {
				ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
				if (!stack.isEmpty() && stack.getItem() instanceof ItemAnalogCamera) {
					if (keyBindings[1].isPressed()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0.1,0,0));
					} else if (keyBindings[2].isPressed()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(-0.1,0,0));
					} else if (keyBindings[3].isPressed()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0.1,0));
					} else if (keyBindings[4].isPressed()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,-0.1,0));
					} else if (keyBindings[5].isPressed()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0,0.1));
					} else if (keyBindings[6].isPressed()) {
						PacketManager.cameraDimensions.sendToServer(new CameraDimensionsMessage(0,0,-0.1));
					}
				}
			}
			if(keyBindings[7].isPressed()) {
				Entity riding = player.getRidingEntity();
				if(riding != null && riding instanceof EntityCar) {
					if(riding.getPassengers().indexOf(player) == 0) {
						System.out.println("Beep beep");
						PacketManager.honk.sendToServer(new HonkMessage());
					}
				}
			}
		}

		/*
		 * if (player != null) { Entity riding = player.getRidingEntity(); KeyBinding
		 * accelerate = keyBindings[1]; if (accelerate.isPressed() ||
		 * accelerate.isKeyDown()) { if (riding != null && riding instanceof
		 * IControllable) { ((IControllable)riding).handleKey(0, (byte)1);
		 * PacketManager.controllableKeyUpdate.sendToServer(new
		 * ControllableKeyUpdateMessage(0, (byte) 1)); } } else if
		 * (!accelerate.isPressed() && !accelerate.isKeyDown()) { if (riding != null &&
		 * riding instanceof IControllable) { ((IControllable)riding).handleKey(0,
		 * (byte)0); PacketManager.controllableKeyUpdate.sendToServer(new
		 * ControllableKeyUpdateMessage(0, (byte) 0)); } } }
		 */
	}
}
