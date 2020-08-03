package com.nuparu.sevendaystomine.electricity.network;

import java.util.List;

import net.minecraft.util.math.BlockPos;

public interface INetwork {
	
	
	public List<BlockPos> getConnections();
	public void connectTo(INetwork toConnect);
	public void disconnect(INetwork toDisconnect);
	public boolean isConnectedTo(INetwork net);
	public void disconnectAll();
	public BlockPos getPosition();
	
}
