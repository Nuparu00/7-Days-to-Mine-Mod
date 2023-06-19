package nuparu.sevendaystomine.client.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.PhotoToServerMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.item.AnalogCameraItem;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.List;
import java.util.UUID;

public class CameraHelper {
    private IntBuffer pixelBuffer;
    private int[] pixelValues;
    public static final CameraHelper INSTANCE = new CameraHelper();

    public void saveScreenshot(int width, int height, RenderTarget buffer, Player playerIn) {
        if (ServerConfig.allowPhotos.get()) {
            saveScreenshot(null, width, height, buffer);
        }
    }

    public void saveScreenshot(String screenshotName, int width, int height, RenderTarget buffer) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null)
            return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!stack.isEmpty() && stack.getItem() == ModItems.ANALOG_CAMERA.get()) {
            double dW = 1 - AnalogCameraItem.getWidth(stack, player);
            double dH = 1 - AnalogCameraItem.getHeight(stack, player);

            saveScreenshot(screenshotName, (int) (width * dW / 2d), (int) (height * dH / 2d),
                    (int) (width - width * dW / 2d), (int) (height - height * dH / 2d), buffer);
        }
    }

    public void saveScreenshot(String screenshotName, int x, int y, int xx, int yy,
                               RenderTarget buffer) {

        int width = xx - x;
        int height = yy - y;
        int screenWidth = buffer.width;
        int screenHeight = buffer.height;
        int i = screenWidth * screenHeight;

        try (NativeImage nativeimage = new NativeImage(screenWidth, screenHeight, false);
             NativeImage res = new NativeImage(width, height, false);) {

            RenderSystem.bindTexture(buffer.getColorTextureId());
            nativeimage.downloadTexture(0, true);
            nativeimage.flipY();

            nativeimage.resizeSubRectTo(x, y, width, height, res);
            sendFile(res);
        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }

    public void sendFile(NativeImage img) throws IOException {
        File tempFile = File.createTempFile("sdtm-", "-tmpimg");
        img.writeToFile(tempFile);
        tempFile.deleteOnExit();

        byte[] bytes = FileUtils.readFileToByteArray(tempFile);
        List<byte[]> chunks = Utils.divideArray(bytes, 30000);
        int parts = chunks.size();
        String ID = UUID.randomUUID().toString();
        for (int i = 0; i < parts; i++) {
            PacketManager.photoToServer.sendToServer(new PhotoToServerMessage(chunks.get(i), i, parts, ID));
        }
    }

}
