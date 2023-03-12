package nuparu.sevendaystomine.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

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
    public static double getDoubleInRange(RandomSource random, double min, double max) {
        return min + random.nextDouble() * (max - min);
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

    public static double roundToNDecimal(double d, int places) {
        double fac = Math.pow(10, places);
        return Math.round(d*fac)/fac;
    }


    public static double getSpeedBlocksPerTicks(Entity entity) {
        double x = entity.getX() - entity.xOld;
        double z = entity.getZ() - entity.zOld;
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

    //By Pyrolistical - https://stackoverflow.com/questions/202302/rounding-to-an-arbitrary-number-of-significant-digits
    public static double roundToSignificantFigures(double num, int n) {
        if(num == 0) {
            return 0;
        }

        final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
        final int power = n - (int) d;

        final double magnitude = Math.pow(10, power);
        final long shifted = Math.round(num*magnitude);
        return shifted/magnitude;
    }

}
