package com.nuparu.sevendaystomine.util;

import com.nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import com.nuparu.sevendaystomine.client.gui.GuiBook;
import com.nuparu.sevendaystomine.client.gui.GuiCodeSafeLocked;
import com.nuparu.sevendaystomine.client.gui.GuiDialogue;
import com.nuparu.sevendaystomine.client.gui.GuiKeySafeLocked;
import com.nuparu.sevendaystomine.client.gui.GuiMonitor;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiBackpack;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiCamera;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiCampfire;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiCar;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiChemistryStation;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiCombustionGenerator;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiComputer;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiContainerAirdrop;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiContainerBig;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiContainerLootableEntity;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiContainerSmallOld;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiContainerTiny;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiFlamethrower;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiForge;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiGasGenerator;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiMinibike;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiProjector;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiSafeUnlocked;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiSeparator;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiTurretAdvanced;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiTurretBase;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiWorkbench;
import com.nuparu.sevendaystomine.client.gui.inventory.GuiWorkbenchUncrafting;
import com.nuparu.sevendaystomine.entity.EntityAirdrop;
import com.nuparu.sevendaystomine.entity.EntityCar;
import com.nuparu.sevendaystomine.entity.EntityHuman;
import com.nuparu.sevendaystomine.entity.EntityLootableCorpse;
import com.nuparu.sevendaystomine.entity.EntityMinibike;
import com.nuparu.sevendaystomine.init.ModItems;
import com.nuparu.sevendaystomine.inventory.ContainerAirdrop;
import com.nuparu.sevendaystomine.inventory.ContainerBig;
import com.nuparu.sevendaystomine.inventory.ContainerCampfire;
import com.nuparu.sevendaystomine.inventory.ContainerChemistryStation;
import com.nuparu.sevendaystomine.inventory.ContainerComputer;
import com.nuparu.sevendaystomine.inventory.ContainerForge;
import com.nuparu.sevendaystomine.inventory.ContainerLootableCorpse;
import com.nuparu.sevendaystomine.inventory.ContainerMonitor;
import com.nuparu.sevendaystomine.inventory.ContainerProjector;
import com.nuparu.sevendaystomine.inventory.ContainerSafe;
import com.nuparu.sevendaystomine.inventory.ContainerSmall;
import com.nuparu.sevendaystomine.inventory.ContainerTiny;
import com.nuparu.sevendaystomine.inventory.ContainerWorkbench;
import com.nuparu.sevendaystomine.inventory.ContainerWorkbenchUncrafting;
import com.nuparu.sevendaystomine.inventory.container.ContainerBackpack;
import com.nuparu.sevendaystomine.inventory.container.ContainerCamera;
import com.nuparu.sevendaystomine.inventory.container.ContainerCar;
import com.nuparu.sevendaystomine.inventory.container.ContainerFlamethrower;
import com.nuparu.sevendaystomine.inventory.container.ContainerGenerator;
import com.nuparu.sevendaystomine.inventory.container.ContainerMinibike;
import com.nuparu.sevendaystomine.inventory.container.ContainerSeparator;
import com.nuparu.sevendaystomine.inventory.container.ContainerTurretAdvanced;
import com.nuparu.sevendaystomine.inventory.container.ContainerTurretBase;
import com.nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import com.nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import com.nuparu.sevendaystomine.item.ItemGuide;
import com.nuparu.sevendaystomine.tileentity.TileEntityItemHandler;
import com.nuparu.sevendaystomine.tileentity.TileEntityMonitor;
import com.nuparu.sevendaystomine.tileentity.TileEntityWorkbench;
import com.nuparu.sevendaystomine.tileentity.TileEntityCampfire;
import com.nuparu.sevendaystomine.tileentity.TileEntityChemistryStation;
import com.nuparu.sevendaystomine.tileentity.TileEntityForge;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		ItemStack stack = player.getHeldItemMainhand();
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity instanceof TileEntityItemHandler && !(tileEntity instanceof TileEntityWorkbench)) {
			return ((TileEntityItemHandler<?>) tileEntity).createContainer(player);
		}

		if (ID == 9) {
			Entity entity = world.getEntityByID(y);
			if (entity != null && entity instanceof EntityLootableCorpse) {
				EntityLootableCorpse lootable = (EntityLootableCorpse) entity;
				return new ContainerLootableCorpse(player.inventory, lootable);
			}
		}
		if (ID == 17) {
			Entity entity = world.getEntityByID(y);
			if (entity != null) {
				if (entity instanceof EntityMinibike) {
					EntityMinibike minibike = (EntityMinibike) entity;
					return new ContainerMinibike(player.inventory, minibike);
				} else if (entity instanceof EntityCar) {
					EntityCar car = (EntityCar) entity;
					return new ContainerCar(player.inventory, car);
				}
			}
		}
		if (ID == 18) {
			if (stack.getItem() == ModItems.BACKPACK) {
				final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
						playerInventory);
				return new ContainerBackpack(playerInventoryWrapper,
						stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, EnumFacing.UP));
			}
		}
		if (ID == 21) {
			Entity entity = world.getEntityByID(y);
			if (entity != null && entity instanceof EntityAirdrop) {
				EntityAirdrop airdrop = (EntityAirdrop) entity;
				return new ContainerAirdrop(player.inventory, airdrop);
			}
		}
		
		switch (ID) {
		case 0:
			return ((TileEntityCampfire) tileEntity).createContainer(player);
		case 1:
			return ((TileEntityForge) tileEntity).createContainer(player);
		case 4:
			return null;
		case 5:
			return new ContainerSmall(player.inventory, (IInventory) tileEntity);
		case 6:
			return new ContainerBig(player.inventory, (IInventory) tileEntity);
		case 7:
			return new ContainerComputer(player.inventory, (IInventory) tileEntity);
		case 8:
			return new ContainerMonitor(player, (TileEntityMonitor) tileEntity);
		case 10:
			return new ContainerTiny(player.inventory, (IInventory) tileEntity);
		case 11:
			return ((TileEntityWorkbench) tileEntity).createContainer(player,true);
		case 12:
			return ((TileEntityChemistryStation) tileEntity).createContainer(player);
		case 13:
			return new ContainerProjector(player.inventory, (IInventory) tileEntity);
		case 15:
			return null;
		case 22:
			return null;
		case 27:
			if (stack.getItem() == ModItems.ANALOG_CAMERA) {
				final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
						playerInventory);
				return new ContainerCamera(playerInventoryWrapper,
						stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, EnumFacing.UP));
			}
		case 28:
			Container c = ((TileEntityWorkbench) tileEntity).createContainer(player,false);
			return c;
		}

		return null;

	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		ItemStack stack = player.getHeldItemMainhand();
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (ID == 9) {
			Entity entity = world.getEntityByID(y);
			if (entity != null && entity instanceof EntityLootableCorpse) {
				EntityLootableCorpse lootable = (EntityLootableCorpse) entity;
				return new GuiContainerLootableEntity(player.inventory, lootable,
						new ContainerLootableCorpse(player.inventory, lootable));
			}
		}
		if (ID == 15) {
			Entity entity = world.getEntityByID(x);
			if (entity == null || !(entity instanceof EntityHuman))
				return null;
			return new GuiDialogue((EntityHuman) entity);
		}
		if (ID == 17) {
			Entity entity = world.getEntityByID(y);
			if (entity != null) {
				if (entity instanceof EntityMinibike) {
					EntityMinibike minibike = (EntityMinibike) entity;
					return new GuiMinibike(player.inventory, minibike,
							new ContainerMinibike(player.inventory, minibike));
				} else if (entity instanceof EntityCar) {
					EntityCar car = (EntityCar) entity;
					return new GuiCar(player.inventory, car, new ContainerCar(player.inventory, car));
				}
			}
		}
		if (ID == 18) {
			if (stack.getItem() == ModItems.BACKPACK) {
				final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
						playerInventory);
				return new GuiBackpack(
						new ContainerBackpack(playerInventoryWrapper,
								stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, EnumFacing.UP)),
						stack.getDisplayName());
			}

		}
		if (ID == 21) {
			Entity entity = world.getEntityByID(y);
			if (entity != null && entity instanceof EntityAirdrop) {
				EntityAirdrop airdrop = (EntityAirdrop) entity;
				return new GuiContainerAirdrop(player.inventory, airdrop,
						new ContainerAirdrop(player.inventory, airdrop));
			}
		}

		switch (ID) {
		case 0:
			return new GuiCampfire((ContainerCampfire) ((TileEntityCampfire) tileEntity).createContainer(player));
		case 1:
			return new GuiForge((ContainerForge) ((TileEntityForge) tileEntity).createContainer(player));
		case 2:
			if (tileEntity instanceof TileEntityItemHandler && !(tileEntity instanceof TileEntityWorkbench)) {
				return new com.nuparu.sevendaystomine.client.gui.inventory.GuiContainerSmall(
						(com.nuparu.sevendaystomine.inventory.container.ContainerSmall) ((TileEntityItemHandler<?>) tileEntity)
								.createContainer(player));
			}
		case 3:
			return new GuiCodeSafeLocked(tileEntity, new BlockPos(x, y, z));
		case 4:
			return new GuiKeySafeLocked(tileEntity, new BlockPos(x, y, z));
		case 5:
			return new GuiContainerSmallOld(player.inventory, (IInventory) tileEntity,
					new ContainerSmall(player.inventory, (IInventory) tileEntity));
		case 6:
			return new GuiContainerBig(player.inventory, (IInventory) tileEntity,
					new ContainerBig(player.inventory, (IInventory) tileEntity));
		case 7:
			return new GuiComputer(player.inventory, (IInventory) tileEntity);
		case 8:
			return new GuiMonitor(player, (TileEntityMonitor) tileEntity);
		case 10:
			return new GuiContainerTiny(player.inventory, (IInventory) tileEntity,
					new ContainerTiny(player.inventory, (IInventory) tileEntity));
		case 11:
			return new GuiWorkbench(player.inventory, ((TileEntityWorkbench) tileEntity).createContainer(player,true));
		case 12:
			return new GuiChemistryStation(
					(ContainerChemistryStation) ((TileEntityChemistryStation) tileEntity).createContainer(player));
		case 13:
			if (tileEntity instanceof TileEntityItemHandler) {
				return new GuiGasGenerator(
						(ContainerGenerator) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 14:
			return new GuiProjector(player.inventory, (IInventory) tileEntity,
					new ContainerProjector(player.inventory, (IInventory) tileEntity));
		case 16:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new com.nuparu.sevendaystomine.client.gui.inventory.GuiContainerSmall(
						(com.nuparu.sevendaystomine.inventory.container.ContainerSmall) ((TileEntityItemHandler<?>) tileEntity)
								.createContainer(player));
			}
		case 19:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new com.nuparu.sevendaystomine.client.gui.inventory.GuiBatteryStation(
						(com.nuparu.sevendaystomine.inventory.container.ContainerBatteryStation) ((TileEntityItemHandler<?>) tileEntity)
								.createContainer(player));
			}
		case 20:
			if (tileEntity instanceof TileEntityItemHandler) {
				return new GuiCombustionGenerator(
						(ContainerGenerator) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 22:
			return new GuiBook(((ItemGuide) stack.getItem()).data);

		case 23:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new GuiSeparator(
						(ContainerSeparator) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 24:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new GuiTurretBase(
						(ContainerTurretBase) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 25:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new GuiTurretAdvanced(
						(ContainerTurretAdvanced) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 26:
			if (tileEntity instanceof TileEntityItemHandler) {

				return new GuiFlamethrower(
						(ContainerFlamethrower) ((TileEntityItemHandler<?>) tileEntity).createContainer(player));
			}
		case 27:
			if (stack.getItem() == ModItems.ANALOG_CAMERA) {
				final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
						.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
						playerInventory);
				return new GuiCamera(
						new ContainerCamera(playerInventoryWrapper,
								stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP, EnumFacing.UP)),
						stack.getDisplayName());
			}
		case 28:
			return new GuiWorkbenchUncrafting(player.inventory, ((TileEntityWorkbench) tileEntity).createContainer(player,false));
		}

		return null;
	}

}
