package com.nuparu.sevendaystomine.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
}
