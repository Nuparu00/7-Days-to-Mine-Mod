package com.nuparu.sevendaystomine.network;

import com.nuparu.sevendaystomine.network.packets.AddSubtitleHandler;
import com.nuparu.sevendaystomine.network.packets.AddSubtitleMessage;
import com.nuparu.sevendaystomine.network.packets.ApplyRecoilHandler;
import com.nuparu.sevendaystomine.network.packets.ApplyRecoilMessage;
import com.nuparu.sevendaystomine.network.packets.BreakSyncHandler;
import com.nuparu.sevendaystomine.network.packets.BreakSyncMessage;
import com.nuparu.sevendaystomine.network.packets.BulletImpactHandler;
import com.nuparu.sevendaystomine.network.packets.BulletImpactMessage;
import com.nuparu.sevendaystomine.network.packets.CameraDimensionsMessage;
import com.nuparu.sevendaystomine.network.packets.CameraDimensionsHandler;
import com.nuparu.sevendaystomine.network.packets.ControllableKeyUpdateHandler;
import com.nuparu.sevendaystomine.network.packets.ControllableKeyUpdateMessage;
import com.nuparu.sevendaystomine.network.packets.DialogueSelectionHandler;
import com.nuparu.sevendaystomine.network.packets.DialogueSelectionMessage;
import com.nuparu.sevendaystomine.network.packets.HonkHandler;
import com.nuparu.sevendaystomine.network.packets.HonkMessage;
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
import com.nuparu.sevendaystomine.network.packets.SaveDataHandler;
import com.nuparu.sevendaystomine.network.packets.SaveDataMessage;
import com.nuparu.sevendaystomine.network.packets.SchedulePhotoHandler;
import com.nuparu.sevendaystomine.network.packets.SchedulePhotoMessage;
import com.nuparu.sevendaystomine.network.packets.SendPacketHandler;
import com.nuparu.sevendaystomine.network.packets.SendPacketMessage;
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
	public static SimpleNetworkWrapper saveData;
	// This is packet for INetwork - does have nothing to do with actual
	// server-client packets!!!
	public static SimpleNetworkWrapper sendPacket;
	public static SimpleNetworkWrapper applyRecoil;
	public static SimpleNetworkWrapper bulletImpact;
	public static SimpleNetworkWrapper schedulePhoto;
	public static SimpleNetworkWrapper cameraDimensions;
	public static SimpleNetworkWrapper honk;

	private static int discriminator = 0;

	public PacketManager() {
		INSTANCE = this;
	}

	public void register() {
		playerCapabilitySync = NetworkRegistry.INSTANCE.newSimpleChannel("playerCapSync");
		playerCapabilitySync.registerMessage(new PlayerCapabilitySyncHandler(), PlayerCapabilitySyncMessage.class,
				discriminator++, Side.CLIENT);

		blockBreakSync = NetworkRegistry.INSTANCE.newSimpleChannel("blockBreakSync");
		blockBreakSync.registerMessage(new BreakSyncHandler(), BreakSyncMessage.class, discriminator++, Side.CLIENT);

		gunReload = NetworkRegistry.INSTANCE.newSimpleChannel("gunReload");
		gunReload.registerMessage(new ReloadHandler(), ReloadMessage.class, discriminator++, Side.SERVER);

		openGuiClient = NetworkRegistry.INSTANCE.newSimpleChannel("openGuiClient");
		openGuiClient.registerMessage(new OpenGuiClientHandler(), OpenGuiClientMessage.class, discriminator++,
				Side.CLIENT);

		syncTileEntity = NetworkRegistry.INSTANCE.newSimpleChannel("syncTileEntity");
		syncTileEntity.registerMessage(new SyncTileEntityHandler(), SyncTileEntityMessage.class, discriminator++,
				Side.CLIENT);

		safeCodeUpdate = NetworkRegistry.INSTANCE.newSimpleChannel("SafeCodeReload");
		safeCodeUpdate.registerMessage(new SafeCodeHandler(), SafeCodeMessage.class, discriminator++, Side.SERVER);

		startProcess = NetworkRegistry.INSTANCE.newSimpleChannel("startProcess");
		startProcess.registerMessage(new StartProcessHandler(), StartProcessMessage.class, discriminator++,
				Side.SERVER);

		syncIcon = NetworkRegistry.INSTANCE.newSimpleChannel("syncIcon");
		syncIcon.registerMessage(new SyncIconHandler(), SyncIconMessage.class, discriminator++, Side.SERVER);

		photoToServer = NetworkRegistry.INSTANCE.newSimpleChannel("photoToServer");
		photoToServer.registerMessage(new PhotoToServerHandler(), PhotoToServerMessage.class, discriminator++,
				Side.SERVER);

		photoRequest = NetworkRegistry.INSTANCE.newSimpleChannel("photoRequest");
		photoRequest.registerMessage(new PhotoRequestHandler(), PhotoRequestMessage.class, discriminator++,
				Side.SERVER);

		photoToClient = NetworkRegistry.INSTANCE.newSimpleChannel("photoToClient");
		photoToClient.registerMessage(new PhotoToClientHandler(), PhotoToClientMessage.class, discriminator++,
				Side.CLIENT);

		dialogueSelection = NetworkRegistry.INSTANCE.newSimpleChannel("dialogueSelection");
		dialogueSelection.registerMessage(new DialogueSelectionHandler(), DialogueSelectionMessage.class,
				discriminator++, Side.SERVER);

		addSubtitle = NetworkRegistry.INSTANCE.newSimpleChannel("addSubtitle");
		addSubtitle.registerMessage(new AddSubtitleHandler(), AddSubtitleMessage.class, discriminator++, Side.CLIENT);

		syncInventory = NetworkRegistry.INSTANCE.newSimpleChannel("syncInventory");
		syncInventory.registerMessage(new SyncInventoryHandler(), SyncInventoryMessage.class, discriminator++,
				Side.CLIENT);

		controllableKeyUpdate = NetworkRegistry.INSTANCE.newSimpleChannel("controls");
		controllableKeyUpdate.registerMessage(new ControllableKeyUpdateHandler(), ControllableKeyUpdateMessage.class,
				discriminator++, Side.SERVER);

		syncThrottle = NetworkRegistry.INSTANCE.newSimpleChannel("syncThrottle");
		syncThrottle.registerMessage(new SyncThrottleHandler(), SyncThrottleMessage.class, discriminator++,
				Side.SERVER);

		killProcess = NetworkRegistry.INSTANCE.newSimpleChannel("killProcess");
		killProcess.registerMessage(new KillProcessHandler(), KillProcessMessage.class, discriminator++, Side.SERVER);

		syncProcess = NetworkRegistry.INSTANCE.newSimpleChannel("syncProcess");
		syncProcess.registerMessage(new SyncProcessHandler(), SyncProcessMessage.class, discriminator++, Side.SERVER);

		saveData = NetworkRegistry.INSTANCE.newSimpleChannel("saveData");
		saveData.registerMessage(new SaveDataHandler(), SaveDataMessage.class, discriminator++, Side.SERVER);

		sendPacket = NetworkRegistry.INSTANCE.newSimpleChannel("sendPacket");
		sendPacket.registerMessage(new SendPacketHandler(), SendPacketMessage.class, discriminator++, Side.SERVER);

		applyRecoil = NetworkRegistry.INSTANCE.newSimpleChannel("applyRecoil");
		applyRecoil.registerMessage(new ApplyRecoilHandler(), ApplyRecoilMessage.class, discriminator++, Side.CLIENT);

		bulletImpact = NetworkRegistry.INSTANCE.newSimpleChannel("bulletImpact");
		bulletImpact.registerMessage(new BulletImpactHandler(), BulletImpactMessage.class, discriminator++,
				Side.CLIENT);

		schedulePhoto = NetworkRegistry.INSTANCE.newSimpleChannel("schedulePhoto");
		schedulePhoto.registerMessage(new SchedulePhotoHandler(), SchedulePhotoMessage.class, discriminator++,
				Side.CLIENT);
		
		cameraDimensions = NetworkRegistry.INSTANCE.newSimpleChannel("cameraDimensions");
		cameraDimensions.registerMessage(new CameraDimensionsHandler(), CameraDimensionsMessage.class, discriminator++, Side.SERVER);
		honk = NetworkRegistry.INSTANCE.newSimpleChannel("honk");
		honk.registerMessage(new HonkHandler(), HonkMessage.class, discriminator++, Side.SERVER);
	}
}