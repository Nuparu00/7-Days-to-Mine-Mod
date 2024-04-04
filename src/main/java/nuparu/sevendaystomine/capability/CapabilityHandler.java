package nuparu.sevendaystomine.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import nuparu.sevendaystomine.SevenDaysToMine;

public class CapabilityHandler {

	public static final ResourceLocation EXTENDED_PLAYER_CAP = new ResourceLocation(SevenDaysToMine.MODID,
			"extended_player");
	public static final ResourceLocation EXTENDED_INV_CAP = new ResourceLocation(SevenDaysToMine.MODID, "extended_inv");
	public static final ResourceLocation EXTENDED_CHUNK_CAP = new ResourceLocation(SevenDaysToMine.MODID,
			"extended_chunk");

	@SubscribeEvent
	public void attachCapabilityToEntity(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) event.getObject();
		event.addCapability(EXTENDED_PLAYER_CAP, new ExtendedPlayerProvider().setOwner(player));
		event.addCapability(EXTENDED_INV_CAP, new ExtendedInventoryProvider(3));
	}

	@SubscribeEvent
	public void attachCapabilityToChunk(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(EXTENDED_CHUNK_CAP, new ExtendedChunkProvider());
	}

	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		// EXTENDED PLAYER
		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		IExtendedPlayer oldExtendedPlayer = CapabilityHelper.getExtendedPlayer(event.getOriginal());

		for (String s : oldExtendedPlayer.getRecipes()) {
			extendedPlayer.unlockRecipe(s);
		}
		extendedPlayer.setHorde(oldExtendedPlayer.getHorde());
		extendedPlayer.setWolfHorde(oldExtendedPlayer.getWolfHorde());
		extendedPlayer.setBloodmoon(oldExtendedPlayer.getBloodmoon());

		if (!event.isWasDeath()) {
			extendedPlayer.copy(oldExtendedPlayer);
			
			IItemHandlerExtended extendedInv = CapabilityHelper.getExtendedInventory(player);
			IItemHandlerExtended oldExtendedInv = CapabilityHelper.getExtendedInventory(event.getOriginal());
			extendedInv.copy(oldExtendedInv);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		if(!event.player.world.isRemote) {
			IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(event.player);
			extendedPlayer.onDataChanged();
		}
	}
}
