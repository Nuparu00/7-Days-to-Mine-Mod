package nuparu.sevendaystomine.computer.process;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.client.gui.monitor.elements.Animation;
import nuparu.sevendaystomine.client.util.Animations;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class BootingProcess extends TickingProcess {

	private static final long BOOT_TIME = 300;

	public BootingProcess() {
		super();
	}

	@Override
	public void tick() {
		super.tick();
		if (existedFor >= getBootTime()) {
			computerTE.onBootFinished();
			computerTE.killProcess(this);
		}
		computerTE.markDirty();
	}

	@Override
	public void render(float partialTicks) {

		Animation anim = getLoadingAnimation(computerTE);
		if (anim == null)
			return;
		ResourceLocation res = anim.getFrame((int) Math.round(getFrame()));
		if (res != null) {
			GlStateManager.pushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
		    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.translate(0, 0, 2);
			GlStateManager.clearColor(1, 1, 1, 1);
			RenderUtils.drawTexturedRect(res, screen.getRelativeX(0.5) - 32, screen.getRelativeY(0.5) - 32, 0, 0, 64,
					64, 64, 64, 1, 2);
			GlStateManager.translate(0, 0, -2);
			GL11.glDisable(GL11.GL_BLEND);
			GlStateManager.popMatrix();
		}
	}

	public static Animation getLoadingAnimation(TileEntityComputer computerTE) {
		switch (computerTE.getSystem()) {
		default:
		case NONE:
			return null;
		case WIN10:
			return Animations.WIN10_LOADING;
		case WIN7:
			return Animations.WIN7_LOADING;
		case WIN8:
			return Animations.WIN8_LOADING;
		case WINXP:
			return Animations.WINXP_LOADING;
		case MAC:
			return Animations.MAC_LOADING;
		case LINUX:
			return Animations.LINUX_LOADING;
		}
	}
	
	public long getBootTime() {
		return this.computerTE.getSystem() == TileEntityComputer.EnumSystem.MAC ? 320 : 300;
	}
	
	public float getFrame() {
		return existedFor / (this.computerTE.getSystem() == TileEntityComputer.EnumSystem.MAC ? 20 : 10);
	}

}
