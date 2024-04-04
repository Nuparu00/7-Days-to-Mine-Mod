package nuparu.sevendaystomine.client.gui.monitor.elements;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Animation {
	private final ResourceLocation[] frames;

	public Animation(ResourceLocation[] frames) {
		this.frames = frames;
	}

	public ResourceLocation getFrame(int index) {
		while (index > frames.length - 1) {
			index -= frames.length;
		}

		return frames[index];
	}

	public int getFramesCount() {
		return frames.length;
	}
}
