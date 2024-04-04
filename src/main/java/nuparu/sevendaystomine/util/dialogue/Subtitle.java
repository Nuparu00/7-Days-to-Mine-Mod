package nuparu.sevendaystomine.util.dialogue;

import nuparu.sevendaystomine.entity.EntityHuman;

public class Subtitle {

	protected String dialogue;
	protected EntityHuman sender;
	protected double duration;

	public double showTime = 0l;

	public Subtitle(EntityHuman sender, String dialogue, double duration) {
		this.sender = sender;
		this.dialogue = dialogue;
		this.duration = duration;
	}

	public double getDuration() {
		return this.duration;
	}

	public EntityHuman getSender() {
		return this.sender;
	}

	public String getDialogue() {
		return this.dialogue;
	}
}
