package nuparu.sevendaystomine.util.dialogue;

import java.util.HashMap;

import net.minecraft.util.ResourceLocation;

public class Dialogues {
	private HashMap<String, DialogueTree> dialogueTrees = new HashMap<String, DialogueTree>();
	private String name;
	private ResourceLocation key = null;
	
	public static final Dialogues EMPTY = new Dialogues("empty");

	public Dialogues(String name) {
		this.name = name;
	}

	public void addDialogueTree(String id, DialogueTree tree) {
		dialogueTrees.put(id, tree);
	}
	
	public HashMap<String, DialogueTree>  getTreesMap(){
		return (HashMap<String, DialogueTree>) this.dialogueTrees.clone();
	}
	
	public DialogueTree getTreeByName(String key) {
		return this.getTreesMap().get(key);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return new StringBuilder("Dialogues(Name:").append(name).append(" DialogueTrees:").append(dialogueTrees.toString())
				.append(")").toString();
	}

	public ResourceLocation getKey() {
		return key;
	}

	public void setKey(ResourceLocation key) {
		this.key = key;
	}
}
