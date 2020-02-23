package com.nuparu.sevendaystomine.events;

import com.nuparu.sevendaystomine.entity.IControllable;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.ControllableKeyUpdateMessage;
import com.nuparu.sevendaystomine.proxy.ClientProxy;
import com.nuparu.sevendaystomine.util.ReloadHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeyEventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onKeyPressed(KeyInputEvent event) {
		KeyBinding[] keyBindings = ClientProxy.keyBindings;
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;

		if (keyBindings[0].isPressed()) {
			ReloadHelper.tryToReload();
		}
		/*
		if (player != null) {
			Entity riding = player.getRidingEntity();
			KeyBinding accelerate = keyBindings[1];
			if (accelerate.isPressed() || accelerate.isKeyDown()) {
				if (riding != null && riding instanceof IControllable) {
					((IControllable)riding).handleKey(0, (byte)1);
					PacketManager.controllableKeyUpdate.sendToServer(new ControllableKeyUpdateMessage(0, (byte) 1));
				}
			}
			else if (!accelerate.isPressed() && !accelerate.isKeyDown()) {
				if (riding != null && riding instanceof IControllable) {
					((IControllable)riding).handleKey(0, (byte)0);
					PacketManager.controllableKeyUpdate.sendToServer(new ControllableKeyUpdateMessage(0, (byte) 0));
				}
			}
		}*/
	}
}
