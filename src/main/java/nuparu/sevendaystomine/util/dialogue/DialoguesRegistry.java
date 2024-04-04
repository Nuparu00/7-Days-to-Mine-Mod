package nuparu.sevendaystomine.util.dialogue;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;

public class DialoguesRegistry {

	public static final DialoguesRegistry INSTANCE = new DialoguesRegistry();

	private HashMap<ResourceLocation, Dialogues> registry = new HashMap<ResourceLocation, Dialogues>();

	public void registerDialogues(Dialogues dialogues, ResourceLocation res) {
		if(dialogues == null) return;
		dialogues.setKey(res);
		registry.put(res, dialogues);
	}

	public void registerDialogues(ResourceLocation res) {
		Dialogues dialogues = DialogueParser.INSTANCE.getDialoguesFromResource(res);
		registerDialogues(dialogues, res);
	}

	public ResourceLocation getResByDialogues(Dialogues dialogues) {
		for (Entry<ResourceLocation, Dialogues> entry : registry.entrySet()) {
			if (Objects.equals(dialogues, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public Dialogues getByRes(ResourceLocation res) {
		for (Entry<ResourceLocation, Dialogues> entry : registry.entrySet()) {
			if (Objects.equals(res, entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	public Dialogues getByName(String name) {
		for (Dialogues dialogues : registry.values()) {
			if (dialogues.getName().equals(name)) {
				return dialogues;
			}
		}
		return null;
	}

	public void register() {
		registerDialogues(Dialogues.EMPTY, new ResourceLocation(SevenDaysToMine.MODID, "empty"));
		registerDialogues(new ResourceLocation(SevenDaysToMine.MODID, "data/dialogues/test.json"));
		registerDialogues(new ResourceLocation(SevenDaysToMine.MODID, "data/dialogues/survivor_generic.json"));
		registerDialogues(new ResourceLocation(SevenDaysToMine.MODID, "data/dialogues/survivor.doctor.json"));
		registerDialogues(new ResourceLocation(SevenDaysToMine.MODID, "data/dialogues/survivor.miner.json"));
		registerDialogues(new ResourceLocation(SevenDaysToMine.MODID, "data/dialogues/survivor.electrician.json"));
		registerDialogues(new ResourceLocation(SevenDaysToMine.MODID, "data/dialogues/survivor.farmer.json"));
		registerDialogues(new ResourceLocation(SevenDaysToMine.MODID, "data/dialogues/survivor.none.json"));
	}
}
