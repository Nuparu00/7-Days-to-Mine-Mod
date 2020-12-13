package com.nuparu.sevendaystomine.capability;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.nuparu.sevendaystomine.SevenDaysToMine;

public class CapabilityHandler {

	public static final ResourceLocation LOCKED_RECIPE_CAP = new ResourceLocation(SevenDaysToMine.MODID,
			"locked_recipe");
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
		event.addCapability(LOCKED_RECIPE_CAP, new LockedRecipeProvider());
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
		// LOCKED RECIPE
		ILockedRecipe recipe = player.getCapability(LockedRecipeProvider.LOCKED_RECIPE_CAP, null);
		ILockedRecipe oldRecipe = event.getOriginal().getCapability(LockedRecipeProvider.LOCKED_RECIPE_CAP, null);
		recipe.copy(oldRecipe);
		// EXTENDED PLAYER
		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		IExtendedPlayer oldExtendedPlayer = CapabilityHelper.getExtendedPlayer(event.getOriginal());

		for (String s : oldExtendedPlayer.getRecipes()) {
			extendedPlayer.unlockRecipe(s);
		}
		extendedPlayer.setHorde(oldExtendedPlayer.hasHorde());

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
