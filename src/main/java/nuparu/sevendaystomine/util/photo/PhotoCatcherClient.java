package nuparu.sevendaystomine.util.photo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Maps;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PhotoCatcherClient {

	public static HashMap<String, List<ByteMapItem>> byteMap = Maps.newHashMap();

	/*
	 * Returns an image file if one could be generated
	 */
	public static File addBytesToMap(byte[] bytes, int parts, int index, String name) {
		if (parts == 1 && index == 0) {
			try {
				return finish(bytes, name);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (!byteMap.containsKey(name)) {
			byteMap.put(name, new ArrayList<ByteMapItem>(Arrays
					.asList(new ByteMapItem[] { new ByteMapItem(bytes, index, parts) })));
		} else {
			List<ByteMapItem> existingByteMapItems = byteMap.get(name);
			existingByteMapItems.add(new ByteMapItem(bytes, index, parts));
			if (existingByteMapItems.size() == parts) {
				try {
					return finish(byteMapToBytes(existingByteMapItems), name);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return null;
	}

	public static byte[] byteMapToBytes(List<ByteMapItem> map) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Collections.sort(map, new Comparator<ByteMapItem>() {
			public int compare(ByteMapItem o1, ByteMapItem o2) {
				if (o1.index == o2.index)
					return 0;
				return o1.index < o2.index ? -1 : 1;
			}
		});

		for (ByteMapItem item : map) {
			outputStream.write(item.bytes);
		}
		return outputStream.toByteArray();
	}

	public static File finish(byte[] bytes, String name) throws IOException {
		File file = getTempFile(name);
		file.deleteOnExit();
		file.getParentFile().mkdirs();

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(file, true);
			stream.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static class ByteMapItem {
		public byte[] bytes;
		public int index;
		public int parts;

		public ByteMapItem(byte[] bytes, int index, int parts) {
			this.bytes = bytes;
			this.index = index;
			this.parts = parts;
		}
	}

	private static File getTempFile(String name) throws IOException {
			return File.createTempFile("./resources/temp/photos/" + FilenameUtils.removeExtension(name),FilenameUtils.getExtension(name));
		
	}
}
