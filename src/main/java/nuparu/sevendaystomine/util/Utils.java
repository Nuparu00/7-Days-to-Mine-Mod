package nuparu.sevendaystomine.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.server.ServerLifecycleHooks;
import nuparu.sevendaystomine.SevenDaysToMine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Utils {
    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }


    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }


    public static byte[] getBytes(BufferedImage img) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;

        } catch (IOException e) {
            SevenDaysToMine.LOGGER.debug(e.getMessage());
        }
        return null;
    }

    public static List<byte[]> divideArray(byte[] source, int chunksize) {

        List<byte[]> result = new ArrayList<>();
        int start = 0;
        while (start < source.length) {
            int end = Math.min(source.length, start + chunksize);
            result.add(Arrays.copyOfRange(source, start, end));
            start += chunksize;
        }

        return result;
    }

    public static File getCurrentSaveRootDirectory() {
        MinecraftServer server = getServer();
        Level world = server.overworld();
        return DimensionType.getStorageFolder(world.dimension(),
                world.getServer().getWorldPath(LevelResource.ROOT)).toFile();
    }

    public static boolean isInArea(double mouseX, double mouseY, double x, double y, double width, double height) {
        return (mouseX >= x && mouseY >= y) && (mouseX <= x + width && mouseY <= y + height);
    }

}
