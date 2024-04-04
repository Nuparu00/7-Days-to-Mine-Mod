package nuparu.sevendaystomine.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MathUtils {
	private static Random r = new Random();

	public static float getFloatInRange(float min, float max) {
		return min + r.nextFloat() * (max - min);
	}

	public static double getDoubleInRange(double min, double max) {
		return min + r.nextDouble() * (max - min);
	}

	public static int getIntInRange(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	//Max is exclusive
	public static int getIntInRange(Random rand, int min, int max) {
		return min + rand.nextInt(max - min);
	}

	public static Vec3d lerp(Vec3d st, Vec3d end, float alpha) {
		double x = st.x + alpha * (end.x - st.x);
		double y = st.y + alpha * (end.y - st.y);
		double z = st.z + alpha * (end.z - st.z);
		return new Vec3d(x, y, z);
	}

	public static double lerp(double st, double end, float alpha) {
		return st + alpha * (end - st);
	}

	public static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
		float f;

		for (f = yawOffset - prevYawOffset; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return prevYawOffset + partialTicks * f;
	}

	public static float clamp(float value, float min, float max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}

	public static double clamp(double value, double min, double max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}

	public static int clamp(int value, int min, int max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}

	public static long clamp(long value, long min, long max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}

	public static Vec3d getConeBaseCenter(BlockPos pos, double angle, double range) {
		return new Vec3d(pos.getX() + (Math.cos(angle) * range), pos.getY() + 0.5,
				pos.getZ() + (Math.sin(angle) * range));
	}

	public static Vec3d getConeApex(BlockPos pos, double angle) {
		return new Vec3d(pos.getX() + 0.5 - Math.cos(angle) * 1.1, pos.getY() + 0.5,
				pos.getZ() + 0.5 - Math.sin(angle) * 1.1);
	}

	public static boolean isInCone(Vec3d coneAxis, Vec3d originToTarget, double halfAperture) {

		double angleToAxisCos = originToTarget.dotProduct(coneAxis) / originToTarget.lengthVector()
				/ coneAxis.lengthVector();

		return angleToAxisCos > Math.cos(halfAperture);
	}

	public static boolean isInRange(int value, int min, int max) {
		return (value >= min) && (value <= max);
	}
	
	public static boolean almostEqual(double a, double b, double eps){
	    return Math.abs(a-b)<eps;
	}
	
	public static double bias(double n, double bias) {
		double k = Math.pow(1-bias, 3);
		return (n*k)/(n*k-n+1);
		
	}
	public static double roundToNDecimal(double d, int places) {
		double fac = Math.pow(10, places);
		return Math.round(d*fac)/fac;
	}
	
	
	public static double getSpeedBlocksPerTicks(Entity entity) {
		double x = entity.posX - entity.prevPosX;
		double z = entity.posZ - entity.prevPosZ;
		return Math.sqrt(x*x+z*z);
	}
	public static double getSpeedMetersPerSecond(Entity entity) {
		return getSpeedBlocksPerTicks(entity)*20;
	}
	public static double getSpeedKilometersPerHour(Entity entity) {
		return getSpeedMetersPerSecond(entity)*3.6;
	}
	
	public static double getSpeedMilesPerHour(Entity entity) {
		return getSpeedMetersPerSecond(entity)*2.23694;
	}
}
