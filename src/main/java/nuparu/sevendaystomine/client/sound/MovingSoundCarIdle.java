package nuparu.sevendaystomine.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.entity.EntityCar;
import nuparu.sevendaystomine.util.MathUtils;

@SideOnly(Side.CLIENT)
public class MovingSoundCarIdle extends MovingSound {
	private final EntityCar car;
	private float distance = 0.0F;

	public MovingSoundCarIdle(EntityCar car) {
		super(SoundHelper.MINIBIKE_IDLE, SoundCategory.NEUTRAL);
		this.car = car;
		this.repeat = false;
		this.repeatDelay = 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void update() {

		if (car.getControllingPassenger() == null || !car.canBeDriven()) {
			this.donePlaying = true;
			return;
		}
		this.pitch = (float)(1+MathUtils.getSpeedKilometersPerHour(car)/100d);

		this.xPosF = (float) this.car.posX;
		this.yPosF = (float) this.car.posY;
		this.zPosF = (float) this.car.posZ;
		this.distance = 0.0F;
		this.volume = 2F;

	}
}