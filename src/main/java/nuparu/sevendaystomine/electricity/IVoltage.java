package nuparu.sevendaystomine.electricity;

import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.energy.IEnergyStorage;

public interface IVoltage extends IEnergyStorage {

	public EnumDeviceType getDeviceType();
	
	public int getMaximalInputs();
	
	public int getMaximalOutputs();
	
	public List<ElectricConnection> getInputs();
	
	public List<ElectricConnection> getOutputs();
	
	public long getOutput();
	
	public long getMaxOutput();
	
	public long getOutputForConnection(ElectricConnection connection);
	
	public boolean tryToConnect(ElectricConnection connection);
	
	public boolean canConnect(ElectricConnection connection);

	public long getRequiredPower();
	
	public long getCapacity();
	public long getVoltageStored();
	public void storePower(long power);
	
	/*
	 * Returns the power that could fit
	 */
	public long tryToSendPower(long power, ElectricConnection connection);
	
	public Vec3d getWireOffset();
	
	/*
	 * If is passive, the block does not require a direct connection to an energy pole to receive power (like lamps)
	 */
	public boolean isPassive();
	
	public boolean disconnect(IVoltage voltage);
	public BlockPos getPos();
}
