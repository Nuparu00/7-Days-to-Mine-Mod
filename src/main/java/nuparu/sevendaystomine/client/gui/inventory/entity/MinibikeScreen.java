package nuparu.sevendaystomine.client.gui.inventory.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.inventory.entity.ContainerMinibike;
import nuparu.sevendaystomine.world.item.quality.QualityTier;
import nuparu.sevendaystomine.world.item.quality.QualityManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MinibikeScreen extends AbstractContainerScreen<ContainerMinibike> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/container/minibike.png");
	final ContainerMinibike container;
	boolean chestPrev = false;

	public MinibikeScreen(ContainerMinibike container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.container = container;
		updateSize();
	}

	@Override
	public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
		int mid = (int) ((176/2f)-this.font.width(this.container.minibike.getName())/2);
		int titleWidth = this.font.width(this.container.minibike.getName());

		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;

		if (isInRect(marginHorizontal+ mid, marginVertical+6, titleWidth, 10, mouseX, mouseY)) {
			List<Component> tooltip = new ArrayList<>();

			int quality = container.minibike.getCalculatedQuality();
			QualityTier tier = QualityManager.getQualityTier(quality);
			Component qualityValue = Component.translatable("tooltip.sevendaystomine.quality", quality);
			tooltip.add(qualityValue);

			Component health = Component.translatable("tooltip.sevendaystomine.health", this.container.minibike.getHealth(),this.container.minibike.getMaxHealth());
			tooltip.add(health);

			Component fuel = Component.translatable("tooltip.sevendaystomine.fuel", this.container.minibike.getFuel());
			tooltip.add(fuel);

			Component acceleration = Component.translatable("tooltip.sevendaystomine.acceleration", this.container.minibike.getAcceleration());
			tooltip.add(acceleration);
			this.renderComponentTooltip(matrixStack, tooltip, mouseX, mouseY);
		}
	}

	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
	}

	@Override
	protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);

		boolean chest = container.minibike.getChest();

		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		InventoryScreen.renderEntityInInventory(i + ((this.imageWidth+(chest ? -66 : 0)) / 2), j + 62, 32, (float) (i + 51) - x, (float) (j + 75 - 50) - y,
				this.container.minibike);
	}

	@Override
	protected void renderLabels(@NotNull PoseStack matrixStack, int mouseX, int mouseY) {
		// draw the label for the top of the screen
		this.font.draw(matrixStack, container.minibike.getDisplayName(), (176/2f)-this.font.width(this.container.minibike.getName())/2, 6, Color.darkGray.getRGB()); /// this.font.draw

		// draw the label for the player inventory slots
		this.font.draw(matrixStack, playerInventoryTitle, /// this.font.draw
				8, 75, Color.darkGray.getRGB());
	}

	@Override
	public void containerTick() {
		super.containerTick();
		if (!container.minibike.isAlive()) {
			Minecraft.getInstance().setScreen(null);
		}

		updateSize();
	}

	void updateSize(){
		boolean chest = this.container.minibike.getChest();
		if (chestPrev != chest) {
			if (chest) {
				imageWidth = 246;
			} else {
				imageWidth = 176;
			}
			this.init();
		}
		chestPrev = chest;
	}

}