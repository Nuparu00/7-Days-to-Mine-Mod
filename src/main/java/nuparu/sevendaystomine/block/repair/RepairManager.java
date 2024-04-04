package nuparu.sevendaystomine.block.repair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.util.Utils;

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

	public void removeRepair(Block block, Item item) {
		Iterator<Repair> it = repairList.iterator();
		while (it.hasNext()) {
			Repair repair = it.next();
			if(repair.block == block && repair.getItemStack().getItem() == item) {
				it.remove();
				return;
			}
		}
	}

	public Repair getRepair(Block block) {
		for (Repair repair : repairList) {
			if (repair.block == block) {
				return repair;
			}
		}
		return null;
	}

	public Repair getRepair(Block block, InventoryPlayer inv) {
		Repair lastRepair = null;
		for (Repair repair : repairList) {
			if (repair.block == block) {
				lastRepair = repair;
				for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
					if (repair.getItemStack().getItem() == inv.getStackInSlot(slot).getItem()) {
						return repair;
					}
				}
			}
		}
		return lastRepair;
	}

	public static void repairsInit() {
		try {
			handleMap(readRepairsFile(true));
		} catch (InvalidRepairInputException | IOException e) {
			e.printStackTrace();
		}

		try {
			handleMap(readRepairsFile(false));
		} catch (InvalidRepairInputException | IOException e) {
			e.printStackTrace();
		}

	}

	public static void handleMap(Map<String, Map<String, String>> map) {
		if (map.containsKey("add")) {
			Iterator<Map.Entry<String, String>> add = map.get("add").entrySet().iterator();
			while (add.hasNext()) {
				Map.Entry<String, String> pair = add.next();
				Block block = Block.getBlockFromName((String) pair.getKey());
				Item item = Item.getByNameOrId((String) pair.getValue());
				if (block != null && item != null) {
					RepairManager.INSTANCE.addRepair(block, item, 0.1f);
				}
			}
		}
		if (map.containsKey("remove")) {

			Iterator<Map.Entry<String, String>> remove = map.get("remove").entrySet().iterator();
			while (remove.hasNext()) {
				Map.Entry<String, String> pair = remove.next();
				Block block = Block.getBlockFromName((String) pair.getKey());
				Item item = Item.getByNameOrId((String) pair.getValue());
				if (block != null && item != null) {
					RepairManager.INSTANCE.removeRepair(block, item);
				}
			}
		}
	}

	/**
	 * @param local true = the file inside the .jar file
	 */
	public static Map<String, Map<String, String>> readRepairsFile(boolean local)
			throws InvalidRepairInputException, IOException {
		HashMap<String, String> add = new HashMap<String, String>();
		HashMap<String, String> remove = new HashMap<String, String>();

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		InputStream fis = null;

		if (local) {
			fis = Utils.getInsideFileStream(SevenDaysToMine.MODID.toLowerCase() + "/data/repair.json");
		} else {
			final File file = new File("./config/sevendaystomine/repair.json");
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			fis = new FileInputStream(file);
		}

		InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		String line;
		int lineIndex = 0;

		while ((line = br.readLine()) != null) {
			if (lineIndex++ == 0)
				continue;
			String[] words = line.split(" ");
			if (words.length > 3) {
				br.close();
				throw new InvalidRepairInputException(
						"There can't be more than 4 elements in repair.json. Please , fix this!");
			} else if (words.length < 2) {
				continue;
			}
			String s = "add";
			if (words.length == 3) {
				s = words[2];
			}

			switch (s) {
			case "add":
				add.put(words[0], words[1]);
				break;
			case "remove":
				remove.put(words[0], words[1]);
				break;
			}

		}
		br.close();

		map.put("add", add);
		map.put("remove", remove);
		return map;
	}

}