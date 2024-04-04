package nuparu.sevendaystomine.util.dialogue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.util.Utils;

public class DialogueParser {

	public static final DialogueParser INSTANCE = new DialogueParser();

	public Dialogues getDialoguesFromResource(ResourceLocation resourceLocation) {
		InputStream in = getClass().getResourceAsStream(new StringBuilder().append("/assets/")
				.append(resourceLocation.getResourceDomain()).append("/").append(resourceLocation.getResourcePath()).toString());
		if(in == null) {
			Utils.getLogger().error("An error occured while trying to read " + resourceLocation + ": Missing File.");
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		Gson gson = new Gson();
		JsonElement je = gson.fromJson(reader, JsonElement.class);
		JsonObject json = je.getAsJsonObject();
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!json.has("name")) {
			Utils.getLogger().error("An error occured while trying to read " + resourceLocation + ": Missing Name.");
			return null;
		}
		String name = json.get("name").getAsString();
		if (!json.has("trees")) {
			Utils.getLogger().error("An error occured while trying to read " + resourceLocation + ": Missing Trees.");
			return null;
		}
		JsonArray trees = json.get("trees").getAsJsonArray();

		Dialogues dialogues = new Dialogues(name);

		for (JsonElement je2 : trees) {
			JsonObject jsonObj = je2.getAsJsonObject();
			if (!jsonObj.has("id")) {
				Utils.getLogger().error("An error occured while trying to read " + resourceLocation
						+ ": Tree element is missing an ID.");
				return null;
			}
			String id = jsonObj.get("id").getAsString();
			if (!jsonObj.has("dialogues")) {
				Utils.getLogger().error("An error occured while trying to read " + resourceLocation
						+ ": Tree element is missing an Dialogues.");
				return null;
			}
			JsonArray ja = jsonObj.get("dialogues").getAsJsonArray();
			DialogueTree tree = new DialogueTree();

			for (JsonElement je3 : ja) {
				String dialogue = je3.getAsString();
				tree.addDialogue(dialogue);
			}
			dialogues.addDialogueTree(id, tree);

		}

		return dialogues;
	}
}
