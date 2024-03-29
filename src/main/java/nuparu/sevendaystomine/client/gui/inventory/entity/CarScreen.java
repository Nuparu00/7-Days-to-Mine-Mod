package nuparu.sevendaystomine.client.gui.inventory.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.inventory.entity.ContainerCar;
import nuparu.sevendaystomine.world.item.quality.QualityTier;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarScreen extends AbstractContainerScreen<ContainerCar> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/container/car.png");
	final ContainerCar container;
	//boolean chestPrev = false;

	public CarScreen(ContainerCar container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.container = container;
		imageWidth = 246;
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		int mid = (int) ((176/2f)-this.font.width(this.container.car.getName())/2);
		int titleWidth = this.font.width(this.container.car.getName());

		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;

		if (isInRect(marginHorizontal+ mid, marginVertical+6, titleWidth, 10, mouseX, mouseY)) {
			List<Component> tooltip = new ArrayList<>();

			int quality = container.car.getCalculatedQuality();
			QualityTier tier = QualityManager.getQualityTier(quality);
			Component qualityValue = Component.translatable("tooltip.sevendaystomine.quality", quality);
			tooltip.add(qualityValue);

			Component health = Component.translatable("tooltip.sevendaystomine.health", this.container.car.getHealth(),this.container.car.getMaxHealth());
			tooltip.add(health);

			Component fuel = Component.translatable("tooltip.sevendaystomine.fuel", this.container.car.getFuel());
			tooltip.add(fuel);

			Component acceleration = Component.translatable("tooltip.sevendaystomine.acceleration", this.container.car.getAcceleration());
			tooltip.add(acceleration);
			guiGraphics.renderTooltip(minecraft.font,tooltip, Optional.empty(), mouseX, mouseY);
		}
	}

	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		guiGraphics.blit(TEXTURE, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);

		boolean chest = true;

		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics,i + ((this.imageWidth+(chest ? -66 : 0)) / 2), j + 62, 32, (float) (i + 51) - x, (float) (j + 75 - 50) - y,
				this.container.car);
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
		// draw the label for the top of the screen
		guiGraphics.drawString(minecraft.font, container.car.getDisplayName(), (int)(176/2f)-this.font.width(this.container.car.getName())/2, 6, Color.darkGray.getRGB()); /// this.font.draw

		// draw the label for the player inventory slots
		guiGraphics.drawString(minecraft.font, playerInventoryTitle, /// this.font.draw
				8, 75, Color.darkGray.getRGB());
	}

	@Override
	public void containerTick() {
		super.containerTick();
		if (!container.car.isAlive()) {
			Minecraft.getInstance().setScreen(null);
		}
	}

}
