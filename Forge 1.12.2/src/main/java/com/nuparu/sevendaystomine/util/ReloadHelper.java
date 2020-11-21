package com.nuparu.sevendaystomine.util;

import com.nuparu.sevendaystomine.item.IReloadable;
import com.nuparu.sevendaystomine.item.ItemGun;
import com.nuparu.sevendaystomine.network.PacketManager;
import com.nuparu.sevendaystomine.network.packets.ReloadMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ReloadHelper {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	@SideOnly(Side.CLIENT)
	public static void tryToReload() {
		EntityPlayer player = mc.player;
		if(player == null) return;
		
		ItemStack mainStack = player.getHeldItemMainhand();
		ItemStack secStack = player.getHeldItemMainhand();
		if(mainStack.isEmpty() && secStack.isEmpty()) return;
		
		Item mainItem = mainStack.getItem();
		Item secItem = secStack.getItem();
		if(mainItem instanceof IReloadable || secItem instanceof IReloadable) {
			PacketManager.gunReload.sendToServer(new ReloadMessage());
		}
	}
}
