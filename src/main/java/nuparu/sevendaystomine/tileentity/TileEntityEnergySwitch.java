package nuparu.sevendaystomine.tileentity;

import net.minecraft.util.math.Vec3d;
import nuparu.sevendaystomine.block.BlockEnergySwitch;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.util.ModConstants;

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
