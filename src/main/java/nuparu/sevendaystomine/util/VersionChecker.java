package nuparu.sevendaystomine.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import nuparu.sevendaystomine.SevenDaysToMine;

/**
 * @author jabelar
 *
 */
public class VersionChecker implements Runnable {
	private static boolean isLatestVersion = false;
	public static final boolean dev = false;
	public static final String current = SevenDaysToMine.VERSION;
	private static String latestVersion = "";
	public static String lastCheck;
	public static String gameVersion;
	public static boolean hasNotify;
	public static boolean didOpen = false;
	List<String> content = null;

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		InputStream in = null;
		try {

			File file = new File("config/7days/version.dat");
			file.getParentFile().mkdirs();
			NBTTagCompound nbt = CompressedStreamTools.read(file);
			if (nbt != null) {
				if (nbt.hasKey("lastCheck")) {
					lastCheck = nbt.getString("lastCheck");
				}
				if (nbt.hasKey("hasNotify")) {
					hasNotify = nbt.getBoolean("hasNotify");
				}
			} else {
				lastCheck = "";
				hasNotify = false;
			}

		} catch (IOException e) {
			// e.printStackTrace();
			Utils.getLogger().debug("File version.dat has not been found , generating default values....");
			lastCheck = "";
			hasNotify = false;
		}
		try {
			in = new URL("https://raw.githubusercontent.com/Nuparu00/7-Days-to-Mine/master/version.txt").openStream();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			content = IOUtils.readLines(in);
			latestVersion = content.get(0);
			gameVersion = content.get(1);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
		Utils.getLogger().debug("Latest mod version = " + latestVersion);
		isLatestVersion = latestVersion.equals(current);
		Utils.getLogger().debug("Are you running latest version = " + isLatestVersion);

	}

	public boolean shouldOpen() {
		return (isLatestVersion == false && hasNotify == false && didOpen == false) ? true : false;
	}

	public boolean isLatestVersion() {
		return isLatestVersion;
	}

	public String getLatestVersion() {
		return latestVersion;
	}
}