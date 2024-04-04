package nuparu.sevendaystomine.util.photo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Maps;

import net.minecraftforge.common.DimensionManager;

public class PhotoCatcherServer {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	public static HashMap<String, List<ByteMapItem>> byteMap = Maps.newHashMap();

	/*
	 * Returns an image file if one could be generated
	 */
	public static File addBytesToMap(byte[] bytes, String id, int parts, int index, String nameBase) {
		if (parts == 1 && index == 0) {
			return finish(bytes, nameBase);
		}
		if (!byteMap.containsKey(id)) {
			byteMap.put(id, new ArrayList<ByteMapItem>(Arrays
					.asList(new ByteMapItem[] { new ByteMapItem(bytes, index, parts, nameBase) })));
		} else {
			List<ByteMapItem> existingByteMapItems = byteMap.get(id);
			existingByteMapItems.add(new ByteMapItem(bytes, index, parts, nameBase));
			if (existingByteMapItems.size() == parts) {
				try {
					return finish(byteMapToBytes(existingByteMapItems), nameBase);
				} catch (IOException e) {
					e.printStackTrace();
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

	public static File finish(byte[] bytes, String nameBase) {
		File file = getTimestampedPNGFileForDirectory(DimensionManager.getCurrentSaveRootDirectory(), nameBase);
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
		public String nameBase;

		public ByteMapItem(byte[] bytes, int index, int parts, String nameBase) {
			this.bytes = bytes;
			this.index = index;
			this.parts = parts;
			this.nameBase = nameBase;
		}
	}

	private static File getTimestampedPNGFileForDirectory(File gameDirectory, String nameBase) {
		String s = DATE_FORMAT.format(new Date()).toString();
		int i = 1;

		while (true) {
			File file1 = new File(DimensionManager.getCurrentSaveRootDirectory(),
					"/resources/photos/" + nameBase + "-" + s + (i == 1 ? "" : "_" + i) + ".png");

			if (!file1.exists()) {
				return file1;
			}

			++i;
		}
	}
}
