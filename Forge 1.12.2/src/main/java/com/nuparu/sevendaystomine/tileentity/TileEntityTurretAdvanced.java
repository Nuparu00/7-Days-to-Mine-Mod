package com.nuparu.sevendaystomine.tileentity;

import java.util.Arrays;

import com.nuparu.sevendaystomine.inventory.container.ContainerTurretAdvanced;
import com.nuparu.sevendaystomine.inventory.container.ContainerTurretBase;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import com.nuparu.sevendaystomine.item.ItemCircuit;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurret.AITurretBase;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurret.AITurretShoot;
import com.nuparu.sevendaystomine.tileentity.TileEntityTurret.AITurretTarget;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class TileEntityTurretAdvanced extends TileEntityTurret {

	public TileEntityTurretAdvanced() {
		super();
		targetAI = new AITurretTargetAdvanced(this);
	}
	
	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(10, DEFAULT_NAME);
	}

	@Override
	public ContainerTurretAdvanced createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);

		return new ContainerTurretAdvanced(playerInventoryWrapper, inventory, player, this);
	}

	public boolean hasWhitelist() {
		ItemStack usb = this.getInventory().getStackInSlot(9);
		if (usb.isEmpty() || !(usb.getItem() instanceof ItemCircuit))
			return false;
		NBTTagCompound nbt = usb.getTagCompound();
		if (nbt == null || !nbt.hasKey("data", Constants.NBT.TAG_STRING))
			return false;
		return true;
	}

	public boolean isOnWhitelist(String name) {
		if (!hasWhitelist())
			return false;

		ItemStack usb = this.getInventory().getStackInSlot(9);
		String whitelist = usb.getTagCompound().getString("data");

		String[] names = whitelist.split(" ");

		return Arrays.stream(names).anyMatch(name::equals);
	}

	public class AITurretTargetAdvanced extends AITurretTarget {
		public AITurretTargetAdvanced(TileEntityTurret te) {
			super(te);
		}

		@Override
		public void setTarget(Entity seenEntity) {
			if (seenEntity == te.target) {
				return;
			}
			if (seenEntity instanceof EntityPlayer
					&& (((EntityPlayer) seenEntity).isCreative() || ((EntityPlayer) seenEntity).isSpectator())) {
				return;
			}

			if (isOnWhitelist(seenEntity.getDisplayName().getUnformattedText())) {
				return;
			}
			if (te.target == null || distanceSqrtTo(seenEntity) < distanceSqrtTo(te.target)) {
				te.target = seenEntity;
			}
		}
	}
}
