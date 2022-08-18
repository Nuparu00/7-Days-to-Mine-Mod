package nuparu.sevendaystomine.util;

import net.minecraft.util.RandomSource;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    private static final Random r = new Random();

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
    public static int getIntInRange(Random random, int min, int max) {
        return min + random.nextInt(max - min);
    }

    public static int getIntInRange(RandomSource random, int min, int max) {
        return min + random.nextInt(max - min);
    }
    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }

    public static long clamp(long value, long min, long max) {
        if (value < min) {
            return min;
        }
        return Math.min(value, max);
    }

    public static float lerp(float alpha, float a, float b) {
        return a + alpha * (b - a);
    }

}
