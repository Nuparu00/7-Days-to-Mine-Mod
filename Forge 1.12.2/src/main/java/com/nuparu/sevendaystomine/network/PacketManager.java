package com.nuparu.sevendaystomine.network;

import com.nuparu.sevendaystomine.network.packets.AddSubtitleHandler;
import com.nuparu.sevendaystomine.network.packets.AddSubtitleMessage;
import com.nuparu.sevendaystomine.network.packets.BreakSyncHandler;
import com.nuparu.sevendaystomine.network.packets.BreakSyncMessage;
import com.nuparu.sevendaystomine.network.packets.ControllableKeyUpdateHandler;
import com.nuparu.sevendaystomine.network.packets.ControllableKeyUpdateMessage;
import com.nuparu.sevendaystomine.network.packets.DialogueSelectionHandler;
import com.nuparu.sevendaystomine.network.packets.DialogueSelectionMessage;
import com.nuparu.sevendaystomine.network.packets.KillProcessHandler;
import com.nuparu.sevendaystomine.network.packets.KillProcessMessage;
import com.nuparu.sevendaystomine.network.packets.OpenGuiClientHandler;
import com.nuparu.sevendaystomine.network.packets.OpenGuiClientMessage;
import com.nuparu.sevendaystomine.network.packets.PhotoRequestHandler;
import com.nuparu.sevendaystomine.network.packets.PhotoRequestMessage;
import com.nuparu.sevendaystomine.network.packets.PhotoToClientHandler;
import com.nuparu.sevendaystomine.network.packets.PhotoToClientMessage;
import com.nuparu.sevendaystomine.network.packets.PhotoToServerHandler;
import com.nuparu.sevendaystomine.network.packets.PhotoToServerMessage;
import com.nuparu.sevendaystomine.network.packets.PlayerCapabilitySyncHandler;
import com.nuparu.sevendaystomine.network.packets.PlayerCapabilitySyncMessage;
import com.nuparu.sevendaystomine.network.packets.ReloadHandler;
import com.nuparu.sevendaystomine.network.packets.ReloadMessage;
import com.nuparu.sevendaystomine.network.packets.SafeCodeHandler;
import com.nuparu.sevendaystomine.network.packets.SafeCodeMessage;
import com.nuparu.sevendaystomine.network.packets.StartProcessHandler;
import com.nuparu.sevendaystomine.network.packets.StartProcessMessage;
import com.nuparu.sevendaystomine.network.packets.SyncIconHandler;
import com.nuparu.sevendaystomine.network.packets.SyncIconMessage;
import com.nuparu.sevendaystomine.network.packets.SyncInventoryHandler;
import com.nuparu.sevendaystomine.network.packets.SyncInventoryMessage;
import com.nuparu.sevendaystomine.network.packets.SyncProcessHandler;
import com.nuparu.sevendaystomine.network.packets.SyncProcessMessage;
import com.nuparu.sevendaystomine.network.packets.SyncThrottleHandler;
import com.nuparu.sevendaystomine.network.packets.SyncThrottleMessage;
import com.nuparu.sevendaystomine.network.packets.SyncTileEntityHandler;
import com.nuparu.sevendaystomine.network.packets.SyncTileEntityMessage;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketManager {

	public static PacketManager INSTANCE;

	public static SimpleNetworkWrapper playerCapabilitySync;
	public static SimpleNetworkWrapper blockBreakSync;
	public static SimpleNetworkWrapper gunReload;
	public static SimpleNetworkWrapper openGuiClient;
	public static SimpleNetworkWrapper syncTileEntity;
	public static SimpleNetworkWrapper safeCodeUpdate;
	public static SimpleNetworkWrapper startProcess;
	public static SimpleNetworkWrapper syncIcon;
	public static SimpleNetworkWrapper photoToServer;
	public static SimpleNetworkWrapper photoRequest;
	public static SimpleNetworkWrapper photoToClient;
	public static SimpleNetworkWrapper dialogueSelection;
	public static SimpleNetworkWrapper addSubtitle;
	public static SimpleNetworkWrapper syncInventory;
	public static SimpleNetworkWrapper controllableKeyUpdate;
	public static SimpleNetworkWrapper syncThrottle;
	public static SimpleNetworkWrapper killProcess;
	public static SimpleNetworkWrapper syncProcess;

	private static int discriminator = 0;

	public PacketManager() {
		INSTANCE = this;
	}

	public void register() {
		playerCapabilitySync = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:playerCapSync");
		playerCapabilitySync.registerMessage(new PlayerCapabilitySyncHandler(), PlayerCapabilitySyncMessage.class,
				discriminator++, Side.CLIENT);

		blockBreakSync = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:blockBreakSync");
		blockBreakSync.registerMessage(new BreakSyncHandler(), BreakSyncMessage.class, discriminator++, Side.CLIENT);

		gunReload = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:gunReload");
		gunReload.registerMessage(new ReloadHandler(), ReloadMessage.class, discriminator++, Side.SERVER);

		openGuiClient = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:openGuiClient");
		openGuiClient.registerMessage(new OpenGuiClientHandler(), OpenGuiClientMessage.class, discriminator++,
				Side.CLIENT);

		syncTileEntity = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:syncTileEntity");
		syncTileEntity.registerMessage(new SyncTileEntityHandler(), SyncTileEntityMessage.class, discriminator++,
				Side.CLIENT);

		safeCodeUpdate = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:SafeCodeReload");
		safeCodeUpdate.registerMessage(new SafeCodeHandler(), SafeCodeMessage.class, discriminator++, Side.SERVER);

		startProcess = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:startProcess");
		startProcess.registerMessage(new StartProcessHandler(), StartProcessMessage.class, discriminator++,
				Side.SERVER);

		syncIcon = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:syncIcon");
		syncIcon.registerMessage(new SyncIconHandler(), SyncIconMessage.class, discriminator++, Side.SERVER);

		photoToServer = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:photoToServer");
		photoToServer.registerMessage(new PhotoToServerHandler(), PhotoToServerMessage.class, discriminator++,
				Side.SERVER);

		photoRequest = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:photoRequest");
		photoRequest.registerMessage(new PhotoRequestHandler(), PhotoRequestMessage.class, discriminator++,
				Side.SERVER);

		photoToClient = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:photoToClient");
		photoToClient.registerMessage(new PhotoToClientHandler(), PhotoToClientMessage.class, discriminator++,
				Side.CLIENT);

		dialogueSelection = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:dialogueSelection");
		dialogueSelection.registerMessage(new DialogueSelectionHandler(), DialogueSelectionMessage.class,
				discriminator++, Side.SERVER);

		addSubtitle = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:addSubtitle");
		addSubtitle.registerMessage(new AddSubtitleHandler(), AddSubtitleMessage.class, discriminator++, Side.CLIENT);

		syncInventory = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:syncInventory");
		syncInventory.registerMessage(new SyncInventoryHandler(), SyncInventoryMessage.class, discriminator++,
				Side.CLIENT);
		
		controllableKeyUpdate = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:controls");
		controllableKeyUpdate.registerMessage(new ControllableKeyUpdateHandler(), ControllableKeyUpdateMessage.class, discriminator++,
				Side.SERVER);
		
		syncThrottle = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:syncThrottle");
		syncThrottle.registerMessage(new SyncThrottleHandler(), SyncThrottleMessage.class, discriminator++,
				Side.SERVER);

		killProcess = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:killProcess");
		killProcess.registerMessage(new KillProcessHandler(), KillProcessMessage.class, discriminator++,
				Side.SERVER);
		
		syncProcess = NetworkRegistry.INSTANCE.newSimpleChannel("7D2M:syncProcess");
		syncProcess.registerMessage(new SyncProcessHandler(), SyncProcessMessage.class, discriminator++,
				Side.SERVER);
	}
}