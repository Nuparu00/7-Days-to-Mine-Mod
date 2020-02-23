package com.nuparu.sevendaystomine.util.computer;

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
		ResourceLocation res = Animations.WIN10_LOADING.getFrame((int) Math.round(frame));
		if (res != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 2);
			GlStateManager.clearColor(1, 1, 1, 1);
			RenderUtils.drawTexturedRect(res, screen.getRelativeX(0.5) - 32, screen.getRelativeY(0.5) - 32, 0, 0, 64,
					64, 64, 64, 1, 2);
			GlStateManager.translate(0, 0, -2);
			GlStateManager.popMatrix();
		}
	}

}
