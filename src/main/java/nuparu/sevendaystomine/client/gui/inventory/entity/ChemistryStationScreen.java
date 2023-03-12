package nuparu.sevendaystomine.client.gui.inventory.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.world.inventory.block.ContainerChemistry;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class ChemistryStationScreen extends AbstractContainerScreen<ContainerChemistry> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/container/chemistry_station.png");
	final ContainerChemistry container;

	public ChemistryStationScreen(ContainerChemistry container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.container = container;
	}

	@Override
	public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);

		// draw the cook progress bar
		double cookProgress = container.fractionOfCookTimeComplete();
		int progressLevel = (int) (cookProgress * 24);
		this.blit(matrixStack, marginHorizontal + 119, marginVertical + 43, 176, 14, progressLevel + 1, 16);

		// draw the fuel remaining bar for each fuel slot flame
		double burnRemaining = container.fractionOfFuelRemaining(0);
		int yOffset = (int) (Math.ceil(burnRemaining * 14));
		this.blit(matrixStack, this.getGuiLeft() + 89, this.getGuiTop() + 60 - yOffset, 176, 12 - yOffset, 14, yOffset);
	}

	@Override
	protected void renderLabels(@NotNull PoseStack matrixStack, int mouseX, int mouseY) {
		// draw the label for the top of the screen
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		this.font.draw(matrixStack, this.title, 8, 6, Color.darkGray.getRGB()); /// this.font.draw

		// draw the label for the player inventory slots
		this.font.draw(matrixStack, playerInventoryTitle, /// this.font.draw
				8, 70, Color.darkGray.getRGB());
	}

}
