package nuparu.sevendaystomine.client.gui.inventory.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.messages.SafeCodeMessage;
import nuparu.sevendaystomine.world.level.block.entity.CodeSafeBlockEntity;
import org.jetbrains.annotations.NotNull;

public class LockedCodeSafeScreen extends Screen implements GuiEventListener {

	private static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/container/code_safe_locked.png");

	private CodeSafeBlockEntity safe;
	private final BlockPos pos;

	private int guiLeft;
	private int guiTop;
	private final int xSize = 110;
	private final int ySize = 100;

	public LockedCodeSafeScreen(BlockEntity tileEntity, BlockPos pos) {
		super(Component.translatable("screen.code_safe.locked"));
		if (!(tileEntity instanceof CodeSafeBlockEntity)) {
			throw new IllegalArgumentException("Passed BlockEntity ("+ tileEntity.getClass().toString() + ") is not an instance of CodeSafeBlockEntity!");
		}
		this.safe = (CodeSafeBlockEntity) tileEntity;
		this.pos = pos;
	}

	public void init() {
		super.init();
		guiLeft = (this.width - xSize) / 2;
		guiTop = (this.height - ySize) / 2;
		this.renderables.clear();

		int x1 = guiLeft + xSize/2 - 26;
		int x2 = guiLeft + xSize/2 - 6;
		int x3 = guiLeft + xSize/2 + 14;
		
		int y1 = guiTop + 23;
		int y2 = guiTop + 53;

		addRenderableWidget(new Button.Builder(Component.literal("+"), button -> actionPerformed((CodeButton) button)).pos(x1, y1).size(12,20).build(builder -> new CodeButton(4,builder)));
		addRenderableWidget(new Button.Builder(Component.literal("-"), button -> actionPerformed((CodeButton) button)).pos(x1, y2).size(12,20).build(builder -> new CodeButton(5,builder)));

		addRenderableWidget(new Button.Builder(Component.literal("+"), button -> actionPerformed((CodeButton) button)).pos(x2, y1).size(12,20).build(builder -> new CodeButton(2,builder)));
		addRenderableWidget(new Button.Builder(Component.literal("-"), button -> actionPerformed((CodeButton) button)).pos(x2, y2).size(12,20).build(builder -> new CodeButton(3,builder)));

		addRenderableWidget(new Button.Builder(Component.literal("+"), button -> actionPerformed((CodeButton) button)).pos(x3, y1).size(12,20).build(builder -> new CodeButton(0,builder)));
		addRenderableWidget(new Button.Builder(Component.literal("-"), button -> actionPerformed((CodeButton) button)).pos(x3, y2).size(12,20).build(builder -> new CodeButton(1,builder)));

	}

	public boolean isPauseScreen() {
		return false;
	}

	public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.drawGuiContainerBackgroundLayer(graphics,partialTicks, mouseX, mouseY);
		super.render(graphics,mouseX, mouseY, partialTicks);
		this.drawGuiContainerForegroundLayer(graphics,mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(GuiGraphics graphics, float particalTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		int marginHorizontal = (width - xSize) / 2;
		int marginVertical = (height - ySize) / 2;
		graphics.blit(TEXTURE,marginHorizontal, marginVertical, 0, 0, xSize, ySize);
	}

	protected void drawGuiContainerForegroundLayer(GuiGraphics graphics,int mouseX, int mouseY) {
		int selectedCode = safe.getSelectedCode();

		int h = (selectedCode / 100) % 10;
		int d = (selectedCode / 10) % 10;
		int u = selectedCode % 10;
		
		int x1 = guiLeft + xSize/2 - 23;
		int x2 = guiLeft + xSize/2 - 3;
		int x3 = guiLeft + xSize/2 + 17;
		
		int y1 = guiTop + 15;
		int y2 = guiTop + 45;
		int y3 = guiTop + 75;

		graphics.drawString(minecraft.font,String.valueOf(h), x1, y2, 0xffffff);
		graphics.drawString(minecraft.font,String.valueOf(d), x2, y2, 0xffffff);
		graphics.drawString(minecraft.font,String.valueOf(u), x3, y2, 0xffffff);

		graphics.drawString(minecraft.font,String.valueOf(h==9?0:h+1), x1, y1, 0x555555);
		graphics.drawString(minecraft.font,String.valueOf(d==9?0:d+1), x2, y1, 0x555555);
		graphics.drawString(minecraft.font,String.valueOf(u==9?0:u+1), x3, y1, 0x555555);

		graphics.drawString(minecraft.font,String.valueOf(h==0?9:h-1), x1, y3, 0x555555);
		graphics.drawString(minecraft.font,String.valueOf(d==0?9:d-1), x2, y3, 0x555555);
		graphics.drawString(minecraft.font,String.valueOf(u==0?9:u-1), x3, y3, 0x555555);
	}
	
	protected void actionPerformed(CodeButton button)
	{
		int buttonID = button.id;
		if(buttonID >= 0 && buttonID <= 5) {
			
			int multpleOfTwo = buttonID/2;
			
			int toAdd = (int) Math.pow(10, multpleOfTwo+1);
			if(buttonID%2 == 1) {
				toAdd = -toAdd;
			}
			PacketManager.safeCodeUpdate.sendToServer(new SafeCodeMessage(pos,toAdd));
			BlockEntity te = Minecraft.getInstance().level.getBlockEntity(pos);
			if(te instanceof CodeSafeBlockEntity) {
				safe = (CodeSafeBlockEntity)te;
			}
		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public static class CodeButton extends Button {

		public final int id;
		
		public CodeButton(int id, Builder builder) {
			super(builder);
			this.id = id;
		}
	}

}
