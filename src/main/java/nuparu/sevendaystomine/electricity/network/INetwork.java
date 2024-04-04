package nuparu.sevendaystomine.electricity.network;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public interface INetwork {
	
	
	public List<BlockPos> getConnections();
	public void connectTo(INetwork toConnect);
	public void disconnect(INetwork toDisconnect);
	public boolean isConnectedTo(INetwork net);
	public void disconnectAll();
	public BlockPos getPosition();
	public void sendPacket(String packet, INetwork from, EntityPlayer playerFrom);
	
}
