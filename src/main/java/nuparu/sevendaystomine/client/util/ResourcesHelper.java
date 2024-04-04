package nuparu.sevendaystomine.client.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourcesHelper {
	private HashMap<String, File> files = Maps.newHashMap();
	private HashMap<String, Image> photos = Maps.newHashMap();

	public static final ResourcesHelper INSTANCE = new ResourcesHelper();

	public Image getImage(String name) {
		Iterator<Entry<String, Image>> itr = photos.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Image> entry = itr.next();
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
		DynamicTexture tex = new DynamicTexture(bimg);
		ResourceLocation res = Minecraft.getMinecraft().getTextureManager()
				.getDynamicTextureLocation(FilenameUtils.getName(path), tex);
		if (res == null)
			return null;
		Image img = new Image(res, bimg.getWidth(), bimg.getHeight(),path);
		putImage(img, path);
		return img;
	}

	/**
	 * @param path path that is saved in a NBTTagCompound
	 * @return
	 */
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

	public class Image {
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
