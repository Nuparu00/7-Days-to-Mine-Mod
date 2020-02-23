package com.nuparu.sevendaystomine.block.repair;

import net.minecraft.util.ReportedException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.crash.CrashReport;
import net.minecraft.block.Block;

import java.util.Set;

import com.nuparu.sevendaystomine.SevenDaysToMine;

import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class RepairManager {

	public static final RepairManager INSTANCE = new RepairManager();
	public ArrayList<Repair> repairList = new ArrayList<Repair>();

	public void addRepair(Block block, ItemStack stack, float percentage) {
		Repair repair = new Repair(block, stack, percentage);
		repairList.add(repair);
	}

	public void addRepair(Block block, Item item, float percentage) {
		Repair repair = new Repair(block, item, percentage);
		repairList.add(repair);
	}

	public Repair getRepair(Block block) {
		for (Repair repair : repairList) {
			if (repair.block == block) {
				return repair;
			}
		}
		return null;
	}

	public static void repairsInit() {
		Map<String, String> map = getRepairsFile();
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			Block block = Block.getBlockFromName((String) pair.getKey());
			Item item = Item.getByNameOrId((String) pair.getValue());
			if (block != null && item != null) {
				RepairManager.INSTANCE.addRepair(block, item, 0.1f);
			}
		}

	}

	@SuppressWarnings("resource")
	public static Map<String, String> getRepairsFile() {
		HashMap<String, String> map = new HashMap<String, String>();
		final File file = new File("./config/7days/repair.json");
		new File("./config/7days/").mkdirs();
		try {
			InputStream fis = new FileInputStream(file.getAbsolutePath());
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				String[] words = line.split(" ");
				if (words.length > 2) {
					throw new InvalidRepairInputException("There can't be more than 3 elements in repair.json. Please , fix this!");
				} else if (words.length < 1) {
					throw new InvalidRepairInputException("There is not any element in repair.json. Please , fix this!");
				} else if (words.length == 1) {
					map.put(words[0], "");
				} else {
					map.put(words[0], words[1]);
				}
			}
		} catch (IOException e) {

		} catch (InvalidRepairInputException e) {
			CrashReport crashreport = CrashReport.makeCrashReport(e, "Loading ./config/7days/repair.json");
			throw new ReportedException(crashreport);
		} finally {

		}
		return map;
	}

	public static boolean containsString(Map<String, String> map, String string) {
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String s = iterator.next();
			if (s.equals(string)) {
				return true;
			}
		}
		return false;
	}

	public void createDefualtRepairs(File file) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		File f = new File("./config/7days/");
		f.mkdirs();
		try {
			InputStream fis = getClass().getResourceAsStream("/assets/"+SevenDaysToMine.MODID+"/data/repair.json");
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			fw = new FileWriter(file.getAbsolutePath(), true);
			bw = new BufferedWriter(fw);
			String line = null;
			bw.write(SevenDaysToMine.VERSION);
			bw.newLine();
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			CrashReport crashreport = CrashReport.makeCrashReport(e, "Reading repair.json from the mod file");
			throw new ReportedException(crashreport);
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (Exception e) {
				CrashReport crashreport = CrashReport.makeCrashReport(e, "Reading repair.json from the mod file");
				throw new ReportedException(crashreport);
			}
		}
	}

	public String getFileVersion() {
		try {
			InputStream fis = getClass().getResourceAsStream("/assets/"+SevenDaysToMine.MODID+"/data/repair.json");
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			new File("./config/7days/").mkdirs();
			return br.readLine();
		} catch (IOException e) {
			CrashReport crashreport = CrashReport.makeCrashReport(e, "Checking repair.json versions");
			throw new ReportedException(crashreport);
		}
	}

	public boolean matchFileVersion() {
		return getFileVersion() == null ? false : getFileVersion().equals(SevenDaysToMine.VERSION);
	}

	public void listAllBlocks() {
		final File file = new File("./config/7days/repair.json");
		if (!file.exists() || !file.isFile() || !matchFileVersion()) {
			try {
				Files.deleteIfExists(new File("./config/7days/repair.json").toPath());
			} catch (IOException e) {
				CrashReport crashreport = CrashReport.makeCrashReport(e, "Deleting repair.json");
				throw new ReportedException(crashreport);
			}
			createDefualtRepairs(file);
		}
		Map<String, String> map = getRepairsFile();
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file.getAbsolutePath(), true);
			bw = new BufferedWriter(fw);
			Iterator<Block> iterator = Block.REGISTRY.iterator();
			while (iterator.hasNext()) {
				Block block = iterator.next();
				String s = Block.REGISTRY.getNameForObject(block).toString();
				if (containsString(map, s) == false) {
					bw.write(s);
					bw.newLine();
				}
			}
		} catch (Exception e) {

		} finally {
			try {
				bw.close();
				RepairManager.repairsInit();
			} catch (Exception e) {

			}
		}
	}

}