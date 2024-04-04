package nuparu.sevendaystomine.util.dialogue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SubtitleHelper {

	
	public static final SubtitleHelper INSTANCE = new SubtitleHelper();
	
	private BlockingQueue<Subtitle> subtitleQueue = new LinkedBlockingQueue<Subtitle>();
	
	private Subtitle currentSubtitle;

	public void addSubtitleToQueue(Subtitle subtitle) {
		subtitleQueue.add(subtitle);
	}

	public Subtitle getSubtitleFromQueue() throws InterruptedException {
		return this.subtitleQueue.take();
	}

	public boolean isAnythingInQueue() {
		return this.subtitleQueue.size() > 0;
	}
	
	public Subtitle getCurrentSubtitle() {
		return this.currentSubtitle;
	}
	
	public void setCurrentSubtitle(Subtitle subtitle) {
		this.currentSubtitle = subtitle;
	}
	
	public void clear() {
		this.currentSubtitle = null;
		this.subtitleQueue.clear();
	}
}
