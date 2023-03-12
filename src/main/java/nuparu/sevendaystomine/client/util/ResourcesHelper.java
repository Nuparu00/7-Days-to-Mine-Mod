package nuparu.sevendaystomine.client.util;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ResourcesHelper {
    private final HashMap<String, File> files = Maps.newHashMap();
    private final HashMap<String, Image> photos = Maps.newHashMap();

    public static final ResourcesHelper INSTANCE = new ResourcesHelper();

    public Image getImage(String name) {
        Iterator<Map.Entry<String, Image>> itr = photos.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Image> entry = itr.next();
            if (name.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void putImage(Image img, String name) {
        photos.put(name, img);
    }

    public Image addResourceFromFile(File file, String path) throws IOException {
        BufferedImage bimg = ImageIO.read(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( bimg, "png", baos );
        baos.flush();
        baos.close();

        NativeImage n = NativeImage.read(new FileInputStream(file));

        try {
            DynamicTexture tex = new DynamicTexture(n);
            ResourceLocation res = Minecraft.getInstance().getTextureManager()
                    .register(FilenameUtils.getName(path), tex);
            Image img = new Image(res, n.getWidth(), n.getHeight(), path);
            putImage(img, path);
            return img;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Image tryToGetImage(String path) {
        if (photos.containsKey(path)) {
            return photos.get(path);
        } else if (files.containsKey(path)) {
            try {
                return addResourceFromFile(files.get(path), path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void addFile(File file, String path) {
        files.put(path, file);
    }

    public void clear() {
        photos.clear();
    }

    public static class Image {
        public ResourceLocation res;
        public int width;
        public int height;
        public String path;

        public Image(ResourceLocation res, int width, int height, String path) {
            this.res = res;
            this.width = width;
            this.height = height;
            this.path = path;
        }
    }
}
