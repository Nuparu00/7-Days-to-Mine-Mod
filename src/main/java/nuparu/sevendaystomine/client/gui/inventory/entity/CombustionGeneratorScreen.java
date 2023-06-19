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
import nuparu.sevendaystomine.client.util.ClientUtils;
import nuparu.sevendaystomine.world.inventory.block.ContainerCombustionGenerator;
import org.jetbrains.annotations.NotNull;

public class CombustionGeneratorScreen extends AbstractContainerScreen<ContainerCombustionGenerator> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,"textures/gui/container/combustion_generator.png");
	final ContainerCombustionGenerator container;

	public CombustionGeneratorScreen(ContainerCombustionGenerator container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.container = container;
	}

	@Override
	public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		this.font.draw(matrixStack,Component.translatable("gui.electricity.voltage",container.getProduction()),marginHorizontal+34,marginVertical+44,4210752);
		this.font.draw(matrixStack,Component.translatable("gui.electricity.stored",container.getStored(),container.getCapacity()),marginHorizontal+34,marginVertical+54,4210752);
		this.renderTooltip(matrixStack, mouseX, mouseY);
		if(ClientUtils.isInRect(marginHorizontal + 17,marginVertical + 19,142,9,mouseX,mouseY)){
			this.renderTooltip(matrixStack,Component.translatable("gui.overheating",(int)Math.round(container.getTemperature()*100)),mouseX,mouseY);
		}
	}

	@Override
	protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int marginHorizontal = (this.width - this.imageWidth) / 2;
		int marginVertical = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, marginHorizontal, marginVertical, 0, 0, imageWidth, imageHeight);
		double burnRemaining = container.fractionOfFuelRemaining();
		int yOffset = (int) (Math.ceil(burnRemaining * 14));
		this.blit(matrixStack, this.getGuiLeft() + 8, this.getGuiTop() + 51 - yOffset, 176, 13 - yOffset, 14, yOffset);
		double temperature = container.getTemperature();
		this.blit(matrixStack, marginHorizontal + 19 + (int)(temperature*132), marginVertical+22, 190, 31, 6, 8);
	}

}
