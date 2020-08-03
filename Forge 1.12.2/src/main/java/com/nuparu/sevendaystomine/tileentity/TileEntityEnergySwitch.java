package com.nuparu.sevendaystomine.tileentity;

import com.nuparu.sevendaystomine.block.BlockEnergySwitch;
import com.nuparu.sevendaystomine.electricity.ElectricConnection;
import com.nuparu.sevendaystomine.util.ModConstants;

import net.minecraft.util.math.Vec3d;

public class TileEntityEnergySwitch extends TileEntityEnergyPole {

	public TileEntityEnergySwitch() {

	}

	@Override
	public long tryToSendPower(long power, ElectricConnection connection) {
		if (world.getBlockState(pos).getValue(BlockEnergySwitch.POWERED) == false)
			return 0;
		long canBeAdded = capacity - voltage;
		long delta = Math.min(canBeAdded, power);
		long lost = 0;
		if (connection != null) {
			lost = (long) Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
		}
		long realDelta = delta - lost;
		this.voltage += realDelta;

		return delta;
	}

	@Override
	public Vec3d getWireOffset() {
		return offsetDown;

	}
}
