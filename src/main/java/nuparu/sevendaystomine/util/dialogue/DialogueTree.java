package nuparu.sevendaystomine.util.dialogue;

import java.util.ArrayList;

public class DialogueTree {

	private ArrayList<Dialogue> options = new ArrayList<Dialogue>();

	public DialogueTree() {

	}

	public void addDialogue(Dialogue dialogue) {
		options.add(dialogue);
	}

	public void addDialogue(String dialogueName) {
		options.add(new Dialogue(dialogueName));
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Dialogue> getOptions() {
		return (ArrayList<Dialogue>) this.options.clone();
	}
}
