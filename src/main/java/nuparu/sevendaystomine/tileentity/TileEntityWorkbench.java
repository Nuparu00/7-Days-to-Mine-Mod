package nuparu.sevendaystomine.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.ContainerWorkbench;
import nuparu.sevendaystomine.inventory.ContainerWorkbenchUncrafting;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;

public class TileEntityWorkbench extends TileEntityItemHandler<ItemHandlerNameable> {

	private static final int INVENTORY_SIZE = 1;
	private static final ITextComponent DEFAULT_NAME = new TextComponentTranslation("container.workbench");

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME){
			@Override
		    public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		    {
		        return !stack.isEmpty() && stack.getItem() == ModItems.IRON_SCRAP;
		    }
		};
	}

	@Override
	public Container createContainer(EntityPlayer player) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);
		if (player.isSneaking()) {
			return new ContainerWorkbench(player, playerInventoryWrapper, getInventory(), this, this.getPos());
		} else {
			return new ContainerWorkbenchUncrafting(player, playerInventoryWrapper, getInventory(), this,
					this.getPos());
		}
	}
	
	public Container createContainer(EntityPlayer player, boolean crafting) {
		final IItemHandlerModifiable playerInventory = (IItemHandlerModifiable) player
				.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		final IItemHandlerNameable playerInventoryWrapper = new NameableCombinedInvWrapper(player.inventory,
				playerInventory);
		
		if (crafting) {
			return new ContainerWorkbench(player, playerInventoryWrapper, getInventory(), this, this.getPos());
		} else {
			return new ContainerWorkbenchUncrafting(player, playerInventoryWrapper, getInventory(), this,
					this.getPos());
		}
	}

	@Override
	public void onContainerOpened(EntityPlayer player) {

	}

	@Override
	public void onContainerClosed(EntityPlayer player) {

	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
	}

	public void setDisplayName(String displayName) {
		inventory.setDisplayName(new TextComponentString(displayName));
	}

	@Override
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return null;
	}

}