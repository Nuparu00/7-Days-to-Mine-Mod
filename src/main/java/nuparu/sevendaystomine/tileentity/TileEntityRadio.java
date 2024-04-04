package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.sound.SoundHelper;

public class TileEntityRadio extends TileEntity implements ITickable {

	public static final List<Channel> CHANNELS = new ArrayList<Channel>();
	public static final Channel DEFAULT_CHANNEL = new Channel("none", Integer.MAX_VALUE, null);

	static {
		CHANNELS.add(DEFAULT_CHANNEL);
		CHANNELS.add(new Channel("white_noise", 240, SoundHelper.WHITE_NOISE));
		CHANNELS.add(new Channel("eas", 1400, SoundHelper.EAS));
	}

	private int time = Integer.MAX_VALUE;
	private Channel channel = DEFAULT_CHANNEL;

	public TileEntityRadio() {

	}

	@Override
	public void update() {
		if (this.channel != DEFAULT_CHANNEL && this.channel != null) {
			if (this.time > channel.duration) {
				this.time = channel.duration;
			} else if (this.time <= 0) {
				this.time = channel.duration;
				SevenDaysToMine.proxy.playLoudSound(world,channel.sound, 1.2f, pos, SoundCategory.RECORDS);
			}
			--this.time;

			markNoise();
		} else {
			this.time = Integer.MAX_VALUE;
		}
	}

	public void setChannel(String name) {
		channel = Channel.getByName(name);
		this.time = channel.duration;
		if (channel == DEFAULT_CHANNEL || channel == null) {
			this.time = Integer.MAX_VALUE;
			SevenDaysToMine.proxy.stopLoudSound(pos);
			return;
		}
		SevenDaysToMine.proxy.playLoudSound(world,channel.sound, 1.2f, pos, SoundCategory.RECORDS);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.time = compound.getInteger("time");
		this.channel = Channel.getByName(compound.getString("channel"));

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		compound.setInteger("time", this.time);
		compound.setString("channel", channel == null ? DEFAULT_CHANNEL.name : this.channel.name);
		return compound;
	}

	public static class Channel {
		String name;
		SoundEvent sound;
		int duration;

		public Channel(String name, int duration, SoundEvent sound) {
			this.name = name;
			this.duration = duration;
			this.sound = sound;
		}

		public static Channel getByName(String name) {
			for (Channel ch : CHANNELS) {
				if (ch.name.equals(name)) {
					return ch;
				}
			}
			return null;
		}
	}

	public void markNoise() {

	}

	public void cycleRadio() {
		for (int i = 0; i < CHANNELS.size(); i++) {
			if (CHANNELS.get(i) == channel && i < CHANNELS.size() - 1) {
				channel = CHANNELS.get(i + 1);
				this.time = channel.duration;
				SevenDaysToMine.proxy.playLoudSound(world,channel.sound, 1.2f, pos, SoundCategory.RECORDS);
				return;
			}
		}
		channel = DEFAULT_CHANNEL;
		this.time = Integer.MAX_VALUE;
		SevenDaysToMine.proxy.stopLoudSound(pos);
	}

}
