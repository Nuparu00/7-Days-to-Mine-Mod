package com.nuparu.sevendaystomine.util.computer.process;

import org.lwjgl.opengl.GL11;

import com.nuparu.sevendaystomine.client.gui.monitor.elements.Animation;
import com.nuparu.sevendaystomine.tileentity.TileEntityComputer;
import com.nuparu.sevendaystomine.util.client.Animations;
import com.nuparu.sevendaystomine.util.client.RenderUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class BootingProcess extends TickingProcess {

	private static final long BOOT_TIME = 300;

	public BootingProcess() {
		super();
	}

	@Override
	public void tick() {
		super.tick();
		if (existedFor >= BOOT_TIME) {
			computerTE.onBootFinished();
			computerTE.killProcess(this);
		}
		computerTE.markDirty();
	}

	@Override
	public void render(float partialTicks) {
		float frame = existedFor / 10;

		Animation anim = getLoadingAnimation(computerTE);
		if (anim == null)
			return;
		ResourceLocation res = anim.getFrame((int) Math.round(frame));
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
		}
	}

}
