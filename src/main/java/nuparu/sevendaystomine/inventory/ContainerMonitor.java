package nuparu.sevendaystomine.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import nuparu.sevendaystomine.tileentity.TileEntityMonitor;

public class ContainerMonitor extends Container{
	
	private EntityPlayer player;
	private TileEntityMonitor monitorTE;
	
	public ContainerMonitor(EntityPlayer player, TileEntityMonitor monitorTE) {
		this.player = player;
		this.monitorTE = monitorTE;
	}
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
    {
		super.onContainerClosed(playerIn);
		if(monitorTE != null) {
			monitorTE.removePlayer(playerIn);
		}
    }

}
