package nuparu.sevendaystomine.client.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nuparu.sevendaystomine.client.gui.GuiMp3;

@SideOnly(Side.CLIENT)
public class MP3Helper {
	@SideOnly(Side.CLIENT)
	public static Clip clip = null;
	@SideOnly(Side.CLIENT)
	public static File def;
	
	@SideOnly(Side.CLIENT)
	private
	static AudioPlayer audioPlayer;
	@SideOnly(Side.CLIENT)
	public static Thread playbackThread;
	@SideOnly(Side.CLIENT)
	public static boolean isPlaying;
	@SideOnly(Side.CLIENT)
	public static boolean isPause;
	@SideOnly(Side.CLIENT)
	public static EnumAudioMode mode;
	@SideOnly(Side.CLIENT)
	public static float audioVolume;
	@SideOnly(Side.CLIENT)
	public static String audioFilePath;
	@SideOnly(Side.CLIENT)
	public static String lastOpenPath;
	@SideOnly(Side.CLIENT)
	public static ArrayList<Audio> files;
	@SideOnly(Side.CLIENT)
	public static int selected;
	
	@SideOnly(Side.CLIENT)
	public static void init() {
		def = new File("resources/audio/");
		def.mkdirs();

		setAudioPlayer(new AudioPlayer());
		audioVolume = 0.5f;
		files = new ArrayList<Audio>();
		selected = 0;
		mode = EnumAudioMode.PLAY_ONCE;
	}
	
	public static AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public static void setAudioPlayer(AudioPlayer audioPlayer) {
		MP3Helper.audioPlayer = audioPlayer;
	}

	@SideOnly(Side.CLIENT)
	public static enum EnumAudioMode {
		PLAY_ONCE, LOOP, CYCLE;
	}
	
	@SideOnly(Side.CLIENT)
	public static class Audio {

		private Path path;
		private String name;
		String author;
		private double duration;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getDuration() {
			return duration;
		}
		public void setDuration(double duration) {
			this.duration = duration;
		}
		public Path getPath() {
			return path;
		}
		public void setPath(Path path) {
			this.path = path;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class AudioPlayer implements LineListener {
		private static final int SECONDS_IN_HOUR = 60 * 60;
		private static final int SECONDS_IN_MINUTE = 60;

		/**
		 * this flag indicates whether the playback completes or not.
		 */
		boolean playCompleted;

		/**
		 * this flag indicates whether the playback is stopped or not.
		 */
		boolean isStopped;

		private boolean isPaused;

		private double durationInSecs;
		private Clip audioClip;
		private Audio audio = null;

		/**
		 * Load audio file before playing back
		 * 
		 * @param audioFilePath Path of the audio file.
		 * @throws IOException
		 * @throws UnsupportedAudioFileException
		 * @throws LineUnavailableException
		 */
		public void load(String audioFilePath)
				throws UnsupportedAudioFileException, IOException, LineUnavailableException {
			File audioFile = new File(audioFilePath);

			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

			AudioFormat format = audioStream.getFormat();
			long frames = audioStream.getFrameLength();
			setDurationInSecs((frames + 0.0) / format.getFrameRate());

			DataLine.Info info = new DataLine.Info(Clip.class, format);

			audioClip = (Clip) AudioSystem.getLine(info);

			audioClip.addLineListener(this);

			audioClip.open(audioStream);
			audioStream.close();

		}

		public long getClipSecondLength() {
			return audioClip.getMicrosecondLength() / 1000000;
		}

		public double getDuration() {
			return audioClip.getBufferSize()
					/ (audioClip.getFormat().getFrameSize() * audioClip.getFormat().getFrameRate());
		}

		public String getClipLengthString() {
			String length = "";
			long hour = 0;
			long minute = 0;
			long seconds = audioClip.getMicrosecondLength() / 1000000;

			if (seconds >= SECONDS_IN_HOUR) {
				hour = seconds / SECONDS_IN_HOUR;
				length = String.format("%02d:", hour);
			} else {
				length += "00:";
			}

			minute = seconds - hour * SECONDS_IN_HOUR;
			if (minute >= SECONDS_IN_MINUTE) {
				minute = minute / SECONDS_IN_MINUTE;
				length += String.format("%02d:", minute);

			} else {
				minute = 0;
				length += "00:";
			}

			long second = seconds - hour * SECONDS_IN_HOUR - minute * SECONDS_IN_MINUTE;

			length += String.format("%02d", second);

			return length;
		}

		/**
		 * Play a given audio file.
		 * 
		 * @throws IOException
		 * @throws UnsupportedAudioFileException
		 * @throws LineUnavailableException
		 */
		public void play() throws IOException {
			setVolume(Math.abs(audioVolume - 1));
			/*
			 * if(isRepeatOn){ audioPlayer.audioClip.loop(Clip.LOOP_CONTINUOUSLY); } else{
			 * audioPlayer.audioClip.loop(0); }
			 */
			audioClip.start();

			playCompleted = false;
			isStopped = false;
			setPaused(false);
			isPlaying = true;
			while (!playCompleted) {
				// wait for the playback completes
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
					if (isStopped) {
						audioClip.stop();
						break;
					}
					if (isPaused()) {
						audioClip.stop();
					} else {
						audioClip.start();
					}
				}
			}

			audioClip.close();

		}

		/**
		 * Stop playing back.
		 */
		public void stop() {
			isStopped = true;
			audioClip.stop();
			isPlaying = false;

		}

		public void pause() {
			setPaused(true);
			audioClip.stop();

		}

		public void resume() {
			setPaused(false);
			audioClip.start();
		}

		public void setVolume(float volume) {
			if (audioClip != null) {
				FloatControl control = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
				float range = control.getMinimum();
				float result = range * volume;
				control.setValue(result);
			}
		}

		/**
		 * Listens to the audio line events to know when the playback completes.
		 */
		@Override
		public void update(LineEvent event) {
			LineEvent.Type type = event.getType();
			if (type == LineEvent.Type.STOP) {
				if (isPaused()) {

				} else if (isStopped) {
					event.getLine().close();
					playCompleted = true;
					audioClip.stop();
					isPlaying = false;
				} else {
					event.getLine().close();
					if (mode == EnumAudioMode.LOOP) {
						// audioClip.setMicrosecondPosition(0L);
						GuiMp3.playMusic(files.get(selected));

					} else if (mode == EnumAudioMode.CYCLE) {
						if (selected < files.size()) {
							selected++;
							GuiMp3.playMusic(files.get(selected));
						} else {
							selected = 0;
							GuiMp3.playMusic(files.get(selected));
						}
					} else {
						playCompleted = true;
						audioClip.stop();
						isPlaying = false;
					}
				}

			}
		}

		public Clip getAudioClip() {
			return audioClip;
		}

		public double getDurationInSecs() {
			return durationInSecs;
		}

		public void setDurationInSecs(double durationInSecs) {
			this.durationInSecs = durationInSecs;
		}

		public boolean isPaused() {
			return isPaused;
		}

		public void setPaused(boolean isPaused) {
			this.isPaused = isPaused;
		}

		public Audio getAudio() {
			return audio;
		}

		public void setAudio(Audio audio) {
			this.audio = audio;
		}
	}
}
